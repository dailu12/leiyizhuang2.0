package com.ynyes.lyz.controller.management;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynyes.lyz.entity.DeliveryCheckReport;
import com.ynyes.lyz.entity.GarmentFranchisorReport;
import com.ynyes.lyz.entity.TdActiveUser;
import com.ynyes.lyz.entity.TdAgencyFund;
import com.ynyes.lyz.entity.TdCity;
import com.ynyes.lyz.entity.TdDiySite;
import com.ynyes.lyz.entity.TdGoodsInOut;
import com.ynyes.lyz.entity.TdManager;
import com.ynyes.lyz.entity.TdManagerDiySiteRole;
import com.ynyes.lyz.entity.TdManagerRole;
import com.ynyes.lyz.entity.TdOwn;
import com.ynyes.lyz.entity.TdReceipt;
import com.ynyes.lyz.entity.TdReserveOrder;
import com.ynyes.lyz.entity.TdReturnReport;
import com.ynyes.lyz.entity.TdSales;
import com.ynyes.lyz.entity.TdSalesDetail;
import com.ynyes.lyz.entity.TdSalesForContinuousBuy;
import com.ynyes.lyz.entity.TdSubOwn;
import com.ynyes.lyz.entity.TdWareHouse;
import com.ynyes.lyz.service.TdActiveUserService;
import com.ynyes.lyz.service.TdAgencyFundService;
import com.ynyes.lyz.service.TdCityService;
import com.ynyes.lyz.service.TdDeliveryCheckReportService;
import com.ynyes.lyz.service.TdDiySiteRoleService;
import com.ynyes.lyz.service.TdDiySiteService;
import com.ynyes.lyz.service.TdGarmentFranchisorReportService;
import com.ynyes.lyz.service.TdGatheringService;
import com.ynyes.lyz.service.TdGoodsInOutService;
import com.ynyes.lyz.service.TdManagerRoleService;
import com.ynyes.lyz.service.TdManagerService;
import com.ynyes.lyz.service.TdOwnService;
import com.ynyes.lyz.service.TdReceiptService;
import com.ynyes.lyz.service.TdReserveOrderService;
import com.ynyes.lyz.service.TdReturnReportService;
import com.ynyes.lyz.service.TdSalesDetailService;
import com.ynyes.lyz.service.TdSalesForActiveUserService;
import com.ynyes.lyz.service.TdSalesForContinuousBuyService;
import com.ynyes.lyz.service.TdSalesService;
import com.ynyes.lyz.service.TdSubOwnService;
import com.ynyes.lyz.service.TdUserService;
import com.ynyes.lyz.service.TdWareHouseService;
import com.ynyes.lyz.util.SiteMagConstant;

@Controller
@RequestMapping("/Verwalter/statement")
public class TdManagerStatementController extends TdManagerBaseController {
	
	@Autowired
	TdGoodsInOutService tdGoodsInOutService;
	@Autowired
	TdManagerService tdManagerService;
	@Autowired
	TdManagerRoleService tdManagerRoleService;
	@Autowired
	TdWareHouseService tdWareHouseService;
	@Autowired
	TdUserService tdUserService;
	@Autowired
	TdDiySiteService tdDiySiteService;
	@Autowired
	TdCityService tdCityService;
	@Autowired
	TdAgencyFundService tdAgencyFundService;
	@Autowired
	TdGatheringService tdGatheringService;
	@Autowired
	TdReceiptService tdReceiptService;
	@Autowired
	TdSalesDetailService tdSalesDetailService;
	@Autowired
	TdReturnReportService tdReturnReportService;
	@Autowired
	TdDiySiteRoleService tdDiySiteRoleService;
	@Autowired
	TdSalesService tdSalesService;
	@Autowired
	TdOwnService tdOwnService;
	@Autowired
	TdReserveOrderService tdReserveOrderService;
	@Autowired
	TdSubOwnService tdSubOwnService;
	@Autowired
	TdSalesForActiveUserService tdSalesForActiveUserService;
	@Autowired
	TdActiveUserService tdActiveUserService;
	@Autowired
	TdDeliveryCheckReportService tdDeliveryCheckReportService;
	
	@Autowired
	TdGarmentFranchisorReportService tdGarmentFranchisorReportService;
	
	@Autowired
	TdSalesForContinuousBuyService tdSalesForContinuousBuyService;
	
	
    /*
	 * 报表下载
	 */
	@RequestMapping(value = "/downdata",method = RequestMethod.GET)
	@ResponseBody
	public String dowmDataGoodsInOut(HttpServletRequest req,ModelMap map,String begindata,String enddata,HttpServletResponse response,String diyCode,String cityName,Long statusId) throws ParseException
	{
		//检查登录
		String username = (String) req.getSession().getAttribute("manager");
		if (null == username) {
			return "redirect:/Verwalter/login";
		}
		
		//检查权限
		TdManager tdManager = tdManagerService.findByUsernameAndIsEnableTrue(username);
		TdManagerRole tdManagerRole = null;
		if (null != tdManager && null != tdManager.getRoleId())
		{
			tdManagerRole = tdManagerRoleService.findOne(tdManager.getRoleId());
		}
		if (tdManagerRole == null)
		{
			return "redirect:/Verwalter/login";
		}
		
		Date begin = new Date();
		Date end = new Date();
		if(statusId==9){
			
		}else{
			 begin = stringToDate(begindata,null);
			 end = stringToDate(enddata,null);
			//设置默认时间
			if(null==begin){
				begin=getStartTime();
			}
			if(null==end){
				end=getEndTime();
			}
		}
		
		//门店管理员只能查询归属门店
		if (tdManagerRole.getTitle().equalsIgnoreCase("门店")) 
			{
	        	diyCode=tdManager.getDiyCode();
	        	cityName=null;
			}
		
		//获取到导出的excel
		HSSFWorkbook wb=acquireHSSWorkBook(statusId, begin, end, diyCode, cityName, username,tdDiySiteRoleService.userRoleDiyId(tdManagerRole, tdManager));
		

		String exportAllUrl = SiteMagConstant.backupPath;
		if (statusId==7) {
			downloadForSale(wb, exportAllUrl, response, acquireFileName(statusId));
		}else{
			 download(wb, exportAllUrl, response,acquireFileName(statusId));
		}
        return "";
	}
	
	/**
	 * 报表 展示
	 * 选择城市 刷新门店列表
	 * @param keywords 订单号
	 * @param page 当前页
	 * @param size 每页显示行数
	 * @param __EVENTTARGET
	 * @param __EVENTARGUMENT
	 * @param __VIEWSTATE
	 * @param map 
	 * @param req
	 * @param orderStartTime 开始时间
	 * @param orderEndTime 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市名称
	 * @param statusid 0:出退货报表 1:代收款报表2：收款报表3：销售明细报表4：退货报表5：领用记录报表
	 * @return
	 */
	@RequestMapping(value = "/goodsInOut/list/{statusId}")
	public String goodsListDialog(String keywords,@PathVariable Long statusId, Integer page, Integer size,
			String __EVENTTARGET, String __EVENTARGUMENT, String __VIEWSTATE,
			ModelMap map, HttpServletRequest req,String orderStartTime,String orderEndTime,String diyCode,String cityName,
			String oldOrderStartTime,String oldOrderEndTime) {
		
		String username = (String) req.getSession().getAttribute("manager");
		//判断是否登陆
		if (null == username)
		{
			return "redirect:/Verwalter/login";
		}

		TdManager tdManager = tdManagerService.findByUsernameAndIsEnableTrue(username);
		TdManagerRole tdManagerRole = null;
		if (null != tdManager && null != tdManager.getRoleId())
		{
			tdManagerRole = tdManagerRoleService.findOne(tdManager.getRoleId());
		}
		//判断是否有权限
		if (tdManagerRole == null)
		{
			return "redirect:/Verwalter/login";
		}
		//查询用户管辖门店权限
    	TdManagerDiySiteRole diySiteRole= tdDiySiteRoleService.findByTitle(tdManagerRole.getTitle());
    	//获取管理员管辖城市
    	List<TdCity> cityList= new ArrayList<TdCity>();
    	//获取管理员管辖门店
    	List<TdDiySite> diyList=new ArrayList<TdDiySite>(); 
    	
    	//管理员获取管辖的城市和门店
    	tdDiySiteRoleService.userRoleCityAndDiy(cityList, diyList, diySiteRole, tdManagerRole, tdManager);
    	
    	
		if (null == page || page < 0) {
			page = 0;
		}

		if (null == size || size <= 0) {
			size = SiteMagConstant.pageSize;
		}

		if (null != __EVENTTARGET) {
			if (__EVENTTARGET.equalsIgnoreCase("btnCancel"))
			{
			} 
			else if (__EVENTTARGET.equalsIgnoreCase("btnConfirm"))
			{
			}
			else if (__EVENTTARGET.equalsIgnoreCase("btnDelete"))
			{
			}
			else if (__EVENTTARGET.equalsIgnoreCase("btnPage")) 
			{
				if (null != __EVENTARGUMENT) 
				{
					page = Integer.parseInt(__EVENTARGUMENT);
				}
			}else if(__EVENTTARGET.equals("btnSearch")){
				page=0;
			}
		}
		
		//修改城市刷新门店列表
		if(StringUtils.isNotBlank(cityName)){
			//需要删除的diy
			List<TdDiySite> diyRemoveList=new ArrayList<TdDiySite>(); 
			for (TdDiySite tdDiySite : diyList) {
				if(!cityName.equals(tdDiySite.getCity())){
					diyRemoveList.add(tdDiySite);
					if(tdDiySite.getStoreCode().equals(diyCode)){
						diyCode=null;
					}
				}
			}
			diyList.removeAll(diyRemoveList);
		}
		
		Date begin=null;
		Date end=null;
		try {//字符串转换为时间
			begin=stringToDate(orderStartTime,null);
			end=stringToDate(orderEndTime,null);
		} catch (Exception e) {
			System.out.println(e);
		}
		if(begin==null){//如果为空设置为默认时间
			begin=getStartTime();
			orderStartTime=dateToString(begin,null);
		}
		if(end==null){//如果为空设置为默认时间
			end=getEndTime();
			orderEndTime=dateToString(end,null);
		}
		
		/*if(!orderStartTime.equals(oldOrderStartTime) || !orderEndTime.equals(oldOrderEndTime)){
			//调用存储过程查询数据
			callProcedure(statusId, __EVENTTARGET, begin, end, username);
		}*/
		
		//根据报表类型 查询相应数据到map
		//addOrderListToMap(map, statusId, keywords, begin, end, diyCode, cityName, username, size, page,tdDiySiteRoleService.userRoleDiyId(tdManagerRole, tdManager));
	
		
    	//城市和门店信息
    	map.addAttribute("diySiteList",diyList);
		map.addAttribute("cityList", cityList);
		
		// 参数注回
		map.addAttribute("orderNumber", keywords);
		map.addAttribute("orderStartTime", orderStartTime);
		map.addAttribute("orderEndTime", orderEndTime);
		map.addAttribute("diyCode", diyCode);
		map.addAttribute("cityName", cityName);
		map.addAttribute("oldOrderStartTime", orderStartTime);
		map.addAttribute("oldOrderEndTime", orderEndTime);

		map.addAttribute("page", page);
		map.addAttribute("size", size);
		map.addAttribute("keywords", keywords);
		map.addAttribute("statusId", statusId);
		map.addAttribute("__EVENTTARGET", __EVENTTARGET);
		map.addAttribute("__EVENTARGUMENT", __EVENTARGUMENT);
		map.addAttribute("__VIEWSTATE", __VIEWSTATE);

		if(statusId==9){
			return "/site_mag/statement_list_reserve_order";
		}
		return "/site_mag/statement_list";
	}

	

