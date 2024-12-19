package jdev.kovalev.controller;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.service.SocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
@Validated
public class SocksController {

    private final SocksService socksService;

    @PostMapping("/socks/income")
    public ResponseEntity<SocksResponseDto> income(@RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.income(requestDto));
    }

    @PostMapping("/socks/outcome")
    public ResponseEntity<SocksResponseDto> ountcome(@RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.outcome(requestDto));
    }
}
