CREATE TABLE tb_venda (
    id_venda BIGINT NOT NULL AUTO_INCREMENT,
    data_venda DATETIME NOT NULL,
    cliente VARCHAR(150),
    valor_total DECIMAL(12,2) NOT NULL,
    forma_pagamento VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'CONCLUIDA',
    observacoes VARCHAR(500),

    PRIMARY KEY (id_venda)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tb_item_venda (
    id_item_venda BIGINT NOT NULL AUTO_INCREMENT,
    id_venda BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade DECIMAL(10,2) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,

    PRIMARY KEY (id_item_venda),
    CONSTRAINT fk_item_venda_venda FOREIGN KEY (id_venda) REFERENCES tb_venda(id_venda) ON DELETE CASCADE,
    CONSTRAINT fk_item_venda_produto FOREIGN KEY (id_produto) REFERENCES tb_produto(id_produto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
