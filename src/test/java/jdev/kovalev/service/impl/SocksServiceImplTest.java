package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.IncomeRequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.mapper.SocksMapper;
import jdev.kovalev.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class SocksServiceImplTest {
    @Mock
    private SocksRepository socksRepository;

    @Mock
    private SocksMapper socksMapper;

    @InjectMocks
    private SocksServiceImpl socksService;

    private IncomeRequestDto incomeRequestDto;
    private Socks socks;

    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("e487f331-5b7d-40b9-b49d-b41f94b2c960");
        incomeRequestDto = IncomeRequestDto.builder()
                .color("white")
                .cottonPercentage(23)
                .number(1)
                .build();
        socks = Socks.builder()
                .color("white")
                .cottonPercentage(0.23)
                .number(800)
                .build();
    }

    @Test
    void income_whenAlreadyPresent() {
        SocksResponseDto expected = SocksResponseDto.builder()
                .id(uuid)
                .color("white")
                .cottonPercentage("23%")
                .number(801)
                .build();

        Mockito.when(socksRepository.findSocksByColorAndCottonPercentage(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(socks));
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(socks))
                .thenReturn(expected);

        SocksResponseDto actual = socksService.income(incomeRequestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void income_whenNotPresent() {
        SocksResponseDto expected = SocksResponseDto.builder()
                .id(uuid)
                .color("white")
                .cottonPercentage("23%")
                .number(1)
                .build();

        Socks socksForSave = Socks.builder()
                .color("white")
                .cottonPercentage(0.23)
                .number(1)
                .build();

        Socks savedSocks = Socks.builder()
                .id(uuid)
                .color("white")
                .cottonPercentage(0.23)
                .number(1)
                .build();

        Mockito.when(socksRepository.findSocksByColorAndCottonPercentage(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(socksRepository.save(socksForSave))
                .thenReturn(savedSocks);
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(savedSocks))
                .thenReturn(expected);

        SocksResponseDto actual = socksService.income(incomeRequestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }
}