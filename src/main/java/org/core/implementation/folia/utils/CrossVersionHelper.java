package org.core.implementation.folia.utils;

import org.core.TranslateCore;
import org.core.platform.plugin.details.CorePluginVersion;
import org.core.utils.Singleton;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

public final class CrossVersionHelper {

    private static final Singleton<CorePluginVersion> minecraftVersion = new Singleton<>(
            () -> TranslateCore.getPlatform().getMinecraftVersion());

    private CrossVersionHelper() {
        throw new RuntimeException("Do not create");
    }

    public static <E extends Enum<E>> E enumOf(Class<E> enumClass,
                                               Supplier<String> useWhenGreater,
                                               Supplier<String> useWhenLower,
                                               int minor,
                                               @Nullable Integer patch) {
        String toUse = (minecraftVersion.get().isGreater(1, minor, patch) ? useWhenGreater : useWhenLower).get();
        return EnumSet
                .allOf(enumClass)
                .stream()
                .filter(index -> index.name().equals(toUse))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        "Cannot find value of '" + toUse + "' in " + enumClass.getSimpleName() + " on version: "
                                + minecraftVersion.get().asString()));
    }
}
