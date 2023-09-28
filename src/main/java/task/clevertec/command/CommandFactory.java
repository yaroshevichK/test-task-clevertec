package task.clevertec.command;

import task.clevertec.command.impl.ExpenseMoney;
import task.clevertec.command.impl.IncomeMoney;
import task.clevertec.command.impl.TransferMoney;
import task.clevertec.command.impl.TransferMoneyOther;
import task.clevertec.entity.TypeTransaction;
import task.clevertec.entity.User;

import java.util.Optional;

public class CommandFactory {
    public static Command getCommand(Integer number, User authUser) {
        TypeTransaction typeTransaction = TypeTransaction.getType(number - 1);

        return Optional.ofNullable(typeTransaction)
                .map(
                        type -> switch (type) {
                            case INCOME -> new IncomeMoney(authUser);
                            case EXPENSE -> new ExpenseMoney(authUser);
                            case TRANSFER -> new TransferMoney(authUser);
                            case TRANSFER_OTHER_BANK -> new TransferMoneyOther(authUser);
                        }
                )
                .orElse(null);
    }
}
