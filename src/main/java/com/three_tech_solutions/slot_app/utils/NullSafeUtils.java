package com.three_tech_solutions.slot_app.utils;

import java.util.function.Function;

public final class NullSafeUtils {

    private NullSafeUtils() {}

    public static <T, R> R getValueOrNull(T object, Function<T, R> accessor) {
        return object != null ? accessor.apply(object) : null;
    }
}