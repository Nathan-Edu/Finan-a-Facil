package alves.ransani.ifpr.repository;

import alves.ransani.ifpr.dao.ContaParcelada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaParceladaRepository extends JpaRepository<ContaParcelada, Long> {
    List<ContaParcelada> findByUsuarioId(Long usuarioId);

}

