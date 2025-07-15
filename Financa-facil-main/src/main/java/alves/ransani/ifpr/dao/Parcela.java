package alves.ransani.ifpr.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    private boolean paga;

    @ManyToOne
    private ContaParcelada contaParcelada;
}
