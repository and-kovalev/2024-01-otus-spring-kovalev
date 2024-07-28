package ru.otus.hw.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class RegistrationPersonDto {
    @NotBlank(message = "Отсутствует поле ФИО родителя")
    @Length(min = 10, message = "ФИО родителя должно быть от 10 символов")
    private String fio;

    @NotBlank(message = "Отсутствует поле телефон родителя")
    @Digits(integer = 11, fraction = 0, message = "Необходимо задать телефон родителя")
    private String telephone;

    @NotBlank(message = "Отсутствует поле ФИО школьника")
    @Length(min = 10, message = "ФИО школьника должно быть от 10 символов")
    private String childFio;

    @NotBlank(message = "Отсутствует поле телефон школьника")
    @Digits(integer = 11, fraction = 0, message = "Необходимо задать телефон школьника")
    private String childTelephone;

}
