package alves.ransani.ifpr.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaParcelada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valorTotal;

    private int quantidadeParcelas;

    private LocalDate dataInicio;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "contaParcelada", cascade = CascadeType.ALL)
    private List<Parcela> parcelas;
}
