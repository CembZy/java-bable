package com.cemb.mybatis.executor.resultset;

import com.cemb.mybatis.config.MappedStatement;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultResultHandler implements ResultHandler {
    private MappedStatement mappedStatment;

    public DefaultResultHandler(MappedStatement mappedStatment) {
        this.mappedStatment = mappedStatment;
    }

    @Override
    public <E> List<E> handleResultSets(ResultSet resultSet) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (null != resultSet) {
            List<E> ret = new ArrayList<E>();
            while (resultSet.next()) {
                Class<?> aClass = Class.forName(mappedStatment.getResultType());
                Object instance = aClass.newInstance();
                if (Integer.class.isAssignableFrom(aClass)) {
                    instance = resultSet.getInt(0);
                } else if (String.class.isAssignableFrom(aClass)) {
                    instance = resultSet.getString(0);
                } else if (Long.class.isAssignableFrom(aClass)) {
                    instance = resultSet.getLong(0);
                } else if (Boolean.class.isAssignableFrom(aClass)) {
                    instance = resultSet.getBoolean(0);
                } else if (Date.class.isAssignableFrom(aClass)) {
                    instance = resultSet.getDate(0);
                } else {
                    //获取对象中的所有属性
                    Field[] fields = aClass.getDeclaredFields();
                    if (null != fields && fields.length > 0) {
                        for (Field field : fields) {
                            String fieldName = field.getName();
                            field.setAccessible(true);
                            parseResult(resultSet, instance, field, fieldName);
                        }
                    }
                }
                ret.add((E) instance);
            }
            return ret;
        }
        return null;
    }

    //结果集填充到对象中
    private void parseResult(ResultSet resultSet, Object instance, Field field, String fieldName) throws IllegalAccessException, SQLException {
        Class<?> fieldType = field.getType();
        if (Integer.class.isAssignableFrom(fieldType)) {
            int anInt = resultSet.getInt(fieldName);
            field.set(instance, anInt);
        } else if (String.class.isAssignableFrom(fieldType)) {
            field.set(instance, resultSet.getString(fieldName));
        } else if (Long.class.isAssignableFrom(fieldType)) {
            field.set(instance, resultSet.getLong(fieldName));
        } else if (Boolean.class.isAssignableFrom(fieldType)) {
            field.set(instance, resultSet.getBoolean(fieldName));
        } else if (Date.class.isAssignableFrom(fieldType)) {
            field.set(instance, resultSet.getDate(fieldName));
        }
    }
}
