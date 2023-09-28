package task.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {
    private Integer id;
    private LocalDateTime dateTransaction;
    private TypeTransaction typeTransaction;
    private Account account;
    private Account accountTransfer;
    private Double amount;
    private String note;
}
