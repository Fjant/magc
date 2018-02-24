package com.xincheng.customer.action;

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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import com.xincheng.config.model.FileConfig;
import com.xincheng.customer.model.CustomerMsg;
import com.xincheng.customer.service.CustomerMsgService;
import com.xincheng.utils.JsonUtils;
import com.xincheng.utils.Struts2Utils;
import com.xincheng.utils.Zip4jUtil;
import com.xincheng.web.BaseAction;

@Namespace("/customer")
@Action(value ="customerMsg")
@Results( { @Result(name=BaseAction.SUCCESS, type = "json") })
public class CustomerMsgAction extends BaseAction<CustomerMsg>{

	private static final long serialVersionUID = -8458189009041769305L;
	
	@Autowired
	private CustomerMsgService customerMsgService;
	
	@Autowired
	private FileConfig fileConfig;
	
	public String list(){
		try {
			Map<String , Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
			filter = initPage(filter);
			List<CustomerMsg> list = customerMsgService.getByPage(filter);
			int count = customerMsgService.count(filter);
			page.setResult(list);
			page.setTotalCount(count);
		} catch (Exception e) {
			logger.error("获取客户消息失败！");
		} finally {
			JsonUtils.renderJson(page);
		}
		return SUCCESS;
	}

	public void exportCustomerMsg(){

		Map<String, Object> filter = WebUtils.getParametersStartingWith(Struts2Utils.getRequest(), "search_");
		filter.put("pageFrom", 0);
		filter.put("pageTo", Integer.MAX_VALUE);
		
		//设置excel工作表的标题
		String[] title = { "客户号",  "保单号",  "服务类型",  "处理状态" ,  "推送时间", "微信推送内容"};

		// 获得开始时间
		long start = System.currentTimeMillis();
		// 输出的excel的路径
		String xlsFilePath = fileConfig.getFileDownloadPath() + "/msgc/msg/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/客户消息情况_" + start + ".xls";
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
			WritableSheet sheet = wwb.createSheet("客户消息列表", 0);
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
			List<CustomerMsg> list = customerMsgService.getByPage(filter);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			for (int i = 0; i < list.size(); i++) {
					
				label = new Label(0, i + 1, list.get(i).getCustomerNo());
				sheet.addCell(label);
				
				label = new Label(1, i + 1, list.get(i).getBillNo());
				sheet.addCell(label);

				label = new Label(2, i + 1, list.get(i).getTypeId() == 1 ? "续期缴费":"生日提醒");
				sheet.addCell(label);
                
				label = new Label(3, i + 1, list.get(i).getStatusId() == 1 ? "已发送":"未发送");
				sheet.addCell(label);
				
				Date date = list.get(i).getSendTime();
				if(null != date){
					String newdate = sdf.format(date);
					label = new Label(4, i + 1, newdate);
					sheet.addCell(label);
					
				label = new Label(5, i + 1, list.get(i).getContent());
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
	public CustomerMsg getModel() {
		// TODO Auto-generated method stub
		return entity;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
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
