package org.core.implementation.folia.eco.vault.transaction;

import net.milkbowl.vault.economy.EconomyResponse;
import org.core.eco.account.Account;
import org.core.eco.transaction.Transaction;
import org.core.eco.transaction.pending.PendingSingleTransaction;
import org.core.eco.transaction.result.TransactionResult;
import org.core.eco.transaction.result.TransactionsResult;
import org.core.eco.transaction.result.impl.TransactionsResultImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VaultPendingTransaction implements PendingSingleTransaction {

    private final EconomyResponse response;
    private final Transaction transaction;
    private final Account account;
    private final double originalAmount;

    public VaultPendingTransaction(@NotNull Transaction transaction,
                                   @NotNull Account account,
                                   @NotNull EconomyResponse response,
                                   double originalAmount) {
        this.response = response;
        this.account = account;
        this.transaction = transaction;
        this.originalAmount = originalAmount;
    }

    @Override
    public @NotNull Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public @NotNull Account getTarget() {
        return this.account;
    }

    @Override
    public @NotNull TransactionsResult getCurrentResult() {
        return new TransactionsResultImpl(List.of(this.getResult()));
    }

    @Override
    public @NotNull CompletableFuture<TransactionResult> awaitCurrentTransaction() {
        return CompletableFuture.completedFuture(this.getResult());
    }

    @Override
    public @NotNull CompletableFuture<TransactionsResult> awaitComplete() {
        return CompletableFuture.completedFuture(this.getCurrentResult());

    }

    @Override
    public boolean isComplete() {
        return true;
    }

    private TransactionResult getResult() {
        return new VaultTransactionResult(this.transaction, this.originalAmount, this.response);
    }
}
