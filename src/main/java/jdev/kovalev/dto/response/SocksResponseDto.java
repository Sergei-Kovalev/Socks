package jdev.kovalev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocksResponseDto {
    private UUID id;
    private String color;
    private String cottonPercentage;
    private Integer number;
}
