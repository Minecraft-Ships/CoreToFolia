package org.core.implementation.folia;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.core.TranslateCore;
import org.core.config.ConfigManager;
import org.core.eco.CurrencyManager;
import org.core.event.EventManager;
import org.core.implementation.folia.configuration.FConfigManager;
import org.core.implementation.folia.eco.vault.VaultCurrencyManager;
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

public class CoreToFolia extends TranslateCore.CoreImplementation {

    protected final BukkitPlatform platform = new BukkitPlatform();
    protected final ScheduleManager schedule = new BScheduleManager();
    protected final BEventManager manager = new BEventManager();
    protected final BServer server = new BServer();
    protected final PlatformConsole console = new PlatformConsole();
    private final ConfigManager configManager = new FConfigManager();
    private final CurrencyManager currencyManager = new VaultCurrencyManager();

    public CoreToFolia() {
        this.init();
    }

    private void init() {
        CoreImplementation.IMPLEMENTATION = this;
        this.platform.init();

    }

    public void init2(Plugin plugin) {
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
    public PlatformServer getRawServer() {
        return this.server;
    }

    @Override
    @Deprecated(forRemoval = true)
    public ServerBossBar bossBuilder() {
        return bossBuilder(BossBar.bossBar(Component.empty(), 0, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS));
    }

    @Override
    @Deprecated(forRemoval = true)
    public ServerBossBar bossBuilder(BossBar bar) {
        return new BServerBossBar(bar);
    }

    @Override
    public ConfigManager getRawConfigManager() {
        return this.configManager;
    }

    @Override
    public CurrencyManager getRawCurrencyManager() {
        return this.currencyManager;
    }
}
