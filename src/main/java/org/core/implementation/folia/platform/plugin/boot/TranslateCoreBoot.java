package org.core.implementation.folia.platform.plugin.boot;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.TranslateCore;
import org.core.command.CommandRegister;
import org.core.command.commands.TranslateCoreCommands;
import org.core.command.commands.timings.TimingsCommand;
import org.core.implementation.folia.CoreToFolia;
import org.core.implementation.folia.command.BCommand;
import org.core.implementation.folia.command.BCommandWrapper;
import org.core.implementation.folia.logger.BJavaLogger;
import org.core.implementation.folia.logger.BSLF4JLogger;
import org.core.implementation.folia.platform.plugin.loader.CoreBukkitPluginWrapper;
import org.core.logger.Logger;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.loader.CommonLoad;
import org.core.schedule.Scheduler;
import org.core.utils.Else;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class TranslateCoreBoot extends JavaPlugin {

    private final Collection<CorePlugin> plugins = new TreeSet<>();
    private final CoreToFolia core;

    public TranslateCoreBoot() {
        this.core = new CoreToFolia();
    }

    private CommandMap getLegacyCommandMap(PluginManager manager) throws NoSuchFieldException, IllegalAccessException {
        return this.getFromField(manager, "commandMap");
    }

    private CommandMap getModernCommandMap(PluginManager manager)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (CommandMap) manager.getClass().getDeclaredMethod("getCommandMap").invoke(manager);
    }

    private CommandMap getCommandMap(PluginManager manager) throws Throwable {
        return Else.throwMultiple(Throwable.class, () -> getModernCommandMap(manager),
                                  () -> getLegacyCommandMap(manager));
    }

    @Override
    public void onLoad() {
        Optional<Class<? extends CorePlugin>> opLauncher = TranslateCore.getStandAloneLauncher();
        if (opLauncher.isPresent()) {
            Class<? extends CorePlugin> pluginClass = opLauncher.get();
            CorePlugin plugin = CommonLoad.loadStandAlonePlugin(pluginClass);

            Logger logger = new BSLF4JLogger(this.getSLF4JLogger());

            plugin.onConstruct(this, logger);
            PluginManager pluginManager = Bukkit.getPluginManager();
            try {
                CommandMap map = this.getCommandMap(pluginManager);
                map.register("TranslateCore",
                             new BCommandWrapper(new BCommand(new TranslateCoreCommands(new TimingsCommand()))));


                CommandRegister cmdReg = new CommandRegister();
                plugin.onRegisterCommands(cmdReg);
                cmdReg.getCommands().forEach(commandLauncher -> {
                    Command command = new BCommandWrapper(new BCommand(commandLauncher));
                    map.register(commandLauncher.getName(), command);
                });
            } catch (Throwable e) {
                e.printStackTrace();
            }

            this.plugins.add(plugin);
            return;
        }
        File folder = this.core.getRawPlatform().getTranslatePluginsFolder();
        this.plugins.addAll(this.loadPlugins(folder));
    }

    @Override
    public void onDisable() {
        this.plugins.forEach(CorePlugin::onShutdown);
        TranslateCore.getScheduleManager().getSchedules().parallelStream().forEach(Scheduler::cancel);

    }

    @Override
    public void onEnable() {
        this.core.init2(this);
        this.plugins.forEach(plugin -> {
            plugin.onCoreReady();
            Bukkit.getScheduler().runTask((Plugin) plugin.getPlatformLauncher(), plugin::onCoreFinishedInit);
        });
    }

    private <T> T getFromField(Object from, String field) throws NoSuchFieldException, IllegalAccessException {
        Field jField = from.getClass().getDeclaredField(field);
        jField.setAccessible(true);
        return (T) jField.get(from);
    }

    private List<CorePlugin> loadPlugins(File folder) {
        if (!folder.exists()) {
            try {
                Files.createDirectories(folder.toPath());
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }
        File[] files = folder.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        List<CorePlugin> plugins = CommonLoad.loadPlugin(this.getClassLoader(), files);
        PluginManager pluginManager = Bukkit.getPluginManager();

        try {
            this.registerCommands(pluginManager, plugins);
        } catch (Throwable e) {
            TranslateCore.getConsole().sendMessage(Component.text("Failed to load commands. Ignoring"));
            e.printStackTrace();
            plugins.parallelStream().forEach(plugin -> plugin.onConstruct(TranslateCoreBoot.this));
            return plugins;

        }
        return plugins;
    }

    private void registerCommands(PluginManager manager, Collection<CorePlugin> plugins) throws Throwable {
        Set<CoreBukkitPluginWrapper> bukkitPlugins = plugins
                .parallelStream()
                .map(CoreBukkitPluginWrapper::new)
                .collect(Collectors.toSet());

        CommandMap map = this.getCommandMap(manager);
        bukkitPlugins.forEach(plugin -> {
            CommandRegister cmdReg = new CommandRegister();
            plugin.getPlugin().onRegisterCommands(cmdReg);
            cmdReg.getCommands().forEach(commandLauncher -> {
                Command command = new BCommandWrapper(new BCommand(commandLauncher));
                map.register(commandLauncher.getName(), command);
            });
        });
        bukkitPlugins
                .parallelStream()
                .forEach(plugin -> plugin.getPlugin().onConstruct(plugin, new BJavaLogger(plugin.getLogger())));
    }
}
