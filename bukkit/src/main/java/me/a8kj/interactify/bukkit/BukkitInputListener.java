package me.a8kj.interactify.bukkit;

import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.platform.PlatformInputListener;
import me.a8kj.interactify.api.platform.PlatformProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Bukkit-specific implementation of {@link PlatformInputListener} that listens for chat input events.
 * <p>
 * This listener intercepts {@link AsyncPlayerChatEvent} to capture player input and forward it to
 * the Interactify API when the player has an active input session. The event is cancelled to prevent
 * the message from being broadcast to other players.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public class BukkitInputListener extends PlatformInputListener<AsyncPlayerChatEvent, Player> implements Listener {

    /**
     * Constructs a new Bukkit input listener.
     *
     * @param api      the Interactify API instance
     * @param platform the Bukkit platform provider
     */
    public BukkitInputListener(InteractifyAPI<Player> api, PlatformProvider<Player> platform) {
        super(api, platform);
    }

    /**
     * Handles player chat events at the lowest priority to capture input early.
     *
     * @param event the chat event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        super.onInputReceived(event);
    }

    /**
     * Extracts the player source from the chat event.
     *
     * @param event the chat event
     * @return the player who sent the message
     */
    @Override
    protected Player getSource(AsyncPlayerChatEvent event) {
        return event.getPlayer();
    }

    /**
     * Extracts the chat message from the event.
     *
     * @param event the chat event
     * @return the message sent by the player
     */
    @Override
    protected String getMessage(AsyncPlayerChatEvent event) {
        return event.getMessage();
    }

    /**
     * Cancels the chat event to prevent message propagation.
     *
     * @param event the chat event
     */
    @Override
    protected void cancelEvent(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
    }
}