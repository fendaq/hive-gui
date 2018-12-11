package com.aiyi.core.sql.where;

public enum C {

	EQ,NE,LIKE,DA,IXAO,IN,NOTIN;
	
	public static String getSqlWhere(C c){
		switch (c) {
		case EQ:
			return " = ";
		case NE:
			return " <>";
		case LIKE:
			return " like ";	
		case DA:
			return " > ";	
		case IXAO:
			return " < ";	
		case IN:
			return " in ";
		case NOTIN:
			return " not in ";
		default:
			return " = ";	
		}
	}
}
