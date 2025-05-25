package org.contextmapper.sample.tlas.infrastructure.migration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static org.contextmapper.sample.tlas.domain.tla.TLAGroup.COMMON_GROUP;

@Component
public class DatabaseSeedHandler implements Function<String, String> {

    private static Log logger = LogFactory.getLog(DatabaseSeedHandler.class);

    private TlaGroupsApplicationService service;

    public DatabaseSeedHandler(final TlaGroupsApplicationService service) {
        this.service = service;
    }

    @Override
    public String apply(String unused) {
        logger.info("DatabaseSeedHandler called");
        try {
            service.addTLAGroup(new TLAGroup.TLAGroupBuilder(COMMON_GROUP)
                    .withDescription("Common TLA group")
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("TLA")
                            .withMeaning("Three Letter Abbreviation")
                            .withAlternativeMeaning("Three Letter Acronym")
                            .build())
                    .build());

            service.addTLAGroup(new TLAGroup.TLAGroupBuilder("AppArch")
                    .withDescription("Application Architecture")
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("ADR")
                            .withMeaning("Architectural Decision Record")
                            .withLink("https://adr.github.io/")
                            .build())
                    .build());

            service.addTLAGroup(new TLAGroup.TLAGroupBuilder("DDD")
                    .withDescription("Domain-Driven Design")
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("OHS")
                            .withMeaning("Open Host Service")
                            .build())
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("PL")
                            .withMeaning("Published Language")
                            .build())
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("CF")
                            .withMeaning("Conformist")
                            .build())
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("SK")
                            .withMeaning("Shared Kernel")
                            .build())
                    .withTLA(new ThreeLetterAbbreviation.TLABuilder("ACL")
                            .withMeaning("Anticorruption Layer")
                            .build())
                    .build());
        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return e.getMessage();
        }
        return "ok";
    }
}
