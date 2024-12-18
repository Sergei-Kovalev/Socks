package jdev.kovalev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.kovalev.controller.hadler.ExceptionHandlerController;
import jdev.kovalev.dto.request.IncomeRequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SocksControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SocksService socksService;

    @InjectMocks
    private SocksController socksController;

    private ObjectMapper objectMapper;
    private IncomeRequestDto incomeRequestDto;
    private SocksResponseDto socksResponseDto;
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
        incomeRequestDto = IncomeRequestDto.builder()
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

    }

    @Test
    @SneakyThrows
    void income_whenCorrectRequest() {
        String jsonRequestQuery = objectMapper.writeValueAsString(incomeRequestDto);
        String jsonResponseQuery = objectMapper.writeValueAsString(socksResponseDto);

        Mockito.when(socksService.income(incomeRequestDto))
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
        incomeRequestDto.setNumber(1000000);
        String jsonRequestQuery = objectMapper.writeValueAsString(incomeRequestDto);

        Mockito.when(socksService.income(incomeRequestDto))
                .thenReturn(socksResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/socks/income")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestQuery))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(socksService, Mockito.never()).income(Mockito.any());
    }
}