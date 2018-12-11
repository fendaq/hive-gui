package com.gsk.bigdata.web;

import com.aiyi.core.beans.ResultBean;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.sql.enums.SqlFunEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * 字段相关接口
 * @author gsk
 */
@RestController
@RequestMapping("api/maint")
public class ApiMaintController {

    @GetMapping("sql/fieldTypes")
    public ResultBean listFieldTypes(){
        return ResultBean.success("字段类型字典表").putResponseBody("list", CommAttr.SQL.FIELD_TYPE.TYPE_LIST());
    }

    @GetMapping("sql/funs")
    public ResultBean listFunctions(){
        List<SqlFunEnum.Entity> result = new LinkedList<>();
        for(SqlFunEnum funEnum: SqlFunEnum.values()){
            result.add(funEnum.getEntity());
        }
        return ResultBean.success("函数列表获取成功").putResponseBody("list", result);
    }


}
