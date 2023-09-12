package task.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {
    private Integer id;
    private String number;
    private Currency currency;
    private Bank bank;
    private User user;
    private LocalDate dateOpen;
    private Double balance;
    private LocalDate dateIncomePercent;
}
