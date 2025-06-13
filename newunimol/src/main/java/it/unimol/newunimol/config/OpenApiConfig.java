package it.unimol.newunimol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configurazione per aggiungere alla curl di swagger le informazioni sull'autenticazione.
 * In alto a destra su swagger si trova il pulsante "Authorize".
 * Selezionando il campo di testo si può inserire il token, basta solo il valore senza "Bearer"
 * In automatico, swagger invierà le richieste con il token nel campo "Authorization".
 * Il formato di questa richiesta sarà Authorization: Bearer <token>
 */


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}