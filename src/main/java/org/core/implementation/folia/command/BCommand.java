package org.core.implementation.folia.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.core.TranslateCore;
import org.core.command.BaseCommandLauncher;
import org.core.exceptions.NotEnoughArguments;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.source.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BCommand implements TabExecutor {

    private final BaseCommandLauncher command;

    public BCommand(BaseCommandLauncher command) {
        this.command = command;
    }

    public BaseCommandLauncher getWrapper() {
        return this.command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        CommandSource source = ((BukkitPlatform) TranslateCore.getPlatform()).getSource(commandSender);
        try {
            return this.command.run(source, strings);
        } catch (NotEnoughArguments notEnoughArguments) {
            commandSender.sendMessage(Component.text(notEnoughArguments.getMessage()));
        } catch (Throwable e) {
            e.printStackTrace();
            commandSender.sendMessage(Component.text(e.getMessage()));
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        CommandSource source = ((BukkitPlatform) TranslateCore.getPlatform()).getSource(commandSender);
        return this.command.tab(source, strings);
    }
}
