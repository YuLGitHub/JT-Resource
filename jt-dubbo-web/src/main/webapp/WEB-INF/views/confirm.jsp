<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
   <meta http-equiv="pragma" content="no-cache" />
   <meta http-equiv="cache-control" content="no-cache" />
   <meta http-equiv="expires" content="0" /> 
   <meta name="format-detection" content="telephone=no" />  
   <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" /> 
   <meta name="format-detection" content="telephone=no" />
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   <link type="text/css" rel="stylesheet" href="/css/base.css" />
   <link rel="stylesheet" type="text/css" href="/css/purchase.base.2012.css" />
   <link rel="stylesheet" type="text/css" href="/css/purchase.sop.css" />
   <link rel="stylesheet" type="text/css" href="/css/main.css">
   <link rel="stylesheet" type="text/css" href="/css/bankList.css">
   <link rel="stylesheet" type="text/css" href="/css/confirm.css">
   <link rel="icon shortcut bookmark" type="image/png" href="/favicon.ico">
   <title>银联支付确认页面 - 京淘商城</title>
   <script type="text/javascript" src="/js/jquery-1.2.6.min.js"></script>
   <script type="text/javascript" src="/js/base-2011.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jquery.cookie.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jt.js" charset="utf-8"></script>
   <!-- 为支付添加 -->
  
</head> <body id="mainframe">
<!--shortcut start-->
<jsp:include page="../commons/shortcut.jsp" />
<!--shortcut end-->
<div class="w" id="headers">
		<div id="logo"><a href="www.jt.com/index.html"><img alt="京淘商城" src="/images/jt-logo.png"></a></div>
		<div class="clr"></div>
</div>
	<div id="warp" class="w main">
	<div>
		<h3>
			订单号：${p2_Order}
			<br>
			付款金额 ：${p3_Amt }
		</h3>
		<form action="https://www.yeepay.com/app-merchant-proxy/node" method="post">
			<input type="hidden" name="pd_FrpId" value="${pd_FrpId }" />
			<input type="hidden" name="p0_Cmd" value="${p0_Cmd }" />
			<input type="hidden" name="p1_MerId" value="${p1_MerId }" />
			<input type="hidden" name="p2_Order" value="${p2_Order }" />
			<input type="hidden" name="p3_Amt" value="${p3_Amt }" />
			<input type="hidden" name="p4_Cur" value="${p4_Cur }" />
			<input type="hidden" name="p5_Pid" value="${p5_Pid }" />
			<input type="hidden" name="p6_Pcat" value="${p6_Pcat }" />
			<input type="hidden" name="p7_Pdesc" value="${p7_Pdesc }" />
			<input type="hidden" name="p8_Url" value="${p8_Url }" />
			<input type="hidden" name="p9_SAF" value="${p9_SAF }" />
			<input type="hidden" name="pa_MP" value="${pa_MP }" />
			<input type="hidden" name="pr_NeedResponse" value="${pr_NeedResponse }" />
			<input type="hidden" name="hmac" value="${hmac }" />
			<input type="submit" value="确认支付" />
		</form>
	</div>
</div>
	
  <div class="w">
	<!-- links start -->
    <jsp:include page="../commons/footer-links.jsp"></jsp:include>
    <!-- links end -->
</div><!-- footer end -->
     </body> 
</html>