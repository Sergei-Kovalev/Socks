package jdev.kovalev.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на фронт после операций с носками")
public class SocksResponseDto {
    @Schema(description = "id присвоенное базой данных для данного типа носков",
            example = "a1f8a5e4-f712-46ed-ba8c-2dc3d1823979")
    private UUID id;

    @Schema(description = "Цвет носков", example = "Оранжевый")
    private String cottonPercentage;

    @Schema(description = "Процент содержания хлопка (от 0 до 100)", example = "25")
    private String color;

    @Schema(description = "Количество носков на складе", example = "300")
    private Integer number;
}
