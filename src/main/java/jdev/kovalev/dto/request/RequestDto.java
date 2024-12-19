package jdev.kovalev.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    @NotNull(message = "Цвет носков должен быть указан")
    @Length(max = 20, message = "Ограничение по длине для поля цвет - до 20 символов")
    private String color;

    @NotNull(message = "Процентное содержание хлопка должно быть указано")
    @Max(value = 100, message = "Процентное содержание хлопка не может быть больше 100")
    @PositiveOrZero(message = "Процентное содержание хлопка должно быть больше либо равно 0")
    private Integer cottonPercentage;

    @NotNull(message = "Укажите количество пар носков")
    @Max(value = 100000, message = "Количество не должно превышать 100 тысяч")
    @Positive(message = "Количество носков должно быть положительным числом больше нуля")
    private Integer number;
}
