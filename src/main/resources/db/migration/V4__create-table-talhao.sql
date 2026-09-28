CREATE TABLE tb_talhao (
    id_talhao BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    area DECIMAL(12,2) NOT NULL,
    unidade_area VARCHAR(20) NOT NULL,
    localizacao VARCHAR(200) NOT NULL,
    observacoes VARCHAR(500),

    PRIMARY KEY (id_talhao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
