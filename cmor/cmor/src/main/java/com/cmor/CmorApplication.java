package com.cmor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Ponto de entrada da aplicação CMOR.
 *
 * Estende SpringBootServletInitializer para permitir deploy como WAR
 * em um servidor de aplicação externo (ex: Tomcat standalone).
 * O método configure() é obrigatório para esse cenário.
 *
 * Para rodar localmente: mvn spring-boot:run ou executar o main().
 */
@SpringBootApplication
public class CmorApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CmorApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CmorApplication.class, args);
    }
}
