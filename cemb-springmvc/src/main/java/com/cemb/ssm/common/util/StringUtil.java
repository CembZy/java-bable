package com.cemb.ssm.common.util;

public class StringUtil {

    /**
     * @methodName: strConversion
     * @description: 字符串转换
     * @auther: CemB
     * @date: 2018/7/10 14:20
     */
    public static String strConversion(String v1) {
        return v1.indexOf("/") > 0 ? v1 : "/" + v1;
    }

    /**
     * @methodName: strFormat
     * @description: 路径字符串格式化
     * @auther: CemB
     * @date: 2018/7/11 16:11
     */
    public static String strFormat(String v1) {
        return v1.replace("//", "/");
    }

    /**
     * @methodName: toLowerCaseFirstOne
     * @description: 首字母大写转小写
     * @auther: CemB
     * @date: 2018/7/10 17:49
     */
    public static String toLowerCaseFirstOne(String v1) {
        if (Character.isLowerCase(v1.charAt(0)))
            return v1;
        else
            return (new StringBuilder()).append(Character.toLowerCase(v1.charAt(0))).append(v1.substring(1)).toString();
    }

}
