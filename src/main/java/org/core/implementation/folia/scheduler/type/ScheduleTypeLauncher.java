package org.core.implementation.folia.scheduler.type;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

interface ScheduleTypeLauncher {

    int invoke(Plugin plugin, Runnable runner, long delay, @Nullable Integer iterator);

}
