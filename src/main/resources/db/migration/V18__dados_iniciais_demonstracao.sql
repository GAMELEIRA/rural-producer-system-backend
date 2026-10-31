-- 1. Safras
INSERT INTO tb_safra (id_safra, nome, data_inicio, data_fim, status, observacoes) VALUES
(10, 'Safra 2026/1 - Outono-Inverno', '2026-03-01', '2026-07-31', 'FINALIZADA', 'Ciclo de outono/inverno.'),
(11, 'Safra 2026/2 - Primavera-Verão', '2026-08-01', '2026-12-31', 'EM_ANDAMENTO', 'Ciclo de verão focado em grãos e frutas.')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- 2. Talhões
INSERT INTO tb_talhao (id_talhao, nome, area, unidade_area, localizacao, observacoes) VALUES
(10, 'Talhão 01 - Horta Comercial', 1.20, 'ha', 'Setor Sul', 'Solo corrigido e adubado com canteiros elevados.'),
(11, 'Talhão 02 - Pomar de Frutas', 2.50, 'ha', 'Setor Leste', 'Sistema de irrigação por gotejamento instalado.'),
(12, 'Talhão 03 - Cultivo de Grãos', 4.80, 'ha', 'Setor Norte', 'Área plana de alta fertilidade natural com drenagem.'),
(13, 'Talhão 04 - Estufa Hidropônica', 0.50, 'ha', 'Área Central', 'Estufa climatizada com bancadas para folhosas.')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- 3. Culturas
INSERT INTO tb_cultura (id_cultura, nome, tipo, ciclo_estimado, unidade_ciclo, observacoes, imagem_codigo) VALUES
(10, 'Tomate', 'Hortaliça', 90, 'dias', 'Para saladas e molhos', 'TOMATE'),
(11, 'Alface Crespa', 'Hortaliça', 45, 'dias', 'Hortaliças frescas', 'ALFACE'),
(12, 'Milho', 'Grão', 80, 'dias', 'Para comercialização em feiras', 'MILHO'),
(13, 'Morango', 'Fruta', 60, 'dias', 'Frutos frescos e doces', 'MORANGO'),
(14, 'Cenoura', 'Raiz/Tubérculo', 95, 'dias', 'Coloração intensa', 'CENOURA')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- 4. Insumos
INSERT INTO tb_insumo (id_insumo, nome, tipo, unidade_medida, quantidade_estoque, preco_unitario, observacoes, imagem_codigo) VALUES
(10, 'Adubo NPK 04-14-08', 'Fertilizante', 'sc', 25.000, 135.00, 'Adubação de plantio e base rica em fósforo.', 'FERTILIZANTE'),
(11, 'Composto Orgânico', 'Corretivo', 'sc', 60.000, 28.50, 'Enriquecimento de matéria orgânica do solo.', 'COMPOSTO'),
(12, 'Calcário', 'Corretivo', 'sc', 40.000, 22.00, 'Correção de acidez e fornecimento de cálcio/magnésio.', 'CALCARIO'),
(13, 'Sementes de Tomate Italiano', 'Semente', 'un', 45.000, 45.00, 'Envelopes com 1.000 sementes com alta germinação.', 'SEMENTES'),
(14, 'Biofertilizante', 'Bioinsumo', 'L', 18.000, 68.00, 'Micronutrientes quelatados de rápida absorção.', 'BIOINSUMO')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- 5. Produtos
INSERT INTO tb_produto (id_produto, nome, unidade_medida, preco_venda, quantidade_estoque, observacoes, id_cultura, imagem_codigo) VALUES
(10, 'Tomate Selecionado', 'cx', 65.00, 42.000, 'Caixas de madeira padrão (20kg) padronizadas.', 10, 'TOMATE'),
(11, 'Alface Crespa Hidropônica', 'un', 3.50, 490.000, 'Maços embalados individualmente com raiz limpa.', 11, 'ALFACE'),
(12, 'Milho Verde', 'sc', 55.00, 30.000, 'Sacas com 60 espigas frescas de primeira linha.', 12, 'MILHO'),
(13, 'Morango em Bandejas', 'cx', 42.00, 25.000, 'Caixa com 4 bandejas de 250g cada.', 13, 'MORANGO'),
(14, 'Cenoura Lavada', 'sc', 48.00, 35.000, 'Sacas de 20kg lavadas e classificadas.', 14, 'CENOURA')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- 6. Plantações
INSERT INTO tb_plantacao (id_plantacao, id_talhao, id_cultura, id_safra, data_plantio, data_colheita, quantidade_plantada, quantidade_colhida, status, observacoes, id_produto_colheita) VALUES
(10, 10, 10, 10, '2026-03-10', '2026-06-15', 1200.00, 95.00, 'COLHIDO', 'Colheita finalizada. Entrada líquida de 95 cx no estoque.', 10),
(11, 13, 11, 10, '2026-04-05', '2026-05-20', 800.00, 720.00, 'COLHIDO', 'Ciclo rápido na estufa hidropônica. 720 un colhidas.', 11),
(12, 12, 12, 11, '2026-08-15', NULL, 4.80, NULL, 'EM_ANDAMENTO', 'Milho verde em fase vegetativa plena.', NULL),
(13, 11, 13, 11, '2026-09-01', NULL, 2500.00, NULL, 'EM_ANDAMENTO', 'Canteiros com mulching plástico e floração iniciada.', NULL)
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- 7. Aplicações de Insumos
INSERT INTO tb_aplicacao_insumo (id_aplicacao, id_plantacao, id_insumo, data_aplicacao, quantidade, custo_total, observacoes) VALUES
(10, 10, 10, '2026-03-12', 10.000, 1350.00, 'Adubação no plantio do tomate.'),
(11, 10, 11, '2026-03-15', 15.000, 427.50, 'Incorporação de composto orgânico nos canteiros.'),
(12, 12, 14, '2026-08-25', 4.000, 272.00, 'Aplicação foliar de micronutrientes no milho.'),
(13, 13, 10, '2026-09-05', 5.000, 675.00, 'Adubação de base para plantio de morango.')
ON DUPLICATE KEY UPDATE custo_total = VALUES(custo_total);

