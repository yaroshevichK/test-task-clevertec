package task.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum TypeTransaction implements Serializable {
    INCOME("Пополнение"),
    EXPENSE("Снятие"),
    TRANSFER("Перевод другому клиенту"),
    TRANSFER_OTHER_BANK("Перевод в другой банк");

    private final String name;

    public static TypeTransaction getType(int index) {
        try {
            return values()[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
