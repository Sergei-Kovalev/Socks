package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.IncomeRequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.mapper.SocksMapper;
import jdev.kovalev.repository.SocksRepository;
import jdev.kovalev.service.SocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {
    private final SocksRepository socksRepository;
    private final SocksMapper socksMapper;

    @Override
    @Transactional
    public SocksResponseDto income(IncomeRequestDto requestDto) {
        double cottonPercentage = (double) requestDto.getCottonPercentage() / 100;
        return socksRepository.findSocksByColorAndCottonPercentage(requestDto.getColor(), cottonPercentage)
                .map(socks -> {
                    socks.setNumber(requestDto.getNumber() + socks.getNumber());
                    return socksMapper.fromSocksToSocksResponseDto(socks);
                })
                .orElseGet(() -> {
                    Socks socksForSave = Socks.builder()
                            .color(requestDto.getColor())
                            .cottonPercentage(cottonPercentage)
                            .number(requestDto.getNumber())
                            .build();
                    Socks savedSocks = socksRepository.save(socksForSave);
                    return socksMapper.fromSocksToSocksResponseDto(savedSocks);
                });
    }
}
