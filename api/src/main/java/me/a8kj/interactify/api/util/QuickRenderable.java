package me.a8kj.interactify.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * A convenience interface for quickly rendering string-based content into different formats.
 * <p>
 * This interface extends {@link GenericRenderable} with a raw type of {@link String} and provides
 * default implementations for converting the raw string into Adventure {@link Component},
 * legacy formatted text, and plain text.
 * </p>
 *
 * <p>
 * Rendering behavior:
 * <ul>
 *     <li>If the raw string contains legacy color codes (&amp; or §), it is parsed using legacy ampersand serializer.</li>
 *     <li>Otherwise, it is parsed using MiniMessage format.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public interface QuickRenderable extends GenericRenderable<String> {

    /**
     * Converts the raw string into an Adventure {@link Component}.
     * <p>
     * Uses legacy deserialization if legacy color codes are detected,
     * otherwise falls back to MiniMessage parsing.
     * </p>
     *
     * @return the rendered component
     */
    default Component asComponent() {
        String content = getRaw();
        if (content.contains("§") || content.contains("&")) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(content);
        }
        return MiniMessage.miniMessage().deserialize(content);
    }

    /**
     * Converts the rendered component into a legacy section-formatted string.
     *
     * @return the legacy formatted string
     */
    default String asLegacy() {
        return LegacyComponentSerializer.legacySection().serialize(asComponent());
    }

    /**
     * Converts the rendered component into plain text without formatting.
     *
     * @return the plain text representation
     */
    default String asPlain() {
        return PlainTextComponentSerializer.plainText().serialize(asComponent());
    }
}