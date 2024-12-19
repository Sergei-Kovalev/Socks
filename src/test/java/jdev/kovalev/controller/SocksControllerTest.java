package jdev.kovalev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.kovalev.controller.hadler.ExceptionHandlerController;
import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.exception.NotEnoughSocksException;
import jdev.kovalev.exception.UnlogicalFilterConditionException;
import jdev.kovalev.service.SocksService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SocksControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SocksService socksService;

    @InjectMocks
    private SocksController socksController;

    private ObjectMapper objectMapper;
    private RequestDto requestDto;
    private SocksResponseDto socksResponseDto;
    private List<SocksResponseDto> socksResponseDtoList;
    private UUID uuid;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(socksController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        objectMapper = new ObjectMapper();
        uuid = UUID.fromString("1fcc470d-a691-4d0d-9a23-b8b455c5f586");
        requestDto = RequestDto.builder()
                .color("yellow")
                .cottonPercentage(90)
                .number(50)
                .build();
        socksResponseDto = SocksResponseDto.builder()
                .id(uuid)
                .color("yellow")
                .cottonPercentage("90%")
                .number(50)
                .build();

        socksResponseDtoList = List.of(
                SocksResponseDto.builder()
                        .id(uuid)
                        .color("white")
                        .cottonPercentage("20%")
                        .number(30)
                        .build(),
                SocksResponseDto.builder()
                        .id(uuid)
                        .color("white")
                        .cottonPercentage("30%")
                        .number(40)
                        .build());
    }

    @Test
    @SneakyThrows
    void income_whenCorrectRequest() {
        String jsonRequestQuery = objectMapper.writeValueAsString(requestDto);
        String jsonResponseQuery = objectMapper.writeValueAsString(socksResponseDto);

        Mockito.when(socksService.income(requestDto))
                .thenReturn(socksResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/socks/income")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestQuery))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponseQuery))
                .andDo(print());

        Mockito.verify(socksService).income(Mockito.any());
    }

    @Test
    @SneakyThrows
    void income_whenIncorrectRequest_thenReturnErrorResponse() {
        requestDto.setNumber(1000000);
        String jsonRequestQuery = objectMapper.writeValueAsString(requestDto);

        Mockito.when(socksService.income(requestDto))
                .thenReturn(socksResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/socks/income")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestQuery))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(socksService, Mockito.never()).income(Mockito.any());
    }

    @Test
    @SneakyThrows
    void outcome_whenCorrectRequest() {
        requestDto.setNumber(1);
        String jsonRequestQuery = objectMapper.writeValueAsString(requestDto);
        socksResponseDto.setNumber(socksResponseDto.getNumber() - 1);
        String jsonResponseQuery = objectMapper.writeValueAsString(socksResponseDto);

        Mockito.when(socksService.outcome(requestDto))
                .thenReturn(socksResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/socks/outcome")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestQuery))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponseQuery))
                .andDo(print());

        Mockito.verify(socksService).outcome(Mockito.any());
    }

    @Test
    @SneakyThrows
    void outcome_whenNotEnough_orNotPresent_thenReturnErrorResponse() {
        String jsonRequestQuery = objectMapper.writeValueAsString(requestDto);

        Mockito.when(socksService.outcome(requestDto))
                .thenThrow(new NotEnoughSocksException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/socks/outcome")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestQuery))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("На складе недостаточно носков для Вашей операции"))
                .andDo(print());
    }

    @Test
    @SneakyThrows
    void filter_whenCorrectRequest() {
        String color = "white";
        Integer cottonMoreThan = 10;
        Integer cottonLessThan = 50;
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("color", color);
        params.add("cottonMoreThan", String.valueOf(cottonMoreThan));
        params.add("cottonLessThan", String.valueOf(cottonLessThan));

        Mockito.when(socksService.filter(color, cottonMoreThan, cottonLessThan,
                                         null, null, null, null))
                .thenReturn(socksResponseDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/socks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .params(params))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(socksService).filter(Mockito.any(), Mockito.any(),Mockito.any(),
                                            Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    @SneakyThrows
    void filter_whenIncorrectRequest() {
        String color = "white";
        Integer cottonMoreThan = 50;
        Integer cottonLessThan = 10;
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("color", color);
        params.add("cottonMoreThan", String.valueOf(cottonMoreThan));
        params.add("cottonLessThan", String.valueOf(cottonLessThan));

        Mockito.when(socksService.filter(color, cottonMoreThan, cottonLessThan,
                                         null, null, null, null))
                .thenThrow(new UnlogicalFilterConditionException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/socks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .params(params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Параметры фильтрации выбраны некорректно"))
                .andDo(print());
    }
}