package jdev.kovalev.mapper;

import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SocksMapperTest {
    private final SocksMapper socksMapper = new SocksMapperImpl();
    private Socks socks;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("43066641-fcef-472f-9639-edc54730f568");
        socks = Socks.builder()
                .id(uuid)
                .color("black")
                .cottonPercentage(0.25)
                .build();
    }

    @Test
    void fromSocksToSocksResponseDto() {
        SocksResponseDto expected = SocksResponseDto.builder()
                .id(uuid)
                .color("black")
                .cottonPercentage("25%")
                .build();

        SocksResponseDto actual = socksMapper.fromSocksToSocksResponseDto(socks);

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void fromSocksToSocksResponseDto_whenSocksNull() {
        SocksResponseDto actual = socksMapper.fromSocksToSocksResponseDto(null);

        assertThat(actual)
                .isNull();
    }

    @Test
    void fromDoubleToString() {
        double value = 0.33;
        String expected = "33%";

        String actual = socksMapper.fromDoubleToString(value);

        assertThat(actual)
                .isEqualTo(expected);
    }
}