package me.a8kj.interactify.api;

import me.a8kj.interactify.api.context.InputContext;
import me.a8kj.interactify.api.platform.PlatformProvider;
import me.a8kj.interactify.api.result.InputValidationResult;
import me.a8kj.interactify.api.session.InputSession;
import me.a8kj.interactify.api.session.Session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core API class responsible for managing input sessions and contexts.
 * <p>
 * This class acts as a singleton entry point for handling user input flows,
 * including validation, queuing, execution, and cancellation.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
public class InteractifyAPI<S> {

    private static InteractifyAPI<?> instance;
    private final PlatformProvider<S> platform;
    private final Map<UUID, Session<S>> sessions = new ConcurrentHashMap<>();

    /**
     * Constructs the API instance with a platform provider.
     *
     * @param platform the platform abstraction used for messaging and scheduling
     */
    private InteractifyAPI(PlatformProvider<S> platform) {
        this.platform = platform;
    }

    /**
     * Initializes the singleton instance.
     *
     * @param platform the platform provider implementation
     * @param <S>      the source type
     */
    public static <S> void initialize(PlatformProvider<S> platform) {
        if (instance == null) instance = new InteractifyAPI<>(platform);
    }

    /**
     * Retrieves the singleton instance.
     *
     * @param <S> the source type
     * @return the API instance
     * @throws IllegalStateException if not initialized
     */
    @SuppressWarnings("unchecked")
    public static <S> InteractifyAPI<S> getInstance() {
        if (instance == null) throw new IllegalStateException("Not initialized");
        return (InteractifyAPI<S>) instance;
    }

    /**
     * Registers a new input context for a source.
     * If a context is already active, the new one is queued.
     *
     * @param context the input context to register
     */
    public void registerContext(InputContext<S> context) {
        UUID id = platform.getIdentifier(context.getSource());
        Session<S> session = sessions.computeIfAbsent(id, k -> new InputSession<>(context.getSource()));

        if (session.hasActiveContext()) {
            session.enqueue(context);
        } else {
            startContext(session, context);
        }
    }

    /**
     * Starts execution of a context, including prompt sending and timeout scheduling.
     *
     * @param session the session associated with the source
     * @param context the context to start
     */
    private void startContext(Session<S> session, InputContext<S> context) {
        session.setActiveContext(context);

        if (context.getPrompt() != null) {
            platform.sendMessage(context.getSource(), context.getPrompt());
        }

        if (context.getTimeoutSeconds() > 0) {
            context.setScheduledTask(platform.scheduleTask(() -> {
                if (session.getActiveContext() == context) {
                    handleCancel(session);
                    if (context.getTimeoutMessage() != null) {
                        platform.sendMessage(context.getSource(), context.getTimeoutMessage());
                    }
                }
            }, context.getTimeoutSeconds()));
        }
    }

    /**
     * Handles incoming input from a source.
     * Performs cancellation check, validation, and dispatch to receiver.
     *
     * @param source  the input source
     * @param message the input message
     */
    public void handleInput(S source, String message) {
        UUID id = platform.getIdentifier(source);
        Session<S> session = sessions.get(id);
        if (session == null || !session.hasActiveContext()) return;

        InputContext<S> context = session.getActiveContext();

        if (message.equalsIgnoreCase(context.getCancelKeyword())) {
            handleCancel(session);
            return;
        }

        if (context.getValidator() != null) {
            InputValidationResult result = context.getValidator().validate(source, message);
            if (!result.isValid()) {
                platform.sendMessage(source, result.getReason());
                return;
            }
        }

        context.cancelTask();
        session.setActiveContext(null);
        platform.runTask(() -> context.getReceiver().onReceive(source, message));

        processQueue(session);
    }

    /**
     * Cancels the current active context for a session.
     *
     * @param session the session to cancel
     */
    private void handleCancel(Session<S> session) {
        InputContext<S> context = session.getActiveContext();
        context.cancelTask();
        session.setActiveContext(null);
        if (context.getCanceller() != null) {
            platform.runTask(() -> context.getCanceller().onCancel(session.getSource()));
        }
        processQueue(session);
    }

    /**
     * Processes the queued contexts for a session.
     *
     * @param session the session to process
     */
    private void processQueue(Session<S> session) {
        if (session.hasQueuedContexts()) {
            startContext(session, session.pollNext());
        }
    }

    /**
     * Checks whether a session has an active context.
     *
     * @param id the session identifier
     * @return true if active, false otherwise
     */
    public boolean hasActiveSession(UUID id) {
        Session<S> session = sessions.get(id);
        return session != null && session.hasActiveContext();
    }

    /**
     * Checks whether a specific named context is active for a source.
     *
     * @param source the source
     * @param name   the context name
     * @return true if active and matches, false otherwise
     */
    public boolean hasActiveContext(S source, String name) {
        UUID id = platform.getIdentifier(source);
        Session<S> session = sessions.get(id);
        if (session != null && session.hasActiveContext()) {
            InputContext<S> context = session.getActiveContext();
            return context.getName() != null && context.getName().equalsIgnoreCase(name);
        }
        return false;
    }

    /**
     * Retrieves a session by its identifier.
     *
     * @param id the session identifier
     * @return the session or null if not found
     */
    public Session<S> getSession(UUID id) {
        return sessions.get(id);
    }

    /**
     * Invalidates and removes a session for a source.
     *
     * @param source the source
     */
    public void invalidate(S source) {
        UUID id = platform.getIdentifier(source);
        Session<S> session = sessions.remove(id);
        if (session != null) session.clear();
    }
}