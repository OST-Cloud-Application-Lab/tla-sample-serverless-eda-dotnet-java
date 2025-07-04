package org.contextmapper.sample.tlas.domain.tla;

import org.contextmapper.sample.tlas.domain.tla.exception.InvalidTLAStateTransitionException;

import org.jmolecules.ddd.annotation.Entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
public class ThreeLetterAbbreviation implements Comparable<ThreeLetterAbbreviation> {

    private final ShortName name;
    private final String meaning;
    private final Set<String> alternativeMeanings;
    private URL link;

    private ThreeLetterAbbreviation(final TLABuilder builder) {
        checkArgument(builder != null);
        checkArgument(builder.name != null, "A TLAs name cannot be null!");
        checkArgument(builder.meaning != null && !builder.meaning.isEmpty(),
                "A TLAs meaning cannot be null or empty.");

        this.name = builder.name;
        this.meaning = builder.meaning;
        this.alternativeMeanings = builder.alternativeMeanings.stream()
                .filter(s -> !s.isEmpty()).collect(java.util.stream.Collectors.toSet());
        if (builder.link != null) {
            try {
                this.link = new URL(builder.link);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("The passed link is not a valid URL.", e);
            }
        }
    }

    public Set<String> getAlternativeMeanings() {
        return Collections.unmodifiableSet(this.alternativeMeanings);
    }

    public String getLink() {
        if (link == null)
            return null;
        return link.toString();
    }

    public String getMeaning() {
        return meaning;
    }

    public ShortName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreeLetterAbbreviation that = (ThreeLetterAbbreviation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(ThreeLetterAbbreviation tla) {
        return this.name.compareTo(tla.getName());
    }

    public static class TLABuilder {
        private final ShortName name;
        private String meaning;
        private final Set<String> alternativeMeanings = new HashSet<>();
        private String link;

        public TLABuilder(final ShortName name) {
            this.name = name;
        }

        public TLABuilder(final String name) {
            this.name = new ShortName(name);
        }

        public TLABuilder withMeaning(final String meaning) {
            this.meaning = meaning;
            return this;
        }

        public TLABuilder withAlternativeMeaning(final String alternativeMeaning) {
            this.alternativeMeanings.add(alternativeMeaning);
            return this;
        }

        public TLABuilder withAlternativeMeanings(final Collection<String> alternativeMeanings) {
            this.alternativeMeanings.addAll(alternativeMeanings);
            return this;
        }

        public TLABuilder withLink(final String link) {
            this.link = link;
            return this;
        }

        public ThreeLetterAbbreviation build() {
            return new ThreeLetterAbbreviation(this);
        }

    }

}
