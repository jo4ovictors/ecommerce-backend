package br.edu.ifmg.produto.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("itreco API")
                .version("1.0")
                .license(new License().name("Apache 2.0").url("https://www.ifmg.edu.br"))
                .description("itreco system.")
                .contact(new Contact().name("itreco Dev Team").email("support@itreco.com")));
    }

}
