package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.exception.NotEnoughSocksException;
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
    public SocksResponseDto income(RequestDto requestDto) {
        double cottonPercentage = (double) requestDto.getCottonPercentage() / 100;
        return socksRepository.findSocksByColorAndCottonPercentage(requestDto.getColor(), cottonPercentage)
                .map(socks -> {
                    socks.setNumber(socks.getNumber() + requestDto.getNumber());
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

    @Override
    public SocksResponseDto outcome(RequestDto requestDto) {
        double cottonPercentage = (double) requestDto.getCottonPercentage() / 100;
        return socksRepository.findSocksByColorAndCottonPercentage(requestDto.getColor(), cottonPercentage)
                .map(socks -> {
                    if (socks.getNumber() < requestDto.getNumber()) {
                        throw new NotEnoughSocksException();
                    } else {
                        socks.setNumber(socks.getNumber() - requestDto.getNumber());
                        return socksMapper.fromSocksToSocksResponseDto(socks);
                    }
                })
                .orElseThrow(NotEnoughSocksException::new);
    }
}
