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
   <link rel="icon shortcut bookmark" type="image/png" href="/favicon.ico">
   <link rel="stylesheet" href="/css/base.css">
   <title>支付成功页面 - 京淘商城</title>
   <script type="text/javascript" src="/js/jquery-1.2.6.min.js"></script>
   <script type="text/javascript" src="/js/base-2011.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jquery.cookie.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jt.js" charset="utf-8"></script>
   <!-- 为支付添加 -->
   <script type="text/javascript" src="/js/lib/base.js"></script>
   <script type="text/javascript" src="/js/lib/avalon.min.js"></script>
   <script type="text/javascript" src="/js/lib/lodash.min.js"></script>
   <script type="text/javascript" src="/js/lib/common.js"></script>
</head> <body id="mainframe">
<!--shortcut start-->
<jsp:include page="../commons/shortcut.jsp" />
<!--shortcut end-->
<div class="w" id="headers">
		<div id="logo"><a href="www.jt.com/index.html"><img alt="京淘商城" src="/images/jt-logo.png"></a></div>
		<div class="clr"></div>
</div>
<div class="w" id="safeinfo"></div><!--父订单的ID-->
<div class="w main">
	<div class="m m3 msop">
        <div class="mt" id="success_tittle"><s class="icon-succ02"></s><h3 class="ftx-02">感谢您，订单支付成功！</h3>
		</div>
		<div class="mc" id="success_detail">	
		    <ul class="list-order">
			    <li class="li-st">
					<div class="fore1">订单号：<a href="javascript:void(0)">${order.orderId }</a></div>
					<!-- 货到付款 -->
					<div class="fore2">已付款：<strong class="ftx-01">${order.payment}元</strong></div>
					<div class="fore3">
					   	京淘快递 &nbsp; 预计三日内送达&nbsp;
					</div>
				</li>
			</ul>
				<div id="bookDiv"></div>
 					<p class="i-tips01">
				            	您的宝贝正在赶往您的怀抱，请注意查收哦！
             		</p>
		 </div>
		 <div>
		 	<span class="shopping">
                 <b>
                 </b>
                 <a href="http://www.jt.com/index.html" target="_blank" clstag="clickcart|keycount|xincart|coudanlink" id="continue">继续购物</a>
                 <a href="http://www.jt.com/order/myOrder.html" target="_blank" clstag="clickcart|keycount|xincart|coudanlink" id="continue">查看我的订单</a>
             </span>
		 </div>
	</div>
</div>
  <div class="w">
	<!-- links start -->
    <jsp:include page="../commons/footer-links.jsp"></jsp:include>
    <!-- links end -->
  </div><!-- footer end -->
     </body> 
</html>