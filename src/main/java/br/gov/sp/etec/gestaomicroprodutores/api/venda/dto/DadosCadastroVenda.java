package br.gov.sp.etec.gestaomicroprodutores.api.venda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record DadosCadastroVenda(
        @Size(max = 150, message = "O nome do cliente deve ter no máximo 150 caracteres")
        String cliente,

        @Size(max = 50, message = "A forma de pagamento deve ter no máximo 50 caracteres")
        String formaPagamento,

        LocalDateTime dataVenda,

        @NotEmpty(message = "A venda deve conter ao menos um item")
        @Valid
        List<DadosItemVenda> itens,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes
) {
}
