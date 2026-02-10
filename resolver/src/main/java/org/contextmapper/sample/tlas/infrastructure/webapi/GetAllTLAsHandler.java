package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Function;

import static software.amazon.awssdk.http.HttpStatusCode.*;

@Component
public class GetAllTLAsHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Log logger = LogFactory.getLog(GetAllTLAsHandler.class);

    private final TlaGroupsApplicationService service;
    private final ObjectMapper objectMapper;
    private final ResponseEventFactory responseFactory;

    public GetAllTLAsHandler(final TlaGroupsApplicationService service,
                             final ObjectMapper objectMapper,
                             final ResponseEventFactory responseFactory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        logger.info(GetAllTLAsHandler.class.getName() + " called");
        try {
            String name = requestEvent.getPathParameters().get("name");
            var tlaGroups = service.findAllTLAsByName(name).stream()
                    .map(TlaApiDTOMapper::tlaGroupToDto)
                    .toList();
            logger.info(GetAllTLAsHandler.class.getName() + " returning " + tlaGroups.size() + " TLA groups");
            var status = OK;
            if (tlaGroups.isEmpty()) {
                status = NOT_FOUND;
            }
            return responseFactory.createResponseEvent(objectMapper.writeValueAsString(tlaGroups), status);
        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(INTERNAL_SERVER_ERROR)
                    .withBody("Internal error has happened: " + e.getMessage());
        }

    }

}
