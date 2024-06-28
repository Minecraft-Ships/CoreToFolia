package org.core.implementation.folia.eco;

import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.eco.account.NamedAccount;
import org.core.eco.account.PlayerAccount;
import org.core.implementation.folia.eco.vault.VaultCurrencyManager;
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
            this.using = new VaultCurrencyManager();
            return;
        }
        this.using = null;
    }

    @Override
    public boolean areCurrenciesSupported() {
        if (this.using == null) {
            return false;
        }
        return this.using.areCurrenciesSupported();
    }

    @Override
    public boolean isEconomyEnabled() {
        if (this.using == null) {
            return false;
        }
        return this.using.isEconomyEnabled();
    }

    @Override
    public @NotNull Currency getDefaultCurrency() {
        if (this.using == null) {
            throw new IllegalStateException("Economy is not enabled");
        }
        return this.using.getDefaultCurrency();
    }

    @Override
    public @NotNull Collection<Currency> getCurrencies() {
        if (this.using == null) {
            return Collections.emptyList();
        }
        return this.using.getCurrencies();
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
