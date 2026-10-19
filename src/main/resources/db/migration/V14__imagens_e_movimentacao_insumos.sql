ALTER TABLE tb_cultura ADD COLUMN imagem_codigo VARCHAR(40) NULL;
ALTER TABLE tb_insumo ADD COLUMN imagem_codigo VARCHAR(40) NULL;

CREATE TABLE tb_movimentacao_insumo (
    id_movimentacao BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_insumo BIGINT NOT NULL,
    tipo VARCHAR(40) NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    quantidade DECIMAL(14,3) NOT NULL,
    origem VARCHAR(200) NOT NULL,
    CONSTRAINT fk_movimentacao_insumo FOREIGN KEY (id_insumo)
        REFERENCES tb_insumo(id_insumo) ON DELETE CASCADE,
    INDEX idx_mov_insumo_data (id_insumo, data_movimentacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
