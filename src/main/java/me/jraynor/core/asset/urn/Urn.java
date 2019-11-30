package me.jraynor.core.asset.urn;

import com.google.common.base.Objects;
import me.jraynor.core.asset.exceptions.InvalidUrnException;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * A Urn is a urn of the structure "{moduleName}:{resourceName}".
 * <ul>
 * <li>moduleName is the name of the module containing or owning the resource</li>
 * <li>resourceName is the name of the resource</li>
 * <li>fragmentName is an optional identifier for a sub-part of the resource</li>
 * <li>an instance urn indicates a resource that is am independant copy of a resource identified by the rest of the urn</li>
 * </ul>
 * Urn is immutable and comparable.
 *
 * @author Immortius
 */
public class Urn implements Comparable<Urn> {

    public static final String RESOURCE_SEPARATOR = ":";
    private static final Pattern URN_PATTERN = Pattern.compile("([^:]+)");

    private Name moduleName;
    private Name resourceName;

    /**
     * Creates a ModuleUri with the given module:resource combo
     *
     * @param moduleName   The name of the module the resource belongs to
     * @param resourceName The name of the resource itself
     */
    public Urn(Name moduleName, Name resourceName) {
        this.moduleName = moduleName;
        this.resourceName = resourceName;
    }

    /**
     * Creates a ModuleUri with the given module:resource combo
     *
     * @param moduleName   The name of the module the resource belongs to
     * @param resourceName The name of the resource itself
     */
    public Urn(String moduleName, String resourceName) {
        this.moduleName = new Name(moduleName);
        this.resourceName = new Name(resourceName);
    }


    /**
     * Creates a ModuleUrn from a string in the format "module:object(#fragment)(!instance)".
     *
     * @param urn The urn to parse
     * @throws InvalidUrnException if the string is not a valid resource urn
     */
    public Urn(String urn) {
        boolean valid = false;
        if (urn.contains(":")) {
            String[] split = urn.split(":");
            if (split.length == 2) {
                moduleName = new Name(split[0]);
                resourceName = new Name(split[1]);
                valid = true;
            }
        }
        if (!valid)
            throw new InvalidUrnException("Invalid Urn: '" + urn + "'");

    }

    /**
     * @param urn The string to check for validity
     * @return Whether urn is a valid Urn
     */
    public static boolean isValid(String urn) {
        return URN_PATTERN.matcher(urn).matches();
    }

    /**
     * @return The module name part of the urn. This identifies the module that the resource belongs to.
     */
    public Name getModuleName() {
        return moduleName;
    }

    /**
     * @return The resource name part of the urn. This identifies the resource itself.
     */
    public Name getResourceName() {
        return resourceName;
    }


    @Override
    public String toString() {
        return moduleName +
                RESOURCE_SEPARATOR +
                resourceName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Urn) {
            Urn other = (Urn) obj;
            return Objects.equal(moduleName, other.moduleName) && Objects.equal(resourceName, other.resourceName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resourceName, moduleName);
    }

    @Override
    public int compareTo(@Nonnull Urn o) {
        int result = moduleName.compareTo(o.getModuleName());
        if (result == 0) {
            result = resourceName.compareTo(o.getResourceName());
        }
        return result;
    }


}
