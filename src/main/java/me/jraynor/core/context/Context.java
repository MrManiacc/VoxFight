package me.jraynor.core.context;

public interface Context {
    /**
     * @return the object that is known in this context for this type.
     */
    <T> T get(Class<? extends T> type);

    /**
     * Makes the object known in this context to be the object to work with for the given type.
     */
    <T, U extends T> void put(Class<T> type, U object);
}