-- 8. Gastos de Produção Adicionais
INSERT INTO tb_gasto_producao (id_gasto, id_plantacao, descricao, valor, data_gasto, categoria, observacoes) VALUES
(10, 10, 'Reparo em bomba e conexões de gotejamento', 380.00, '2026-03-20', 'Manutenção / Irrigação', 'Troca de registros e filtros de linha.'),
(11, 12, 'Análise de solo', 220.00, '2026-08-10', 'Consultoria / Análise', 'Laudo técnico do laboratório agronômico regional.'),
(12, 13, 'Bobinas de plástico', 450.00, '2026-08-28', 'Estrutura / Proteção', 'Filme plástico para cobertura dos canteiros.')
ON DUPLICATE KEY UPDATE valor = VALUES(valor);

-- 9. Perdas Agrícolas
INSERT INTO tb_perda (id_perda, id_plantacao, data_perda, quantidade, motivo, valor_estimado, observacoes, etapa) VALUES
(10, 10, '2026-04-18', 5.000, 'Queda brusca de temperatura noturna', 325.00, 'Queima em 5 caixas estimadas de produção.', 'EM_CAMPO'),
(11, 10, '2026-06-16', 3.000, 'Danos mecânicos leves no transporte', 195.00, 'Descarte na seleção pós-colheita antes da embalagem.', 'POS_COLHEITA')
ON DUPLICATE KEY UPDATE motivo = VALUES(motivo);

