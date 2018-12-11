package com.aiyi.core.dao.impl;

import com.aiyi.core.annotation.po.BigField;
import com.aiyi.core.annotation.po.DateTime;
import com.aiyi.core.annotation.po.FieldName;
import com.aiyi.core.annotation.po.FieldType;
import com.aiyi.core.beans.*;
import com.aiyi.core.dao.Dao;
import com.aiyi.core.enums.BigFieldType;
import com.aiyi.core.exception.ServiceInvokeException;
import com.aiyi.core.sql.where.C;
import com.aiyi.core.sql.where.SqlUtil;
import com.aiyi.core.util.Formatter;
import com.aiyi.core.util.GenericsUtils;
import com.aiyi.core.util.SqlLog;
import org.apache.http.client.utils.DateUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLDataException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
@Repository
public class DaoImpl<T extends Po, PK extends Serializable> implements Dao<T, PK> {

	/*protected Logger logger = LoggerFactory.getLogger(this.getClass());*/

	@Resource(name = "sqlSessionTemplateASS")
	private SqlSessionTemplate sqlSessionTemplateASS;
	
	private Class<T> entityClass;
	
	private String pkName;					//ʵ�����������
	
	private String idName;					//ʵ����ID�ֶ����
	
	private String seq;						//���������
	
	private String tableName;
	
	private List<Pram> sqlParms;
	
	
	private List<Pram> selectSqlParms;
	
	private SqlUtil<T> sqlUtil;
	
	private String idType;
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	
	@SuppressWarnings("unchecked")
	public DaoImpl(){
		super();

		this.sqlUtil = new SqlUtil<T>();
		
        this.entityClass = (Class<T>) GenericsUtils.getSuperClassGenricType(this.getClass());
        
        this.sqlParms = this.sqlUtil.getPramList(this.entityClass);
        
        this.selectSqlParms = this.sqlUtil.getPramListOfSelect(this.entityClass);
        
        this.tableName = this.sqlUtil.getTableName(this.entityClass);
        
        this.pkName = this.sqlUtil.getPkName(this.entityClass);
        
        this.idName = this.sqlUtil.getIdName(this.entityClass);
        
        this.seq = this.sqlUtil.getPkName(this.entityClass);
        
        this.idType = this.sqlUtil.getIdType(this.entityClass);

//        System.out.println("Init:" + toString());
	
	}

	@Override
	public String toString() {
		return "DaoImpl{" +
				"\n\tsqlSessionTemplateASS=" + sqlSessionTemplateASS +
				", \n\tentityClass=" + entityClass +
				", \n\tpkName='" + pkName + '\'' +
				", \n\tidName='" + idName + '\'' +
				", \n\tseq='" + seq + '\'' +
				", \n\ttableName='" + tableName + '\'' +
				", \n\tsqlParms=" + sqlParms +
				", \n\tselectSqlParms=" + selectSqlParms +
				", \n\tnsqlUtil=" + sqlUtil +
				", \n\tidType='" + idType + '\'' +
				'}';
	}

