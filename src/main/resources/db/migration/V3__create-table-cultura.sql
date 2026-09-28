CREATE TABLE tb_cultura (
    id_cultura BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    ciclo_estimado INT NOT NULL,
    unidade_ciclo VARCHAR(20) NOT NULL,
    observacoes VARCHAR(500),

    PRIMARY KEY (id_cultura)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
