
package com.webkettle.sql.entity;

import java.util.Date;


/**
开发人员:csx 创建时间:2011-11-18 16:24:10
 */
public class SysRoleVO
{

	protected Long dataFromType;
	// roleId
	protected Long roleId;
	// 系统ID  如果为0 则为全局角色
	protected Long systemId;
	// 角色别名
	protected String alias;
	// 角色名
	protected String roleName;
	// 备注
	protected String memo;
	// 允许删除
	protected Short allowDel;
	// 允许编辑
	protected Short allowEdit;
	// 是否启用
	protected Short enabled;
	//子系统
	//protected SubSystem subSystem;

	protected String systemName = "";
	//是否父节点
	protected String isParent = "false";
	protected String fhDistributable;
	protected Integer totalCount;
	//子系统
	protected SubSystemVO subSystem;
	/**
	 * 创建人ID
	 */
	protected Long createBy;
	/**
	 * 创建时间
	 */
	protected Date createtime;
	/**
	 * 更新时间
	 */
	protected Date updatetime;
	/**
	 * 更新人ID
	 */
	protected Long updateBy;
	
	
	public Long getDataFromType()
	{
		return dataFromType;
	}

	public void setDataFromType(Long dataFromType)
	{
		this.dataFromType = dataFromType;
	}

	public Long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}

	public Long getSystemId()
	{
		return systemId;
	}

	public void setSystemId(Long systemId)
	{
		this.systemId = systemId;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public Short getAllowDel()
	{
		return allowDel;
	}

	public void setAllowDel(Short allowDel)
	{
		this.allowDel = allowDel;
	}

	public Short getAllowEdit()
	{
		return allowEdit;
	}

	public void setAllowEdit(Short allowEdit)
	{
		this.allowEdit = allowEdit;
	}

	public Short getEnabled()
	{
		return enabled;
	}

	public void setEnabled(Short enabled)
	{
		this.enabled = enabled;
	}

	public String getSystemName()
	{
		return systemName;
	}

	public void setSystemName(String systemName)
	{
		this.systemName = systemName;
	}

	public String getIsParent()
	{
		return isParent;
	}

	public void setIsParent(String isParent)
	{
		this.isParent = isParent;
	}

	public String getFhDistributable()
	{
		return fhDistributable;
	}

	public void setFhDistributable(String fhDistributable)
	{
		this.fhDistributable = fhDistributable;
	}

	public Integer getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}

	public Long getCreateBy()
	{
		return createBy;
	}

	public void setCreateBy(Long createBy)
	{
		this.createBy = createBy;
	}

	public Date getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}

	public Date getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Date updatetime)
	{
		this.updatetime = updatetime;
	}

	public Long getUpdateBy()
	{
		return updateBy;
	}

	public void setUpdateBy(Long updateBy)
	{
		this.updateBy = updateBy;
	}

	public SubSystemVO getSubSystem()
	{
		return subSystem;
	}

	public void setSubSystem(SubSystemVO subSystem)
	{
		this.subSystem = subSystem;
	}

}
