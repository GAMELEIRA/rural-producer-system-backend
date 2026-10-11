CREATE TABLE tb_safra (
    id_safra BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'EM_ANDAMENTO',
    observacoes VARCHAR(500),

    PRIMARY KEY (id_safra)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE tb_plantacao 
ADD COLUMN id_safra BIGINT,
ADD CONSTRAINT fk_plantacao_safra FOREIGN KEY (id_safra) REFERENCES tb_safra(id_safra);
