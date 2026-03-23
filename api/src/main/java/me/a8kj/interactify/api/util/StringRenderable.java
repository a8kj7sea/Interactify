package me.a8kj.interactify.api.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * A concrete implementation of {@link GenericRenderable} for string-based content.
 * <p>
 * This class provides rendering capabilities for raw string input into different formats,
 * including Adventure {@link Component} and plain text.
 * It automatically detects whether the input uses legacy formatting codes or MiniMessage format.
 * </p>
 *
 * <p>
 * Rendering behavior:
 * <ul>
 *     <li>If the raw string contains legacy color codes (&amp; or §), it is parsed using legacy ampersand serializer.</li>
 *     <li>Otherwise, MiniMessage parsing is used.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
@RequiredArgsConstructor
@Getter
public class StringRenderable implements GenericRenderable<String> {

    /**
     * Unique identifier for this renderable instance.
     */
    private final String id;

    /**
     * The raw string content to be rendered.
     */
    private final String raw;

    /**
     * Converts the raw string into an Adventure {@link Component}.
     *
     * @return the rendered component
     */
    @Override
    public Component asComponent() {
        if (raw.contains("§") || raw.contains("&")) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
        }
        return MiniMessage.miniMessage().deserialize(raw);
    }

    /**
     * Converts the rendered component into a plain text representation.
     *
     * @return the plain text output
     */
    @Override
    public String asPlain() {
        return PlainTextComponentSerializer.plainText().serialize(asComponent());
    }
}