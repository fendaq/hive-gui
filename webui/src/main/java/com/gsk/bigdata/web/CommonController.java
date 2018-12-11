
package com.gsk.bigdata.web;

import com.aiyi.core.beans.ResultBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.LoadingCache;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.sql.entity.*;
import com.webkettle.webservice.client.UserDetailsService;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制台主框架地址
 */
@RestController
@RequestMapping("api/common")
public class CommonController
{

	@Resource
	private UserDetailsService userDetailsService;
	@Value("${auth.login-path}")
	private String loginPath;
	@Value("${auth.system-id}")
	private String systemId;

	public static LoadingCache<String, String> cache = null;
	protected static final Logger logger = LoggerFactory.getLogger(CommonController.class);

//	/**
//	 * 列出手机号与当前账号手机号一致的用户
//	 * @param account
//	 *         用户账号
//	 * @return
//	 *          相同用户列表
//	 */
//	@GetMapping("userAccount")
//	public ResultBean userAccount(String account)
//	{
//		Map<String, Object> list = new HashMap<>();
//		try
//		{
//			String jsonStr = userDetailsService.loadUserByAccountMobile(account);
//			@SuppressWarnings(
//			{ "unchecked", "deprecation" })
//			List<SysUserVO> sysUserList = (List<SysUserVO>) JSONArray.toList(JSONArray.fromObject(jsonStr), SysUserVO.class);
//			list.put("list", sysUserList);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return ResultBean.success("用户获取成功").setResponseBody(list);
//	}

	/**
	 * 查询当前用户管理列表
	 * @param databaseId
	 *          数据库ID
	 * @return
	 *          数据表列表
	 */
	/*@GetMapping("sysUserGoto/{account}/{token}")
	@ResponseBody
	public void sysUserGoto(@PathVariable("account") String account, @PathVariable("token") String token, HttpServletResponse response)
	{
		try
		{
			String url = null;
			response.sendRedirect("http://" + loginPath + "/" + null + "/platform/system/sysUser/list.ht" + "?user=" + account + "&fromSys=" + "CIRBWS" + "&fromSvr=" + url
					+ "&toSys=" + "userSignOnOT" + "&uuid=" + token + "&toUrl=" + "irwWsUserSignOnOT");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}*/

	/**
	 * 获取权限管理用户列表
	 * @return
	 *          相同用户列表
	 */
	@GetMapping("authority/users")
	public ResultBean users(){
		String account = "";
        String jsonStr = userDetailsService.getUserAndOrgList(account);
		return ResultBean.success("权限管理用户列表获取成功")
                .putResponseBody("list", JSONArray.parseArray(jsonStr, LinkedHashMap.class));
	}

	/**
	 * 获取权限管理所有角色列表
	 * @return
	 *          相同用户列表
	 */
	@GetMapping("authority/roles")
	public ResultBean roles(){
        String jsonStr = userDetailsService.getRoleList();
		return ResultBean.success("权限管理所有角色列表获取成功")
                .putResponseBody("list", JSONArray.parseArray(jsonStr, LinkedHashMap.class));
	}

	/**
	 * 获取权限管理用户能够操作的权限列表
	 * @param userId
	 *         用户ID
	 * @param roleId
	 * 			角色ID
	 * @param orgId
	 * 			机构ID
	 * @return
	 *          相同用户列表
	 */
	@GetMapping("authority/search")
	public ResultBean search(Long userId, Long roleId, Long orgId)
	{
        String jsonStr = userDetailsService.getUserAuthorityList(userId, roleId, orgId, ContextUtil.getUserId());
		return ResultBean.success("获取权限管理用户能够操作的权限列表成功")
                .putResponseBody("list", JSONArray.parseArray(jsonStr, LinkedHashMap.class));
	}

	/**
	 * 权限管理用户授权提交
	 * @param authorityVO
	 *         用户账号
	 * @return
	 *          相同用户列表
	 */
	@PostMapping("authority")
	public ResultBean authority(@RequestBody AuthorityVO authorityVO)
	{
	    System.out.println(JSON.toJSONString(authorityVO));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", authorityVO.getUserId());
        jsonObject.put("orgId", authorityVO.getOrgId());
        jsonObject.put("roleId", authorityVO.getRoleId());
        jsonObject.put("groupList", authorityVO.getList());
        jsonObject.put("loginId", ContextUtil.getUserId());
        String jsonStr = userDetailsService.updateAuthority(jsonObject.toString());
		return ResultBean.success("权限管理用户授权提交成功")
                .putResponseBody("list", JSONArray.parseArray(jsonStr, LinkedHashMap.class));
	}
}
