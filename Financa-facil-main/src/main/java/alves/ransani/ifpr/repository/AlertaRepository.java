package alves.ransani.ifpr.repository;

import alves.ransani.ifpr.dao.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByUsuarioId(Long usuarioId);
}
