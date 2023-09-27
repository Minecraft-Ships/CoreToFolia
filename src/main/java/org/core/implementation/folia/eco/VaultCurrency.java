package org.core.implementation.folia.eco;

import net.kyori.adventure.text.Component;
import org.core.eco.Currency;
import org.jetbrains.annotations.NotNull;

public class VaultCurrency implements Currency {
    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("vault");
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public @NotNull Component getSymbol() {
        return Component.empty();
    }
}
