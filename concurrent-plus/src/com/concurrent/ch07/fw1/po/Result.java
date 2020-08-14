package com.concurrent.ch07.fw1.po;


/**
 * 结果集实体
 */
public class Result<R> {

    //失败原因
    private final String reason;

    //类型
    private final ResultType resultType;

    //结果信息
    private final R resultInfo;


    public Result(String reason, ResultType resultType, R resultInfo) {
        this.reason = reason;
        this.resultType = resultType;
        this.resultInfo = resultInfo;
    }

    public Result(ResultType resultType, R resultInfo) {
        this.reason = "成功";
        this.resultType = resultType;
        this.resultInfo = resultInfo;
    }

    public String getReason() {
        return reason;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public R getResultInfo() {
        return resultInfo;
    }

    @Override
    public String toString() {
        return "Result{" +
                "reason='" + reason + '\'' +
                ", resultType=" + resultType +
                ", resultInfo=" + resultInfo +
                '}';
    }
}
