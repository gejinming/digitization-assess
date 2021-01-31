package com.assess.config;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


/**
 * swagger configuration
 *
 * @author gjm
 * @since 2020-01-02
 */

@Configuration
@Profile({"dev", "test"})
public class SwaggerConfig implements WebMvcConfigurer {

    /**
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */

    @Bean
    public Docket customDocket() {
        java.util.List<Parameter> pars = new ArrayList<Parameter>();
        pars.add(new ParameterBuilder().name("Authorization").description("认证token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build());
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(Sets.newHashSet("html/text"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.assess.Controller"))
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("数字化评估系统接口文档")
                //创建人
                .contact(new Contact("Gjm", "", ""))
                //版本号
                .version("1.0")
                //描述
                .description("在相应文档里找到需要得参数")
                .build();
    }

}

