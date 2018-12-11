package com.gsk.bigdata.confs;

import com.aiyi.core.beans.ResultBean;
import com.aiyi.core.exception.AccessOAuthException;
import com.aiyi.core.exception.RequestParamException;
import com.aiyi.core.exception.ServiceInvokeException;
import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理类
 * @author gsk
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandle {

    protected Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 参数校验异常400
     * @param e
     * @return
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultBean validationException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultBean.error(e.getMessage()).setCode(400);
    }

    /**
     * 请求参数异常400
     * @param e
     * @return
     */
    @ExceptionHandler(value = RequestParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultBean requestParamException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultBean.error(e.getMessage()).setCode(400);
    }

    /**
     * 权限认证异常401
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessOAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResultBean accessOAuthException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultBean.error(e.getMessage()).setCode(401);
    }

    /**
     * 服务器处理自定义捕获异常500
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServiceInvokeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultBean invokeException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultBean.error(e.getMessage()).setCode(500);
    }

    /**
     * CXF异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServerSOAPFaultException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultBean ServerSOAPException(Exception e){
        String msg = e.getMessage()
                .replace("Client received SOAP Fault from server:", "")
                .replace("Please see the server log to find more detail regarding exact" +
                        " cause of the failure.", "").trim();
        logger.error(msg, e);
        return ResultBean.error(msg).setCode(500);
    }

    /**
     * 其他未知异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultBean exception(Exception e) {
        String msg = e.getMessage();
        if (null == msg){
            msg = "服务器内部错误";
        }
        logger.error(msg, e);
        StackTraceElement[] stackTrace = e.getStackTrace();
        List<StackTraceElement> stackList = new ArrayList<>();
        for (StackTraceElement stackTraceElement: stackTrace){
            if (stackTraceElement.getClassName().contains("com.webkettle")
                    || stackTraceElement.getClassName().contains("com.gsk")){
                stackList.add(stackTraceElement);
            }
        }
        return ResultBean.error(msg).setCode(500)
                .putResponseBody("exception", e.getClass()).putResponseBody("stackList", stackList);
    }
}
