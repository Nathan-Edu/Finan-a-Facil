    package alves.ransani.ifpr.mapper;

    import alves.ransani.ifpr.dao.Receita;
    import alves.ransani.ifpr.dao.Usuario;
    import alves.ransani.ifpr.dto.receita.ReceitaCreateDto;
    import alves.ransani.ifpr.dto.receita.ReceitaResponseDto;

    public class ReceitaMapper {

        public static Receita toEntity(ReceitaCreateDto dto) {
            Receita receita = new Receita();
            receita.setDescricao(dto.getDescricao());
            receita.setValor(dto.getValor());
            receita.setData(dto.getData());
            receita.setCategoria(dto.getCategoria());

            if (dto.getUsuarioId() != null) {
                Usuario usuario = new Usuario();
                usuario.setId(dto.getUsuarioId());
                receita.setUsuario(usuario);
            }

            return receita;
        }


        public static ReceitaResponseDto toResponseDto(Receita receita) {
            ReceitaResponseDto dto = new ReceitaResponseDto();
            dto.setId(receita.getId());
            dto.setDescricao(receita.getDescricao());
            dto.setValor(receita.getValor());
            dto.setData(receita.getData());
            dto.setCategoria(receita.getCategoria());
            return dto;
        }

    }
