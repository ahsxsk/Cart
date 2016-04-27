package com.shike.service;

import org.springframework.stereotype.Service;
import com.shike.service.*;

/**
 * 查询操作结果
 * Created by shike on 16/3/28.
 */
@Service("getReturn")
public class GetReturn extends AbstractReturn {
    public GetReturn() {}
    public void setRet(Boolean flag, String code, String message) {
        this.setFlag(flag);
        this.setErrorCode(code);
        this.setErrorMessage(message);
    }
}
