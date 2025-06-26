package com.example.demo;

import com.example.demo.service.ServicePost;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
@RestController
@EnableWebMvc
public class BootApplication {
    @Autowired
    private ServicePost servicePost;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(BootApplication.class, args);
    }

    @RequestMapping("/call/")
    public Map<String, Object> hello(@RequestParam(value = "service") String serviceName, @RequestParam(value = "method") String methodName, @RequestBody(required = false) Map<String,Object> body)  {
        Map<String, Object> result = new HashMap<>();
        Object service = null;
        result.put("status", "ok");
        try {
            try
            {
                service = BootApplication.class.getDeclaredField("service" + serviceName).get(this);

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                result.put("status", "error");
                result.put("err_msg",  "등록되지 않은 서비스를 호출하였습니다.\n" + e.getMessage());
            }
            try {
                Class[] methodParameterTypes = new Class[2];
                methodParameterTypes[0] = Map.class;
                methodParameterTypes[1] = Map.class;
                Method method = service.getClass().getMethod(methodName, methodParameterTypes);
                method.invoke(service, body, result);
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                result.put("status", "error");
                result.put("err_msg",  "등록되지 않은 메서드를 호출하였습니다.\n" + e.getMessage());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            result.put("status", "error");
            result.put("err_msg",  "오류가 발생하였습니다..\n" + e.getMessage());
        }
        return result;
    }

    @Bean
    RouterFunction<ServerResponse> spaRouter() {
        ClassPathResource index = new ClassPathResource("static/index.html");
        return route().resource(path("/"), index).build();
    }
}
