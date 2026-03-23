package me.a8kj.interactify.api.util;

import net.kyori.adventure.text.Component;

/**
 * Represents a generic renderable object that can be converted into different representations.
 * <p>
 * This abstraction allows implementations to provide both raw data and formatted outputs
 * such as plain text or Adventure {@link Component} instances.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <T> the underlying raw type of the renderable content
 */
public interface GenericRenderable<T> {

    /**
     * Returns a unique identifier for this renderable instance.
     *
     * @return the identifier string
     */
    String getId();

    /**
     * Returns the raw underlying value of this renderable.
     *
     * @return the raw value
     */
    T getRaw();

    /**
     * Converts this renderable into a {@link Component} representation.
     *
     * @return the component representation
     */
    Component asComponent();

    /**
     * Converts this renderable into a plain string representation.
     *
     * @return the plain text representation
     */
    String asPlain();
}