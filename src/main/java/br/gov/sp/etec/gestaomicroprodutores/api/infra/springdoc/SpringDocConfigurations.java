package br.gov.sp.etec.gestaomicroprodutores.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .info(new Info()
                        .title("Sistema de Gestão para Microprodutores Rurais - API")
                        .description("API REST desenvolvida para Trabalho de Conclusão de Curso (TCC) - ETEC Desenvolvimento de Sistemas. " +
                                "Fornece gerenciamento completo de Culturas, Talhões, Plantações, Safras, Insumos, Perdas, Produtos colhidos, Movimentações de Estoque, Vendas, Gastos de Produção e Consolidação Financeira.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento PTCC Grupo 06")
                                .email("diego.mackevicius@etec.sp.gov.br"))
                        .license(new License()
                                .name("Uso Acadêmico ETEC")));
    }
}
