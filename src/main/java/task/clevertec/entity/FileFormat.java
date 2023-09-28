package task.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum FileFormat implements Serializable {
    TXT,
    PDF
}