	private String changeName(List<TdWareHouse> wareHouses,String number)
	{
//		郑州公司	11	总仓
//		天荣中转仓	1101	分仓
//		五龙口中转仓	1102	分仓
//		东大中转仓	1103	分仓
//		百姓中转仓	1104	分仓
//		主仓库	1105	分仓
		for (TdWareHouse tdWareHouse : wareHouses) {
			if(tdWareHouse.getWhNumber().equals(number)){
				return tdWareHouse.getWhName();
			}
		}
		return number;
		
//		if (name == null || name.equalsIgnoreCase(""))
//		{
//			return "未知";
//		}
//		if (name.equalsIgnoreCase("11"))
//		{
//			return "郑州公司";
//		}
//		else if (name.equalsIgnoreCase("1101"))
//		{
//			return "天荣中转仓";
//		}
//		else if (name.equalsIgnoreCase("1102"))
//		{
//			return "五龙口中转仓";
//		}
//		else if (name.equalsIgnoreCase("1103"))
//		{
//			return "东大中转仓";
//		}
//		else if (name.equalsIgnoreCase("1104"))
//		{
//			return "百姓中转仓";
//		}
//		else if (name.equalsIgnoreCase("1105"))
//		{
//			return "主仓库";
//		}
//		else
//		{
//			return "未知编号：" + name;
//		}
	}
	
