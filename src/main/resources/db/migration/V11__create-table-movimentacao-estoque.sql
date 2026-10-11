CREATE TABLE tb_movimentacao_estoque (
    id_movimentacao BIGINT NOT NULL AUTO_INCREMENT,
    id_produto BIGINT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    quantidade DECIMAL(14,3) NOT NULL,
    saldo_anterior DECIMAL(14,3) NOT NULL,
    saldo_posterior DECIMAL(14,3) NOT NULL,
    origem VARCHAR(100),
    observacoes VARCHAR(500),

    PRIMARY KEY (id_movimentacao),
    CONSTRAINT fk_movimentacao_produto FOREIGN KEY (id_produto) REFERENCES tb_produto(id_produto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
