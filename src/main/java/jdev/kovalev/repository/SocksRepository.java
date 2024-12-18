package jdev.kovalev.repository;

import jdev.kovalev.entity.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocksRepository extends JpaRepository<Socks, UUID> {
    Optional<Socks> findSocksByColorAndCottonPercentage(String color, Double cottonPercentage);
}
