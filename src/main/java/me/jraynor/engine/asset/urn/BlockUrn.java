package me.jraynor.engine.asset.urn;

import com.google.errorprone.annotations.Immutable;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A simple class that stores a urn and a block id
 */
@Immutable
public final class BlockUrn extends Urn {
    @Getter
    private final short id;

    public BlockUrn(Name moduleName, Name resourceName, short id) {
        super(moduleName, resourceName);
        this.id = id;
    }

    public BlockUrn(String moduleName, String resourceName, short id) {
        super(moduleName, resourceName);
        this.id = id;
    }

    public BlockUrn(String urn, short id) {
        super(urn);
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockUrn blockUrn = (BlockUrn) o;
        return id == blockUrn.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public int compareTo(@Nonnull Urn o) {
        if (o instanceof BlockUrn) {
            BlockUrn urn = (BlockUrn) o;
            if (urn.equals(this))
                return 0;
            return this.id - urn.id;
        }
        return super.compareTo(o);
    }
}
