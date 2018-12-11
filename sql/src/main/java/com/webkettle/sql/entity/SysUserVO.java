
package com.webkettle.sql.entity;

import java.util.Date;
import java.util.List;

/**
 开发人员:haxx 创建时间:2011-11-03 16:02:45
 */
public class SysUserVO
{
	// userOrgId
	protected Long userOrgId;
	// userId
	protected Long userId;
	// 姓名
	protected String fullname;
	// 帐号
	protected String account;
	// 密码
	protected String password;
	// 是否过期
	protected Short isExpired;
	// 是否锁定
	protected Short isLock;
	// 创建时间
	// 状态
	protected Short status;
	// 邮箱
	protected String email;
	// 手机
	protected String mobile;
	// 电话
	protected String phone;

	// 性别
	protected String sex;
	// 照片
	protected String picture;

	// 类型
	protected String retype;

	// 组织名称
	protected String orgName;

	// 是否删除
	protected String isDel;

	// 流程接收人轮转时间
	protected Date lastTime;
	/**
	 * 数据来源
	 */
	protected short fromType;

	protected Long dataFromType;
	protected String isFhDistributable;
	protected String orgCd;
	protected Long loginUserId;
	protected String isFhAdminRole;
	protected String userIsDistributable;
	protected String aryPosId;
	protected String aryRoleId;
	protected String token;
	private Long id;
	private String name;
	protected Integer totalCount;
	protected List authorities;
	protected boolean accountNonExpired;
	protected boolean accountNonLocked;
	protected boolean credentialsNonExpired;
	protected boolean enabled;
	protected String roles;
	protected String username;

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

	protected Long systemId;

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getUserOrgId()
	{
		return userOrgId;
	}

	public void setUserOrgId(Long userOrgId)
	{
		this.userOrgId = userOrgId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getFullname()
	{
		return fullname;
	}

	public void setFullname(String fullname)
	{
		this.fullname = fullname;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Short getIsExpired()
	{
		return isExpired;
	}

	public void setIsExpired(Short isExpired)
	{
		this.isExpired = isExpired;
	}

	public Short getIsLock()
	{
		return isLock;
	}

	public void setIsLock(Short isLock)
	{
		this.isLock = isLock;
	}

	public Short getStatus()
	{
		return status;
	}

	public void setStatus(Short status)
	{
		this.status = status;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getRetype()
	{
		return retype;
	}

	public void setRetype(String retype)
	{
		this.retype = retype;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getIsDel()
	{
		return isDel;
	}

	public void setIsDel(String isDel)
	{
		this.isDel = isDel;
	}

	public Date getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(Date lastTime)
	{
		this.lastTime = lastTime;
	}

	public short getFromType()
	{
		return fromType;
	}

	public void setFromType(short fromType)
	{
		this.fromType = fromType;
	}

	public Long getDataFromType()
	{
		return dataFromType;
	}

	public void setDataFromType(Long dataFromType)
	{
		this.dataFromType = dataFromType;
	}

	public String getIsFhDistributable()
	{
		return isFhDistributable;
	}

	public void setIsFhDistributable(String isFhDistributable)
	{
		this.isFhDistributable = isFhDistributable;
	}

	public String getOrgCd()
	{
		return orgCd;
	}

	public void setOrgCd(String orgCd)
	{
		this.orgCd = orgCd;
	}

	public Long getLoginUserId()
	{
		return loginUserId;
	}

	public void setLoginUserId(Long loginUserId)
	{
		this.loginUserId = loginUserId;
	}

	public String getIsFhAdminRole()
	{
		return isFhAdminRole;
	}

	public void setIsFhAdminRole(String isFhAdminRole)
	{
		this.isFhAdminRole = isFhAdminRole;
	}

	public String getUserIsDistributable()
	{
		return userIsDistributable;
	}

	public void setUserIsDistributable(String userIsDistributable)
	{
		this.userIsDistributable = userIsDistributable;
	}

	public String getAryPosId()
	{
		return aryPosId;
	}

	public void setAryPosId(String aryPosId)
	{
		this.aryPosId = aryPosId;
	}

	public String getAryRoleId()
	{
		return aryRoleId;
	}

	public void setAryRoleId(String aryRoleId)
	{
		this.aryRoleId = aryRoleId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public void setCreatetime(java.util.Date createtime)
	{
		this.createtime = createtime;
	}

	/**
	 * 返回 创建时间
	 *
	 * @return
	 */
	public java.util.Date getCreatetime()
	{
		return createtime;
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

	public List getAuthorities()
	{
		return authorities;
	}

	public void setAuthorities(List authorities)
	{
		this.authorities = authorities;
	}

	public boolean isAccountNonExpired()
	{
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired)
	{
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked()
	{
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked)
	{
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired()
	{
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired)
	{
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getRoles()
	{
		return roles;
	}

	public void setRoles(String roles)
	{
		this.roles = roles;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

}
