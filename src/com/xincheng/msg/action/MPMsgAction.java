package com.xincheng.msg.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import com.xincheng.config.model.FileConfig;
import com.xincheng.msg.model.MPMsg;
import com.xincheng.msg.service.MPMsgService;
import com.xincheng.utils.JsonUtils;
import com.xincheng.utils.Struts2Utils;
import com.xincheng.utils.Zip4jUtil;
import com.xincheng.web.BaseAction;

@Namespace("/msg")
@Action(value ="mPMsg")
@Results( { @Result(name=BaseAction.SUCCESS, type = "json") })
public class MPMsgAction extends BaseAction<MPMsg>{

	private static final long serialVersionUID = -5089234600027040002L;

	@Autowired
	private MPMsgService mPMsgService;
	
	@Autowired
	private FileConfig fileConfig;
	
	@Override
	public MPMsg getModel() {
		// TODO Auto-generated method stub
		return entity;
	}
	
	public String list() {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			filter = initPage(filter);
			String sendType =(String)filter.get("sendType");
			String statusId = (String) filter.get("statusId");
			if("".equals(sendType) || null == sendType){
				filter.put("sendType",null);
			}else{
				filter.put("sendType", Integer.parseInt(sendType));
			}
			if("".equals(statusId) || null == statusId){
				filter.put("statusId",null);
			}else{
				filter.put("statusId",Integer.parseInt(statusId));
			}
			List<MPMsg> enter = mPMsgService.pageQuery(filter);
			int count = mPMsgService.count(filter);
			page.setResult(enter);
			page.setTotalCount(count);
		} catch (Exception e) {
			logger.error("查询企业号消息列表出错!", e);
			e.printStackTrace();
		} finally {
			JsonUtils.renderJson(page);
		}
		return SUCCESS;
	}

	 //根据ID获取企业号消息详情
	public String getMPMsgById() throws Exception {
		try {
			Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			String id = (String) filter.get("id");
			if (StringUtils.isEmpty(id)) {
				this.retData.setMessage("没有企业号ID，请重新操作");
			} else {
				MPMsg mpmsg = mPMsgService.getById(Long.parseLong(id));
				this.retData.setRetCode("1");
				this.retData.setReturnData(mpmsg);
			}
		} catch (Exception e) {
			logger.error("获取企业号信息详情异常", e);
		} finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() throws Exception {
		try {
			Map<String,Object> filter=WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			String id = null;
			String[] ids = null;
			if(filter.get("ids[]") instanceof String){
				id = (String) filter.get("ids[]");
			}else{
				ids = (String[])filter.get("ids[]");
			}
			if(null != id){
				mPMsgService.removeById(Long.parseLong(id));
			}
			if(null != ids){
				for (String mPMsgid : ids) {
					mPMsgService.removeById(Long.parseLong(mPMsgid));
				}
			}
			retData.setRetCode("1");
		} catch (Exception e) {
			logger.error("删除企业号信息失败", e);
		}
		finally {
			JsonUtils.renderJson(retData);
		}
		return SUCCESS;
	}
	
	public void exportMPMsg(){

		Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
		filter.put("pageFrom", 0);
		filter.put("pageTo", Integer.MAX_VALUE);
		String sendType =(String)filter.get("sendType");
		String statusId = (String) filter.get("statusId");
		if("".equals(sendType) || null == sendType){
			filter.put("sendType",null);
		}else{
			filter.put("sendType", Integer.parseInt(sendType));
		}
		if("".equals(statusId) || null == statusId){
			filter.put("statusId",null);
		}else{
			filter.put("statusId",Integer.parseInt(statusId));
		}
		//设置excel工作表的标题
		String[] title = { "客户微信号",  "客户姓名",  "模板编号",  "消息类型" ,  "消息状态", "发送结果", "发送类型","重复发送次数","发送时间"};

		// 获得开始时间
		long start = System.currentTimeMillis();
		// 输出的excel的路径
		String xlsFilePath = fileConfig.getFileDownloadPath() + "/msgc/msg/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/服务号消息情况_" + start + ".xls";
		String zipFilePath = fileConfig.getFileDownloadPath() + "/msgc/msg/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + start + ".zip";
		// 创建Excel工作薄
		WritableWorkbook wwb = null;
		OutputStream os = null; 
		try {
			File newExportFile = new File(xlsFilePath);
			File excelsStoreFolder = new File(newExportFile.getParent());
			if (!excelsStoreFolder.exists()) {
				excelsStoreFolder.mkdirs();
			}
			if (!newExportFile.exists()) {
				newExportFile.createNewFile();
			}

			os = new FileOutputStream(xlsFilePath);
			wwb = Workbook.createWorkbook(os);
			
			// 添加第一个工作表并设置第一个Sheet的名字
			WritableSheet sheet = wwb.createSheet("服务号消息列表", 0);
			sheet.getSettings().setDefaultColumnWidth(10);
			sheet.setRowView(0, 400, false);
			// 设置字体;
			WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
			WritableCellFormat cellFormat = new WritableCellFormat(titleFont);
			cellFormat.setBackground(Colour.BLUE_GREY);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			CellView cellView = new CellView();
			cellView.setAutosize(true); // 设置自动大小

			Label label;
			
			for (int i = 0; i < title.length; i++) {
				// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, 0, title[i], cellFormat);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}
					
			//执行查询操作
			List<MPMsg> list = mPMsgService.getByPage(filter);
			jxl.write.Number numb;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			for (int i = 0; i < list.size(); i++) {
					
				label = new Label(0, i + 1, list.get(i).getOpenId());
				sheet.addCell(label);
				
				label = new Label(1, i + 1, list.get(i).getCustomerName());
				sheet.addCell(label);

				label = new Label(2, i + 1, list.get(i).getTemplateId());
				sheet.addCell(label);

				label = new Label(3, i + 1, list.get(i).getTypeId() == 1 ? "续期缴费":"生日提醒");
				sheet.addCell(label);
                
				label = new Label(4, i + 1, list.get(i).getStatusId() == 1 ?"已发送":"未发送");
				sheet.addCell(label);

				label = new Label(5, i + 1, list.get(i).getSendResult());
				sheet.addCell(label);
				
				label = new Label(6, i + 1, list.get(i).getSendType() == 1 ? "直接发送":"队列发送");
				sheet.addCell(label);
				
				numb = new jxl.write.Number(7, i + 1, list.get(i).getResendTimes());
				sheet.addCell(numb);
				
				Date date = list.get(i).getSendTime();
				if(null != date){
					String newdate = sdf.format(date);
					label = new Label(8, i + 1, newdate);
					sheet.addCell(label);
				}
			}
			// 写入数据
			wwb.write();
			os.flush();
				
			long end = System.currentTimeMillis();
			logger.debug("----完成该操作共用的时间是:" + (end - start) / 1000);
							
		} catch (Exception e) {
			logger.error("导出客户抽奖情况错误", e);
			e.printStackTrace();
			retData.setRetCode("1");
		}finally{
			if (wwb != null) {
				try {
					wwb.close();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// 压缩文件,转为ZIP文件
		try {
			ArrayList<String> files = new ArrayList<String>();
			files.add(xlsFilePath.replace("\\", "/").replace("//", "/"));
			Zip4jUtil.zip(zipFilePath, files, true);

			String virtualUrl = (fileConfig.getFileVitualBaseDownloadUrl() + zipFilePath.replace(fileConfig.getFileDownloadPath(), "")).replace("\\", "/").replace("//", "/");
			retData.setRetCode("0");
			retData.setReturnData(virtualUrl);

		} catch (Exception ex) {
			logger.error("压缩文件出现错误。", ex);
		}
		JsonUtils.renderJson(retData);
	
	}
	
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
