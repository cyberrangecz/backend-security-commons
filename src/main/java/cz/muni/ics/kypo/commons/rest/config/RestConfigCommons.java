package cz.muni.ics.kypo.commons.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@EnableSpringDataWebSupport
@Import({SwaggerConfig.class})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.mapping"})
public class RestConfigCommons {

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter jacksonHTTPMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapperForRestAPI());
        return jsonConverter;
    }

    @Bean(name = "objMapperRESTApi")
    @Primary
    public ObjectMapper objectMapperForRestAPI() {
        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(snakeCase());
        return obj;
    }

    /**
     * Naming strategy for returned JSONs.
     *
     * @return Naming Strategy for JSON properties
     */
    @Bean(name = "properyNamingSnakeCase")
    public PropertyNamingStrategy snakeCase() {
        return PropertyNamingStrategy.SNAKE_CASE;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
