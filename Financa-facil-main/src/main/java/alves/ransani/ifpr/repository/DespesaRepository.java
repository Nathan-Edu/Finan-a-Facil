package alves.ransani.ifpr.repository;

import alves.ransani.ifpr.dao.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findByCategoria(Despesa.Categoria categoria);
    List<Despesa> findByUsuarioId(Long usuarioId);


}
