CREATE TABLE tb_plantacao (
    id_plantacao BIGINT NOT NULL AUTO_INCREMENT,
    id_talhao BIGINT NOT NULL,
    id_cultura BIGINT NOT NULL,
    data_plantio DATE NOT NULL,
    data_colheita DATE,
    quantidade_plantada DECIMAL(12,2) NOT NULL,
    quantidade_colhida DECIMAL(12,2),
    status VARCHAR(50) NOT NULL DEFAULT 'EM_ANDAMENTO',
    observacoes VARCHAR(500),

    PRIMARY KEY (id_plantacao),
    CONSTRAINT fk_plantacao_talhao FOREIGN KEY (id_talhao) REFERENCES tb_talhao(id_talhao),
    CONSTRAINT fk_plantacao_cultura FOREIGN KEY (id_cultura) REFERENCES tb_cultura(id_cultura)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
