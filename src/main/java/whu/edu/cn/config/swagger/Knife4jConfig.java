package whu.edu.cn.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * knife4j配置
 */
@Configuration
public class Knife4jConfig {
   @Bean
    public Docket docket(){
        ApiInfo apiinfo=new ApiInfoBuilder()
                .title("服务中心接口文档")
                .version("1.0")
                .description("服务中心的接口文档")
                .build();

        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiinfo)
                .select()
                //指定生成接口需要扫描的包
                .apis(RequestHandlerSelectors.basePackage("whu.edu.cn.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