	@Override
	public int add(T po) {
		// TODO Auto-generated method stub
		String sql = "insert into " + tableName + " (";
		String prams = "";
		String values = "";
		
		List<Pram> pramList = SqlUtil.getPramListofStatic(po);
		
		for (int i = 0; i < pramList.size(); i++) {
			prams += pramList.get(i).getDbField();
			if (pramList.get(i).getValue() == null) {
				values += "null";
			}else if(pramList.get(i).getValue() instanceof Boolean){
				values += "'" + ((boolean)pramList.get(i).getValue() == true ? 1 : 0) + "'";
			}else if (pramList.get(i).getValue() instanceof Date){
				if (pramList.get(i).getField().getAnnotation(DateTime.class) != null){
					long time = 0;
					if (null != pramList.get(i).getValue()){
						time = ((Date) pramList.get(i).getValue()).getTime();
					}
					values += "'" + time + "'";
				}else{
					String sqlDateTra = FieldType.DATE.getMySqlTra();
					if(driverClassName.toUpperCase().contains("ORACLE")){
						sqlDateTra = FieldType.DATE.getOracleTra();
					}
					values += "" + sqlDateTra.replace("{dtStr}", DateUtils.formatDate((Date)pramList.get(i).getValue(), "yyyy-MM-dd HH:mm:ss")) + "";
				}
			}else{
				if (pramList.get(i).getValue() instanceof String){
					StringBuffer thisFieldValue = new StringBuffer();
                    Field field = pramList.get(i).getField();
                    BigField bigField = field.getAnnotation(BigField.class);
                    // 是否是大字段
                    if (bigField == null){
                        values += "'" + (((String) pramList.get(i).getValue()).replace("'", "''")) + "'";
                    }else{
                        BigFieldType fieldType = bigField.value();
                        StringBuffer buffer = new StringBuffer();
                        for(char c: pramList.get(i).getValue().toString().toCharArray()){
                            buffer.append(c);
                            if (buffer.length() >= 1000){
                                thisFieldValue.append(fieldType.getSqlFun() + "('" + buffer.toString()
                                        .replace("'", "''") + "') || ");
                                buffer.setLength(0);
                            }
                        }
                        thisFieldValue.append(fieldType.getSqlFun() + "('" + buffer.toString()
                                .replace("'", "''") + "')");
                        values += thisFieldValue.toString();
                    }
                }else{
					values += "'" + pramList.get(i).getValue() + "'";
				}
            }
			
			if(i < pramList.size() -1){
				prams += ",";
				values += ",";
			}
		}
		
		int result = 0;
		boolean setId = false;
		if(idType.indexOf("String") != -1) {
			PK id = getId();
			sql += prams + "," + this.pkName + ") value (" + values + ",'" + id +"')";
			SqlUtil.setFileValue(po, this.idName, id);
			SqlLog.info(sql);
		}else {
			if(driverClassName.toUpperCase().contains("ORACLE")){
				prams += ", " + pkName + " ";
				values += ", SEQ_ID.NEXTVAL";
				sql += prams + ") values (" + values +")";
			}else{
				sql += prams + ") value (" + values +")";
			}
			SqlLog.info(sql);
			setId = true;
		}
		result = sqlSessionTemplateASS.insert("add", sql);
		
		if(setId) {
//			SqlUtil.setFileValue(po, this.idName, getId());
		}
		return result;
	}


	@Override
	public T get(PK id) {
		// TODO Auto-generated method stub
		String sql = "select ";
		for (int i = 0; i < selectSqlParms.size(); i++) {
			sql += selectSqlParms.get(i).getDbField() + " AS " + selectSqlParms.get(i).getField().getName();
			if(i < selectSqlParms.size() -1){
				sql += ",";
			}else{
				sql += " ";
			}
		}
		sql += " from " + tableName + " where " + this.pkName + "='" + id + "'";
		Map<String, Object> resultMap = sqlSessionTemplateASS.selectOne(
	                "getById", sql);
		SqlLog.info(sql);
		return handleResult(resultMap, this.entityClass);
	}

	@Override
	public Serializable getField(PK id, String fileName) {
		// TODO Auto-generated method stub
		String field = fileName;
		String tabField = "";
		Field f = sqlUtil.getField(this.entityClass, fileName);
		if (null == f) {
			SqlLog.error("查询字段失败(无法找到" + this.entityClass + "中的" + fileName + "字段)");
		}
		FieldName annotation = f.getAnnotation(FieldName.class);
		if (null == annotation) {
			tabField = sqlUtil.toTableString(fileName) + " as " + fileName;
		}else{
			tabField = annotation.name() + " as " + fileName;
		}
		
		String sql = "select ";
		sql += tabField + " from " + tableName + " where " + this.pkName + "='" + id + "'";
		Map<String, Object> resultMap = sqlSessionTemplateASS.selectOne(
                "getFieldById", sql);
		SqlLog.info(sql);
		return (Serializable) resultMap.get(fileName);
	}

	@Override
	public T get(WherePrams where) {
		// TODO Auto-generated method stub
		String sql = "select ";
		for (int i = 0; i < selectSqlParms.size(); i++) {
			sql += selectSqlParms.get(i).getDbField() + " AS " + selectSqlParms.get(i).getField().getName();
			if(i < selectSqlParms.size() -1){
				sql += ",";
			}else{
				sql += " ";
			}
		}
		sql += "from " + tableName + where.getWherePrams();
		
		Map<String, Object> resultMap = sqlSessionTemplateASS.selectOne(
                "getByParm", sql);
		SqlLog.info(sql);
		return handleResult(resultMap, this.entityClass);
	}

