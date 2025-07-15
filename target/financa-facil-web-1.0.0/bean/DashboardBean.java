package webapp.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import response.DespesaResponseDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
@Getter @Setter
public class DashboardBean implements Serializable {

    private List<DespesaResponseDto> despesas;

    @PostConstruct
    public void init() {
        try {
            WebClient client = WebClient.create("http://localhost:8080");

            despesas = client.get()
                    .uri("/despesas")
                    .retrieve()
                    .bodyToFlux(DespesaResponseDto.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException e) {
            e.printStackTrace();
            despesas = new ArrayList<>();
        }
    }
}
