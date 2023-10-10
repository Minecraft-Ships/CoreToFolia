package org.core.implementation.folia.eco.vault;

import org.core.TranslateCore;
import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.entity.living.human.player.User;
import org.core.source.eco.NamedAccount;
import org.core.source.eco.PlayerAccount;
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
        return TranslateCore.getServer().getOfflineUser(uuid).thenApply(op -> op.flatMap(User::getAccount));
    }

    public static boolean canUseVault() {
        return VaultService.getEconomy().isPresent();
    }
}
