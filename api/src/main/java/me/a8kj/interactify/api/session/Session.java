package me.a8kj.interactify.api.session;

import me.a8kj.interactify.api.context.InputContext;

/**
 * Represents a session tied to a specific source that manages active and queued input contexts.
 * <p>
 * A session maintains the current active context along with a queue of pending contexts,
 * allowing sequential handling of multiple input interactions for the same source.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
public interface Session<S> {

    /**
     * Retrieves the source associated with this session.
     *
     * @return the session source
     */
    S getSource();

    /**
     * Gets the currently active input context.
     *
     * @return the active context, or null if none
     */
    InputContext<S> getActiveContext();

    /**
     * Sets the currently active input context.
     *
     * @param context the context to set as active
     */
    void setActiveContext(InputContext<S> context);

    /**
     * Adds a new input context to the queue.
     *
     * @param context the context to enqueue
     */
    void enqueue(InputContext<S> context);

    /**
     * Retrieves and removes the next queued input context.
     *
     * @return the next context in the queue, or null if empty
     */
    InputContext<S> pollNext();

    /**
     * Checks whether there is an active input context.
     *
     * @return true if an active context exists, false otherwise
     */
    boolean hasActiveContext();

    /**
     * Checks whether there are queued input contexts waiting to be processed.
     *
     * @return true if the queue is not empty, false otherwise
     */
    boolean hasQueuedContexts();

    /**
     * Clears the session, removing the active context and any queued contexts.
     */
    void clear();
}