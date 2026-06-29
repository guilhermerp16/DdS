package com.cmor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * JsfConfig — sem registro manual de servlets.
 * O roteamento é controlado inteiramente via application.properties:
 *   - spring.mvc.servlet.path=/api  → DispatcherServlet só escuta /api/**
 *   - joinfaces.faces-servlet.url-mappings[0]=*.xhtml → FacesServlet pega *.xhtml
 */
@Configuration
public class JsfConfig implements WebMvcConfigurer {
    // Intencionalamente vazio — toda configuração está no application.properties
}