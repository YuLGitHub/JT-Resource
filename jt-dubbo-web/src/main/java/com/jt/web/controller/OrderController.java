package com.jt.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Maps;
import com.jt.common.vo.PicUploadResult;
import com.jt.common.vo.SysResult;
import com.jt.dubbo.cart.DubboCartService;
import com.jt.dubbo.order.DubboOrderService;
import com.jt.duboo.pojo.Cart;
import com.jt.duboo.pojo.Order;
import com.jt.duboo.pojo.OrderItem;
import com.jt.web.pojo.User;
import com.jt.web.service.FileService;
import com.jt.web.util.PaymentUtil;
import com.jt.web.util.PropUtils;
import com.jt.web.util.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private DubboCartService cartService;

	@Autowired
	private DubboOrderService orderService;
	
	@Autowired 
	private FileService fileService;
	
	private static AlipayTradeService   tradeService;
	
	private static Log                  log = LogFactory.getLog(OrderController.class);
	/**
	 * 1.需要跳转到订单的确认页面
	 * 2.准备购物车信息
	 * 		CartService 查询购物车信息
	 * 3.将信息会传到页面中 
	 * 4.将页面返回
	 * @return
	 */
	@RequestMapping("/create")
	public String toCreate(Model model){

		//配置拦截器的路径实现set赋值User对象
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = cartService.findCartByUserId(userId);
		model.addAttribute("carts", carts);

		return "order-cart";
	}
	
	@RequestMapping("/myOrder")
	public String toMyOrder(Model model) {
		User user = UserThreadLocal.get();
		Map<String,List<OrderItem>> itemMap = new HashMap<>();
		Integer page = 1;
		Integer rows = 5;
		List<Order> list = (List<Order>)(orderService.findOrdersByUserId(user.getId(), page, rows));
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();// 有错
			String orderId = order.getOrderId();
			List<OrderItem> itemList = orderService.findOrderItemsById(orderId);
			itemMap.put(orderId, itemList);
		}
		model.addAttribute("orders", list);
		model.addAttribute("itemMap", itemMap);
		return "my-orders";
	}
	

	/**
	 * 1.获取order的提交数据
	 * 2.通过threadLocal获取userId
	 * 3.通过服务端程序获取OrderId
	 * 4.判断OrderId是否有效
	 * 		非空校验 如果不为null 直接正确返回
	 * @param order
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		//获取用户Id
		Long userId = UserThreadLocal.get().getId();
		order.setUserId(userId);
		try {

			String orderId = orderService.saveOrder(order);
			if(!StringUtils.isEmpty(orderId)){
				Cart cart = new Cart();
				cart.setUserId(userId);
				//				cart.setId(id);
				cartService.deleteCart(cart);
				return SysResult.oK(orderId);	//正确返回
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SysResult.build(201, "订单新增失败");
	}


	/**
	 * 实现订单的查询 
	 * url:/success.html?id=71521189765462
	 * 查询数据应该是三张一起查询.
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model){

		Order order = orderService.findOrderById(id);

		model.addAttribute("order", order);
		return "success";
	}

	@RequestMapping("/pay-union/{orderId}")
	public String payUnion(@PathVariable String orderId, Model model) {
		Order order = orderService.findOrderById(orderId);
		model.addAttribute("order", order);
		return "pay-union";
	}

	@RequestMapping("/confirm/{orderId}")
	public String confirm(@PathVariable String orderId, HttpServletRequest request, HttpServletResponse response) {
		Order order = orderService.findOrderById(orderId);
		//2、准备第三方支付平台需要的参数
		String p0_Cmd="Buy";//业务类型
		String p1_MerId=PropUtils.getProp("p1_MerId");//商户编号
		String p2_Order=orderId;//商户订单号
		String p3_Amt="0.01";//金额，测试使用
		String p4_Cur="CNY";//交易币种，人民币
//		OrderItem orderItem = order.getOrderItems().get(0);
		String p5_Pid="";//商品名称
		String p6_Pcat="";//商品种类
		String p7_Pdesc="";//商品描述
		String p8_Url=PropUtils.getProp("responseURL");//接受支付成功数据的地址
		String p9_SAF="0";//送货地址，0为不需要保留到易宝系统中
		String pa_MP="";//商户扩展信息
		String pd_FrpId=request.getParameter("pd_FrpId");//支付通道编码
		String pr_NeedResponse="1";//应答机制
		String keyValue=PropUtils.getProp("keyValue");
		String hmac=PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);//签名数据
		request.setAttribute("p0_Cmd", p0_Cmd);
		request.setAttribute("p1_MerId", p1_MerId);
		request.setAttribute("p2_Order", p2_Order);
		request.setAttribute("p3_Amt", p3_Amt);
		request.setAttribute("p4_Cur", p4_Cur);
		request.setAttribute("p5_Pid", p5_Pid);
		request.setAttribute("p6_Pcat", p6_Pcat);
		request.setAttribute("p7_Pdesc", p7_Pdesc);
		request.setAttribute("p8_Url", p8_Url);
		request.setAttribute("p9_SAF", p9_SAF);
		request.setAttribute("pa_MP", pa_MP);
		request.setAttribute("pd_FrpId", pd_FrpId);
		request.setAttribute("pr_NeedResponse", pr_NeedResponse);
		request.setAttribute("hmac", hmac);
		request.setAttribute("order", order);
		return "confirm";
	}

	@RequestMapping("/callBack")
	public String callBack(HttpServletRequest request, HttpServletResponse response) {
		// 防止乱码
		response.setContentType("text/html;charset=utf-8");
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		String hmac = request.getParameter("hmac");
		//安全验证机制，如果结果为false，表明数据被篡改
		boolean verify = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, PropUtils.getProp("keyValue"));
		if(verify){
			try {
				if("1".equals(r9_BType)){
					//测试时使用
					orderService.updatePayStatusById(r6_Order,2);
					//重定向
					response.getWriter().write("支付操作已经执行");
				}else if("2".equals(r9_BType)){
					//点对点通知
					//修改订单支付状态，正式使用时在此处
					orderService.updatePayStatusById(r6_Order,2);
					//响应给第三方支付平台success
					response.getWriter().write("success");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			System.out.println("数据被篡改");
		}
		Order order = orderService.findOrderById(r6_Order);
		request.setAttribute("order", order);
		return "pay-success";
	}
	
	@RequestMapping("/pay-zfb/{orderId}")
	public String payzfb(@PathVariable String orderId, Model model) {
		Order order = orderService.findOrderById(orderId);
		Integer autoCode = (int) (Math.random()*100000000);
		model.addAttribute("order", order);
		model.addAttribute("code", autoCode);
		return "trade_pay";
	}
	
	@RequestMapping("/zfbConfirm/{orderId}")
	public String zfbConfirm(@PathVariable String orderId, Model model){
		String path = "E:/Documents/jt-upload";
//		String payConfirm = orderService.pay(orderId, path);
		Order order = orderService.findOrderById(orderId);
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderId();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuffer().append("京淘扫码支付，订单号：").append(orderId).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(orderId).append("购买商品共花").append(totalAmount).toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        
        List<OrderItem> orderItems = orderService.findOrderItemsById(orderId);
        for (OrderItem orderItem : orderItems) {
        	GoodsDetail goods = GoodsDetail.newInstance(orderItem.getItemId(), orderItem.getTitle(), 
        			orderItem.getPrice(), orderItem.getNum());
        	goodsDetailList.add(goods);
		}
        
        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            .setNotifyUrl("http://yul.s1.natapp.cc/order/alipay_callback.do")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);

        Configs.init("properties/zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                	folder.setWritable(true);
                	folder.mkdirs();
                }
                
                // 需要修改为运行机器上的路径
                String qrPath = String.format(path+"/qr-%s.png",
                    response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                log.info("filePath:" + qrPath);
                
                // 上传图片
                File file = new File(path,qrFileName);
                FileInputStream input;  
                MultipartFile multipartFile = null;
                try {
                	input = new FileInputStream(file);  
                	multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
                } catch (IOException e) {
                	e.printStackTrace();
                }  
                PicUploadResult picUploadResult = fileService.uploadFile(multipartFile);
                model.addAttribute("qrUrl", picUploadResult.getUrl());

            case FAILED:
                log.error("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
        }
		return "pay-show";
	}
	
	// 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (!StringUtils.isEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
	
    @RequestMapping("/alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
    	Map<String, String> params = Maps.newHashMap();
    	Map requestParams = request.getParameterMap();
    	for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
    		String name = (String) iterator.next();
    		String[] values = (String[]) requestParams.get(name);
    		String valueStr = "";
    		for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
    		params.put(name, valueStr);
    	}
//    	log.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if(!alipayRSACheckedV2){
                return "failed";
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常",e);
        }

        //todo 验证各种数据

        String orderNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderService.findOrderById(orderNo);
        if(order == null){
            return "failed";
        }
        if(order.getStatus() >= 2 ){
            return "success";
        }
        if("TRADE_SUCCESS".equals(tradeStatus)){
        	orderService.updatePayStatusById(order.getOrderId(), 2);
            return "success";
        }
        return "failed";
    }
	
    @RequestMapping("/query_payStatus/{orderId}")
	public String query_payStatus(@PathVariable String orderId, Model model) {
		Order order = orderService.findOrderById(orderId);
		int orderStatus = order.getStatus();
		model.addAttribute("order", order);
		if (orderStatus >= 2) {
			return "pay-success";
		}
		return "pay-show";
	}
	
}
