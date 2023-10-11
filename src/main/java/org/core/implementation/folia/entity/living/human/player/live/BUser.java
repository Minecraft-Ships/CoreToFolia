package org.core.implementation.folia.entity.living.human.player.live;

import org.bukkit.OfflinePlayer;
import org.core.entity.living.human.player.User;

import java.util.UUID;

public class BUser implements User {

    protected final OfflinePlayer user;

    public BUser(OfflinePlayer player) {
        this.user = player;
    }

    public OfflinePlayer getBukkitUser() {
        return this.user;
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.user.getUniqueId();
    }
}
