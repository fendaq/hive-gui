package com.webkettle.sql.entity;

import com.aiyi.core.beans.Po;

import java.util.List;

/**
 * 权限列表接收VO
 * @author zhouq
 */
public class AuthorityVO extends Po
{
	private Long userId;
	private Long orgId;
	private Long roleId;
	private List<DatabaseGroupVO> list;
	public Long getUserId()
	{
		return userId;
	}
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	public Long getOrgId()
	{
		return orgId;
	}
	public void setOrgId(Long orgId)
	{
		this.orgId = orgId;
	}
	public Long getRoleId()
	{
		return roleId;
	}
	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}
	public List<DatabaseGroupVO> getList()
	{
		return list;
	}
	public void setList(List<DatabaseGroupVO> list)
	{
		this.list = list;
	}
	
	
}
