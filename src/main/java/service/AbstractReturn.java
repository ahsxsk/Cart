package main.java.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 操作结果
 * Created by shike on 16/3/28.
 */
public abstract class AbstractReturn {
    //标志
    private Boolean flag;
    //错误码
    private String errorCode;
    //错误信息
    private String errorMessage;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
