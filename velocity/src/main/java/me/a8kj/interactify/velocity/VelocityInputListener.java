package me.a8kj.interactify.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.platform.PlatformInputListener;
import me.a8kj.interactify.api.platform.PlatformProvider;

/**
 * Velocity implementation of {@link PlatformInputListener} for handling player chat input.
 * <p>
 * This listener intercepts {@link PlayerChatEvent} to capture player messages and forward them
 * to the Interactify API when the player has an active input session.
 * The event is denied to prevent the message from being broadcast.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public class VelocityInputListener extends PlatformInputListener<PlayerChatEvent, Player> {

    /**
     * Constructs a new Velocity input listener.
     *
     * @param api      the Interactify API instance
     * @param platform the platform provider implementation
     */
    public VelocityInputListener(InteractifyAPI<Player> api, PlatformProvider<Player> platform) {
        super(api, platform);
    }

    /**
     * Handles player chat events.
     *
     * @param event the chat event
     */
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        super.onInputReceived(event);
    }

    /**
     * Extracts the player source from the event.
     *
     * @param event the chat event
     * @return the player who sent the message
     */
    @Override
    protected Player getSource(PlayerChatEvent event) {
        return event.getPlayer();
    }

    /**
     * Extracts the message from the chat event.
     *
     * @param event the chat event
     * @return the message content
     */
    @Override
    protected String getMessage(PlayerChatEvent event) {
        return event.getMessage();
    }

    /**
     * Cancels the chat event to prevent message propagation.
     *
     * @param event the chat event
     */
    @Override
    protected void cancelEvent(PlayerChatEvent event) {
        event.setResult(PlayerChatEvent.ChatResult.denied());
    }
}