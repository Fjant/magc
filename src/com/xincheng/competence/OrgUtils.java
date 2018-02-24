package com.xincheng.competence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.icitic.model.Orgnization;
import com.xincheng.memcached.MemcachedUtils;

public class OrgUtils {
	private final static String ORG_KEY = "YXY_ORG_INFO";

	/**
	 * 缓存所有组织结构数据
	 * */
	// public static void setCache(List<Orgnization> sourceOrgs) {
	//
	// if (sourceOrgs == null) {
	// return;
	// }
	//
	// // 1、先按级别排序
	// List<SimpleOrgInfo> simpleOrgs = new ArrayList<SimpleOrgInfo>();
	// for (Orgnization org : sourceOrgs) {
	// SimpleOrgInfo item = new SimpleOrgInfo();
	// item.setId(org.getId());
	// item.setName(org.getFullname());
	// item.setGrade(item.getGrade());
	// item.setHighId(org.getHighid());
	// simpleOrgs.add(item);
	// }
	// Collections.sort(simpleOrgs);
	//
	// // 2、将组织结构数据扁平化
	// Map<String, SimpleOrgInfo> orgMaps = new HashMap<String,
	// SimpleOrgInfo>();
	// List<SimpleOrgInfo> cacheOrgs = new ArrayList<SimpleOrgInfo>();
	// for (SimpleOrgInfo org : simpleOrgs) {
	// // 一二级组织直接处理，减少循环次数
	// if (org.getGrade() == 1) {
	// org.setFirstId(org.getId());
	// } else if (org.getGrade() == 2) {
	// org.setFirstId(org.getHighId());
	// org.setSecondId(org.getId());
	// } else {
	// SimpleOrgInfo parentOrg = (SimpleOrgInfo) orgMaps.get(org.getHighId());
	//
	// if (parentOrg == null) {
	// continue;
	// }
	//
	// getParentOrg(org, parentOrg);
	// }
	//
	// orgMaps.put(org.getId(), org);
	// cacheOrgs.add(org);
	// }
	//
	// if (!MemcachedUtils.containsKey(ORG_KEY)) {
	// MemcachedUtils.set(ORG_KEY, cacheOrgs, new Date(1000 * 60 * 15));
	// }
	//
	// }

	/**
	 * 
	 * */
	// private static SimpleOrgInfo getParentOrg(SimpleOrgInfo target,
	// SimpleOrgInfo parentOrg) {
	//
	// if (target.getGrade() == 3) {
	// target.setFirstId(parentOrg.getFirstId());
	// target.setSecondId(parentOrg.getSecondId());
	// target.setThirdId(target.getId());
	// } else if (target.getGrade() == 4) {
	// target.setFirstId(parentOrg.getFirstId());
	// target.setSecondId(parentOrg.getSecondId());
	// target.setThirdId(parentOrg.getThirdId());
	// target.setFourthId(target.getId());
	// } else if (target.getGrade() == 5) {
	// target.setFirstId(parentOrg.getFirstId());
	// target.setSecondId(parentOrg.getSecondId());
	// target.setThirdId(parentOrg.getThirdId());
	// target.setFourthId(parentOrg.getFourthId());
	// target.setFifthId(target.getId());
	// } else if (target.getGrade() == 6) {
	// target.setFirstId(parentOrg.getFirstId());
	// target.setSecondId(parentOrg.getSecondId());
	// target.setThirdId(parentOrg.getThirdId());
	// target.setFourthId(parentOrg.getFourthId());
	// target.setFifthId(parentOrg.getFifthId());
	// target.setSixthId(target.getId());
	// } else if (target.getGrade() == 7) {
	// target.setFirstId(parentOrg.getFirstId());
	// target.setSecondId(parentOrg.getSecondId());
	// target.setThirdId(parentOrg.getThirdId());
	// target.setFourthId(parentOrg.getFourthId());
	// target.setFifthId(parentOrg.getFifthId());
	// target.setSixthId(parentOrg.getSixthId());
	// target.setSeventhId(target.getId());
	// }
	//
	// return target;
	// }

