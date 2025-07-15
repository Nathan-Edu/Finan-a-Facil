package br.com.financa.web.bean;

import br.com.financa.web.dto.DespesaResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@ViewScoped
@Getter
@Setter
public class DespesaBean implements Serializable {

    private List<DespesaResponseDto> despesas = new ArrayList<>();
    private List<DespesaResponseDto> despesasFiltradas = new ArrayList<>();

    private String filtroDescricao;
    private String filtroCategoria = "ALL";
    private BigDecimal totalDespesas;
    private int qtdDespesas;

    private final String API_BASE = "http://localhost:8080/api/despesas";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        carregarDespesas();
        filtrar();
    }

    public void carregarDespesas() {
        try {
            Long usuarioId = loginBean.getUsuario().getId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/usuario/" + usuarioId))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            despesas = objectMapper.readValue(response.body(), new TypeReference<>() {});
            despesasFiltradas = new ArrayList<>(despesas);
            aplicarResumo();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            despesas = new ArrayList<>();
            despesasFiltradas = new ArrayList<>();
        }
    }

    public void aplicarResumo() {
        List<DespesaResponseDto> doMes = despesasFiltradas.stream()
                .filter(d -> d.getData() != null &&
                        d.getData().getMonthValue() == LocalDate.now().getMonthValue() &&
                        d.getData().getYear() == LocalDate.now().getYear())
                .toList();

        totalDespesas = doMes.stream()
                .map(DespesaResponseDto::getValor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        qtdDespesas = doMes.size();
    }

    public void filtrar() {
        despesasFiltradas = despesas.stream()
                .filter(d -> (filtroDescricao == null || d.getDescricao().toLowerCase().contains(filtroDescricao.toLowerCase())) &&
                        (filtroCategoria.equals("ALL") || d.getCategoria().equalsIgnoreCase(filtroCategoria)))
                .toList();
        aplicarResumo();
    }

    public String getTotalDespesasFormatado() {
        return String.format("R$ %.2f", totalDespesas);
    }

    public List<SelectItem> getCategorias() {
        List<SelectItem> itens = new ArrayList<>();
        itens.add(new SelectItem("ALL", "Todas as categorias"));
        itens.add(new SelectItem("MORADIA", "Moradia"));
        itens.add(new SelectItem("ALIMENTACAO", "Alimentação"));
        itens.add(new SelectItem("SAUDE", "Saúde"));
        itens.add(new SelectItem("TRANSPORTE", "Transporte"));
        itens.add(new SelectItem("OUTROS", "Outros"));
        return itens;
    }

    public void excluir(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                despesas.removeIf(d -> d.getId().equals(id));
                despesasFiltradas.removeIf(d -> d.getId().equals(id));
                aplicarResumo();

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Despesa excluída com sucesso."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir despesa."));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
