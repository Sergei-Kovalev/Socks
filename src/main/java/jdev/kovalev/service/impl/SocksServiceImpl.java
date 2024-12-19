package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.exception.NotEnoughSocksException;
import jdev.kovalev.exception.UnlogicalFilterConditionException;
import jdev.kovalev.mapper.SocksMapper;
import jdev.kovalev.repository.SocksRepository;
import jdev.kovalev.service.SocksService;
import jdev.kovalev.service.specification.SocksFilterSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
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

    @Override
    public List<SocksResponseDto> filter(String color, Integer cottonMoreThan, Integer cottonLessThan,
                                         Integer cottonEqual, Integer numberMoreThan, Integer numberLessThan,
                                         Integer numberEqual) {
        Specification<Socks> specification = configureSpecification(color, cottonMoreThan, cottonLessThan,
                                                                         cottonEqual, numberMoreThan, numberLessThan, 
                                                                         numberEqual);
        Sort sortByColor = Sort.by(Sort.Direction.ASC, "color");
        Sort sortByCottonPercentage = Sort.by(Sort.Direction.ASC, "cottonPercentage");

        Sort sort = sortByColor.and(sortByCottonPercentage);

        return socksRepository.findAll(specification, sort).stream()
                .map(socksMapper::fromSocksToSocksResponseDto)
                .toList();
    }

    private Specification<Socks> configureSpecification(String color, Integer cottonMoreThan, Integer cottonLessThan,
                                                        Integer cottonEqual, Integer numberMoreThan,
                                                        Integer numberLessThan, Integer numberEqual) {
        if (cottonEqual != null && (cottonMoreThan != null || cottonLessThan != null)) {
            throw new UnlogicalFilterConditionException();
        }
        if (numberEqual != null && (numberMoreThan != null || numberLessThan != null)) {
            throw new UnlogicalFilterConditionException();
        }
        if ((cottonMoreThan != null && cottonLessThan != null) && cottonMoreThan > cottonLessThan) {
            throw new UnlogicalFilterConditionException();
        }
        if ((numberMoreThan != null && numberLessThan != null) && numberMoreThan > numberLessThan) {
            throw new UnlogicalFilterConditionException();
        }
        return SocksFilterSpecification.hasColor(color)
                .and(SocksFilterSpecification.hasCottonMoreThan(cottonMoreThan))
                .and(SocksFilterSpecification.hasCottonLessThan(cottonLessThan))
                .and(SocksFilterSpecification.hasCottonEqual(cottonEqual))
                .and(SocksFilterSpecification.hasNumberMoreThan(numberMoreThan))
                .and(SocksFilterSpecification.hasNumberLessThan(numberLessThan))
                .and(SocksFilterSpecification.hasNumberEqual(numberEqual));
    }
}
