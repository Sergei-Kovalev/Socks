package jdev.kovalev.mapper;

import jdev.kovalev.dto.response.SocksResponseDto;
import jdev.kovalev.entity.Socks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocksMapper {

    @Mapping(target = "cottonPercentage", expression = "java(fromDoubleToString(socks.getCottonPercentage()))")
    SocksResponseDto fromSocksToSocksResponseDto(Socks socks);

    default String fromDoubleToString(Double value) {
        return String.format("%s%%", (int)(value * 100));
    }
}
