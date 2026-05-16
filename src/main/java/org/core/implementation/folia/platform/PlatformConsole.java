package org.core.implementation.folia.platform;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.core.source.command.ConsoleSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.UUID;

public class PlatformConsole implements ConsoleSource, ForwardingAudience {

    @Override
    public void sendMessage(@NotNull Component message) {
        ForwardingAudience.super.sendMessage(message);
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), wholeCommand);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Collections.singleton(Bukkit.getConsoleSender());
    }
}
