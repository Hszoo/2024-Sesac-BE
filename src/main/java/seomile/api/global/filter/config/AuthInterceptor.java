package seomile.api.global.filter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 인터셉터 로직을 작성합니다.
        return true; // true를 반환하면 요청을 계속 처리하고, false를 반환하면 요청 처리를 중단합니다.
    }
}