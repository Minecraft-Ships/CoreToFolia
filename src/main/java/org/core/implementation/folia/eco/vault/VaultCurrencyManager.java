package org.core.implementation.folia.eco.vault;

import org.core.TranslateCore;
import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.implementation.folia.entity.living.human.player.live.BUser;
import org.core.eco.account.NamedAccount;
import org.core.eco.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VaultCurrencyManager implements CurrencyManager {

    private static final VaultCurrency CURRENCY = new VaultCurrency();

    @Override
    public boolean areCurrenciesSupported() {
        return false;
    }

    @Override
    public boolean isEconomyEnabled() {
        return VaultService.getEconomy().isPresent();
    }

    @Override
    public @NotNull Currency getDefaultCurrency() {
        return CURRENCY;
    }

    @Override
    public @NotNull Collection<Currency> getCurrencies() {
        return Collections.singleton(CURRENCY);
    }

    @Override
    public @NotNull CompletableFuture<Optional<NamedAccount>> getSourceFor(@NotNull String accountName) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public @NotNull CompletableFuture<Optional<PlayerAccount>> getSourceFor(@NotNull UUID uuid) {
        return TranslateCore
                .getServer()
                .getOfflineUser(uuid)
                .thenApply(
                        op -> op.map(user -> (BUser) user).map(user -> new PlayerVaultAccount(user.getBukkitUser())));
    }

    public static boolean canUseVault() {
        return VaultService.getEconomy().isPresent();
    }
}
