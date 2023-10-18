package org.core.implementation.folia.world.boss;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.core.TranslateCore;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.folia.entity.BLiveEntity;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.world.boss.ServerBossBar;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BServerBossBar implements ServerBossBar {

    final BossBar bossBar;

    public BServerBossBar() {
        this(BossBar.bossBar(Component.empty(), 0, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS));
    }

    public BServerBossBar(BossBar boss) {
        this.bossBar = boss;
    }

    @Override
    public BossBar bossBar() {
        return this.bossBar;
    }

    @Override
    public Set<LivePlayer> getPlayers() {
        return Bukkit
                .getOnlinePlayers()
                .stream()
                .filter(player -> StreamSupport
                        .stream(player.activeBossBars().spliterator(), false)
                        .anyMatch(bossBar -> bossBar.equals(this.bossBar())))
                .map(player -> (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(player))
                .collect(Collectors.toSet());
    }

    @Override
    public ServerBossBar register(LivePlayer... players) {
        for (LivePlayer player : players) {
            this.bossBar.addViewer(((BLiveEntity<Player>) player).getBukkitEntity());
        }
        return this;
    }

    @Override
    public ServerBossBar deregister(LivePlayer... players) {
        for (LivePlayer player : players) {
            this.bossBar.removeViewer(((BLiveEntity<Player>) player).getBukkitEntity());
        }
        return this;
    }
}
