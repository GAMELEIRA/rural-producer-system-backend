CREATE TABLE tb_gasto_producao (
    id_gasto BIGINT NOT NULL AUTO_INCREMENT,
    id_plantacao BIGINT NOT NULL,
    descricao VARCHAR(150) NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    data_gasto DATE NOT NULL,
    categoria VARCHAR(50),
    observacoes VARCHAR(500),

    PRIMARY KEY (id_gasto),
    CONSTRAINT fk_gasto_plantacao FOREIGN KEY (id_plantacao) REFERENCES tb_plantacao(id_plantacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
