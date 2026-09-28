CREATE TABLE tb_produto (
    id_produto BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    unidade_medida VARCHAR(20) NOT NULL,
    preco_venda DECIMAL(12,2) NOT NULL,
    quantidade_estoque DECIMAL(14,3) NOT NULL DEFAULT 0,
    observacoes VARCHAR(500),

    PRIMARY KEY (id_produto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
