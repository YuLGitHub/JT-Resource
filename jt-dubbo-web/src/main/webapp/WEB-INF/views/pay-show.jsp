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
   <title>支付宝支付确认页面 - 京淘商城</title>
   <script type="text/javascript" src="/js/jquery-1.2.6.min.js"></script>
   <script type="text/javascript" src="/js/base-2011.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jquery.cookie.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jt.js" charset="utf-8"></script>
   <link rel="stylesheet" type="text/css" href="/bootstrap-4.0.0-dist/css/bootstrap.css">
   <!-- 支付宝 -->
   <link rel="stylesheet" type="text/css" href="/css/zfb/main.css">
   <script type="text/javascript" src="/js/common.js"></script>
   <script type="text/javascript" src="/js/zfb.js"></script>
   <script type="text/javascript" src="/js/wl.js"></script>
   <script type="text/javascript" src="/js/tdpay.js"></script>
</head> <body id="mainframe">
<!--shortcut start-->
<jsp:include page="../commons/shortcut.jsp" />
<!--shortcut end-->
<div class="w" id="headers">
		<div id="logo"><a href="www.jt.com/index.html"><img alt="京淘商城" src="/images/jt-logo.png"></a></div>
		<div class="clr"></div>
</div>

<div class="w">
	<div class="payment">
            <!-- 支付宝支付 -->
            <div class="pay-weixin">
                <div class="p-w-hd">支付宝支付</div>
                <div class="o-left"><h3 class="o-title">正在付款的订单：${orderId}</h3></div>
                <br />
                <div class="p-w-bd" style="position:relative">
                    <div class="p-w-box">
                        <div class="pw-box-hd" width="300" height="300">
                            <img src="${qrUrl}" border-style="solid;" width="256" height="256" >
                        </div>
                        <div class="pw-box-ft">
                            <p>请使用支付宝扫一扫</p>
                            <p>扫描二维码支付</p>
                        </div>
                    </div>
                    <div class="p-w-sidebar"></div>
                </div>
            </div>
            <!-- 支付宝支付 end -->
            <div style="text-align: center;">
            	<a class="btn btn-primary active" role="button" onclick ="queryPayStatus()">支付完成</a>
            </div>
            <br />
        </div>
</div>
	
  <div class="w">
	<!-- links start -->
    <jsp:include page="../commons/footer-links.jsp"></jsp:include>
    <!-- links end -->
</div><!-- footer end -->

<script type="text/javascript">
	function queryPayStatus() {
		window.location.href="http://www.jt.com/order/query_payStatus/" + ${orderId} +".html";
	}
</script>
</body> 
</html>