	// /**
	// * 获取指定组织下的所有子组织，包含自己，以逗号间隔，如1001,2030,4059
	// * */
	// public static String getSubOrgnizations(String orgId) {
	// String orgIds = "";
	// if (MemcachedUtils.containsKey(ORG_KEY)) {
	// List<SimpleOrgInfo> orgList = (List<SimpleOrgInfo>)
	// MemcachedUtils.get(ORG_KEY);
	// for (SimpleOrgInfo info : orgList) {
	// if (info.getFirstId().equals(orgId) || info.getSecondId().equals(orgId)
	// || info.getThirdId().equals(orgId) || info.getFourthId().equals(orgId) ||
	// info.getFifthId().equals(orgId) || info.getSixthId().equals(orgId) ||
	// info.getSeventhId().equals(orgId)) {
	// orgIds += info.getId() + ",";
	// }
	// }
	// if (StringUtils.isNotEmpty(orgIds)) {
	// orgIds = orgIds.substring(0, orgIds.length() - 1);
	// }
	// }
	// return orgIds;
	// }

	public static String getSimpleSubOrgnizations(String orgId) {
		String orgIds = null;
		if (MemcachedUtils.containsKey(ORG_KEY)) {
			Map<String, String> orgList = (Map<String, String>) MemcachedUtils.get(ORG_KEY);
			if (orgList.containsKey(orgId)) {
				orgIds = orgList.get(orgId);
			}
		}
		return orgIds;
	}

	public static void setSimpleCache(List<Orgnization> sourceOrgs) {

		if (sourceOrgs == null) {
			return;
		}

		// // 1、先按级别排序
		// Map<String, String> simpleOrgs = new HashMap<String, String>();
		// for (Orgnization org : sourceOrgs) {
		// if (simpleOrgs.containsKey(org.getId())) {
		// continue;
		// }
		//
		// String orgId = org.getId();
		// String parentIds = org.getId();
		// // 如果当前组织没有上级组织，则查询结束
		// if (StringUtils.isEmpty(org.getHighid())) {
		// simpleOrgs.put(orgId, parentIds);
		// } else {
		// //获取上级部门
		// //getParentId(parentIds, org.getHighid(), sourceOrgs, simpleOrgs);
		// //获取级级部门
		// getParentId(parentIds, org.getHighid(), sourceOrgs, simpleOrgs);
		// simpleOrgs.put(orgId, parentIds);
		// }
		// }

		// 1、先按级别排序
		Map<String, String> simpleOrgs = new HashMap<String, String>();
		for (Orgnization org : sourceOrgs) {
			if (simpleOrgs.containsKey(org.getId())) {
				continue;
			}

			if (StringUtils.isNotEmpty(org.getId())) {
				String orgId = org.getId();
				StringBuilder subOrgIds = new StringBuilder(org.getId());

				// 获取级级部门
				getSubIds(subOrgIds, org.getId(), sourceOrgs, simpleOrgs);

				simpleOrgs.put(orgId, subOrgIds.toString());
			}
		}

		if (!MemcachedUtils.containsKey(ORG_KEY)) {
			MemcachedUtils.set(ORG_KEY, simpleOrgs, new Date(1000 * 60 * 60 * 24));
		}

	}

	// /**
	// * 循环获取上级组织
	// * */
	// private static void getParentId(String sourceOrgIds, String parentId,
	// List<Orgnization> sourceOrgs, Map<String, String> simpleOrgs) {
	// if (simpleOrgs.containsKey(parentId)) {
	// sourceOrgIds += simpleOrgs.get(parentId);
	// } else {
	// for (Orgnization org : sourceOrgs) {
	// if (org.getId().equals(parentId)) {
	// sourceOrgIds += "," + parentId;
	// if (StringUtils.isNotEmpty(org.getHighid())) {
	// getParentId(sourceOrgIds, org.getHighid(), sourceOrgs, simpleOrgs);
	// }
	//
	// break;
	// }
	// }
	// }
	// }

	private static void getSubIds(StringBuilder subOrgIds, String parentId, List<Orgnization> sourceOrgs, Map<String, String> simpleOrgs) {
		if (simpleOrgs.containsKey(parentId)) {
			subOrgIds.append(",").append(simpleOrgs.get(parentId));
		} else {
			for (Orgnization org : sourceOrgs) {
				if (org.getHighid().equals(parentId)) {
					// 添加子部门到列表中
					subOrgIds.append(",").append(org.getId());

					getSubIds(subOrgIds, org.getId(), sourceOrgs, simpleOrgs);
				}
			}
		}
	}
}