-- 10. Vendas e Itens
INSERT INTO tb_venda (id_venda, data_venda, cliente, valor_total, forma_pagamento, status, observacoes) VALUES
(10, '2026-06-20 10:30:00', 'Restaurante Fogão a Lenha', 1905.00, 'PIX', 'CONCLUIDA', 'Entrega semanal de hortaliças frescas no restaurante.'),
(11, '2026-07-10 14:15:00', 'Quitanda Central & Hortifruti', 1300.00, 'TRANSFERENCIA', 'CONCLUIDA', 'Fornecimento para comércio varejista local.'),
(12, '2026-07-15 09:00:00', 'Feira Livre Municipal', 525.00, 'DINHEIRO', 'CONCLUIDA', 'Venda direta a consumidores na banca da praça.'),
(13, '2026-07-28 16:45:00', 'Mercado e Açougue São José', 975.00, 'CARTAO', 'CONCLUIDA', 'Entrega de tomates selecionados para revenda.')
ON DUPLICATE KEY UPDATE valor_total = VALUES(valor_total);

INSERT INTO tb_item_venda (id_item_venda, id_venda, id_produto, quantidade, preco_unitario, subtotal) VALUES
(10, 10, 10, 25.00, 65.00, 1625.00),
(11, 10, 11, 80.00, 3.50, 280.00),
(12, 11, 10, 20.00, 65.00, 1300.00),
(13, 12, 11, 150.00, 3.50, 525.00),
(14, 13, 10, 15.00, 65.00, 975.00)
ON DUPLICATE KEY UPDATE subtotal = VALUES(subtotal);

-- 11. Movimentações de Estoque (Auditoria)
INSERT INTO tb_movimentacao_estoque (id_produto, tipo, data_movimentacao, quantidade, saldo_anterior, saldo_posterior, origem, observacoes) VALUES
(10, 'ENTRADA_COLHEITA', '2026-06-15 11:00:00', 95.000, 0.000, 95.000, 'Plantação #10', 'Entrada líquida da colheita de Tomate Italiano'),
(11, 'ENTRADA_COLHEITA', '2026-05-20 10:00:00', 720.000, 0.000, 720.000, 'Plantação #11', 'Entrada líquida da colheita de Alface Crespa'),
(10, 'SAIDA_VENDA', '2026-06-20 10:30:00', 25.000, 95.000, 70.000, 'Venda para Restaurante Fogão a Lenha', 'Venda #10'),
(11, 'SAIDA_VENDA', '2026-06-20 10:30:00', 80.000, 720.000, 640.000, 'Venda para Restaurante Fogão a Lenha', 'Venda #10'),
(10, 'SAIDA_VENDA', '2026-07-10 14:15:00', 20.000, 70.000, 50.000, 'Venda para Quitanda Central', 'Venda #11'),
(11, 'SAIDA_VENDA', '2026-07-15 09:00:00', 150.000, 640.000, 490.000, 'Venda para Feira Livre Municipal', 'Venda #12'),
(10, 'SAIDA_VENDA', '2026-07-28 16:45:00', 15.000, 50.000, 35.000, 'Venda para Mercado São José', 'Venda #13');

INSERT INTO tb_movimentacao_insumo (id_insumo, tipo, data_movimentacao, quantidade, origem) VALUES
(10, 'ENTRADA_COMPRA', '2026-03-05 08:00:00', 35.000, 'Compra inicial Cooperativa Agrícola - NF 4589'),
(10, 'SAIDA_APLICACAO', '2026-03-12 09:00:00', 10.000, 'Aplicação na Plantação #10 (Tomate Italiano)'),
(11, 'ENTRADA_COMPRA', '2026-03-08 14:00:00', 75.000, 'Aquisição de composto orgânico a granel ensacado'),
(11, 'SAIDA_APLICACAO', '2026-03-15 10:30:00', 15.000, 'Aplicação na Plantação #10 (Tomate Italiano)'),
(14, 'ENTRADA_COMPRA', '2026-08-10 11:00:00', 22.000, 'Compra revenda agropecuária - NF 8821'),
(14, 'SAIDA_APLICACAO', '2026-08-25 15:00:00', 4.000, 'Aplicação na Plantação #12 (Milho Verde)');
