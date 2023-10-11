package org.core.implementation.folia.scheduler.type;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public enum ScheduleType {
    SYNC_DELAY(((plugin, runner, delay, iterator) -> Bukkit
            .getScheduler()
            .scheduleSyncDelayedTask(plugin, runner, delay))),
    SYNC_REPEAT(((plugin, runner, delay, iterator) -> Bukkit
            .getScheduler()
            .scheduleSyncRepeatingTask(plugin, runner, delay, Objects.requireNonNull(iterator)))),
    ASYNC_DELAY(
            (plugin, runner, delay, iterator) -> Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, runner, delay)),
    ASYNC_REPEAT(((plugin, runner, delay, iterator) -> Bukkit
            .getScheduler()
            .scheduleAsyncRepeatingTask(plugin, runner, delay, Objects.requireNonNull(iterator))));

    private final ScheduleTypeLauncher toBukkitId;

    ScheduleType(ScheduleTypeLauncher toBukkitId) {
        this.toBukkitId = toBukkitId;
    }

    public int start(Plugin plugin, Runnable runner, long delay, @Nullable Integer iterator) {
        return this.toBukkitId.invoke(plugin, runner, delay, iterator);
    }

}
