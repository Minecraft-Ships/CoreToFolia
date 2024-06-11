package org.core.implementation.folia.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.core.TranslateCore;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.implementation.folia.CoreToFolia;
import org.core.implementation.folia.platform.plugin.boot.TranslateCoreBoot;
import org.core.permission.Permission;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksCommand implements ArgumentCommand {
    @Override
    public List<CommandArgument<?>> getArguments() {
        return List.of(new ExactArgument("Tasks"));
    }

    @Override
    public String getDescription() {
        return "Shows all running tasks";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        var translateCoreSchedules = TranslateCore
                .getScheduleManager()
                .getSchedules()
                .stream()
                .map(scheduler -> scheduler.getDisplayName() + ": Async = " + scheduler.isAsync())
                .collect(Collectors.toList());

        Plugin translateCorePlugin = TranslateCoreBoot.getBoot();

        var platformPendingTasks = Bukkit
                .getScheduler()
                .getPendingTasks()
                .stream()
                .filter(bukkitTask -> bukkitTask.getOwner().equals(translateCorePlugin))
                .map(task -> task.getTaskId() + ": Async = " + !task.isSync())
                .collect(Collectors.toList());

        var platformTasks = Bukkit
                .getScheduler()
                .getActiveWorkers()
                .stream()
                .filter(bukkitTask -> bukkitTask.getOwner().equals(translateCorePlugin))
                .map(task -> task.getTaskId() + ": Async = " + task.getThread().getName())
                .toList();

        commandContext
                .getSource()
                .sendMessage(Component
                                     .text("|---|TranslateCore|" + translateCoreSchedules.size() + "|---|")
                                     .color(NamedTextColor.GREEN));
        for (var schedule : translateCoreSchedules) {
            commandContext.getSource().sendMessage(Component.text("- " + schedule));
        }

        commandContext.getSource().sendMessage(Component.text("|---|Platform|---|").color(NamedTextColor.BLUE));
        for (var task : platformPendingTasks) {
            commandContext.getSource().sendMessage(Component.text("- " + task));
        }
        for (var task : platformTasks) {
            commandContext.getSource().sendMessage(Component.text("- " + task));
        }

        return true;
    }
}
