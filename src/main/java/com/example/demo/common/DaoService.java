package com.example.demo.common;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
public class DaoService {
    @Autowired
    private SqlSessionTemplate sqlSession;

    private void reloadMappers() throws IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Configuration configuration = sqlSession.getConfiguration();

        // Clear existing mappings
        {
            configuration.getMappedStatements().clear();
            configuration.getCaches().clear();
            configuration.getResultMaps().clear();
            configuration.getParameterMaps().clear();
            configuration.getKeyGenerators().clear();
            configuration.getSqlFragments().clear();
            try {
                Field field = configuration.getClass().getDeclaredField("loadedResources");
                field.setAccessible(true);
                Set setConfig = (Set) field.get(configuration);
                setConfig.clear();
            }
            catch (Exception _) {}
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String mapperLocation = "file:src/main/java/**/*.xml";
        Resource[] resources = resolver.getResources(mapperLocation);

        for (Resource resource : resources) {
            System.out.println(resource.toString());
            try (InputStream inputStream = resource.getInputStream()) {
                XMLMapperBuilder builder = new XMLMapperBuilder(
                        inputStream,
                        configuration,
                        resource.toString(),
                        configuration.getSqlFragments()
                );
                builder.parse();
            }
        }
    }

    public <T> T getMapper(Class<T> type) {
        try {
            reloadMappers();
        }
        catch (Exception e) {
            System.out.println("●●●●●●●●●●●●● reload 실패");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return sqlSession.getMapper(type);
    }
}
