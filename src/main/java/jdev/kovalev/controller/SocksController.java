package jdev.kovalev.controller;

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
public class SocksController {

    private final SocksService socksService;

    @PostMapping("/socks/income")
    public ResponseEntity<SocksResponseDto> income(@RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.income(requestDto));
    }

    @PostMapping("/socks/outcome")
    public ResponseEntity<SocksResponseDto> outcome(@RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.outcome(requestDto));
    }

    @GetMapping("/socks")
    public ResponseEntity<List<SocksResponseDto>> filter(@RequestParam(required = false) String color,
                                                         @RequestParam(required = false) Integer cottonMoreThan,
                                                         @RequestParam(required = false) Integer cottonLessThan,
                                                         @RequestParam(required = false) Integer cottonEqual,
                                                         @RequestParam(required = false) Integer numberMoreThan,
                                                         @RequestParam(required = false) Integer numberLessThan,
                                                         @RequestParam(required = false) Integer numberEqual) {
        return ResponseEntity.ok(socksService.filter(color, cottonMoreThan, cottonLessThan, cottonEqual,
                                                     numberMoreThan, numberLessThan, numberEqual));
    }

    @PutMapping("/socks/{id}")
    public ResponseEntity<SocksResponseDto> update(@PathVariable String id,
                                                   @RequestBody @Valid RequestDto requestDto) {
        return ResponseEntity.ok(socksService.update(id, requestDto));
    }
}
