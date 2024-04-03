package whu.edu.cn.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import whu.edu.cn.controller.DataResourceController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ServiceInterceptor implements HandlerInterceptor {
    @Autowired
    private DataResourceController dataResourceController;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果服务关闭，则拦截, 返回403状态码
        if(!dataResourceController.COVERAGE_SERVICES_IS_ON){
            System.out.println("拦截成功");
            response.setStatus(403);
            return false;
        }
        return true;
    }
}
