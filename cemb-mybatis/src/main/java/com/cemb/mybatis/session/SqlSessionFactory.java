package com.cemb.mybatis.session;


import com.cemb.mybatis.config.Configuration;
import com.cemb.mybatis.config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

//加载配置文件，初始化核心配置类Configuration
public class SqlSessionFactory {

    private final Configuration configuration = Configuration.newConfiguration();

    public SqlSessionFactory() {
        //初始化mapper配置
        ScanMapperAndParse();
        //初始化jdbc配置
        loadDbInfo();
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }


    //扫描mapper配置文件并且解析
    private void ScanMapperAndParse() {
        //存放mapper配置文件的路径
        String mapperPath = Configuration.MAPPER_CONFIG_LOCATION;
        URL resource = SqlSessionFactory.class.getClassLoader().getResource(mapperPath);
        ScanMappers(resource.getPath());
    }

    //扫描所有的mapper配置文件
    private void ScanMappers(String resourcePath) {
        File file = new File(resourcePath);
        if (null != file && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    ScanMappers(file1.getAbsolutePath());
                } else {
                    //解析
                    parseMapper(file1);
                }
            }
        } else {
            //解析
            parseMapper(file);
        }
    }

    //解析mapper配置文件
    private void parseMapper(File file) {
        // 创建saxReader对象
        SAXReader saxReader = new SAXReader();

        Document document = null;
        try {
            //获取一个指定文件的XML对象
            document = saxReader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获取根节点
        Element rootElement = document.getRootElement();
        if (null != rootElement) {
            String namespace = rootElement.attribute("namespace").getData().toString();

            try {
                //获取空间对应的mapper接口
                Class<?> aClass = Class.forName(namespace);
                Object instance = aClass.getInterfaces();
                if (null == instance) {
                    throw new RuntimeException("mapper接口不存在");
                }
                //遍历Mapper接口中所有的方法，用于后面的匹配
                Method[] declaredMethods = aClass.getDeclaredMethods();
                Map map = configuration.getMappedStatements();
                if (null != declaredMethods && declaredMethods.length > 0) {
                    List<String> methods = new ArrayList<>();
                    for (Method method : declaredMethods) {
                        methods.add(method.getName());
                    }
                    map.put(namespace, methods);
                }


                //获取select子节点列表
                List<Element> selects = rootElement.elements("select");
                if (null != selects && selects.size() > 0) {
                    List<String> mapperMethods = (List<String>) map.get(namespace);
                    for (Element element : selects) {
                        String id = element.attribute("id").getData().toString();
                        if (mapperMethods.indexOf(id) == -1) {
                            throw new RuntimeException("mapper接口不存在");
                        }

                        MappedStatement mappedStatement = new MappedStatement();
                        String resultType = element.attribute("resultType").getData().toString();
                        String sql = element.getData().toString();
                        String sourceId = namespace + "." + id;
                        mappedStatement.setSourceId(sourceId);
                        mappedStatement.setResultType(resultType);
                        mappedStatement.setSql(sql);
                        mappedStatement.setNamespace(namespace);

                        //注册将映射关系
                        configuration.getMappedStatements().put(sourceId, mappedStatement);
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    //加载jdbc配置信息
    private void loadDbInfo() {

    }
}
