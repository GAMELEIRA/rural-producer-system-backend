package br.gov.sp.etec.gestaomicroprodutores.api.infra.security;

import br.gov.sp.etec.gestaomicroprodutores.api.usuario.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var login = loginRepository.findByEmail(username);
        if (login == null) {
            throw new UsernameNotFoundException("Usuario nao encontrado com o e-mail: " + username);
        }
        return login;
    }
}
