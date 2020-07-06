package io.diego.compasso.tech.eval.config;

import com.fasterxml.classmate.TypeResolver;
import io.diego.compasso.tech.eval.controller.HomeController;
import io.diego.compasso.tech.eval.model.dto.city.CitySearchDTO;
import io.diego.compasso.tech.eval.model.dto.client.ClientSearchDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(HomeController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .additionalModels(
                        typeResolver.resolve(CitySearchDTO.class),
                        typeResolver.resolve(ClientSearchDTO.class)
                );
    }
}

