package com.aiyi.core;

import com.aiyi.core.beans.ResultBean;
import com.aiyi.core.exception.AccessOAuthException;
import com.aiyi.core.exception.RequestParamException;
import com.aiyi.core.util.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : Xunhengda
 * @Prackage Name : com.aiyi.core
 * @Description :
 * @Author : 郭胜凯
 * @Creation Date : 2018/5/6 下午12:05
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018/5/6
 */
public class AiYiThreadFilterConfiger implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    String token = httpServletRequest.getHeader("token");
    ThreadUtil.setToken(token);
    String request_id = httpServletRequest.getHeader("request_id");
    if (null == request_id){
      request_id = "DEFAULT_REQUEST";
    }
    ThreadUtil.setRequestId(request_id);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    if (e != null){
      ResultBean resultBean = new ResultBean();
      resultBean.setCode(-1);
      resultBean.setMessage(e.getMessage());
      resultBean.setSuccess(false);
      Map<String, Object> body = new HashMap<>();
      body.put("exception", e.getClass().getName());
      StackTraceElement[] stackTrace = e.getStackTrace();
      List<StackTraceElement> stackTraceElements = new ArrayList<>();
      for(StackTraceElement stackTraceElement: stackTrace){
        if (stackTraceElement.getClassName().indexOf("com.xhd") == 0){
          stackTraceElements.add(stackTraceElement);
        }
      }
      body.put("stackTrace", stackTraceElements);
      resultBean.setResponseBody(body);
      httpServletResponse.setCharacterEncoding("UTF-8");
      httpServletResponse.setContentType("application/json;charset=utf-8");
      //预定异常对应的状态码
      if (e instanceof AccessOAuthException){
        httpServletResponse.setStatus(401);
        resultBean.setCode(401);
      } else if(e instanceof RequestParamException){
        httpServletResponse.setStatus(400);
        resultBean.setCode(400);
      } else if(e instanceof ValidationException){
        httpServletResponse.setStatus(400);
        resultBean.setCode(400);
      } else{
        httpServletResponse.setStatus(500);
        resultBean.setCode(500);
      }
      PrintWriter writer = httpServletResponse.getWriter();
      writer.print(JSONObject.toJSONString(resultBean));
      writer.flush();
      writer.close();
    }
  }

  public void sendAuthError(HttpServletResponse response, AccessOAuthException e){
    ResultBean resultBean = new ResultBean();
    resultBean.setCode(401);
    resultBean.setMessage(e.getMessage());
    resultBean.setSuccess(false);
    Map<String, Object> body = new HashMap<>();
    body.put("exception", e.getClass().getName());
    StackTraceElement[] stackTrace = e.getStackTrace();
    List<StackTraceElement> stackTraceElements = new ArrayList<>();
    for(StackTraceElement stackTraceElement: stackTrace){
      if (stackTraceElement.getClassName().indexOf("com.xhd") == 0){
        stackTraceElements.add(stackTraceElement);
      }
    }
    body.put("stackTrace", stackTraceElements);
    resultBean.setResponseBody(body);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=utf-8");
    response.setStatus(401);
    PrintWriter writer = null;
    try {
      writer = response.getWriter();
      writer.print(JSONObject.toJSONString(resultBean));
      writer.flush();
    }catch (Exception ex){
      ex.printStackTrace();
    }finally {
      if (writer != null){
        writer.close();
        throw e;
      }
    }


  }
}
