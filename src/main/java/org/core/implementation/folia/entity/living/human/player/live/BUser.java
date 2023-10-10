package org.core.implementation.folia.entity.living.human.player.live;

import org.bukkit.OfflinePlayer;
import org.core.eco.Currency;
import org.core.entity.living.human.player.User;
import org.core.implementation.folia.eco.vault.VaultService;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
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
