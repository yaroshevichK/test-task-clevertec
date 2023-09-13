package task.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum Period implements Serializable {
    MONTH("Месяц"),
    YEAR("Год"),
    ALL("За весь период");

    private final String name;

    public static Period getType(int index) {
        try {
            return values()[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
