package org.core.implementation.folia.eco.vault;

import org.bukkit.OfflinePlayer;
import org.core.eco.Currency;
import org.core.eco.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class PlayerVaultAccount implements PlayerAccount {

    private final @NotNull OfflinePlayer player;

    public PlayerVaultAccount(@NotNull OfflinePlayer player) {
        this.player = player;
    }


    @Override
    public BigDecimal getBalance(@NotNull Currency currency) {
        return VaultService.getBalance(this.player).map(BigDecimal::valueOf).orElse(BigDecimal.ZERO);
    }

    @Override
    public void setBalance(@NotNull Currency currency, @NotNull BigDecimal decimal) {
        VaultService.setBalance(this.player, decimal.doubleValue());
    }

    @Override
    public @NotNull UUID getId() {
        return this.player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return Objects.requireNonNull(this.player.getName());
    }
}
