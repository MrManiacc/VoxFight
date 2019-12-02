package me.jraynor.engine.asset.urn;

import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.util.Locale;

@Immutable
public final class Name implements Comparable<Name> {
    /**
     * The Name equivalent of an empty String
     */
    public static final Name EMPTY = new Name("");

    private final String originalName;
    private final String normalisedName;

    public Name(String name) {
        Preconditions.checkNotNull(name);
        this.originalName = name;
        this.normalisedName = name.toLowerCase(Locale.ENGLISH);
    }

    /**
     * @return Whether this name is empty (equivalent to an empty string)
     */
    public boolean isEmpty() {
        return normalisedName.isEmpty();
    }

    /**
     * @return The Name in lowercase consistent with Name equality (so two names that are equal will have the same lowercase)
     */
    public String toLowerCase() {
        return normalisedName;
    }

    /**
     * @return The Name in uppercase consistent with Name equality (so two names that are equal will have the same uppercase)
     */
    public String toUpperCase() {
        return originalName.toUpperCase(Locale.ENGLISH);
    }

    @Override
    public int compareTo(Name o) {
        return normalisedName.compareTo(o.normalisedName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Name) {
            Name other = (Name) obj;
            return normalisedName.equals(other.normalisedName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return normalisedName.hashCode();
    }

    @Override
    public String toString() {
        return originalName;
    }


}
