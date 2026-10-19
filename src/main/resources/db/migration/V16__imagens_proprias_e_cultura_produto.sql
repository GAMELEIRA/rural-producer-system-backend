ALTER TABLE tb_cultura ADD COLUMN imagem_personalizada LONGTEXT NULL;
ALTER TABLE tb_insumo ADD COLUMN imagem_personalizada LONGTEXT NULL;
ALTER TABLE tb_produto ADD COLUMN id_cultura BIGINT NULL,
    ADD COLUMN imagem_codigo VARCHAR(40) NULL,
    ADD COLUMN imagem_personalizada LONGTEXT NULL;
ALTER TABLE tb_produto ADD CONSTRAINT fk_produto_cultura
    FOREIGN KEY (id_cultura) REFERENCES tb_cultura(id_cultura);
CREATE INDEX idx_produto_cultura ON tb_produto(id_cultura);