	@Override
	public Serializable getFile(WherePrams where, String fileName) {
		// TODO Auto-generated method stub
		String field = fileName;
		String tabField = "";
		Field f = sqlUtil.getField(this.entityClass, fileName);
		if (null == f) {
			SqlLog.error("查询字段失败(无法找到" + this.entityClass + "中的" + fileName + "字段)");
		}
		FieldName annotation = f.getAnnotation(FieldName.class);
		if (null == annotation) {
			tabField = sqlUtil.toTableString(fileName) + " as " + fileName;
		}else{
			tabField = annotation.name() + " as " + fileName;
		}
		
		String sql = "select ";
		sql += tabField + " from " + tableName + where.getWherePrams();
		Map<String, Object> resultMap = sqlSessionTemplateASS.selectOne(
                "getFieldByParm", sql);
		SqlLog.info(sql);
		return (Serializable) resultMap.get(fileName);
	}

	@Override
	public List<T> list(WherePrams where) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT ";
		for (int i = 0; i < selectSqlParms.size(); i++) {
			sql += selectSqlParms.get(i).getDbField() + " AS " + selectSqlParms.get(i).getField().getName();
			if(i < selectSqlParms.size() -1){
				sql += ",";
			}else{
				sql += " ";
			}
		}
        sql += "FROM " + tableName;
		String limit = where.getLimit();
		if (null != limit){
			if (driverClassName.toUpperCase().contains("MYSQL")){
				sql += where.getWherePrams();
			}else if(driverClassName.toUpperCase().contains("ORACLE")){
				String[] page = limit.toUpperCase().replace("LIMIT", "").trim().replace(" ", "").split(",");
				String pageId = "RN________";
				sql += where.getWherePrams().replace(where.getLimit(), "");
				String pageSql = "";
				for (int i = 0; i < selectSqlParms.size(); i++) {
                    String file = selectSqlParms.get(i).getField().getName().toUpperCase();
//                    file = file.substring(file.indexOf("AS ") + 2).trim();
                    pageSql += file;
					if(i < selectSqlParms.size() -1){
						pageSql += ",";
					}else{
						pageSql += " ";
					}
				}
                pageSql += ", ROWNUM AS " + pageId;
				sql = "SELECT * FROM (SELECT " + pageSql + " FROM ( " + sql + " )t)t2 WHERE t2." + pageId + " > " + page[0] + " AND t2." + pageId + " <= (" + page[0] + " + " + page[1] + ")";
			}
		}else{
			sql += where.getWherePrams();
		}
		SqlLog.info(sql);
		List<Map<String, Object>> selectList = sqlSessionTemplateASS.selectList("selectList", sql);
		
		List<T> list = new ArrayList<>();
		for (Map<String, Object> map : selectList) {
			T t = handleResult(map, this.entityClass);
			list.add(t);
		}
		
