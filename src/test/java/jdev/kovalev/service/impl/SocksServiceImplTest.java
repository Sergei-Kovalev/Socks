package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.exception.NotEnoughSocksException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class SocksServiceImplTest {
    @Mock
    private SocksRepository socksRepository;

    @Mock
    private SocksMapper socksMapper;

    @InjectMocks
    private SocksServiceImpl socksService;

    private RequestDto requestDto;
    private Socks socks;
    private SocksResponseDto responseDto;

    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("e487f331-5b7d-40b9-b49d-b41f94b2c960");
        requestDto = RequestDto.builder()
                .color("white")
                .cottonPercentage(23)
                .number(1)
                .build();
        socks = Socks.builder()
                .color("white")
                .cottonPercentage(0.23)
                .number(800)
                .build();
        responseDto = SocksResponseDto.builder()
                .id(uuid)
                .color("white")
                .cottonPercentage("23%")
                .number(801)
                .build();
    }

    @Test
    void income_whenAlreadyPresent() {
        Mockito.when(socksRepository.findSocksByColorAndCottonPercentage(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(socks));
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(socks))
                .thenReturn(responseDto);

        SocksResponseDto actual = socksService.income(requestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(responseDto);
    }

    @Test
    void income_whenNotPresent() {
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
                .thenReturn(responseDto);

        SocksResponseDto actual = socksService.income(requestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(responseDto);
    }

    @Test
    void outcome_whenEnough() {
        Mockito.when(socksRepository.findSocksByColorAndCottonPercentage(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(socks));
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(socks))
                .thenReturn(responseDto);

        SocksResponseDto actual = socksService.outcome(requestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(responseDto);
    }

    @Test
    void outcome_whenPresent_butNotEnough_thenThrowException() {
        socks.setNumber(1);
        Mockito.when(socksRepository.findSocksByColorAndCottonPercentage(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(socks));

        assertThatThrownBy(() -> socksService.outcome(requestDto))
                .isInstanceOf(NotEnoughSocksException.class)
                .hasMessageContaining("На складе недостаточно носков для Вашей операции");
    }

    @Test
    void outcome_whenNotPresent_thenThrowException() {
        socks.setColor("Strange color");
        Mockito.when(socksRepository.findSocksByColorAndCottonPercentage(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(socks));

        assertThatThrownBy(() -> socksService.outcome(requestDto))
                .isInstanceOf(NotEnoughSocksException.class)
                .hasMessageContaining("На складе недостаточно носков для Вашей операции");
    }
}