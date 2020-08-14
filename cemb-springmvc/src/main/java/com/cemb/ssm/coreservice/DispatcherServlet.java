package com.cemb.ssm.coreservice;

import com.cemb.ssm.annotation.*;
import com.cemb.ssm.common.util.StringUtil;
import com.cemb.ssm.handler.impl.HandlerAdpaterImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: DispatcherServlet
 * @Auther: CemB
 * @Description: DispatcherServlet实现, 在web.xml中配置servlet映射
 * @Email: 729943791@qq.com
 * @Date: 2018/7/10 10:58
 */
public class DispatcherServlet extends HttpServlet {

    private static final String HANDLER_ADPATER = "handlerAdpater";

    //bean容器
    private volatile List<Class<?>> classList = new ArrayList<>();

    //bean注册中心
    private final Map<Object, Object> beanMappings = new ConcurrentHashMap<>();


    public DispatcherServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (classList.size() <= 0) {
            System.out.println("初始化bean失败");
        }
        //获取到对应的method
        Method method = (Method) beanMappings.get(req.getServletPath());
        if (null == method) {
            System.out.println("地址不正确");
        }

        //曹勇策略模式--执行方法
        HandlerAdpaterImpl handlerAdpater = (HandlerAdpaterImpl) beanMappings.get(HANDLER_ADPATER);
        Object[] args = handlerAdpater.handlerMethod(req, resp, method, beanMappings);
        String classPath = method.getDeclaringClass().getName();
        Object o = beanMappings.get(classPath);
        if (null == o) {
            System.out.println("找不到bean实例");
        }
        try {
            method.invoke(o, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

//        doMethod(req, resp, method);
    }

    /*
     * @methodName: init
     * @description: 初始化容器，加载bean
     * @auther: CemB
     * @date: 2018/7/10 11:03
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //扫描项目根目录下的所有的bean，并将这些bean加入到list中
        String basePage = this.getClass().getClassLoader().getResource("/").getPath();

        //初始化所有的bean
        initBean(basePage);

        //注册所有的bean
        initInstance(classList);

        //控制层依赖注入bean
        doIoc();


        super.init(config);
    }

    /**
     * @methodName: doMethod
     * @description: 执行控制层方法
     * @auther: CemB
     * @date: 2018/7/10 19:49
     */
    protected synchronized void doMethod(HttpServletRequest req, HttpServletResponse resp, Method method) {
        Object[] args = null;
        Parameter[] methodParameters = method.getParameters();
        args = new Object[methodParameters.length];
        int i = 0;
        if (null != methodParameters && methodParameters.length > 0) {
            for (Parameter parameter : methodParameters) {
                Class<?> type = parameter.getType();
                // TODO  扩展性差，需改进
                if (ServletRequest.class.isAssignableFrom(type)) {
                    args[i++] = req;
                } else if (ServletResponse.class.isAssignableFrom(type)) {
                    args[i++] = resp;
                }
                Annotation[] parameterAnnotations = parameter.getAnnotations();
                if (null != parameterAnnotations && parameterAnnotations.length > 0) {
                    for (Annotation annotation : parameterAnnotations) {
                        if (annotation instanceof CemBRequestParam) {
                            CemBRequestParam cemBRequestParam = (CemBRequestParam) annotation;
                            if ("".equals(cemBRequestParam.value())) {
                                args[i++] = req.getParameter(parameter.getName());
                            } else {
                                // TODO    参数类型匹配，需改进
//                                    Class<?> parameterType = parameter.getType();
                                args[i++] = req.getParameter(cemBRequestParam.value());
                            }
                        }
                    }
                }
            }
            try {
                String classPath = method.getDeclaringClass().getName();
                Object o = beanMappings.get(classPath);
                if (null == o) {
                    System.out.println("找不到bean实例");
                }
                method.invoke(o, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @methodName: doInstance
     * @description: 注册控制业务层bean
     * @auther: CemB
     * @date: 2018/7/10 19:49
     */
    protected synchronized void initInstance(List<Class<?>> classList) {
        if (classList.size() <= 0) {
            System.out.println("初始化实例为空");
        }
        for (Class clazz : classList) {
            // TODO  存在扩展性问题，需改进
            if (clazz.isAnnotationPresent(CemBController.class)) {//控制器bean
                CemBRequestMapping requestMappingAnnotation = (CemBRequestMapping) clazz.getAnnotation(CemBRequestMapping.class);
                try {
                    Object instance = clazz.newInstance();
                    beanMappings.put(clazz.getName(), instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                initHandlerMappingMethods(clazz, requestMappingAnnotation.value());
            } else if (clazz.isAnnotationPresent(CemBService.class)) {//业务bean
                CemBService cemBService = (CemBService) clazz.getAnnotation(CemBService.class);
                Object instance = null;
                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if ("".equals(cemBService.value())) {
                    beanMappings.put(StringUtil.toLowerCaseFirstOne(clazz.getSimpleName()), instance);
                } else {
                    beanMappings.put(cemBService.value(), instance);
                }
            }
        }
    }


    /**
     * @methodName: initHandlerMapping
     * @description: 注册控制器
     * @auther: CemB
     * @date: 2018/7/10 14:05
     */
    protected synchronized void doIoc() {
        if (beanMappings.size() >= 0) {
            for (Map.Entry<Object, Object> entry : beanMappings.entrySet()) {
                Object instance = entry.getValue();
                Class<?> valueClass = instance.getClass();
                // TODO  存在扩展性问题，需改进
                if (valueClass.isAnnotationPresent(CemBController.class)) {
                    //获取所有属性
                    Field[] clazzFields = valueClass.getDeclaredFields();
                    if (null != clazzFields && clazzFields.length > 0) {
                        for (Field field : clazzFields) {
                            if (field.isAnnotationPresent(CemBAutowired.class)) {
                                CemBAutowired cemBAutowired = field.getAnnotation(CemBAutowired.class);
                                if (cemBAutowired.required()) {//根据类型查找bean
                                    // TODO required的实现，需改进

                                    try {
                                        //私有属性可见
                                        field.setAccessible(true);
                                        field.set(instance, beanMappings.get(field.getName()));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        //私有属性可见
                                        field.setAccessible(true);
                                        field.set(instance, beanMappings.get(field.getName()));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @methodName: initMethod
     * @description: 注册url和方法映射
     * @auther: CemB
     * @date: 2018/7/10 14:55
     */
    protected synchronized String initHandlerMappingMethods(Class<?> clazz, String classPath) {
        Method[] clazzMethods = clazz.getMethods();
        if (null != clazzMethods && clazzMethods.length > 0) {
            for (Method method : clazzMethods) {
                if (method.isAnnotationPresent(CemBRequestMapping.class)) {
                    CemBRequestMapping requestMappingAnnotation = method.getAnnotation(CemBRequestMapping.class);
                    if (null == requestMappingAnnotation.value() || "".equals(requestMappingAnnotation.value())) {
                        System.out.println("url为空");
                    } else {
                        beanMappings.put(StringUtil.strFormat((null != classPath ? classPath : "") + StringUtil.strConversion(requestMappingAnnotation.value())), method);
                    }
                }
            }
        }
        return null;
    }


    /**
     * @methodName: doInitBean
     * @description: 初始化bean
     * @auther: CemB
     * @date: 2018/7/10 12:00
     */
    protected synchronized void initBean(String basePackage) {
        File file = new File(basePackage);
        //获取所有的目录和文件
        File[] files = file.listFiles();
        if (files.length <= 0) {
            System.out.println("初始化实例为空");
        }
        for (File file1 : files) {
            basePackage = file1.getAbsolutePath();
            if (file1.isDirectory()) {
                initBean(basePackage);
            } else {
                try {
                    String classPath = basePackage.replace("\\", "/").split("/classes/")[1].replace("/", ".").split(".class")[0];
                    Class<?> clazz = Class.forName(classPath);
                    classList.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
