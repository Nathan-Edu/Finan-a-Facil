package alves.ransani.ifpr.repository;

import alves.ransani.ifpr.dao.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    List<Parcela> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);
}