	/**
	 * 根据报表类型 调用相应的存储过程 插入数据
	 * @param statusId 报表类型
	 * @param __EVENTTARGET
	 * @param begin 开始时间
	 * @param end 结算时间
	 * @param username 当前用户
	 */
	@SuppressWarnings("unused")
	private void callProcedure(Long statusId,String __EVENTTARGET,Date begin,Date end,String username){
		try {//调用存储过程 报错
			if(null != __EVENTTARGET && __EVENTTARGET.equalsIgnoreCase("btnPage")){
				return;
			}else if(statusId==0){//出退货报表
//				tdGoodsInOutService.callinsertGoodsInOutInitial(begin, end,username);
			}else if(statusId==1){//代收款报表
//				tdAgencyFundService.callInsertAgencyFund(begin, end,username);
			}else if(statusId==2){//收款报表
//				tdReceiptService.callInsertReceipt(begin, end, username);
			}else if(statusId==3){//销售明细报表
//				tdSalesDetailService.callInsertSalesDetail(begin, end, username);
			}else if(statusId==4){//退货报表
				tdReturnReportService.callInsertReturnReport(begin, end, username);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * 根据报表类型 查询相应数据到map
	 * 增加门店id查询
	 * @param map 
	 * @param statusId 报表类型
	 * @param keywords 订单号
	 * @param begin 开始时间
	 * @param end 结算时间
	 * @param diySiteCode 门店编号
	 * @param cityName 城市名称
	 * @param username 当前用户
	 * @param roleDiyIds 门店id集合
	 * @param size
	 * @param page
	 */
	@SuppressWarnings("unused")
	private void addOrderListToMap(ModelMap map,Long statusId,String keywords,Date begin,Date end,String diySiteCode,String cityName,String username,
			int size,int page,List<String> roleDiyIds){
		if(statusId==0){//出退货报表
//			map.addAttribute("order_page",tdGoodsInOutService.searchList(keywords,begin, end, diySiteCode, cityName,username, size, page,roleDiyIds));
		}else if(statusId==1){//代收款报表
			map.addAttribute("order_page",tdAgencyFundService.searchList(keywords,begin, end,cityName ,diySiteCode ,username, size, page,roleDiyIds));
		}else if(statusId==2){//收款报表
//			map.addAttribute("order_page",tdGatheringService.searchList(keywords,begin, end,cityName ,diySiteCode ,username, size, page,roleDiyIds));
		}else if(statusId==3){//销售明细报表
//			map.addAttribute("order_page",tdSalesDetailService.queryPageList(begin, end, cityName, diySiteCode, roleDiyIds, size, page));
		}else if(statusId==4){//退货报表
			map.addAttribute("order_page",tdReturnReportService.searchList(keywords,begin, end,cityName ,diySiteCode ,username, size, page,roleDiyIds));
		}

	}
	/**
	 * 根据报表类型获取报表名
	 * @param statusId
	 * @return
	 */
	private String acquireFileName(Long statusId){
		String fileName="";
		if(statusId==0){
			fileName="配送单出退货报表";
		}else if(statusId==1){
			fileName= "代收款报表";
		}else if(statusId==2){
			fileName= "收款报表";
		}else if(statusId==3){
			fileName= "销售明细报表";
		}else if(statusId==4){
			fileName= "退货报表";
		}else if(statusId==5){
			fileName= "领用记录报表";
		}else if(statusId==6) {
			fileName="自提单出退货报表";
		}else if(statusId==7){
			fileName="销量报表";
		}else if(statusId==8){
			fileName="欠款报表";
		}else if(statusId==9){
			fileName="未提货报表";
		}else if(statusId==10){
			fileName="活跃会员报表";
		}else if(statusId==11){
			fileName="配送考核报表";
		}else if(statusId==12){
			fileName="加盟商对账报表";
		}else if(statusId==13){
			fileName="会员连续购买记录";
		}
		return fileName;
	}
	/**
	 * 根据报表状态 设置相应的报表
	 * @param statusId
	 * @param begin 开始时间
	 * @param end 结算时间
	 * @param diyCode 门店编号
	 * @param cityName 城市名称
	 * @param username 当前用户
	 * @return
	 * @throws ParseException 
	 */
	private HSSFWorkbook acquireHSSWorkBook(Long statusId,Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds) throws ParseException{
		HSSFWorkbook wb= new HSSFWorkbook();  
		if(statusId==0){//出退货明细报表
			wb=goodsInOutWorkBook(begin, end, diyCode, cityName, username,roleDiyIds);
		}else if(statusId==1){//代收款报表
			wb=agencyFundWorkBook(begin, end, diyCode, cityName, username,roleDiyIds);
		}else if(statusId==2){//收款报表
			wb=payWorkBook(begin, end, diyCode, cityName, username,roleDiyIds);
		}else if(statusId==3){//销售明细报表
			wb=salesDetailWorkBook(begin, end, diyCode, cityName, username,roleDiyIds);
		}else if(statusId==4){//退货报表
			wb=returnWorkBook(begin, end, diyCode, cityName, username,roleDiyIds);
		}else if(statusId==6) {//自提单出退货
			wb=goodsInOutTakeWorkBook(begin, end, diyCode, cityName, username, roleDiyIds);
		}else if(statusId==7){//销量报表
			wb=SalesWorkBook(begin, end, diyCode, cityName, username, roleDiyIds);
		}else if(statusId==8){//欠款报表
			wb=OwnWorkBook(begin, end, diyCode, cityName, username, roleDiyIds);
		}else if(statusId==9){//未提货报表
			wb=ReserveOrderBook(diyCode, cityName, username, roleDiyIds);
		}else if(statusId==10){//活跃会员报表
			wb=salesForActiveUserBook(begin, end, diyCode, cityName, username, roleDiyIds);
		}else if(statusId==11){//配送考核报表
			wb=deliveryCheckBook(begin, end, diyCode, cityName, username, roleDiyIds);
		}else if(statusId==12){//加盟商对账报表
			wb=garmentFranchisor(begin, end, diyCode, cityName, username, roleDiyIds);
		}else if(statusId==13){//会员连续月份购买报表
			wb=continuousBuyBook(begin,end,diyCode,cityName,username,roleDiyIds);
		}
		return wb;
	}
	

	

	

	

	/**
	 * 出退货明细报表
	 * 查询条件增加管理员管辖门店
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
 	 * @param username 当前用户
	 * @return
	 */
	private HSSFWorkbook goodsInOutWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdGoodsInOut> goodsInOutList=tdGoodsInOutService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
        
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(goodsInOutList!=null){
        	maxSize=goodsInOutList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,18,13,18,18,13,13,
	        		18,9,9,9,9,9,13,13,13,18,
	        		13,13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);


	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"门店名称","主单号","分单号","下单日期","出&退货日期","订单状态","导购","客户名称","客户电话","产品编号",
	        		"产品名称","数量","单价","总价","现金卷","品牌","商品父分类","商品子分类","配送方式","中转仓",
	        		"配送人员","配送人员电话","收货人姓名","收货人电话","收货人地址","订单备注"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdGoodsInOut goodsInOut= goodsInOutList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	//门店名称
	        	row.createCell(0).setCellValue(objToString(goodsInOut.getDiySiteName()));

	        	//代付款订单没有主单号  分单号显示到主单号位置
	        	if(goodsInOut.getStatusId() != null && goodsInOut.getStatusId().equals(2L)){
	        		row.createCell(1).setCellValue(objToString(goodsInOut.getOrderNumber()));
	        	}else{
	        		row.createCell(1).setCellValue(objToString(goodsInOut.getMainOrderNumber()));
	        		row.createCell(2).setCellValue(objToString(goodsInOut.getOrderNumber()));
	        	} 
	        	//下单时间
	        	row.createCell(3).setCellValue(objToString(goodsInOut.getOrderTime()));
	        	//确认时间
	        	row.createCell(4).setCellValue(objToString(goodsInOut.getSalesTime()));
	        	//订单状态
	        	row.createCell(5).setCellValue(orderStatus(goodsInOut.getStatusId()));
	        	//导购
				row.createCell(6).setCellValue(objToString(goodsInOut.getSellerRealName()));
				//客户名称
				row.createCell(7).setCellValue(objToString(goodsInOut.getRealName()));
				//客户电话
				row.createCell(8).setCellValue(objToString(goodsInOut.getUsername()));
				//产品编号
				row.createCell(9).setCellValue(objToString(goodsInOut.getSku()));
				//产品名称
				row.createCell(10).setCellValue(objToString(goodsInOut.getGoodsTitle()));
				//产品数量
				row.createCell(11).setCellValue(objToString(goodsInOut.getQuantity()));
				//产品价格
				row.createCell(12).setCellValue(objToString(goodsInOut.getPrice()));
				//产品总价
				row.createCell(13).setCellValue((goodsInOut.getTotalPrice()*100)/100);
	        	//现金卷
	            row.createCell(14).setCellValue(objToString(goodsInOut.getCashCoupon()));
	          	//品牌
				row.createCell(15).setCellValue(objToString(goodsInOut.getBrandTitle()));
	    		//商品父分类
				row.createCell(16).setCellValue(objToString(goodsInOut.getGoodsParentTypeTitle()));
				//商品子分类
	        	row.createCell(17).setCellValue(objToString(goodsInOut.getGoodsTypeTitle()));
				//配送方式
	        	row.createCell(18).setCellValue(objToString(goodsInOut.getDeliverTypeTitle()));
				//中转仓
	            row.createCell(19).setCellValue(objToString(goodsInOut.getWhName()));
	    		//配送人员
	        	row.createCell(20).setCellValue(objToString(goodsInOut.getDeliverRealName()));
	        	//配送人员电话
	        	row.createCell(21).setCellValue(objToString(goodsInOut.getDeliverUsername()));
	        	//收货人姓名 收货人电话 收货人地址
	        	if(!"门店自提".equals(goodsInOut.getDeliverTypeTitle())){
	        		row.createCell(22).setCellValue(objToString(goodsInOut.getShippingName()));
	        		row.createCell(23).setCellValue(objToString(goodsInOut.getShippingPhone()));
	        		row.createCell(24).setCellValue(objToString(goodsInOut.getShippingAddress()));
	        	}
	        	//订单备注
	        	row.createCell(25).setCellValue(objToString(goodsInOut.getRemark()));
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
        
	}
	
	/**
	 * 代收款报表(1.1修改)
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
 	 * @param username 当前用户
	 * @return
	 */
	private HSSFWorkbook agencyFundWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        //List<TdAgencyFund> agencyFundList = tdAgencyFundService.searchAgencyFund(begin, end, cityName, diyCode,username,roleDiyIds);
        
        // 修改代收款报表逻辑和呈现方式  ----- 2016-11-14 10:43 ----- 闫乐
        
        List<TdAgencyFund> agencyFundList = tdAgencyFundService.queryDownList(begin, end, cityName, diyCode,username,roleDiyIds);
  
        
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(agencyFundList!=null){
        	maxSize=agencyFundList.size();
        }
        int sheets = maxSize/maxRowNum+1;
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,13,13,18,13,13,9,9,9,
	        		9,9,13,13,13,13,13,25,18,18,
	        		25};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);


	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        //版本1.1
	        String[] cellValues={"门店名称","主单号","订单状态","导购","订单日期","客户名称","客户电话","需代收金额","实收现金","实收pos",
	        		"实收总金额","欠款","仓库名称","配送人员","配送人电话","收货人","收货人电话","收货人地址","预约配送时间","实际配送时间",
	        		"备注信息","是否审核","是否通过"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdAgencyFund agencyFund= agencyFundList.get(j+i*maxRowNum);
				
				row = sheet.createRow((int) j + 1);
				//门店名称
				row.createCell(0).setCellValue(objToString(agencyFund.getDiySiteName()));
				//主单号
				row.createCell(1).setCellValue(objToString(agencyFund.getMainOrderNumber()));
				//订单状态
				String statusStr = orderStatus(agencyFund.getStatusId());
				row.createCell(2).setCellValue(objToString(statusStr));
				//导购
				row.createCell(3).setCellValue(objToString(agencyFund.getSellerRealName()));
				//订单日期
				row.createCell(4).setCellValue(objToString(dateToString(agencyFund.getOrderTime(), null)));
				//客户名称
				row.createCell(5).setCellValue(objToString(agencyFund.getRealUserRealName()));
				//客户电话
				row.createCell(6).setCellValue(objToString(agencyFund.getRealUserUsername()));
				//需代收金额
				if (null != agencyFund.getPayPrice() && !"".equals(agencyFund.getPayPrice())) {
					row.createCell(7).setCellValue(objToString(agencyFund.getPayPrice()));
				}else{
					agencyFund.setPayPrice(0.0);
					row.createCell(7).setCellValue(0.0);
				}
				//实收现金
				if (null != agencyFund.getPayMoney() && !"".equals(agencyFund.getPayMoney())) {
					row.createCell(8).setCellValue(objToString(agencyFund.getPayMoney()));
				}else{
					agencyFund.setPayMoney(0.0);
					row.createCell(8).setCellValue(0.0);
				}
				
				//实收pos
				if (null != agencyFund.getPayPos() && !"".equals(agencyFund.getPayPos())) {
					row.createCell(9).setCellValue(objToString(agencyFund.getPayPos()));
				}else{
					agencyFund.setPayPos(0.0);
					row.createCell(9).setCellValue(objToString(0.0));
				}
				
				//实收总金额
				row.createCell(10).setCellValue(objToString(agencyFund.getPayMoney()+agencyFund.getPayPos()));
				//欠款
				row.createCell(11).setCellValue(objToString(agencyFund.getPayPrice()-agencyFund.getPayMoney()-agencyFund.getPayPos()));
				//仓库名称
				row.createCell(12).setCellValue(objToString(agencyFund.getWhName()));
				//配送人员
				row.createCell(13).setCellValue(objToString(agencyFund.getDeliveryName()));
				//配送人电话
				row.createCell(14).setCellValue(objToString(agencyFund.getDeliveryPhone()));
				//收货人
				row.createCell(15).setCellValue(objToString(agencyFund.getShippingName()));
				//收货人电话
				row.createCell(16).setCellValue(objToString(agencyFund.getShippingPhone()));
				//收货人地址
				row.createCell(17).setCellValue(objToString(agencyFund.getShippingAddress()));
				//预约配送时间
				String dayTime = agencyFund.getDeliveryDate();
				dayTime = dayTime + " " + agencyFund.getDeliveryDetailId() + ":30";
				row.createCell(18).setCellValue(objToString(dayTime));
				//实际配送时间
				row.createCell(19).setCellValue(objToString(dateToString(agencyFund.getDeliveryTime(), null)));
				//备注信息
				row.createCell(20).setCellValue(objToString(agencyFund.getRemark()));
				if (null !=agencyFund.getIsEnable() && (agencyFund.getIsEnable()==true)) {
					row.createCell(21).setCellValue("已审核");
					if(null != agencyFund.getIsPassed() && agencyFund.getIsPassed()==true){
						row.createCell(22).setCellValue("已通过");
					}else{
						row.createCell(22).setCellValue("未通过");
					}
				}else{
					row.createCell(21).setCellValue("未审核");
					row.createCell(22).setCellValue("未通过");
				}
				
	        }
		}
        return wb;
	}
	
	/**
	 * 收款报表
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
 	 * @param username 当前用户

	 */
	private HSSFWorkbook payWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        //List<TdReceipt> receiptList = tdReceiptService.searchReceipt(begin, end, cityName, diyCode, username, roleDiyIds);
        List<TdReceipt> receiptList = tdReceiptService.queryDownList(begin, end, cityName, diyCode, username, roleDiyIds);
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(receiptList!=null){
        	maxSize=receiptList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={8,13,25,25,13,13,13,13,13,13,
	        		13,13,13,13};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);


	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"门店名称","付款类型","主单号","会员名称","会员电话","导购","下单时间","配送方式","收款时间&退款时间","预存款","第三方支付","门店现金",
					"门店POS","门店其他","汇总","门店真实收款时间"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdReceipt receipt = receiptList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	if (null != receipt.getDiySiteName())
	        	{//门店名称
	            	row.createCell(0).setCellValue(receipt.getDiySiteName());
	    		}
	        	if (null != receipt.getReceiptType())
	        	{//付款类型
	            	row.createCell(1).setCellValue(receipt.getReceiptType());
	    		}
	        	if (null != receipt.getMainOrderNumber())
	        	{//主单号
	            	row.createCell(2).setCellValue(receipt.getMainOrderNumber());
	    		}
	        	/*if (null != receipt.getOrderNumber())
	        	{//分单号
	            	row.createCell(3).setCellValue(receipt.getOrderNumber());
	    		}*/
	        	if (null != receipt.getRealUserRealName())
	        	{//会员名称
	            	row.createCell(3).setCellValue(receipt.getRealUserRealName());
	    		}
	        	if (null != receipt.getUsername()) {
	        		//会员电话
	        		row.createCell(4).setCellValue(receipt.getUsername());
				}
	        	if (null != receipt.getSellerRealName())
	        	{//导购
	            	row.createCell(5).setCellValue(receipt.getSellerRealName());
	    		}
	        	if (null != receipt.getOrderTime())
	        	{//订单日期
	        		Date orderTime = receipt.getOrderTime();
	        		String orderTimeStr = orderTime.toString();
	            	row.createCell(6).setCellValue(orderTimeStr);
	    		}
	        	if (null != receipt.getDeliverTypeTitle())
	        	{//配送方式
	            	row.createCell(7).setCellValue(receipt.getDeliverTypeTitle());
	    		}
	        	if (null != receipt.getReceiptTime())
	        	{//收款&退款时间
	            	row.createCell(8).setCellValue(receipt.getReceiptTime());
	    		}
	        	if (null != receipt.getActualPay())
	        	{//预存款
	            	row.createCell(9).setCellValue(receipt.getActualPay());
	    		}
	        	if (null != receipt.getOtherPay())
	        	{//第三方支付
	            	row.createCell(10).setCellValue(receipt.getOtherPay());
	    		}
	        	if (null != receipt.getDiyCash())
	        	{//门店现金
	            	row.createCell(11).setCellValue(receipt.getDiyCash());
	    		}
	        	if (null != receipt.getDiyPos())
	        	{//门店POS
	            	row.createCell(12).setCellValue(receipt.getDiyPos());
	    		}
	        	if(null != receipt.getDiyOther()){
	        		row.createCell(13).setCellValue(receipt.getDiyOther());
	        	}
	        	
	        	if(null!= receipt.getSummary()){//汇总 
	        			row.createCell(14).setCellValue(receipt.getSummary());
	        			
	        	}
	        	//门店收款时间
	        	if(null !=receipt.getRealPayTime()){
	        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
	        		row.createCell(15).setCellValue(sdf.format(receipt.getRealPayTime()).toString());
	        	}
	        	
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
	}
	/**
	 * 销售细报表
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
 	 * @param username 当前用户
	 * @return
	 */
	private HSSFWorkbook salesDetailWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdSalesDetail> salesDetailList= tdSalesDetailService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
