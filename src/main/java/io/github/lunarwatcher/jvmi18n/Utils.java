package io.github.lunarwatcher.jvmi18n;

import org.jetbrains.annotations.Nullable;

/**
 * @author LunarWatcher
 */
public class Utils {

    /**
     * Production-working assertion. Alternative to the assert keyword and is used for
     * throwing an exception if the input is false.
     * Example usage: `assertion(x != null)`. It's the same as calling `assertion(false)` or
     * `assertion(true)`. If x is null, it is false and throws a RuntimeException
     * @param input The boolean case
     * @param message (optional) message to write along with the exception
     */
    public static void assertion(boolean input, @Nullable String message){
        if(!input) throw new RuntimeException("Assertion failed. " + (message == null ? "No info provided" : message));
    }
}
