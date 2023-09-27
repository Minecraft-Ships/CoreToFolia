package org.core.implementation.folia.scheduler.type;

import org.bukkit.plugin.Plugin;

interface ScheduleTypeLauncher {

    int invoke(Plugin plugin, Runnable runner, long delay, int iterator);

}
