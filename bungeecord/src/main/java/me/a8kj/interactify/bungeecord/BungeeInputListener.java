package me.a8kj.interactify.bungeecord;

import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.platform.PlatformInputListener;
import me.a8kj.interactify.api.platform.PlatformProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * BungeeCord implementation of {@link PlatformInputListener} for handling chat input events.
 * <p>
 * This listener intercepts {@link ChatEvent} to capture player messages and forward them
 * to the Interactify API when the sender has an active input session.
 * Commands and non-player senders are ignored.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public class BungeeInputListener extends PlatformInputListener<ChatEvent, ProxiedPlayer> implements Listener {

    /**
     * Constructs a new Bungee input listener.
     *
     * @param api      the Interactify API instance
     * @param platform the platform provider implementation
     */
    public BungeeInputListener(InteractifyAPI<ProxiedPlayer> api, PlatformProvider<ProxiedPlayer> platform) {
        super(api, platform);
    }

    /**
     * Handles chat events from BungeeCord.
     * <p>
     * Ignores non-player senders and command messages.
     * </p>
     *
     * @param event the chat event
     */
    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        if (event.isCommand()) return;

        super.onInputReceived(event);
    }

    /**
     * Extracts the player source from the event.
     *
     * @param event the chat event
     * @return the player who sent the message
     */
    @Override
    protected ProxiedPlayer getSource(ChatEvent event) {
        return (ProxiedPlayer) event.getSender();
    }

    /**
     * Extracts the message from the chat event.
     *
     * @param event the chat event
     * @return the message content
     */
    @Override
    protected String getMessage(ChatEvent event) {
        return event.getMessage();
    }

    /**
     * Cancels the chat event to prevent further processing.
     *
     * @param event the chat event
     */
    @Override
    protected void cancelEvent(ChatEvent event) {
        event.setCancelled(true);
    }
}