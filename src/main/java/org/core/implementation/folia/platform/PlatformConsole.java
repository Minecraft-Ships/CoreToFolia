package org.core.implementation.folia.platform;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.source.command.ConsoleSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlatformConsole implements ConsoleSource {

    @Override
    public PlatformConsole sendMessage(AText message, UUID uuid) {
        String legacy = message.toLegacy();
        Bukkit.getConsoleSender().sendMessage(uuid, legacy);
        return this;
    }

    @Override
    public PlatformConsole sendMessage(AText message) {
        if (message instanceof AdventureText adventureText) {
            this.sendMessage(adventureText.getComponent());
            return this;
        }
        Bukkit.getConsoleSender().sendMessage(message.toLegacy());
        return this;
    }

    @Override
    public void sendMessage(@NotNull Component message, @Nullable UUID uuid) {
        Identity identity = uuid == null ? Identity.nil() : Identity.identity(uuid);
        Bukkit.getConsoleSender().sendMessage(identity, message);
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), wholeCommand);
    }
}
