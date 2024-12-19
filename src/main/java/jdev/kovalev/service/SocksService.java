package jdev.kovalev.service;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;

public interface SocksService {
    SocksResponseDto income(RequestDto requestDto);
    SocksResponseDto outcome(RequestDto requestDto);
}
