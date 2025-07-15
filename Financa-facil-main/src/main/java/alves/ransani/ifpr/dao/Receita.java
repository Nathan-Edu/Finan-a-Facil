package alves.ransani.ifpr.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    @Column(nullable = false)
    private String categoria;

    @ManyToOne
    private Usuario usuario;


    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }




}




