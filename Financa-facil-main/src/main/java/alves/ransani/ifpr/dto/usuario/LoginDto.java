package alves.ransani.ifpr.dto.usuario;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String email;
    private String senha;
}
