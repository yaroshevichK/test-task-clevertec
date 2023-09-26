package task.clevertec.entity.response;

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
public class AccountResponse implements Serializable {
    private Integer id;
    private String number;
    private CurrencyResponse currency;
    private BankResponse bank;
    private UserResponse user;
    private LocalDate dateOpen;
    private Double balance;
    private LocalDate dateIncomePercent;
}
