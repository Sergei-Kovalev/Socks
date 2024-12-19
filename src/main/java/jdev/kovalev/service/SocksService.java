package jdev.kovalev.service;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;

import java.util.List;

public interface SocksService {
    SocksResponseDto income(RequestDto requestDto);
    SocksResponseDto outcome(RequestDto requestDto);
    List<SocksResponseDto> filter(String color, Integer cottonMoreThan, Integer cottonLessThan, Integer cottonEqual,
                                  Integer numberMoreThan, Integer numberLessThan, Integer numberEqual);

    SocksResponseDto update(String id, RequestDto requestDto);
}
