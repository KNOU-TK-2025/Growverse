package com.example.demo.common;

import com.example.demo.service.ServicePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages="com.example.demo")
@RestController
public class ServiceController {
    @Autowired
    private ServicePost servicePost;

    @RequestMapping("/call/")
    public Map<String, Object> call(@RequestParam(value = "service") String serviceName, @RequestParam(value = "method") String methodName, @RequestBody(required = false) Map<String,Object> body)  {
        Map<String, Object> result = new HashMap<>();
        Object service = null;
        result.put("status", "ok");
        try {
            try
            {
                service = ServiceController.class.getDeclaredField("service" + serviceName).get(this);

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
}
