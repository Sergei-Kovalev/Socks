package jdev.kovalev.repository;

import jdev.kovalev.controller.TestContainersConfiguration;
import jdev.kovalev.entity.Socks;
import jdev.kovalev.service.specification.SocksFilterSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestContainersConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/socks-data-init.sql"})
class SocksRepositoryTest {

    @Autowired
    private SocksRepository socksRepository;

    @Test
    void findSocksByColorAndCottonPercentage_whenPresentInDatabase() {
        String color = "red";
        Double cottonPercentage = 0.90;
        Socks expected = Socks.builder()
                .id(UUID.fromString("386a6e15-ff4c-4202-a135-a2847acc4e92"))
                .color(color)
                .cottonPercentage(cottonPercentage)
                .number(135).build();

        Socks actual = socksRepository.findSocksByColorAndCottonPercentage(color, cottonPercentage).orElseThrow();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void findSocksByColorAndCottonPercentage_whenNotPresentInDatabase() {
        String color = "red";
        Double cottonPercentage = 1.00;

        Optional<Socks> actual = socksRepository.findSocksByColorAndCottonPercentage(color, cottonPercentage);

        assertThat(actual)
                .isEmpty();
    }

    @Test
    void findAllByFilter() {
        String color = "yellow";
        List<Socks> expected = List.of(
                Socks.builder()
                        .id(UUID.fromString("9d1b9573-a16e-4591-bad5-16df7a8339a5"))
                        .color(color)
                        .cottonPercentage(0.20)
                        .number(20)
                        .build(),
                Socks.builder()
                        .id(UUID.fromString("02c2a042-6016-4949-8f87-41bb98987118"))
                        .color(color)
                        .cottonPercentage(0.60)
                        .number(50)
                        .build());


        Specification<Socks> specification = SocksFilterSpecification.hasColor(color);
        Sort sort = Sort.by(Sort.Direction.ASC, "cottonPercentage");
        List<Socks> actual = socksRepository.findAll(specification, sort);

        assertThat(actual)
                .hasSize(2)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void findByUUID_whenPresentInDatabase() {
        UUID uuid = UUID.fromString("c5dd15cc-c091-4fee-95e5-f3d5244e0050");
        Socks expected = Socks.builder()
                .id(UUID.fromString("c5dd15cc-c091-4fee-95e5-f3d5244e0050"))
                .color("red")
                .cottonPercentage(0.10)
                .number(10)
                .build();

        Socks actual = socksRepository.findById(uuid).orElseThrow();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void findByUUID_whenNotPresentInDatabase() {
        UUID uuid = UUID.fromString("c6dd15cc-c091-4fee-95e5-f3d5244e0051");

        Optional<Socks> actual = socksRepository.findById(uuid);

        assertThat(actual)
                .isEmpty();
    }
}