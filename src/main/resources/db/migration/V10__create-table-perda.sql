CREATE TABLE tb_perda (
    id_perda BIGINT NOT NULL AUTO_INCREMENT,
    id_plantacao BIGINT NOT NULL,
    data_perda DATE NOT NULL,
    quantidade DECIMAL(14,3) NOT NULL,
    motivo VARCHAR(150) NOT NULL,
    valor_estimado DECIMAL(12,2),
    observacoes VARCHAR(500),

    PRIMARY KEY (id_perda),
    CONSTRAINT fk_perda_plantacao FOREIGN KEY (id_plantacao) REFERENCES tb_plantacao(id_plantacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
