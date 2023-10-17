package org.core.implementation.folia.world.boss;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.folia.entity.BLiveEntity;
import org.core.implementation.folia.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.folia.world.boss.colour.BBossColour;
import org.core.world.boss.ServerBossBar;
import org.core.world.boss.colour.BossColour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Bukkit.getOnlinePlayers().stream().filter(player -> player.activeBossBars())


        List<org.bukkit.entity.Player> players = this.bossBar.getPlayers();
        Set<LivePlayer> set = new HashSet<>();
        players.forEach(p -> set.add(new BLivePlayer(p)));
        return set;
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
