package com.cemb.mybatis.executor.parameter;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultParameterHandler implements ParameterHandler {
    private Object parameters;

    public DefaultParameterHandler(Object parameters) {
        this.parameters = parameters;
    }

    //填充参数值
    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        if (null == parameters) {
            return;
        }
        if (parameters.getClass().isArray()) {
            Object[] paramArray = (Object[]) parameters;
            for (int i = 0; i < paramArray.length; i++) {
                Object object = paramArray[i];
                if (object instanceof Integer) {
                    ps.setInt(i + 1, (Integer) object);
                } else if (object instanceof String) {
                    ps.setString(i + 1, (String) object);
                } else if (object instanceof Long) {
                    ps.setLong(i + 1, (Long) object);
                }
            }
        }

    }
}
