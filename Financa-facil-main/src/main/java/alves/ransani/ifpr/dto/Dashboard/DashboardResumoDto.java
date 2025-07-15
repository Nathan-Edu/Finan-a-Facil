package alves.ransani.ifpr.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResumoDto {
    private double totalReceitas;
    private double totalDespesas;
    private double saldo;
    private int totalContasParceladas;
    private int totalAlertas;
}
