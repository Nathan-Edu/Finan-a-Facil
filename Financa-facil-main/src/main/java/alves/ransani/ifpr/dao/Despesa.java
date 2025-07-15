package alves.ransani.ifpr.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @ManyToOne
    private Usuario usuario;


    public enum Categoria {
        ALIMENTACAO,
        MORADIA,
        TRANSPORTE,
        LAZER,
        EDUCACAO,
        SAUDE,
        OUTROS
    }
}
