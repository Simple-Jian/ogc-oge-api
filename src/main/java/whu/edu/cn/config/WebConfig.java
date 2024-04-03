package whu.edu.cn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import whu.edu.cn.interceptor.ServiceInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ServiceInterceptor serviceInterceptor;
    // 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceInterceptor).addPathPatterns("/coverages_api/**");
    }
}