		return list;
		
	}



	@Override
	public int update(T po) {
		// TODO Auto-generated method stub
		
		Serializable id = sqlUtil.getFileValue(po, this.idName);
		
		if(null == id){
			return 0;
		}
		String sql = "update " + tableName + " set ";
		
		List<Pram> prams = sqlUtil.getPramList(po);
		
		for (int i = 0; i < prams.size(); i++) {
			if(null != prams.get(i).getValue()){
				sql += prams.get(i).getDbField() + "=";
				Object value = prams.get(i).getValue();
				if (value instanceof byte[] ) {
					sql += "'" + new String((byte[]) value) + "'";
				}else if(value instanceof Boolean){
					sql += "'" + ((boolean)value == true ? 1 : 0) + "'";
				}else if (value instanceof Date){
					if (prams.get(i).getField().getAnnotation(DateTime.class) != null){
						long time = 0;
						if (null != prams.get(i).getValue()){
							time = ((Date) prams.get(i).getValue()).getTime();
						}
						sql += "'" + time + "'";
					}else{
						String sqlDateTra = FieldType.DATE.getMySqlTra();
						if(driverClassName.toUpperCase().contains("ORACLE")){
							sqlDateTra = FieldType.DATE.getOracleTra();
						}
						sql += "" + sqlDateTra.replace("{dtStr}", DateUtils.formatDate((Date)value, "yyyy-MM-dd HH:mm:ss")) + "";
					}
				}else{
					if (value instanceof String){
                        StringBuffer thisFieldValue = new StringBuffer();
                        Field field = prams.get(i).getField();
                        BigField bigField = field.getAnnotation(BigField.class);
                        // 是否是大字段
                        if (bigField == null){
                            sql += "'" + (((String) value).replace("'", "''")) + "'";
                        }else{
                            BigFieldType fieldType = bigField.value();
                            StringBuffer buffer = new StringBuffer();
                            for(char c: value.toString().toCharArray()){
                                buffer.append(c);
                                if (buffer.length() >= 1000){
                                    thisFieldValue.append(fieldType.getSqlFun() + "('" + buffer.toString()
                                            .replace("'", "''") + "') || ");
                                    buffer.setLength(0);
                                }
                            }
                            thisFieldValue.append(fieldType.getSqlFun() + "('" + buffer.toString()
                                    .replace("'", "''") + "')");
                            sql += thisFieldValue.toString();
                        }
					}else{
						sql += "'" + value + "'";
					}
				}
//				sql += prams.get(i).getFile() + "='" + prams.get(i).getValue() + "'";
				if (i < prams.size() -1) {
					sql += ",";
				}
			}else{
				sql += prams.get(i).getDbField() + "=null";
				if (i < prams.size() -1) {
					sql += ",";
				}
			}
		}
		sql += " where " +this.pkName+ "='" + id + "'";
		SqlLog.info(sql);
		return sqlSessionTemplateASS.update("update", sql);
	}

	@Override
	public int update(T po, WherePrams where) {
		String sql = "update " + tableName + " set ";
		Object[] o = new Object[sqlParms.size()];
		for (int i = 0; i < sqlParms.size(); i++) {
			if(null != sqlParms.get(i).getValue()){
				if(sqlParms.get(i).getValue() instanceof Boolean){
					sql += sqlParms.get(i).getDbField() + "='" + (((boolean)sqlParms.get(i).getValue()) == true ? 1 : 0) + "'";
				}else{
					sql += sqlParms.get(i).getDbField() + "='";
					if (sqlParms.get(i).getValue() instanceof String){
                        StringBuffer thisFieldValue = new StringBuffer();
                        Field field = sqlParms.get(i).getField();
                        BigField bigField = field.getAnnotation(BigField.class);
                        // 是否是大字段
                        if (bigField == null){
                            sql += "'" + (((String) sqlParms.get(i).getValue()).replace("'", "''")) + "'";
                        }else{
                            BigFieldType fieldType = bigField.value();
                            StringBuffer buffer = new StringBuffer();
                            for(char c: sqlParms.get(i).getValue().toString().toCharArray()){
                                buffer.append(c);
                                if (buffer.length() >= 1000){
                                    thisFieldValue.append(fieldType.getSqlFun() + "('" + buffer.toString()
                                            .replace("'", "''") + "') || ");
                                    buffer.setLength(0);
                                }
                            }
                            thisFieldValue.append(fieldType.getSqlFun() + "('" + buffer.toString()
                                    .replace("'", "''") + "')");
                            sql += thisFieldValue.toString();
                        }
					}else{
						sql += "'" + sqlParms.get(i).getValue() + "'";
					}
//					sql += sqlParms.get(i).getFile() + "='" + sqlParms.get(i).getValue() + "'";
				}
				if (i < sqlParms.size() -1) {
					sql += ",";
				}
			}else{
				sql += sqlParms.get(i).getDbField() + "=null";
				if (i < sqlParms.size() -1) {
					sql += ",";
				}
			}
		}
		sql += where.getWherePrams() + "'";
		SqlLog.info(sql);
		return sqlSessionTemplateASS.update("updateByPram", sql);
		
	}

	@Override
	public int del(PK id) {
		// TODO Auto-generated method stub
		String sql = "delete from " + tableName + " where " + this.pkName + "='" + id + "'";
		SqlLog.info(sql);
		return sqlSessionTemplateASS.delete("deleteById", sql);
	}

	@Override
	public int del(WherePrams where) {
		// TODO Auto-generated method stub
		
		String sql = "delete from " + tableName + where.getWherePrams();
		SqlLog.info(sql);
		return sqlSessionTemplateASS.delete("deleteByparm", sql);
	}

	@Override
	public List<Map<String, Object>> listBySql(String sql) {
		// TODO Auto-generated method stub
		SqlLog.info(sql);
		List<Map<String, Object>> selectList = sqlSessionTemplateASS.selectList("selectBySql", sql);
		return selectList;
	}

	@Override
	public int excuse(String sql) {
		// TODO Auto-generated method stub
		SqlLog.info(sql);
		return sqlSessionTemplateASS.update("excuse", sql);
	}

	@Override
	public long count(WherePrams where) {
		// TODO Auto-generated method stub
		
		String sql = "select count(*) from ";
		
		sql += tableName + where.getWherePrams();
		SqlLog.info(sql);
		long count = sqlSessionTemplateASS.selectOne("selectCountByParm", sql);
		
		return count;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from " + tableName;
		SqlLog.info(sql);
		long count = sqlSessionTemplateASS.selectOne("selectCount", sql);
		return count;
	}

	@Override
	public boolean isExist(T po) {
		// TODO Auto-generated method stub
		WherePrams wherePrams = Method.createDefault();

		List<Pram> list = SqlUtil.getPramListofStatic(po);
		
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				wherePrams = Method.where(list.get(i).getDbField(), C.EQ, (Serializable)list.get(i).getValue());
			}else{
				wherePrams.and(list.get(i).getDbField(), C.EQ, (Serializable)list.get(i).getValue());
			}
		}
		
		
		return count(wherePrams) > 0;
	}

	@Override
	public boolean isExist(WherePrams where) {
		// TODO Auto-generated method stub
		return count(where) > 0;
	}

	@Override
	public List<T> in(String fileName, Serializable[] values) {
		// TODO Auto-generated method stub
		if (values.length < 1){
			return new LinkedList<>();
		}
		String sql = "select ";
		for (int i = 0; i < selectSqlParms.size(); i++) {
			sql += selectSqlParms.get(i).getDbField() + " AS " + selectSqlParms.get(i).getField().getName();
			if(i < selectSqlParms.size() -1){
				sql += ",";
			}else{
				sql += " ";
			}
		}
		sql += "from " + tableName + " where " + fileName + " in ";
		String value = "(";
		for(int i = 0; i < values.length; i++){
			if(i < values.length -1){
				value += values[i] + ","; 
			}else{
				value += values[i] + ")"; 
			}
		}
		sql += value;
		SqlLog.info(sql);
		List<Map<String, Object>> selectList = sqlSessionTemplateASS.selectList("selectIn", sql);
		
		List<T> list = new ArrayList<>();
		for (Map<String, Object> map : selectList) {
			T t = handleResult(map, this.entityClass);
			list.add(t);
		}
		
		return list;
	}
	
	public T handleResult(Map<String, Object> resultMap, Class<T> tClazz) {
		if (null == resultMap) {
			return null;
		}
        T t = null;
        try {
            t = tClazz.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
			SqlLog.error("/********************************");
			SqlLog.error("实例化Bean失败(" + this.entityClass + ")!");
			SqlLog.error("/********************************");
        	throw new ServiceInvokeException("实例化Bean失败", e);
        }
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            Serializable val = (Serializable) entry.getValue();
			try {
				SqlUtil.setFileValueByOracle(t, key, val);
			} catch (Exception e) {
				SqlLog.error("/********************************");
				SqlLog.error("/字段赋值失败(" + this.entityClass + ")不能赋值(" + key + "):");
				SqlLog.error("/********************************");
				throw new ServiceInvokeException("实例化Bean失败", e);
			}
        }
        return t;
    }
	
	/**
	 * 获取下一个自增ID
	 */
	public long nextId(){
		String sql = "SELECT auto_increment FROM information_schema.`TABLES` WHERE TABLE_NAME='" + tableName + "' AND TABLE_SCHEMA=(select database())";
		Long idVal = sqlSessionTemplateASS.selectOne("fetchSeqNextval", sql);
		if (null == idVal) {
			SqlLog.error("/********************************");
			SqlLog.error("/获取自增ID失败" + tableName + "无法获取");
			SqlLog.error("/********************************");
			return 0;
		}
		return idVal;
		
	}

	@Override
	public List<T> listFormat(WherePrams where, Formatter fmt) {
		// TODO Auto-generated method stub
		String sql = "SELECT ";
		
		String sqlTab = tableName + " as t_0";
		
		List<FmtParm> listFmtParm = fmt.listFmtParm();
		
		for (int i = 0; i < selectSqlParms.size(); i++) {
			
			String field = selectSqlParms.get(i).getDbField();
			
			sql += "t_0." + field;
			
			if(i < selectSqlParms.size() -1){
				sql += ",";
			}else{
				sql += " ";
			}
			
		}
		
		
		//是需要格式化的
		int index = 1;
		
		//临时缓存已处理的关联表名
		List<String> tempFmt = new ArrayList<>();
		
		String tabWhere = "";
		
		for (FmtParm fmtParm : listFmtParm) {
			
			String tName = this.sqlUtil.getTableNameByClazz(fmtParm.getPo());
			
			sql += ", t_" + index + "." + fmtParm.getSelect() + " as " + fmtParm.getFieldName();
			
			sqlTab += ", " + tName + " as t_" + index;
			
			String wherePrams = fmtParm.getWhere().getWherePrams();
			
			Pattern p = Pattern.compile("'(\\[fmt.R.+)'");
			Matcher m = p.matcher(wherePrams);
			while (m.find()) {
				String temp = m.group();
				
				wherePrams = wherePrams.replace(temp, temp.replace("'", "").replace("[fmt.R", "t_" + index + ".").replace("[fmt.L", "t_" + index).replace("]", ""));
			}
			p = Pattern.compile("'(\\[fmt.L.+)'");
			m = p.matcher(wherePrams);
			while (m.find()) {
				String temp = m.group();
				wherePrams = wherePrams.replace(temp, temp.replace("'", "").replace("[fmt.R", "t_0.").replace("[fmt.L", "t_0").replace("]", ""));
			}
			wherePrams = wherePrams.replace("[fmt.R]", "t_" + index).replace("[fmt.L]", "t_0");
			
			if (tabWhere.length() < 1) {
				tabWhere += wherePrams;
			}else{
				tabWhere += (wherePrams.replace("where", "AND").replace("WHERE", "AND"));
			}
			
			
			//增加别名索引
			if (!isExcTab(tempFmt, tName)) {
				index ++;
			}
			
		}
		
		if (where.getWherePrams().length() < 1) {
			sql += " FROM " + sqlTab + tabWhere;
		}else{
			
			String leftWhere = where.getWherePrams();
			
			Pattern p = Pattern.compile("(?<=\\s?)\\w+(?=\\s?(?:=|like|>|<|<>|=| like|LIKE|is|IS|is not|IS NOT))");
			Matcher m = p.matcher(leftWhere);
			while (m.find()) {
				String temp = m.group();
//				temp = temp.replaceAll("(WHERE|where|and|AND|or|OR) ", "").replace(" ", "");
				if (temp.equals("1")) {
					continue;
				}
				int i = leftWhere.indexOf(temp);
				String head = leftWhere.substring(0, i);
				String foot = leftWhere.substring(i);
				leftWhere = head + "t_0." + foot;
//				leftWhere = leftWhere.replace(temp, "t_0." + temp);
			}
			
			if (leftWhere.indexOf("order by") != -1 || leftWhere.indexOf("ORDER BY") != -1) {
				int start = leftWhere.indexOf("order by ") + 9;
				int endIndex = leftWhere.indexOf(",", start);
				if (endIndex == -1) {
					endIndex = leftWhere.indexOf(" ", start);
				}else{
					while (endIndex != -1) {
						//替换
						String substring = leftWhere.substring(start, endIndex);
						leftWhere = leftWhere.replace(substring, "t_0." + substring);
						//重新生成条件
						start = endIndex + 5;
						endIndex = leftWhere.indexOf(",", start);
					}
				}
				
			}
			
			sql += " FROM " + sqlTab + tabWhere + (leftWhere.replace("where", "AND").replace("WHERE", "AND"));
		}
		SqlLog.info(sql);
		List<Map<String, Object>> selectList = sqlSessionTemplateASS.selectList("selectList", sql);
		
		List<T> list = new ArrayList<>();
		for (Map<String, Object> map : selectList) {
			T t = handleResult(map, this.entityClass);
			list.add(t);
		}
		
		return list;
	}
	
	/**
	 * 是否为SQL表达符号
	 * @param c
	 * @return
	 */
	private boolean isC(String c){
		switch (c) {
		case "=":
			return true;
		case "<":
			return true;
		case ">":
			return true;
		case "<>":
			return true;
		case "like":
			return true;
		case "in":
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * 从List<String>集合中检查是否有存在的元素
	 * @param list
	 * @param tabName
	 * @return
	 */
	private boolean isExcTab (List<String> list, String tabName){
		for (String string : list) {
			if (tabName .equals( string)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private PK getId() {
		
		if(this.idType.indexOf("String") != -1) {
			return (PK)UUID.randomUUID().toString().replace("-", "");
		}
		return sqlSessionTemplateASS.selectOne("SELECT LAST_INSERT_ID()");
	}



}
