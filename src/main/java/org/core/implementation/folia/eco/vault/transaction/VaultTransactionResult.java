package org.core.implementation.folia.eco.vault.transaction;

import net.milkbowl.vault.economy.EconomyResponse;
import org.core.eco.transaction.Transaction;
import org.core.eco.transaction.result.TransactionResult;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

public class VaultTransactionResult implements TransactionResult {

    private final Transaction transaction;
    private final EconomyResponse response;
    private final double originalBalance;

    public VaultTransactionResult(Transaction transaction, double originalBalance, EconomyResponse response) {
        this.transaction = transaction;
        this.originalBalance = originalBalance;
        this.response = response;
    }

    @Override
    public @NotNull Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public boolean wasSuccessful() {
        return this.response.transactionSuccess();
    }

    @Override
    public @NotNull BigDecimal getOriginalAmount() {
        return BigDecimal.valueOf(this.originalBalance);
    }

    @Override
    public @NotNull BigDecimal getAfterAmount() {
        return BigDecimal.valueOf(this.response.amount);
    }

    @Override
    public Optional<String> getFailedReason() {
        return Optional.ofNullable(this.response.errorMessage);
    }
}
