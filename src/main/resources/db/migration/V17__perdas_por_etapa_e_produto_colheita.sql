ALTER TABLE tb_plantacao ADD COLUMN id_produto_colheita BIGINT NULL;
ALTER TABLE tb_plantacao ADD CONSTRAINT fk_plantacao_produto_colheita
    FOREIGN KEY (id_produto_colheita) REFERENCES tb_produto(id_produto);
ALTER TABLE tb_perda ADD COLUMN etapa VARCHAR(30) NOT NULL DEFAULT 'EM_CAMPO';

-- Associa colheitas antigas quando existe movimentação identificando a plantação.
UPDATE tb_plantacao p
JOIN tb_movimentacao_estoque m
  ON m.tipo = 'ENTRADA_COLHEITA'
 AND m.origem = CONCAT('Plantação #', p.id_plantacao)
SET p.id_produto_colheita = m.id_produto
WHERE p.id_produto_colheita IS NULL;
