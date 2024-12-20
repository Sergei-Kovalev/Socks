package jdev.kovalev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.service.SocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "Socks APIs")
public class SocksController {
    private final SocksService socksService;

    @Operation(summary = "Регистрация прихода носков")
    @PostMapping("/socks/income")
    public ResponseEntity<SocksResponseDto> income(@RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.income(requestDto));
    }

    @Operation(summary = "Регистрация отпуска носков")
    @PostMapping("/socks/outcome")
    public ResponseEntity<SocksResponseDto> outcome(@RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.outcome(requestDto));
    }

    @Operation(summary = "Получение общего количества носков с фильтрацией")
    @GetMapping("/socks")
    public ResponseEntity<List<SocksResponseDto>> filter(
            @RequestParam(required = false)
            @Parameter(description = "Цвет носков", example = "Красный")
            String color,
            @RequestParam(required = false)
            @Parameter(description = "Содержание хлопка более чем", example = "10")
            Integer cottonMoreThan,
            @RequestParam(required = false)
            @Parameter(description = "Содержание хлопка менее чем", example = "90")
            Integer cottonLessThan,
            @RequestParam(required = false)
            @Parameter(description = "Содержание хлопка равно", example = "50")
            Integer cottonEqual,
            @RequestParam(required = false)
            @Parameter(description = "Количество на складе более чем", example = "1")
            Integer numberMoreThan,
            @RequestParam(required = false)
            @Parameter(description = "Количество на складе менее чем", example = "100")
            Integer numberLessThan,
            @RequestParam(required = false)
            @Parameter(description = "Количество на складе равно", example = "16")
            Integer numberEqual) {
        return ResponseEntity.ok(socksService.filter(color, cottonMoreThan, cottonLessThan, cottonEqual,
                                                     numberMoreThan, numberLessThan, numberEqual));
    }

    @Operation(summary = "Обновление данных носков")
    @PutMapping("/socks/{id}")
    public ResponseEntity<SocksResponseDto> update(@PathVariable String id,
                                                   @RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.update(id, requestDto));
    }

    @Operation(summary = "Загрузка партий носков из Excel файла")
    @PostMapping("/socks/batch")
    public ResponseEntity<String> upload(
            @RequestParam
            @Parameter(description = "Путь к файлу Excel", example = "F:/Excel_example.xlsx")
            String path) {
        return ResponseEntity.ok(socksService.upload(path));
    }
}
