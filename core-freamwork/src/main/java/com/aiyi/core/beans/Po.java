package com.aiyi.core.beans;


import com.aiyi.core.annotation.po.vali.Validation;
import com.aiyi.core.annotation.po.vali.enums.ValidationType;

import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * @Description : 统一约束参数实体类
 * @Creation Date : 2018/5/11 下午6:03
 * @Author : 郭胜凯
 */
public class Po {

  /**
   * @Description : 校验自身字段合法性
   * @Creation Date : 2018/5/11 下午6:03
   * @Author : 郭胜凯
   */
  public void check(){
    Field[] declaredFields = this.getClass().getDeclaredFields();
    for(Field field: declaredFields){
      Validation validation = field.getAnnotation(Validation.class);
      if (validation == null){continue;}
      field.setAccessible(true);
      Object value = null;
      try {
        value = field.get(this);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("字段校验失败", e);
      }
      if (null == value){continue;}
      String fieldName = validation.name();
      if ("".equals(fieldName)){
        fieldName = field.getName();
      }
      if (value instanceof String){
        if(!Pattern.matches(validation.value().getRegex(), (String)value)){
          throw new ValidationException(fieldName + "格式不符合规范");
        }
        if (validation.minLength() != 0 && validation.minLength() > ((String) value).length()){
          throw new ValidationException(fieldName + "内容长度不能小于" + validation.minLength() + "位字符");
        }
        if (validation.maxLength() != 0 && validation.maxLength() < ((String) value).length()){
          throw new ValidationException(fieldName + "内容长度不能大于" + validation.minLength() + "位字符");
        }
      }else if (value instanceof Number){
        if (Double.valueOf((String) value).doubleValue() < validation.minValue()){
          throw new ValidationException(fieldName + "最小不能小于" + validation.minValue());
        }
        if (Double.valueOf((String) value).doubleValue() > validation.maxValue()){
          throw new ValidationException(fieldName + "最大不能大于" + validation.maxValue());
        }
      }else {
        // TODO 留作扩展...
      }
    }
  }

  /**
   * @param value   要校验的值
   * @param name    要校验的值中文名称
   * @param type    要校验的值类型
   * @Description : 检验某个字段值是否符合指定类型
   * @Creation Date : 2018/5/21 下午4:41
   * @Author : 郭胜凯
   */
  public static void checkField(String value, String name, ValidationType type){
    if(!Pattern.matches(type.getRegex(), value)){
      throw new ValidationException(name + "格式不符合规范");
    }
  }
}
