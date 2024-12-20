package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.exception.NotEnoughSocksException;
import jdev.kovalev.exception.SocksNotFoundException;
import jdev.kovalev.exception.UnlogicalFilterConditionException;
import jdev.kovalev.exception.WrongUUIDFormatException;
import jdev.kovalev.mapper.SocksMapper;
import jdev.kovalev.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
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

    @Test
    @SuppressWarnings("unchecked")
    void filter_whenParametersCorrect() {
        Socks blue = Socks.builder().color("blue").build();
        Socks red = Socks.builder().color("red").build();

        SocksResponseDto blueDto = SocksResponseDto.builder().color("blue").build();
        SocksResponseDto redDto = SocksResponseDto.builder().color("red").build();
        List<SocksResponseDto> expected = List.of(blueDto, redDto);

        List<Socks> socksList = List.of(blue, red);

        Mockito.when(socksRepository.findAll(Mockito.any(Specification.class), Mockito.any(Sort.class)))
                .thenReturn(socksList);
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(blue))
                .thenReturn(blueDto);
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(red))
                .thenReturn(redDto);

        List<SocksResponseDto> actual = socksService.filter(
                null, 10, 20, null,
                null, null, null);

        assertThat(actual)
                .isNotNull()
                .hasSize(2)
                .hasSameElementsAs(expected);
    }

    @Test
    void filter_whenParametersIncorrect_tooManyCottonParameters_thenThrowException() {
        Integer cottonMoreThan = 10;
        Integer cottonLessThan = 20;
        Integer cottonEqual = 15;

        assertThatThrownBy(() -> socksService.filter(null, cottonMoreThan, cottonLessThan, cottonEqual,
                                                     null, null, null))
                .isInstanceOf(UnlogicalFilterConditionException.class)
                .hasMessageContaining("Параметры фильтрации выбраны некорректно");
    }

    @Test
    void filter_whenParametersIncorrect_tooManyNumberParameters_thenThrowException() {
        Integer numberMoreThan = 10;
        Integer numberLessThan = 20;
        Integer numberEqual = 15;

        assertThatThrownBy(() -> socksService.filter(null, null, null, null,
                                                     numberMoreThan, numberLessThan, numberEqual))
                .isInstanceOf(UnlogicalFilterConditionException.class)
                .hasMessageContaining("Параметры фильтрации выбраны некорректно");
    }

    @Test
    void filter_whenParametersIncorrect_wrongCottonMoreAndLess_thenThrowException() {
        Integer cottonMoreThan = 20;
        Integer cottonLessThan = 10;

        assertThatThrownBy(() -> socksService.filter(null, cottonMoreThan, cottonLessThan, null,
                                                     null, null, null))
                .isInstanceOf(UnlogicalFilterConditionException.class)
                .hasMessageContaining("Параметры фильтрации выбраны некорректно");
    }

    @Test
    void filter_whenParametersIncorrect_wrongNumberMoreAndLess_thenThrowException() {
        Integer numberMoreThan = 20;
        Integer numberLessThan = 10;

        assertThatThrownBy(() -> socksService.filter(null, null, null, null,
                                                     numberMoreThan, numberLessThan, null))
                .isInstanceOf(UnlogicalFilterConditionException.class)
                .hasMessageContaining("Параметры фильтрации выбраны некорректно");
    }

    @Test
    void update_whenParametersCorrect() {
        Mockito.when(socksRepository.findById(uuid))
                .thenReturn(Optional.of(socks));
        Mockito.when(socksMapper.fromSocksToSocksResponseDto(Mockito.any()))
                .thenReturn(responseDto);

        SocksResponseDto actual = socksService.update(uuid.toString(), requestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(responseDto);
    }

    @Test
    void update_whenUUIDIncorrect() {
       assertThatThrownBy(() -> socksService.update("bla bla bla", requestDto))
               .isInstanceOf(WrongUUIDFormatException.class)
               .hasMessageContaining("Неверный формат id - он должен быть формата UUID");
    }

    @Test
    void update_whenSocksNotFound() {
        Mockito.when(socksRepository.findById(uuid))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> socksService.update(uuid.toString(), requestDto))
                .isInstanceOf(SocksNotFoundException.class)
                .hasMessageContaining("На складе нет носков с таким id");
    }
}