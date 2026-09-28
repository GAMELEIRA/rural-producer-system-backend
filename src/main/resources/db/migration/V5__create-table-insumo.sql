CREATE TABLE tb_insumo (
    id_insumo BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    unidade_medida VARCHAR(20) NOT NULL,
    quantidade_estoque DECIMAL(14,3) NOT NULL DEFAULT 0,
    preco_unitario DECIMAL(12,2) NOT NULL,
    observacoes VARCHAR(500),

    PRIMARY KEY (id_insumo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
