package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

import static org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper.tlaDtoToTla;
import static org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper.createTlaGroupDtoToTlaGroup;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLAGroupDto;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class AcceptTLAHandler implements Function <ScheduledEvent, Void> {

    private static final Log logger = LogFactory.getLog(AcceptTLAHandler.class);

    private final TlaGroupsApplicationService service;
    private final ObjectMapper objectMapper;

    public AcceptTLAHandler(final TlaGroupsApplicationService service, final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    @Override
    public Void apply(ScheduledEvent event) {
        logger.info("AcceptTLAHandler triggered by EventBridge event");
        try {
            Map<String, Object> detail = event.getDetail();
            Map<String, Object> data = (Map<String, Object>) detail.get("data");

            Object groupName = data.get("tlaGroupName");
            Object groupDescription = data.get("tlaGroupDescription");
            Object tlaName = data.get("tlaName");
            Object tlaMeaning = data.get("tlaMeaning");
            Object tlaAlternativeMeanings = data.get("tlaAlternativeMeanings");
            Object tlaLink = data.get("tlaLink");

            String jsonString = objectMapper.writeValueAsString(data);
            logger.info("Received accept event with the following information: " + jsonString);

            if (groupName == null || tlaName == null || tlaMeaning == null) {
                logger.error("Missing required fields in event detail");
                return null;
            }

            String acceptedGroupName = groupName.toString();
            String acceptedTlaGroupDescription = groupDescription.toString();
            String acceptedTlaName = tlaName.toString();
            String acceptedTlaMeaning = tlaMeaning.toString();
            Set<String> acceptedTlaAlternativeMeanings = null;
            if (tlaAlternativeMeanings instanceof List) {
                acceptedTlaAlternativeMeanings = ((List<?>) tlaAlternativeMeanings).stream()
                        .map(Object::toString)
                        .filter(s -> !s.isEmpty())
                        .collect(java.util.stream.Collectors.toSet());
            }
            String acceptedTlaLink = null;
            if (tlaLink != null) {
                acceptedTlaLink = tlaLink.toString();
            }

            persistTLA(acceptedGroupName, acceptedTlaGroupDescription, acceptedTlaName, acceptedTlaMeaning, acceptedTlaAlternativeMeanings, acceptedTlaLink);
            return null;

        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return null;
        }
    }

    private void persistTLA(String groupName, String groupDescription, String tlaName, String tlaMeaning, Set<String> tlaAlternativeMeanings, String tlaLink) {
        var tlaDto = new TLADto(tlaName, tlaMeaning)
                .alternativeMeanings(tlaAlternativeMeanings)
                .link(tlaLink);

        var groupDto = new TLAGroupDto(groupName, groupDescription, new ArrayList<TLADto>());
        service.addTLAGroup(createTlaGroupDtoToTlaGroup(groupDto));
        service.addTLA(groupName, tlaDtoToTla(tlaDto));
    }
}