package org.core.implementation.folia.eco.vault;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.core.eco.Currency;
import org.core.eco.account.PlayerAccount;
import org.core.eco.transaction.Transaction;
import org.core.eco.transaction.pending.PendingSingleTransactionImpl;
import org.core.eco.transaction.pending.PendingTransaction;
import org.core.eco.transaction.result.impl.TransactionResultImpl;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerVaultAccount implements PlayerAccount {

    private final @NotNull OfflinePlayer player;

    public PlayerVaultAccount(@NotNull OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public @NotNull UUID getId() {
        return this.player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return Objects.requireNonNull(this.player.getName());
    }

    @Override
    public @NotNull PendingTransaction transact(@NotNull Transaction transaction) {
        double originalAmount = VaultService.getBalance(this.player).orElse(0.0);

        EconomyResponse response = switch (transaction.getType()) {
            case DEPOSIT -> VaultService.deposit(this.player, transaction.getAmount().doubleValue());
            case WITHDRAW -> VaultService.withdraw(this.player, transaction.getAmount().doubleValue());
            case SET -> VaultService.setBalance(this.player, transaction.getAmount().doubleValue());
        };
        TransactionResultImpl result;
        if (response.transactionSuccess()) {
            result = new TransactionResultImpl(transaction, BigDecimal.valueOf(originalAmount),
                                               BigDecimal.valueOf(response.amount));
        } else {
            result = new TransactionResultImpl(transaction, BigDecimal.valueOf(originalAmount), response.errorMessage);
        }
        return new PendingSingleTransactionImpl(this, transaction, CompletableFuture.completedFuture(result));
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Currency currency) {
        return VaultService.getBalance(this.player).map(BigDecimal::valueOf).orElse(BigDecimal.ZERO);
    }
}
