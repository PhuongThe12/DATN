//package luckystore.datn.infrastructure.security.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer{
//
//    private final TokenIntercreptor tokenIntercreptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenIntercreptor)
//                .addPathPatterns("/admin/**", "/user/**", "/customer/**", "/admin/ban-hang");
//    }
//}
