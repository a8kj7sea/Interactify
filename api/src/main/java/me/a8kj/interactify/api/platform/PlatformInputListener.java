package me.a8kj.interactify.api.platform;

import me.a8kj.interactify.api.InteractifyAPI;

import java.util.UUID;

/**
 * Abstract listener responsible for bridging platform-specific input events with the Interactify API.
 * <p>
 * This class listens for platform events, extracts the source and message, and delegates the input
 * handling to {@link InteractifyAPI} if the source has an active session.
 * It also provides a mechanism to cancel the original platform event to prevent default handling.
 * </p>
 *
 * <p>
 * Subclasses must implement platform-specific extraction of the source, message, and event cancellation logic.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <E> the platform event type
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
public abstract class PlatformInputListener<E, S> {

    /**
     * The Interactify API instance used to manage input sessions.
     */
    protected final InteractifyAPI<S> api;

    /**
     * The platform provider used for identifier resolution and platform operations.
     */
    protected final PlatformProvider<S> platform;

    /**
     * Constructs a new platform input listener.
     *
     * @param api      the Interactify API instance
     * @param platform the platform provider implementation
     */
    protected PlatformInputListener(InteractifyAPI<S> api, PlatformProvider<S> platform) {
        this.api = api;
        this.platform = platform;
    }

    /**
     * Called when an input event is received from the platform.
     * <p>
     * This method extracts the source and message from the event, checks if the source has an active session,
     * cancels the event if necessary, and forwards the input to the API for processing.
     * </p>
     *
     * @param event the platform-specific event
     */
    public void onInputReceived(E event) {
        S source = getSource(event);
        UUID id = platform.getIdentifier(source);

        if (api.hasActiveSession(id)) {
            cancelEvent(event);
            api.handleInput(source, getMessage(event));
        }
    }

    /**
     * Extracts the source from the platform event.
     *
     * @param event the platform event
     * @return the extracted source
     */
    protected abstract S getSource(E event);

    /**
     * Extracts the input message from the platform event.
     *
     * @param event the platform event
     * @return the input message as a string
     */
    protected abstract String getMessage(E event);

    /**
     * Cancels the platform event to prevent default processing.
     *
     * @param event the platform event
     */
    protected abstract void cancelEvent(E event);
}