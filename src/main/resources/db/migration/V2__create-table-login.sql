CREATE TABLE tb_login (
    id_login BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    id_usuario BIGINT NOT NULL UNIQUE,

    PRIMARY KEY (id_login),
    CONSTRAINT fk_login_usuario FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
