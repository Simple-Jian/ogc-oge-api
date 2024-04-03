package whu.edu.cn;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableAsync
@SpringBootApplication
@EnableOpenApi
public class OGEAPIBootApplication {

    @Value("${server.http.port}")
    private int httpPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        return connector;
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(OGEAPIBootApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String httpPort = env.getProperty("server.http.port");
        String httpsPort = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("\n---------------------------------------------------------------\n\t" +
                "OGE - OGC API 后台正在运行...\n\t" +
                "Local: \t\thttp://localhost:" + httpPort + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + httpPort + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + httpPort + path + "/swagger-ui/index.html\n\t" +
                "Doc: \t\thttp://" + ip + ":" + httpPort + path + "/doc.html\n" +
                "\n\t" +
                "Local: \t\thttps://localhost:" + httpsPort + path + "/\n\t" +
                "External: \thttps://" + ip + ":" + httpsPort + path + "/\n\t" +
                "swagger-ui: \thttps://" + ip + ":" + httpsPort + path + "/swagger-ui/index.html\n\t" +
                "Doc: \t\thttps://" + ip + ":" + httpsPort + path + "/doc.html\n" +
                "---------------------------------------------------------------");
    }

   /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GeocubeBootSubmitApplication.class);
    }*/

}
