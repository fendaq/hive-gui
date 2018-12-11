package com.aiyi.core.util;

import com.aiyi.core.beans.FmtParm;
import com.aiyi.core.beans.Po;
import com.aiyi.core.beans.WherePrams;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL查询格式化工具类
 * @author 郭胜凯
 * @time 2016年6月14日下午4:33:29
 * @email 719348277@qq.com
 *
 */
public class FormatterSql implements Formatter {

	/**
	 * 格式化条件
	 */
	private List<FmtParm> fmtParms = new ArrayList<>();
	
	
	@Override
	public  void addFmt(String fieldName, String select, Class<? extends Po> po, WherePrams where) {
		// TODO Auto-generated method stub
		fmtParms.add(new FmtParm(fieldName, select, po, where));
	}

	@Override
	public void addFmt(FmtParm parm) {
		// TODO Auto-generated method stub
		fmtParms.add(parm);
	}

	@Override
	public List<FmtParm> listFmtParm() {
		// TODO Auto-generated method stub
		return fmtParms;
	}

}
