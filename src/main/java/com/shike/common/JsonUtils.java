package com.shike.common;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.text.SimpleDateFormat;

public final class JsonUtils {
    private static ObjectMapper MAPPER;
    static {
        MAPPER = generateMapper(JsonSerialize.Inclusion.ALWAYS);
    }

    private JsonUtils() {
    }

    /**
     * 对象转Json
     * @param src 多想
     * @param <T> 引用类型
     * @return 返回json
     * @throws IOException
     */
    public static <T> String toJson(T src) throws IOException {
        return src instanceof String ? (String) src : MAPPER.writeValueAsString(src);
    }
    /**
     * 通过Inclusion创建ObjectMapper对象
     * {@link org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion
     * Inclusion 对象枚举}
     * <ul>
     * <li>{@link org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion
     * Inclusion.ALWAYS 全部列入}</li>
     * <li>{@link org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion
     * Inclusion.NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion
     * Inclusion.NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion
     * Inclusion.NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     *
     * @param inclusion 传入一个枚举值, 设置输出属性
     * @return 返回ObjectMapper对象
     */
    private static ObjectMapper generateMapper(JsonSerialize.Inclusion inclusion) {

        ObjectMapper customMapper = new ObjectMapper();

        // 设置输出时包含属性的风格
        customMapper.setSerializationInclusion(inclusion);

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        customMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        customMapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        // 所有日期格式都统一为以下样式
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }
}
