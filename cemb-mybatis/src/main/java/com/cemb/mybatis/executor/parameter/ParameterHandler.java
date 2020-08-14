package com.cemb.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {

    //填充参数
    void setParameters(PreparedStatement ps) throws SQLException;
}
