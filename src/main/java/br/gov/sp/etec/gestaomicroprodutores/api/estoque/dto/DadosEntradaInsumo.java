package br.gov.sp.etec.gestaomicroprodutores.api.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record DadosEntradaInsumo(
        @NotNull Long idInsumo,
        @NotNull @DecimalMin("0.001") BigDecimal quantidade,
        @Size(max = 200) String origem
) {}
