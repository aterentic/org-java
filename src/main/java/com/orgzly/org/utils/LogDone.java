package com.orgzly.org.utils;

/**
 * What to record when a note moves to a done state.
 *
 * <p>Mirrors org's {@code org-log-done}, which can be set globally, per file with
 * {@code #+STARTUP:}, or per subtree with the {@code LOGGING} property.
 */
public enum LogDone {
    /** Change the state only. */
    NONE,

    /** Record a closed time. */
    TIME,

    /**
     * Record a closed time and a note.
     *
     * <p>Callers without a way to prompt for the note should treat this as {@link #TIME}
     * rather than as {@link #NONE}: the closed time is the half that can still be honoured.
     */
    NOTE;

    /**
     * Reads one {@code #+STARTUP:} or {@code LOGGING} token.
     *
     * @return null for any token that does not govern done-logging, so that callers scanning
     *         a list of mixed tokens can tell "said nothing" from "said off"
     */
    public static LogDone fromToken(String token) {
        if (token == null) {
            return null;
        }

        switch (token) {
            case "nologdone":
                return NONE;
            case "logdone":
                return TIME;
            case "lognotedone":
                return NOTE;
            default:
                return null;
        }
    }
}
