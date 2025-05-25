package org.contextmapper.sample.tlas.application;

import org.contextmapper.sample.tlas.application.exception.TLAGroupNameAlreadyExists;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameDoesNotExist;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameNotValid;
import org.contextmapper.sample.tlas.domain.tla.*;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup.TLAGroupBuilder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TlaGroupsApplicationService {

    private final TLAGroupRepository repository;

    public TlaGroupsApplicationService(final TLAGroupRepository repository) {
        this.repository = repository;
    }

    public List<TLAGroup> findAllTLAGroups() {
        return repository.findAll().stream()
                .filter(group -> !group.getTLAs().isEmpty())
                .toList();
    }

    public List<TLAGroup> findAllTLAsByName(final String name) {
        return findAllTLAGroups().stream()
                .filter(group -> group.getTLAs().stream().anyMatch(tla -> tla.getName().toString().equals(name)))
                .map(group -> new TLAGroupBuilder(
                        group.getName().toString())
                        .withDescription(group.getDescription())
                        .withTLA(group.getTLAs().stream()
                                .filter(tla -> tla.getName().toString().equals(name))
                                .findFirst().orElseThrow())
                        .build())
                .toList();
    }

    public TLAGroup findGroupByName(final String name) {
        return getGroupByName(name);
    }

    public TLAGroup addTLAGroup(final TLAGroup tlaGroup) {
        if (tlaGroupAlreadyExists(tlaGroup.getName())) {
            return tlaGroup;
        }
        return repository.save(tlaGroup);
    }

    public TLAGroup addTLA(final String groupName, final ThreeLetterAbbreviation tla) {
        var group = getGroupByName(groupName);
        group.addTLA(tla);
        return repository.save(group);
    }

    private TLAGroup getGroupByName(final String name) {
        try {
            var shortName = new ShortName(name);
            var group = repository.findByName(shortName);
            if (group.isPresent()) {
                return group.get();
            } else {
                throw new TLAGroupNameDoesNotExist(name);
            }
        } catch (IllegalArgumentException e) {
            throw new TLAGroupNameNotValid(name);
        }
    }

    private boolean tlaGroupAlreadyExists(ShortName name) {
        return findAllTLAGroups().stream()
                .anyMatch(group -> group.getName().equals(name));
    }
}
