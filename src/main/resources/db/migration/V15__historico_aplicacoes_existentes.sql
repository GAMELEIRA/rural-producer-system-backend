-- Reconstrói as saídas ainda ativas que ocorreram antes da instalação do histórico.
-- O saldo atual dos insumos permanece inalterado. Não inventamos entradas antigas.
INSERT INTO tb_movimentacao_insumo (id_insumo, tipo, data_movimentacao, quantidade, origem)
SELECT id_insumo, 'SAIDA_APLICACAO', CAST(data_aplicacao AS DATETIME), quantidade,
       CONCAT('Aplicação existente #', id_aplicacao, ' (histórico importado)')
FROM tb_aplicacao_insumo;
