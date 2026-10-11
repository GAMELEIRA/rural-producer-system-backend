CREATE TABLE tb_aplicacao_insumo (
    id_aplicacao BIGINT NOT NULL AUTO_INCREMENT,
    id_plantacao BIGINT NOT NULL,
    id_insumo BIGINT NOT NULL,
    data_aplicacao DATE NOT NULL,
    quantidade DECIMAL(14,3) NOT NULL,
    custo_total DECIMAL(12,2) NOT NULL,
    observacoes VARCHAR(500),

    PRIMARY KEY (id_aplicacao),
    CONSTRAINT fk_aplicacao_plantacao FOREIGN KEY (id_plantacao) REFERENCES tb_plantacao(id_plantacao),
    CONSTRAINT fk_aplicacao_insumo FOREIGN KEY (id_insumo) REFERENCES tb_insumo(id_insumo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
