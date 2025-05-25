package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper.tlaDtoToTla;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class AcceptTLAHandler implements Function <ScheduledEvent, Void> {

    private static final Log logger = LogFactory.getLog(AcceptTLAHandler.class);

    private final TlaGroupsApplicationService service;
    private final ObjectMapper objectMapper;

    public AcceptTLAHandler(final TlaGroupsApplicationService service, 
                            final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    @Override
    public Void apply(ScheduledEvent event) {
        logger.info("AcceptTLAHandler triggered by EventBridge event");
        Map<String, Object> detail = event.getDetail();

        Object groupName = detail.get("tlaGroupName");
        Object tlaName = detail.get("tlaName");
        Object tlaMeaning = detail.get("tlaMeaning");
        Object tlaAlternativeMeanings = detail.get("tlaAlternativeMeanings");
        Object tlaLink = detail.get("tlaLink");

        logger.info("Group name: " + groupName);
        logger.info("TLA name: " + tlaName);
        logger.info("TLA meaning: " + tlaMeaning);
        logger.info("TLA alternative meanings: " + tlaAlternativeMeanings);
        logger.info("TLA link: " + tlaLink);

        //use objectMapper to convert the detail map to a JSON string
        String jsonString = objectMapper.writeValueAsString(detail);
        logger.info("Detail JSON: " + jsonString);

        if (groupName == null || tlaName == null || tlaMeaning == null) {
            logger.error("Missing required fields in event detail");
            return (Void)null;
        }

        String acceptedGroupName = groupName.toString();
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

        persistTLA(acceptedGroupName, acceptedTlaName, acceptedTlaMeaning, acceptedTlaAlternativeMeanings, acceptedTlaLink);
        return null;
    }

    private void persistTLA(String groupName, String tlaName, String tlaMeaning, Set<String> tlaAlternativeMeanings, String tlaLink) {
        var dto = new TLADto(tlaName, tlaMeaning)
                .alternativeMeanings(tlaAlternativeMeanings)
                .link(tlaLink);
        service.addTLA(groupName, tlaDtoToTla(dto));
    }
}