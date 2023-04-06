package org.core.implementation.folia;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.TranslateCore;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.event.EventManager;
import org.core.implementation.folia.configuration.YAMLConfigurationFile;
import org.core.implementation.folia.event.BEventManager;
import org.core.implementation.folia.event.BukkitListener;
import org.core.implementation.folia.event.PaperListener;
import org.core.implementation.folia.platform.BServer;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.implementation.folia.platform.PlatformConsole;
import org.core.implementation.folia.scheduler.BScheduleManager;
import org.core.implementation.folia.world.boss.BServerBossBar;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.ScheduleManager;
import org.core.source.command.ConsoleSource;
import org.core.world.boss.ServerBossBar;

import java.io.File;

public class CoreToFolia extends TranslateCore.CoreImplementation {

    protected final BukkitPlatform platform = new BukkitPlatform();
    protected final ScheduleManager schedule = new BScheduleManager();
    protected final BEventManager manager = new BEventManager();
    protected final BServer server = new BServer();
    protected final PlatformConsole console = new PlatformConsole();

    public CoreToFolia() {
        this.init();
    }

    private void init() {
        CoreImplementation.IMPLEMENTATION = this;
        this.platform.init();

    }

    public void init2(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new PaperListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitListener(), plugin);
    }

    @Override
    public Platform getRawPlatform() {
        return this.platform;
    }

    @Override
    public ScheduleManager getRawScheduleManager() {
        return this.schedule;
    }

    @Override
    public EventManager getRawEventManager() {
        return this.manager;
    }

    @Override
    public ConsoleSource getRawConsole() {
        return this.console;
    }

    @Override
    public ConfigurationStream.ConfigurationFile createRawConfigurationFile(File file, ConfigurationFormat type) {
        if (file == null) {
            throw new IllegalStateException("File cannot be null");
        }
        if (type == null) {
            throw new IllegalStateException("ConfigurationFormat cannot be null");
        }
        boolean check = false;
        for (String fileExt : type.getFileType()) {
            if (file.getName().endsWith(fileExt)) {
                check = true;
            }
        }
        if (!check) {
            throw new IllegalStateException("Unknown file type");
        }
        if (type.equals(ConfigurationFormat.FORMAT_YAML)) {
            return new YAMLConfigurationFile(file);
        }
        throw new IllegalStateException("ConfigurationFormat is not supported: " + type.getName());
    }

    @Override
    public PlatformServer getRawServer() {
        return this.server;
    }

    @Override
    public ServerBossBar bossBuilder() {
        return new BServerBossBar();
    }
}
