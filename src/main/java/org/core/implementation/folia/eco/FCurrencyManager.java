package org.core.implementation.folia.eco;

import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.implementation.folia.eco.vault.VaultCurrencyManager;
import org.core.eco.account.NamedAccount;
import org.core.eco.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FCurrencyManager implements CurrencyManager {

    private final @Nullable CurrencyManager using;

    public FCurrencyManager() {
        if (VaultCurrencyManager.canUseVault()) {
            using = new VaultCurrencyManager();
            return;
        }
        using = null;
    }

    @Override
    public boolean areCurrenciesSupported() {
        if (using == null) {
            return false;
        }
        return using.areCurrenciesSupported();
    }

    @Override
    public boolean isEconomyEnabled() {
        if (using == null) {
            return false;
        }
        return using.isEconomyEnabled();
    }

    @Override
    public @NotNull Currency getDefaultCurrency() {
        if (using == null) {
            throw new IllegalStateException("Economy is not enabled");
        }
        return using.getDefaultCurrency();
    }

    @Override
    public @NotNull Collection<Currency> getCurrencies() {
        if (using == null) {
            return Collections.emptyList();
        }
        return using.getCurrencies();
    }

    @Override
    public @NotNull CompletableFuture<Optional<NamedAccount>> getSourceFor(@NotNull String accountName) {
        if (this.using == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return this.using.getSourceFor(accountName);
    }

    @Override
    public @NotNull CompletableFuture<Optional<PlayerAccount>> getSourceFor(@NotNull UUID uuid) {
        if (this.using == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return this.using.getSourceFor(uuid);
    }
}