//      long startTimne = System.currentTimeMillis();

        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(salesDetailList!=null){
        	maxSize=salesDetailList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,13,18,18,13,13,18,
	        		9,9,9,9,9,13,13,13,18,13,
	        		13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);


	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"门店名称","主单号","分单号","下单日期","订单状态","导购","客户名称","客户电话","产品编号","产品名称",
	        		"数量","单价","总价","现金卷","品牌","商品父分类","商品子分类","配送方式","中转仓","配送人员",
	        		"配送人员电话","收货人姓名","收货人电话","收货人地址","订单备注"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdSalesDetail salesDetail= salesDetailList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	//门店名称
	        	row.createCell(0).setCellValue(objToString(salesDetail.getDiySiteName()));

	        	//代付款订单没有主单号  分单号显示到主单号位置
	        	if(salesDetail.getStatusId() != null && salesDetail.getStatusId().equals(2L)){
	        		row.createCell(1).setCellValue(objToString(salesDetail.getOrderNumber()));
	        	}else{
	        		row.createCell(1).setCellValue(objToString(salesDetail.getMainOrderNumber()));
	        		row.createCell(2).setCellValue(objToString(salesDetail.getOrderNumber()));
	        	} 
	        	//下单时间
	        	row.createCell(3).setCellValue(objToString(salesDetail.getOrderTime()));
	        	//订单状态
	        	row.createCell(4).setCellValue(orderStatus(salesDetail.getStatusId()));
	        	//导购
				row.createCell(5).setCellValue(objToString(salesDetail.getSellerRealName()));
				//客户名称
				row.createCell(6).setCellValue(objToString(salesDetail.getRealName()));
				//客户电话
				row.createCell(7).setCellValue(objToString(salesDetail.getUsername()));
				//产品编号
				row.createCell(8).setCellValue(objToString(salesDetail.getSku()));
				//产品名称
				row.createCell(9).setCellValue(objToString(salesDetail.getGoodsTitle()));
				//产品数量
				row.createCell(10).setCellValue(objToString(salesDetail.getQuantity()));
				//产品价格
				row.createCell(11).setCellValue(objToString(salesDetail.getPrice()));
				//产品总价
				row.createCell(12).setCellValue((salesDetail.getTotalPrice()*100)/100);
	        	//现金卷
	            row.createCell(13).setCellValue(objToString(salesDetail.getCashCoupon()));
	          	//品牌
				row.createCell(14).setCellValue(objToString(salesDetail.getBrandTitle()));
	    		//商品父分类
				row.createCell(15).setCellValue(objToString(salesDetail.getGoodsParentTypeTitle()));
				//商品子分类
	        	row.createCell(16).setCellValue(objToString(salesDetail.getGoodsTypeTitle()));
				//配送方式
	        	row.createCell(17).setCellValue(objToString(salesDetail.getDeliverTypeTitle()));
				//中转仓
	            row.createCell(18).setCellValue(objToString(salesDetail.getWhName()));
	    		//配送人员
	        	row.createCell(19).setCellValue(objToString(salesDetail.getDeliverRealName()));
	        	//配送人员电话
	        	row.createCell(20).setCellValue(objToString(salesDetail.getDeliverUsername()));
	        	//收货人姓名
	        	if(!"门店自提".equals(salesDetail.getDeliverTypeTitle())){
	        		row.createCell(21).setCellValue(objToString(salesDetail.getShippingName()));
	        	}
	        	//收货人电话
	        	if(!"门店自提".equals(salesDetail.getDeliverTypeTitle())){
	        		row.createCell(22).setCellValue(objToString(salesDetail.getShippingPhone()));
	        	}
	        	//收货人地址
	        	if(!"门店自提".equals(salesDetail.getDeliverTypeTitle())){
	        		row.createCell(23).setCellValue(objToString(salesDetail.getShippingAddress()));
	        	}
	        	//订单备注
	        	row.createCell(24).setCellValue(objToString(salesDetail.getRemark()));
				
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
        
	}
	/**
	 * 退货报表
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
 	 * @param username 当前用户
	 * @return
	 */
	private HSSFWorkbook returnWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
		List<TdReturnReport> returnReportList = tdReturnReportService.searchReturnReport(begin, end, cityName, diyCode, username,roleDiyIds);
		List<TdWareHouse> wareHouseList = tdWareHouseService.findAll();
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(returnReportList!=null){
        	maxSize=returnReportList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,20,13,13,13,13,13,13,13};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);


	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"退货门店","原订单号","退货单号","退货单状态","品牌","商品类别","导购","订单日期","退货日期","客户名称",
					"客户电话","产品编号","产品名称","退货数量","退货单价","退货金额","客户备注","中转仓","配送人员","配送人电话",
			"退货地址"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdReturnReport returnReport= returnReportList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	if (returnReport.getDiySiteName() != null)
				{//退货门店
					row.createCell(0).setCellValue(returnReport.getDiySiteName());
				}
				if (returnReport.getOrderNumber() != null)
				{//原订单号
					row.createCell(1).setCellValue(returnReport.getOrderNumber());
					row.createCell(9).setCellValue(returnReport.getRealName());//客户名称 
					row.createCell(10).setCellValue(returnReport.getUsername());// 客户电话
					row.createCell(6).setCellValue(returnReport.getSellerRealName());//导购
					//					        	row.createCell(16).setCellValue(returnReport.getCashCoupon());//退现金卷金额
					//					            row.createCell(17).setCellValue(returnReport.getProductCoupon());//退产品卷金额
					row.createCell(20).setCellValue(returnReport.getShippingAddress());//退货地址
					row.createCell(18).setCellValue(returnReport.getDeliverRealName());//配送人员
					row.createCell(19).setCellValue(returnReport.getDeliverUsername());//配送人员电话

				}
				if (returnReport.getReturnNumber() != null)
				{//退货单号
					row.createCell(2).setCellValue(returnReport.getReturnNumber());
				}
				if (returnReport.getStatusId() != null)
				{//退货单状态
					if(returnReport.getStatusId().equals(1L)){
						row.createCell(3).setCellValue("确认退货单");
					}
					if(returnReport.getStatusId().equals(2L)){
						row.createCell(3).setCellValue("通知物流");
					}
					if(returnReport.getStatusId().equals(3L)){
						row.createCell(3).setCellValue("验货确认");
					}
					if(returnReport.getStatusId().equals(4L)){
						row.createCell(3).setCellValue("确认退款");
					}
					if(returnReport.getStatusId().equals(5L)){
						row.createCell(3).setCellValue("已完成");
					}
				}
				if (returnReport.getBrandTitle() != null)
				{//品牌
					row.createCell(4).setCellValue(returnReport.getBrandTitle());
				}
				if (returnReport.getCategoryTitle() != null)
				{//商品类别
					row.createCell(5).setCellValue(returnReport.getCategoryTitle());
				}
				if (returnReport.getOrderTime() != null)
				{//订单日期
					row.createCell(7).setCellValue(returnReport.getOrderTime().toString());
				}
				if (returnReport.getCancelTime() != null)
				{//退货日期
					row.createCell(8).setCellValue(returnReport.getCancelTime().toString());
				}
				if (returnReport.getSku() != null)
				{//产品编号
					row.createCell(11).setCellValue(returnReport.getSku());
				}
				if (returnReport.getGoodsTitle() != null)
				{//产品名称
					row.createCell(12).setCellValue(returnReport.getGoodsTitle());
				}
				if (returnReport.getQuantity() != null)
				{//退货数量
					row.createCell(13).setCellValue(returnReport.getQuantity());
				}
				if (returnReport.getPrice() != null)
				{//退货单价
					row.createCell(14).setCellValue(returnReport.getPrice());
				}

				if (returnReport.getQuantity() != null && returnReport.getPrice() != null)
				{//退货总价
					row.createCell(15).setCellValue(returnReport.getQuantity()*returnReport.getPrice());
				}

				if(returnReport.getRemarkInfo() != null){//客户备注
					row.createCell(16).setCellValue(returnReport.getRemarkInfo());
				}
				if(returnReport.getWhNo() != null){//中转仓
					row.createCell(17).setCellValue(changeName(wareHouseList,returnReport.getWhNo()));
				}
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
			
		}
        
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
	}
	
	/**
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
	 * @param username 当前用户
	 * @param roleDiyIds
	 * @return
	 */
	private HSSFWorkbook goodsInOutTakeWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdGoodsInOut> goodsInOutList=tdGoodsInOutService.queryDownListTake(begin, end, cityName, diyCode, roleDiyIds);
        
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(goodsInOutList!=null){
        	maxSize=goodsInOutList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,18,13,18,18,13,13,
	        		18,9,9,9,9,9,13,13,13,18,
	        		13,13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);


	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"门店名称","主单号","分单号","下单日期","确认日期","订单状态","导购","客户名称","客户电话","产品编号",
	        		"产品名称","数量","单价","总价","现金卷","品牌","商品父分类","商品子分类","订单备注"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdGoodsInOut goodsInOut= goodsInOutList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	//门店名称
	        	row.createCell(0).setCellValue(objToString(goodsInOut.getDiySiteName()));

	        	//代付款订单没有主单号  分单号显示到主单号位置
	        	if(goodsInOut.getStatusId() != null && goodsInOut.getStatusId().equals(2L)){
	        		row.createCell(1).setCellValue(objToString(goodsInOut.getOrderNumber()));
	        	}else{
	        		row.createCell(1).setCellValue(objToString(goodsInOut.getMainOrderNumber()));
	        		row.createCell(2).setCellValue(objToString(goodsInOut.getOrderNumber()));
	        	} 
	        	//下单时间
	        	row.createCell(3).setCellValue(objToString(goodsInOut.getOrderTime()));
	        	//确认时间
	        	row.createCell(4).setCellValue(objToString(goodsInOut.getSalesTime()));
	        	//订单状态
	        	row.createCell(5).setCellValue(orderStatus(goodsInOut.getStatusId()));
	        	//导购
				row.createCell(6).setCellValue(objToString(goodsInOut.getSellerRealName()));
				//客户名称
				row.createCell(7).setCellValue(objToString(goodsInOut.getRealName()));
				//客户电话
				row.createCell(8).setCellValue(objToString(goodsInOut.getUsername()));
				//产品编号
				row.createCell(9).setCellValue(objToString(goodsInOut.getSku()));
				//产品名称
				row.createCell(10).setCellValue(objToString(goodsInOut.getGoodsTitle()));
				//产品数量
				row.createCell(11).setCellValue(objToString(goodsInOut.getQuantity()));
				//产品价格
				row.createCell(12).setCellValue(objToString(goodsInOut.getPrice()));
				//产品总价
				row.createCell(13).setCellValue((goodsInOut.getTotalPrice()*100)/100);
	        	//现金卷
	            row.createCell(14).setCellValue(objToString(goodsInOut.getCashCoupon()));
	          	//品牌
				row.createCell(15).setCellValue(objToString(goodsInOut.getBrandTitle()));
	    		//商品父分类
				row.createCell(16).setCellValue(objToString(goodsInOut.getGoodsParentTypeTitle()));
				//商品子分类
	        	row.createCell(17).setCellValue(objToString(goodsInOut.getGoodsTypeTitle()));
				//配送方式
	        	/*row.createCell(18).setCellValue(objToString(goodsInOut.getDeliverTypeTitle()));
				//中转仓
	            row.createCell(19).setCellValue(objToString(goodsInOut.getWhName()));
	    		//配送人员
	        	row.createCell(20).setCellValue(objToString(goodsInOut.getDeliverRealName()));
	        	//配送人员电话
	        	row.createCell(21).setCellValue(objToString(goodsInOut.getDeliverUsername()));
	        	//收货人姓名 收货人电话 收货人地址
	        	if(!"门店自提".equals(goodsInOut.getDeliverTypeTitle())){
	        		row.createCell(22).setCellValue(objToString(goodsInOut.getShippingName()));
	        		row.createCell(23).setCellValue(objToString(goodsInOut.getShippingPhone()));
	        		row.createCell(24).setCellValue(objToString(goodsInOut.getShippingAddress()));
	        	}*/
	        	//订单备注
	        	row.createCell(18).setCellValue(objToString(goodsInOut.getRemark()));
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
        
	}
	
	
	/**
	 * 销量报表
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
	 * @param username 当前用户
	 * @param roleDiyIds
	 * @return
	 */
	private HSSFWorkbook SalesWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdSales> tdSalesList = tdSalesService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
        System.out.println("总共有"+tdSalesList.size()+"条记录");
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;	
        if(tdSalesList!=null){
        	maxSize=tdSalesList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,18,13,18,18,13,13,
	        		18,9,9,9,9,9,13,13,13,18,
	        		13,13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);
	        DecimalFormat df = new DecimalFormat("0.00");

	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"城市","品牌","门店","项目","主单号","分单号","订单日期","客户编号","客户姓名","客户类型","销顾电话",
	        		"销顾姓名","商品编码","商品名称","赠品标识","商品单价","商品数量","商品总价","产品现金券单价","产品现金券数量","产品现金券总额","产品券单价","产品券数量","产品券总额","汇总"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdSales sales= tdSalesList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	
	        	//城市名称
	        	row.createCell(0).setCellValue(objToString(sales.getCityName()));
	        	
	        	
	        	//品牌
	        	
	        	if(sales.getOrderNumber().contains("HR")){
	        		row.createCell(1).setCellValue("华润");
	        	}else if(sales.getOrderNumber().contains("YR")){
	        		row.createCell(1).setCellValue("莹润");
	        	}else if(sales.getOrderNumber().contains("LYZ")){
	        		row.createCell(1).setCellValue("乐易装");
	        	}
	        	
	        	//门店名称
	        	row.createCell(2).setCellValue(objToString(sales.getDiySiteName()));

	        	//销量项目
	        	row.createCell(3).setCellValue(objToString(sales.getStype()));

	        	//主单号
	        	row.createCell(4).setCellValue(objToString(sales.getMainOrderNumber()));
	        	
	        	//分单号
	        	row.createCell(5).setCellValue(objToString(sales.getOrderNumber()));
	        	
	        	//订单日期
	        	row.createCell(6).setCellValue(objToString(sales.getOrderTime()));
	        	
	        	//会员编号
				row.createCell(7).setCellValue(objToString(sales.getUsername()));
				
				//会员名称
				row.createCell(8).setCellValue(objToString(sales.getRealName()));
				
				if(null!=sales.getIdentityType()){
					if(sales.getIdentityType()==true){
						row.createCell(9).setCellValue("零售");
					}else{
						row.createCell(9).setCellValue("会员");
					}
				}else{
					row.createCell(9).setCellValue("未知");
				}
				
				//销顾电话
				row.createCell(10).setCellValue(objToString(sales.getSellerUsername()));
				
				//销顾姓名
				row.createCell(11).setCellValue(objToString(sales.getSellerRealName()));
				
				//产品编码
				row.createCell(12).setCellValue(objToString(sales.getSku()));
				
				//产品名称
				row.createCell(13).setCellValue(objToString(sales.getGoodsTitle()));
				
				//赠品标识
				if(null != sales.getIsGift()){
					if(sales.getIsGift().equalsIgnoreCase("N")){
						row.createCell(14).setCellValue("否");
					}else {
						row.createCell(14).setCellValue("是");
					}
				}else{
					row.createCell(13).setCellValue("否");
				}
				
				//商品单价
				if(null==sales.getGoodsPrice()){
					sales.setGoodsPrice(0.0);
					row.createCell(15).setCellValue(objToString(0));
				}else{
					row.createCell(15).setCellValue(objToString(sales.getGoodsPrice()));
				}
	        	//商品数量
				if(null == sales.getGoodsQuantity()){
					sales.setGoodsQuantity(0L);
					row.createCell(16).setCellValue(objToString(0));
				}else{
					 row.createCell(16).setCellValue(objToString(sales.getGoodsQuantity()));
				}
	          	//商品总价
	            
				row.createCell(17).setCellValue(objToString(df.format(sales.getGoodsPrice()*sales.getGoodsQuantity())));
				
				//产品现金券单价
				if(null==sales.getCashCouponPrice()){
					sales.setCashCouponPrice(0.0);
					row.createCell(18).setCellValue(objToString(sales.getCashCouponPrice()));
				}else{
					row.createCell(18).setCellValue(objToString(sales.getCashCouponPrice()));
				}
	    		
				//产品现金券数量
				if(null == sales.getCashCouponQuantity()){
					sales.setCashCouponQuantity(0L);
					row.createCell(19).setCellValue(objToString(sales.getCashCouponQuantity()));
				}else{
					row.createCell(19).setCellValue(objToString(sales.getCashCouponQuantity()));
				}
				
				//产品现金券总额
	        	row.createCell(20).setCellValue(objToString(df.format(sales.getCashCouponPrice()*sales.getCashCouponQuantity())));
	        	
	        	//产品券单价
	        	if(null == sales.getProductCouponPrice()){
	        		sales.setCashCouponPrice(0.0);
	        		 row.createCell(21).setCellValue(objToString(sales.getProductCouponPrice()));
	        	}else{
	        		 row.createCell(21).setCellValue(objToString(sales.getProductCouponPrice()));
	        	}
				
	           
	    		
	            //产品券数量
	        	if(null == sales.getProductCouponQuantity()){
	        		sales.setCashCouponQuantity(0L);
	        		
	        	}else{
	        		row.createCell(22).setCellValue(objToString(sales.getProductCouponQuantity()));
	        	}
	        	
	        	//产品券总额
	        	row.createCell(23).setCellValue(objToString(df.format(sales.getProductCouponPrice()*sales.getProductCouponQuantity())));
	        	//汇总
	        	row.createCell(24).setCellValue(objToString(df.format(sales.getGoodsPrice()*(sales.getGoodsQuantity()-sales.getProductCouponQuantity())-sales.getCashCouponPrice()*sales.getCashCouponQuantity())));
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        System.out.println(new Date());
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
        
	}
	
	/**
	 * 欠款报表
	 * 
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编号
	 * @param cityName 城市编号
	 * @param username 当前用户
	 * @param roleDiyIds
	 * @return
	 */
	private HSSFWorkbook OwnWorkBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdOwn> tdOwnList = tdOwnService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
        for (int i = 0; i < tdOwnList.size(); i++) {
			if(!(tdOwnList.get(i).getOwned()>0.0)){
				tdOwnList.remove(i);
			}
		}
       // System.out.println(tdOwnList);
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(tdOwnList!=null){
        	maxSize=tdOwnList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,18,13,18,18,13,13,
	        		18,9,9,9,9,9,13,13,13,18,
	        		13,13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);

	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"门店","主单号","订单日期","销顾姓名","销顾电话",
	        		"客户电话","客户姓名","华润欠款","其它欠款","出货仓库","配送员姓名","配送员电话","收货地址","订单备注"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdOwn own= tdOwnList.get(j+i*maxRowNum);
					row = sheet.createRow((int) j + 1);
		        	
		        	//门店名称
		        	row.createCell(0).setCellValue(objToString(own.getDiySiteName()));
		        	
		        	//主单号
		        	row.createCell(1).setCellValue(objToString(own.getMainOrderNumber()));

		        	//订单日期
		        	row.createCell(2).setCellValue(objToString(own.getOrderTime()));

		        	//销顾姓名
		        	row.createCell(3).setCellValue(objToString(own.getSellerRealName()));
		        	
		        	//销顾电话
		        	row.createCell(4).setCellValue(objToString(own.getSellerUsername()));
		        	
		        	//客户电话
		        	row.createCell(5).setCellValue(objToString(own.getUsername()));
		        	
		        	//客户姓名
					row.createCell(6).setCellValue(objToString(own.getRealUserRealName()));
					
					
					
					List<TdSubOwn> tdSubOwnList = tdSubOwnService.queryDownListDetail(own.getMainOrderNumber());
					
					Double HRTotalPrice = 0.0;//华润订单总金额
					Double OtherTotalPrice = 0.0;//其它品牌订单总金额
					Double HRotherPay = 0.0;//华润第三方支付
					Double elseOtherPay = 0.0;//其它品牌第三方支付
					Double HROwn = 0.0;//华润欠款
					Double otherOwn = 0.0;//其它品牌欠款
					Double deliveryReceipt = 0.0;//总单配送代收
					Double diyReceipt = 0.0; //门店收款总额
					for (TdSubOwn tdSubOwn : tdSubOwnList) {
						if(tdSubOwn.getOrderNumber().contains("HR")){
							HRTotalPrice+=tdSubOwn.getTotalPrice();
							HRotherPay+=tdSubOwn.getOtherPay();
						}else if(tdSubOwn.getOrderNumber().contains("LYZ") || tdSubOwn.getOrderNumber().contains("YR")){
							OtherTotalPrice+=tdSubOwn.getTotalPrice();
							elseOtherPay+=tdSubOwn.getOtherPay();
						}
						deliveryReceipt = tdSubOwn.getMoney()+tdSubOwn.getPos();
						diyReceipt = tdSubOwn.getBackMoney()+tdSubOwn.getBackPos()+tdSubOwn.getBackOther();
					}
					
					if((deliveryReceipt+diyReceipt)<=(OtherTotalPrice-elseOtherPay)){
						HROwn = HRTotalPrice-HRotherPay;
						otherOwn = OtherTotalPrice-elseOtherPay-deliveryReceipt-diyReceipt; 
					}else{
						HROwn = HRTotalPrice+OtherTotalPrice-HRotherPay-elseOtherPay-deliveryReceipt-diyReceipt;
						otherOwn = 0.0;
					}
					
					//华润欠款
					
					row.createCell(7).setCellValue(objToString(HROwn));
					
					
					//其它品牌欠款
					row.createCell(8).setCellValue(objToString(otherOwn));
					
					//出货仓库
					row.createCell(9).setCellValue(objToString(own.getWhName()));
					
					//配送员姓名
					row.createCell(10).setCellValue(objToString(own.getDuRealName()));
					
					//配送员电话
					row.createCell(11).setCellValue(objToString(own.getDuUsername()));
					
					//收货地址
					row.createCell(12).setCellValue(objToString(own.getShippingAddress()));
					
					//订单备注
					
					row.createCell(13).setCellValue(objToString(own.getRemark()));
				
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        System.out.println(new Date());
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
        
	}
	
	
	/**
	 * 未提货报表
	 * 
	 * @param begin
	 * @param end
	 * @param diyCode
	 * @param cityName
	 * @param username
	 * @param roleDiyIds
	 * @return
	 */
	private HSSFWorkbook ReserveOrderBook( String diyCode, String cityName, String username,
			List<String> roleDiyIds) {
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdReserveOrder> tdReserveOrderList = tdReserveOrderService.queryDownList(cityName, diyCode, roleDiyIds);
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(tdReserveOrderList!=null){
        	maxSize=tdReserveOrderList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,18,13,18,18,13,13,
	        		18,9,9,9,9,9,13,13,13,18,
	        		13,13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);

	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"城市","未提货类型","门店","券订单号","确认日期","会员姓名","会员电话","销顾姓名",
	        		"商品编码","数量","单价","总额"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdReserveOrder	 tdReserveOrder= tdReserveOrderList.get(j+i*maxRowNum);
					row = sheet.createRow((int) j + 1);
		        	
		        	//城市
		        	row.createCell(0).setCellValue(objToString(tdReserveOrder.getCity()));
		        	
		        	//未提货类型
		        	row.createCell(1).setCellValue(objToString(tdReserveOrder.getReserveType()));
		        	
		        	//门店
		        	
		        	row.createCell(2).setCellValue(objToString(tdReserveOrder.getDiySiteName()));
		        	
		        	//券订单号
		        	row.createCell(3).setCellValue(objToString(tdReserveOrder.getCouponOrderNumber()));

		        	//确认日期
		        	row.createCell(4).setCellValue(objToString(tdReserveOrder.getGetTime()));
		        	
		        	//会员姓名
		        	row.createCell(5).setCellValue(objToString(tdReserveOrder.getRealUserRealName()));
		        	
		        	//会员电话
		        	row.createCell(6).setCellValue(objToString(tdReserveOrder.getUsername()));
		        	
		        	//销顾姓名
					row.createCell(7).setCellValue(objToString(tdReserveOrder.getSellerRealName()));
					
					//商品编码
					row.createCell(8).setCellValue(objToString(tdReserveOrder.getSku()));
					
					//数量
					row.createCell(9).setCellValue(objToString(tdReserveOrder.getQuantity()));
					
					//单价
					row.createCell(10).setCellValue(objToString(tdReserveOrder.getBuyPrice()));
					
					//总额
					row.createCell(11).setCellValue(objToString(tdReserveOrder.getQuantity()*tdReserveOrder.getBuyPrice()));
					
					
				
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        System.out.println(new Date());
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
	}
	
	
	/**
	 * 活跃会员报表
	 * @param diyCode
	 * @param cityName
	 * @param username
	 * @param roleDiyIds
	 * @return
	 */
	private HSSFWorkbook salesForActiveUserBook(Date begin,Date end,String diyCode,String cityName,String username,List<String> roleDiyIds){
		// 第一步，创建一个webbook，对应一个Excel文件 
        HSSFWorkbook wb = new HSSFWorkbook();  
        
 
        // 第五步，设置值  
        List<TdActiveUser> saleForActiveUserList = tdActiveUserService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
        //System.out.println(tdSalesList);
//        long startTimne = System.currentTimeMillis();
        
        //excel单表最大行数是65535
        int maxRowNum = 60000;
        int maxSize=0;
        if(saleForActiveUserList!=null){
        	maxSize=saleForActiveUserList.size();
        }
        int sheets = maxSize/maxRowNum+1;
        
		//写入excel文件数据信息
		for(int i=0;i<sheets;i++){
			
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        //列宽
	        int[] widths={13,25,25,18,18,13,18,18,13,13,
	        		18,9,9,9,9,9,13,13,13,18,
	        		13,13,13,13,20,20};
	        sheetColumnWidth(sheet,widths);
	        
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	        style.setWrapText(true);

	       	//设置标题
	        HSSFRow row = sheet.createRow((int) 0); 
	        
	        String[] cellValues={"城市","门店","销顾电话",
	        		"销顾姓名","活跃会员数"};
			cellDates(cellValues, style, row);
			
			for(int j=0;j<maxRowNum;j++)
	        {
				if(j+i*maxRowNum>=maxSize){
					break;
				}
				TdActiveUser sales= saleForActiveUserList.get(j+i*maxRowNum);
	        	row = sheet.createRow((int) j + 1);
	        	
				// 城市名称
				row.createCell(0).setCellValue(objToString(sales.getCityName()));

				// 门店名称
				row.createCell(1).setCellValue(objToString(sales.getDiySiteName()));

				// 销顾电话
				row.createCell(2).setCellValue(objToString(sales.getSellerUsername()));

				// 销顾姓名
				row.createCell(3).setCellValue(objToString(sales.getSellerRealName()));

				// 活跃会员数
				row.createCell(4).setCellValue(objToString((sales.getSalesSummary().intValue())));
				
//	            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
			}
//			System.out.println("正在生成excel文件的 sheet"+(i+1));
		}
        System.out.println(new Date());
        
//        long endTime = System.currentTimeMillis();
//        System.out.println("用时="+((endTime-startTimne)/1000)+"秒");
        return wb;
        
	}
	
	
	/**
	 * 配送考核报表
	 * @param begin
	 * @param end
	 * @param diyCode
	 * @param cityName
	 * @param username
	 * @param roleDiyIds
	 * @return
	 */
	private HSSFWorkbook deliveryCheckBook(Date begin, Date end, String diyCode, String cityName, String username,
			List<String> roleDiyIds) {
		
		//创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		
		List<DeliveryCheckReport> deliveryCheckList = tdDeliveryCheckReportService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
		
		 int maxRowNum = 60000;
	        int maxSize=0;
	        if(deliveryCheckList!=null){
	        	maxSize=deliveryCheckList.size();
	        }
	        int sheets = maxSize/maxRowNum+1;
		
	        
			//写入excel文件数据信息
			for(int i=0;i<sheets;i++){
				
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
		        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		        //列宽
		        int[] widths={13,13,18,25,15,13,25,25,25,25,
		        		25,25 };
		        sheetColumnWidth(sheet,widths);
		        
		        // 第四步，创建单元格，并设置值表头 设置表头居中  
		        HSSFCellStyle style = wb.createCellStyle();  
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		        style.setWrapText(true);

		       	//设置标题
		        HSSFRow row = sheet.createRow((int) 0); 
		        
		        String[] cellValues={"配送仓","配送员","配送员电话","主单号","订单归属门店","订单导购","收货地址","下单时间","出货时间",
		        		"代收款(系统计算)","备注信息","操作时间" };
				cellDates(cellValues, style, row);
				
				for(int j=0;j<maxRowNum;j++)
		        {
					if(j+i*maxRowNum>=maxSize){
						break;
					}
					DeliveryCheckReport deliveryCheckReport= deliveryCheckList.get(j+i*maxRowNum);
		        	row = sheet.createRow((int) j + 1);
		        	
		        	//仓库名称
		        	if (null != deliveryCheckReport.getWhName()) {
		        		row.createCell(0).setCellValue(objToString(deliveryCheckReport.getWhName()));
					}else{
						row.createCell(0).setCellValue(objToString("NULL"));
					}
					
		        	//配送员
		        	
		        	if (null != deliveryCheckReport.getDistributionName()) {
		        		row.createCell(1).setCellValue(objToString(deliveryCheckReport.getDistributionName()));
					}else{
						row.createCell(1).setCellValue(objToString("NULL"));
					}
		        	
		        	//配送员电话
		        	
		        	if (null != deliveryCheckReport.getDistributionPhone()) {
		        		row.createCell(2).setCellValue(objToString(deliveryCheckReport.getDistributionPhone()));
					}else{
						row.createCell(2).setCellValue(objToString("NULL"));
					}
		        	
		        	//主单号
		        	
		        	if (null != deliveryCheckReport.getMainOrderNumber()) {
		        		row.createCell(3).setCellValue(objToString(deliveryCheckReport.getMainOrderNumber()));
					}else{
						row.createCell(3).setCellValue(objToString("NULL"));
					}
		        	
		        	//门店名称
		        	
		        	if (null != deliveryCheckReport.getDiySiteName()) {
		        		row.createCell(4).setCellValue(objToString(deliveryCheckReport.getDiySiteName()));
					}else{
						row.createCell(4).setCellValue(objToString("NULL"));
					}
		        	
		        	//销顾
		        	
		        	if (null != deliveryCheckReport.getSellerRealName()) {
		        		row.createCell(5).setCellValue(objToString(deliveryCheckReport.getSellerRealName()));
					}else{
						row.createCell(5).setCellValue(objToString("NULL"));
					}
		        	
		        	//送货地址
		        	
		        	if (null != deliveryCheckReport.getShippingAddress()) {
		        		row.createCell(6).setCellValue(objToString(deliveryCheckReport.getShippingAddress()));
					}else{
						row.createCell(6).setCellValue(objToString("NULL"));
					}
		        	
		        	//下单时间
		        	
		        	if (null != deliveryCheckReport.getOrderTime()) {
		        		row.createCell(7).setCellValue(objToString(deliveryCheckReport.getOrderTime()));
					}else{
						row.createCell(7).setCellValue(objToString("NULL"));
					}
		        	
		        	//出货时间
		        	
		        	if (null != deliveryCheckReport.getSendTime()) {
		        		row.createCell(8).setCellValue(objToString(deliveryCheckReport.getSendTime()));
					}else{
						row.createCell(8).setCellValue(objToString("NULL"));
					}
		        	
		        	//代收款，系统计算
		        	if (null != deliveryCheckReport.getAgencyFund()) {
		        		row.createCell(9).setCellValue(objToString(deliveryCheckReport.getAgencyFund()));
					}else{
						row.createCell(9).setCellValue(objToString("NULL"));
					}
		        	
		        	//备注信息
		        	
		        	if (null != deliveryCheckReport.getRemark()) {
		        		row.createCell(10).setCellValue(objToString(deliveryCheckReport.getRemark()));
					}else{
						row.createCell(10).setCellValue(objToString("NULL"));
					}
		        	
		        	//操作时间
		        	if (null != deliveryCheckReport.getOperationTime()) {
		        		row.createCell(11).setCellValue(objToString(deliveryCheckReport.getOperationTime()));
					}else{
						row.createCell(11).setCellValue(objToString(""));
					}
		        	
//		            System.out.println("正在生成excel文件的 sheet"+(i+1)+"第"+(j+1)+"行");
				}
			}
	        
		return wb;
	}
	
	
	/**
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编码
	 * @param cityName	城市名称
	 * @param username	管理员用户名
	 * @param roleDiyIds  授权管理门店的id
	 * @return
	 */
	private HSSFWorkbook garmentFranchisor(Date begin, Date end, String diyCode, String cityName, String username,
			List<String> roleDiyIds) {
		
		//创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		//查询要写入excel的行记录
		List<GarmentFranchisorReport> garmengFranchisorList = tdGarmentFranchisorReportService.queryDownList(begin, end, cityName, diyCode, roleDiyIds);
		
		 int maxRowNum = 60000;
	        int maxSize=0;
	        if(garmengFranchisorList!=null){
	        	maxSize=garmengFranchisorList.size();
	        }
	        int sheets = maxSize/maxRowNum+1;
		
	        
			//写入excel文件数据信息
			for(int i=0;i<sheets;i++){
				
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		        HSSFSheet sheet = wb.createSheet("第"+(i+1)+"页");  
		        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		        //列宽
		        int[] widths={18,18,18,18,20,18,20,18,30,30,
		        		25,25,18,18,18,18,18,18};
		        sheetColumnWidth(sheet,widths);
		        
		        // 第四步，创建单元格，并设置值表头 设置表头居中  
		        HSSFCellStyle style = wb.createCellStyle();  
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		        style.setWrapText(true);

		       	//设置标题
		        HSSFRow row = sheet.createRow((int) 0); 
		        
		        String[] cellValues={"城市","门店名称","仓库名称","配送员名称","配送员电话","会员姓名","会员电话","导购姓名","主单号",
		        		"分单号","订单时间","配送时间","订单状态","预存款","第三方支付支付宝","第三方支付银联","现金支付","现金券总额","总金额" };
				cellDates(cellValues, style, row);
				
				for(int j=0;j<maxRowNum;j++)
		        {
					if(j+i*maxRowNum>=maxSize){
						break;
					}
					GarmentFranchisorReport garmentFranchisorReport= garmengFranchisorList.get(j+i*maxRowNum);
		        	row = sheet.createRow((int) j + 1);
		        	
		        	//城市名称
		        	
		        	if(null != garmentFranchisorReport.getCityName()){
		        		row.createCell(0).setCellValue(objToString(garmentFranchisorReport.getCityName()));
		        	}else{
		        		row.createCell(0).setCellValue(" ");
		        	}
		        	
		        	//门店名称
		        	
		        	if(null != garmentFranchisorReport.getDiySiteName()){
		        		row.createCell(1).setCellValue(objToString(garmentFranchisorReport.getDiySiteName()));
		        	}else{
		        		row.createCell(1).setCellValue(" ");
		        	}
		        	
		        	//仓库名称
		        	if (null != garmentFranchisorReport.getWhName()) {
		        		row.createCell(2).setCellValue(objToString(garmentFranchisorReport.getWhName()));
					}else{
						row.createCell(2).setCellValue(objToString(" "));
					}
		        	
		        	//配送员姓名
		        	if (null != garmentFranchisorReport.getDeliveryName()) {
		        		row.createCell(3).setCellValue(objToString(garmentFranchisorReport.getDeliveryName()));
					}else{
						row.createCell(3).setCellValue(objToString(" "));
					}
					
		        	//配送员电话
		        	if (null != garmentFranchisorReport.getDeliveryPhone()) {
		        		row.createCell(4).setCellValue(objToString(garmentFranchisorReport.getDeliveryPhone()));
					}else{
						row.createCell(4).setCellValue(objToString(" "));
					}
		        	
		        	//会员姓名
		        	if (null != garmentFranchisorReport.getRealUserRealName()) {
		        		row.createCell(5).setCellValue(objToString(garmentFranchisorReport.getRealUserRealName()));
					}else{
						row.createCell(5).setCellValue(objToString(" "));
					}
		        	
		        	//会员电话
		        	if (null != garmentFranchisorReport.getRealUserUsername()) {
		        		row.createCell(6).setCellValue(objToString(garmentFranchisorReport.getRealUserUsername()));
					}else{
						row.createCell(6).setCellValue(objToString(" "));
					}
		        	
		        	//导购姓名
		        	if (null != garmentFranchisorReport.getSellerRealName()) {
		        		row.createCell(7).setCellValue(objToString(garmentFranchisorReport.getSellerRealName()));
					}else{
						row.createCell(7).setCellValue(objToString(" "));
					}
		        	
		        	//主单号
		        	if (null != garmentFranchisorReport.getMainOrderNumber()) {
		        		row.createCell(8).setCellValue(objToString(garmentFranchisorReport.getMainOrderNumber()));
					}else{
						row.createCell(8).setCellValue(objToString(" "));
					}
		        	
		        	//分单号
		        	if (null != garmentFranchisorReport.getOrderNumber()) {
		        		row.createCell(9).setCellValue(objToString(garmentFranchisorReport.getOrderNumber()));
					}else{
						row.createCell(9).setCellValue(objToString(" "));
					}
		        	
		        	//订单时间
		        	if (null != garmentFranchisorReport.getOrderTime()) {
		        		row.createCell(10).setCellValue(objToString(garmentFranchisorReport.getOrderTime()));
					}else{
						row.createCell(10).setCellValue(objToString(" "));
					}
		        	
		        	//配送时间
		        	if (null != garmentFranchisorReport.getDeliveryTime()) {
		        		row.createCell(11).setCellValue(objToString(garmentFranchisorReport.getDeliveryTime()));
					}else{
						row.createCell(11).setCellValue(objToString(" "));
					}
		        	
		        	
		        	//订单状态
		        	if (null != garmentFranchisorReport.getStatusId()) {
		        		switch (garmentFranchisorReport.getStatusId()) {
						case 4:
							row.createCell(12).setCellValue("待签收");
							break;
						case 5:
							if(garmentFranchisorReport.getOrderNumber().contains("T")){
								row.createCell(12).setCellValue("退货已完成");
							}else{
								row.createCell(12).setCellValue("待评价");
							}
							break;
						case 6:
							row.createCell(12).setCellValue("已完成");
							break;
						case 7:
							row.createCell(12).setCellValue("已取消");
							break;
						case 8:
							row.createCell(12).setCellValue("已删除");
							break;
						case 9:
							row.createCell(12).setCellValue("退货中");
							break;
						case 10:
							row.createCell(12).setCellValue("退货确认");
							break;
						case 11:
							row.createCell(12).setCellValue("退货取消");
							break;
						case 12:
							row.createCell(12).setCellValue("退货完成");
							break;
						default:
							break;
						}
					}else{
						row.createCell(12).setCellValue(objToString(" "));
					}
		        	
		        	//预存款
		        	if (null != garmentFranchisorReport.getActualPay()) {
		        		row.createCell(13).setCellValue(objToString(garmentFranchisorReport.getActualPay()));
					}else{
						garmentFranchisorReport.setActualPay(0.0);
						row.createCell(13).setCellValue(objToString("0"));
					}
		        	
		        	//第三方支付支付宝及第三方支付银联
		        	if(null != garmentFranchisorReport.getPayTypeId() && garmentFranchisorReport.getPayTypeId()==3){
		        		row.createCell(14).setCellValue(objToString(garmentFranchisorReport.getOtherPay()));
		        		row.createCell(15).setCellValue("0");
		        	}else if(null != garmentFranchisorReport.getPayTypeId() && garmentFranchisorReport.getPayTypeId()==5){
		        		row.createCell(14).setCellValue("0");
		        		row.createCell(15).setCellValue(objToString(garmentFranchisorReport.getOtherPay()));
		        	}else{
		        		row.createCell(14).setCellValue("0");
		        		row.createCell(15).setCellValue("0");
		        	}
		        	
		        	//现金支付
		        	if (null != garmentFranchisorReport.getCashPay()) {
		        		row.createCell(16).setCellValue(objToString(garmentFranchisorReport.getCashPay()));
					}else{
						garmentFranchisorReport.setCashPay(0.0);
						row.createCell(16).setCellValue(objToString("0"));
					}
		        	
		        	//现金券总额
		        	if (null != garmentFranchisorReport.getCashCoupon()) {
		        		row.createCell(17).setCellValue(objToString(garmentFranchisorReport.getCashCoupon()));
					}else{
						row.createCell(17).setCellValue(objToString("0"));
					}
		        	
		         	
		        	//总额
		        	row.createCell(18).setCellValue(objToString(garmentFranchisorReport.getActualPay()+garmentFranchisorReport.getOtherPay()+garmentFranchisorReport.getCashCoupon()));
				
				}
			}
	        
		return wb;
	}
	
	
	/**
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param diyCode 门店编码
	 * @param cityName	城市名称
	 * @param username	管理员用户名
	 * @param roleDiyIds  授权管理门店的id
	 * @return
	 * @throws ParseException 
	 */
	private HSSFWorkbook continuousBuyBook(Date begin, Date end, String diyCode, String cityName, String username,
			List<String> roleDiyIds) throws ParseException {

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();

		String beginStr = null;
		String endStr = null;
		// 判断空值
		if (begin == null) {
			beginStr = "201609";
		} else {
			beginStr = dateToString(begin);
		}
		if (end == null) {
			endStr = dateToString(new Date());
		} else {
			endStr = dateToString(end);
		}
		// 查询要写入excel的行记录
		List<TdSalesForContinuousBuy> buyList = tdSalesForContinuousBuyService.queryDownList(beginStr, endStr, cityName,
				diyCode, roleDiyIds);

		int maxRowNum = 60000;
		int maxSize = 0;
		if (buyList != null) {
			maxSize = buyList.size();
		}
		int indicator = 0;
		int sheets = maxSize / maxRowNum + 1;

		// 写入excel文件数据信息
		for (int i = 0; i < sheets; i++) {

			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet("第" + (i + 1) + "页");
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			// 列宽
			int[] widths = { 18, 18, 18, 18, 20, 18, 20, 18, 30, 30, 25, 25, 18, 18, 18, 18, 18, 18 };
			sheetColumnWidth(sheet, widths);

			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setWrapText(true);

			// 设置标题
			HSSFRow row = sheet.createRow((int) 0);

			// 月份数组
			List<String> listStr = getMonth(beginStr, endStr);

			// 拼接标题栏
			String[] cellVal01 = { "城市", "门店名称", "销顾编号", "销顾名称", "会员名称", "会员编号", "会员电话" };
			String[] cellVal02 = (String[]) listStr.toArray(new String[listStr.size()]);
			String[] cellValues = concat(cellVal01, cellVal02);

			cellDates(cellValues, style, row);

			for (int j = 0; j < maxRowNum; j++) {
				if (j + i * maxRowNum >= maxSize) {
					break;
				}
				TdSalesForContinuousBuy continuousBuy = buyList.get(j + i * maxRowNum);
				row = sheet.createRow((int) j + 1);

				// 城市
				if (null != continuousBuy.getCityName()) {
					row.createCell(0).setCellValue(objToString(continuousBuy.getCityName()));
				} else {
					row.createCell(0).setCellValue(" ");
				}

				// 门店名称

				if (null != continuousBuy.getDiySiteName()) {
					row.createCell(1).setCellValue(objToString(continuousBuy.getDiySiteName()));
				} else {
					row.createCell(1).setCellValue(" ");
				}

				// 销顾编号

				if (null != continuousBuy.getSellerUsername()) {
					row.createCell(2).setCellValue(objToString(continuousBuy.getSellerUsername()));
				} else {
					row.createCell(2).setCellValue(" ");
				}

				// 销顾名称
				if (null != continuousBuy.getSellerRealName()) {
					row.createCell(3).setCellValue(objToString(continuousBuy.getSellerRealName()));
				} else {
					row.createCell(3).setCellValue(" ");
				}
				// 会员名称
				if (null != continuousBuy.getRealName()) {
					row.createCell(4).setCellValue(objToString(continuousBuy.getRealName()));
				} else {
					row.createCell(4).setCellValue(" ");
				}
				// 会员编号
				if (null != continuousBuy.getUsername()) {
					row.createCell(5).setCellValue(objToString(continuousBuy.getUsername()));
				} else {
					row.createCell(5).setCellValue(" ");
				}

				// 会员电话
				if (null != continuousBuy.getUsername()) {
					row.createCell(6).setCellValue(objToString(continuousBuy.getUsername()));
				} else {
					row.createCell(6).setCellValue(" ");
				}

				String beginStrSplit = beginStr.substring(0, 4) + "-" + beginStr.substring(4, 6);
				String endStrSplit = endStr.substring(0, 4) + "-" + endStr.substring(4, 6);
				Date d1 = new SimpleDateFormat("yyyy-MM").parse(beginStrSplit);// 定义起始日期

				Date d2 = new SimpleDateFormat("yyyy-MM").parse(endStrSplit);// 定义结束日期

				Calendar dd = Calendar.getInstance();// 定义日期实例

				dd.setTime(d1);// 设置日期起始时间
				int tempFlag = 7;

				while (!dd.getTime().after(d2)) {// 判断是否到结束日期
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
					String monthStr = sdf.format(dd.getTime());
					Integer tempSale = tdSalesForContinuousBuyService.retriveSale(continuousBuy.getDiySiteName(),
							continuousBuy.getUsername(), continuousBuy.getSellerUsername(), monthStr);
					if(null !=tempSale ){
						row.createCell(tempFlag).setCellValue(objToString(tempSale.intValue()));
					}else{
						row.createCell(tempFlag).setCellValue(objToString(0));
					}
					
					tempFlag++;
					dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1
				}

			indicator++;
			System.out.println(indicator);
			}
		}

		return wb;
	}
	
	private String objToString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * 日期转字符串
	 */
	public static String dateToString(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date).substring(0, 6);

	}
	
	
	/**
	 * 获取月份数组
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getMonth(String begin,String end) throws ParseException{
		
		 List<String> listStr = new ArrayList<String>();
		 String beginStr = begin.substring(0,4)+"-"+begin.substring(4,6);
		 String endStr = end.substring(0,4)+"-"+end.substring(4,6);
		  Date d1 = new SimpleDateFormat("yyyy-MM").parse(beginStr);//定义起始日期

		  Date d2 = new SimpleDateFormat("yyyy-MM").parse(endStr);//定义结束日期

		  Calendar dd = Calendar.getInstance();//定义日期实例

		  dd.setTime(d1);//设置日期起始时间

		  while(!dd.getTime().after(d2)){//判断是否到结束日期

		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		  String str = sdf.format(dd.getTime());

		  System.out.println(str);//输出日期结果
		  listStr.add(str);

		  dd.add(Calendar.MONTH, 1);//进行当前日期月份加1

		  }
	        
		System.out.println(listStr);
		return listStr;
		
	}
	
}
