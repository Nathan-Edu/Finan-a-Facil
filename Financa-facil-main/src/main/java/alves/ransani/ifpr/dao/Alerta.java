package alves.ransani.ifpr.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    private LocalDate dataGeracao;

    private boolean visualizado;

    @ManyToOne
    private Usuario usuario;

}
