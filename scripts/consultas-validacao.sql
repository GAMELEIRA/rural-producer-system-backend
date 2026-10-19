-- Execute no MySQL Workbench para inspecionar os dados do Meu Gestor Rural.
USE gestao_microprodutores_db;

-- Tabelas disponíveis e quantidades de registros
SHOW TABLES;
SELECT table_name, table_rows
FROM information_schema.tables
WHERE table_schema = 'gestao_microprodutores_db'
ORDER BY table_name;

-- Cadastros e autenticação (atenção: tb_login contém hashes de senhas)
SELECT id_login, id_usuario, email FROM tb_login;
SELECT * FROM tb_usuario;
SELECT * FROM tb_cultura;
SELECT * FROM tb_talhao;
SELECT * FROM tb_insumo;
SELECT * FROM tb_produto;

-- Fluxo de produção
SELECT * FROM tb_safra;
SELECT * FROM tb_plantacao;
SELECT * FROM tb_aplicacao_insumo;
SELECT * FROM tb_gasto_producao;
SELECT * FROM tb_perda;

-- Estoque e comercial
SELECT * FROM tb_movimentacao_estoque ORDER BY data_movimentacao DESC;
SELECT * FROM tb_venda ORDER BY data_venda DESC;
SELECT * FROM tb_item_venda;

-- Plantações com safra, cultura e talhão
SELECT p.id_plantacao, s.nome AS safra, c.nome AS cultura,
       t.nome AS talhao, p.data_plantio, p.data_colheita,
       p.quantidade_plantada, p.quantidade_colhida, p.status
FROM tb_plantacao p
LEFT JOIN tb_safra s ON s.id_safra = p.id_safra
JOIN tb_cultura c ON c.id_cultura = p.id_cultura
JOIN tb_talhao t ON t.id_talhao = p.id_talhao
ORDER BY p.id_plantacao DESC;

-- Custo automático dos insumos por plantação
SELECT a.id_plantacao, SUM(a.custo_total) AS total_insumos
FROM tb_aplicacao_insumo a
GROUP BY a.id_plantacao;

-- Receitas: não incluir vendas canceladas
SELECT COALESCE(SUM(valor_total), 0) AS faturamento
FROM tb_venda WHERE status = 'CONCLUIDA';

-- Histórico de entradas de colheita
SELECT * FROM tb_movimentacao_estoque
WHERE tipo = 'ENTRADA_COLHEITA'
ORDER BY data_movimentacao DESC;
