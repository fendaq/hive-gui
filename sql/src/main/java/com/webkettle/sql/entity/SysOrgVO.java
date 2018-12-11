
package com.webkettle.sql.entity;

import java.util.Date;
import java.util.List;

/**
 /**
 * 对象功能:组织架构SYS_ORG Model对象

 * 开发人员:pkq
 * 创建时间:2011-11-09 11:20:13
 */
public class SysOrgVO
{

	// 组织ID
	private Long orgId;
	// 维度编号
	private Long demId;
	// 维度名称
	private String demName;
	// 名称
	private String orgName;
	// 描述
	private String orgDesc;
	//组织路径
	private String orgPathname;
	// 上级
	private Long orgSupId;
	// 上级组织名称(如果等于维度ID则说明该组织是该维度的根节点)
	private String orgSupName;
	// 路径
	private String path;
	// 层次
	private Integer depth;
	// 类型
	private Long orgType;
	// 建立人
	private Long creatorId;
	// 建立时间
	private Date createtime;
	// 修改人
	private Long updateId;
	// 修改时间
	private Date updatetime;
	//负责人id
	private String ownUser;
	//负责人姓名
	private String ownUserName;
	//创建人
	private String createName;
	//修改人
	private String updateName;
	// 序号
	private Long sn = 0L;
	//在线人数
	private Integer onlineNum = 0;
	private Short isPrimary;
	//是否根节点（0，非根节点,1,根节点)
	private Short isRoot = 0;
	//数据来源（0，系统添加,1,来自AD同步)
	private Short fromType = 0;
	//图标
	private String iconPath = "";
	//树展开
	private String open = "true";
	// 是否叶子结点(0否,1是),主要用于数据库保存
	private Integer isLeaf;
	// 是否父类,主要用于树的展示时用
	private String isParent;

	private Short isDelete = 0;
	protected List<SysUserVO> userList;
	private Long id;
	private String name;
	protected Integer totalCount;
	protected Integer orgLevel;

	/**
	 * 创建人ID
	 */
	protected Long createBy;
	/**
	 * 更新人ID
	 */
	protected Long updateBy;


	public Long getOrgId()
	{
		return orgId;
	}

	public void setOrgId(Long orgId)
	{
		this.orgId = orgId;
	}

	public Long getDemId()
	{
		return demId;
	}

	public void setDemId(Long demId)
	{
		this.demId = demId;
	}

	public String getDemName()
	{
		return demName;
	}

	public void setDemName(String demName)
	{
		this.demName = demName;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getOrgDesc()
	{
		return orgDesc;
	}

	public void setOrgDesc(String orgDesc)
	{
		this.orgDesc = orgDesc;
	}

	public String getOrgPathname()
	{
		return orgPathname;
	}

	public void setOrgPathname(String orgPathname)
	{
		this.orgPathname = orgPathname;
	}

	public Long getOrgSupId()
	{
		return orgSupId;
	}

	public void setOrgSupId(Long orgSupId)
	{
		this.orgSupId = orgSupId;
	}

	public String getOrgSupName()
	{
		return orgSupName;
	}

	public void setOrgSupName(String orgSupName)
	{
		this.orgSupName = orgSupName;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Integer getDepth()
	{
		return depth;
	}

	public void setDepth(Integer depth)
	{
		this.depth = depth;
	}

	public Long getOrgType()
	{
		return orgType;
	}

	public void setOrgType(Long orgType)
	{
		this.orgType = orgType;
	}

	public Long getCreatorId()
	{
		return creatorId;
	}

	public void setCreatorId(Long creatorId)
	{
		this.creatorId = creatorId;
	}

	public Date getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}

	public Long getUpdateId()
	{
		return updateId;
	}

	public void setUpdateId(Long updateId)
	{
		this.updateId = updateId;
	}

	public Date getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Date updatetime)
	{
		this.updatetime = updatetime;
	}

	public String getOwnUser()
	{
		return ownUser;
	}

	public void setOwnUser(String ownUser)
	{
		this.ownUser = ownUser;
	}

	public String getOwnUserName()
	{
		return ownUserName;
	}

	public void setOwnUserName(String ownUserName)
	{
		this.ownUserName = ownUserName;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Long getSn()
	{
		return sn;
	}

	public void setSn(Long sn)
	{
		this.sn = sn;
	}

	public Integer getOnlineNum()
	{
		return onlineNum;
	}

	public void setOnlineNum(Integer onlineNum)
	{
		this.onlineNum = onlineNum;
	}

	public Short getIsPrimary()
	{
		return isPrimary;
	}

	public void setIsPrimary(Short isPrimary)
	{
		this.isPrimary = isPrimary;
	}

	public Short getIsRoot()
	{
		return isRoot;
	}

	public void setIsRoot(Short isRoot)
	{
		this.isRoot = isRoot;
	}

	public Short getFromType()
	{
		return fromType;
	}

	public void setFromType(Short fromType)
	{
		this.fromType = fromType;
	}

	public String getIconPath()
	{
		return iconPath;
	}

	public void setIconPath(String iconPath)
	{
		this.iconPath = iconPath;
	}

	public String getOpen()
	{
		return open;
	}

	public void setOpen(String open)
	{
		this.open = open;
	}

	public Integer getIsLeaf()
	{
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public String getIsParent()
	{
		return isParent;
	}

	public void setIsParent(String isParent)
	{
		this.isParent = isParent;
	}

	public Short getIsDelete()
	{
		return isDelete;
	}

	public void setIsDelete(Short isDelete)
	{
		this.isDelete = isDelete;
	}

	public List<SysUserVO> getUserList()
	{
		return userList;
	}

	public void setUserList(List<SysUserVO> userList)
	{
		this.userList = userList;
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

	public Long getUpdateBy()
	{
		return updateBy;
	}

	public void setUpdateBy(Long updateBy)
	{
		this.updateBy = updateBy;
	}

	public Integer getOrgLevel()
	{
		return orgLevel;
	}

	public void setOrgLevel(Integer orgLevel)
	{
		this.orgLevel = orgLevel;
	}

}
