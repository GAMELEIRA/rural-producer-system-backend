package br.gov.sp.etec.gestaomicroprodutores.api.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    // Utilizado pelo Spring Security no processo de autenticação
    UserDetails findByEmail(String email);

    // Utilizado para busca do objeto Login completo quando necessário
    Optional<Login> findLoginByEmail(String email);

    Optional<Login> findByUsuario(Usuario usuario);

    boolean existsByEmail(String email);
}
