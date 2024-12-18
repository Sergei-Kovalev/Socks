package jdev.kovalev.service;

import jdev.kovalev.dto.request.IncomeRequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;

public interface SocksService {
    SocksResponseDto income(IncomeRequestDto requestDto);
}
