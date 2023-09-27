package org.core.implementation.folia.scheduler.type;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public enum ScheduleType {
    SYNC_DELAY(((plugin, runner, delay, iterator) -> Bukkit
            .getScheduler()
            .scheduleSyncDelayedTask(plugin, runner, delay))),
    SYNC_REPEAT(((plugin, runner, delay, iterator) -> Bukkit
            .getScheduler()
            .scheduleSyncRepeatingTask(plugin, runner, delay, iterator))),
    ASYNC_DELAY(
            (plugin, runner, delay, iterator) -> Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, runner, delay)),
    ASYNC_REPEAT(((plugin, runner, delay, iterator) -> Bukkit
            .getScheduler()
            .scheduleAsyncRepeatingTask(plugin, runner, delay, iterator)));

    private final ScheduleTypeLauncher toBukkitId;

    ScheduleType(ScheduleTypeLauncher toBukkitId) {
        this.toBukkitId = toBukkitId;
    }

    public int start(Plugin plugin, Runnable runner, long delay, int iterator) {
        return this.toBukkitId.invoke(plugin, runner, delay, iterator);
    }

}
