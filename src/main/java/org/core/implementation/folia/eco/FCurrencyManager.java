package org.core.implementation.folia.eco;

import org.core.TranslateCore;
import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.implementation.folia.VaultService;
import org.core.source.eco.EcoSource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FCurrencyManager implements CurrencyManager {

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
    public CompletableFuture<Optional<EcoSource>> getSourceFor(@NotNull UUID uuid) {
        return TranslateCore.getServer().getOfflineUser(uuid).thenApply(op -> op.map(t -> t));
    }
}
