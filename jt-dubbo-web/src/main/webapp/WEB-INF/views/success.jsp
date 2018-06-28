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
   <title>订单成功页面 - 京淘商城</title>
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
		<ul class="step" id="step3">
			<li class="fore1">1.我的购物车<b></b></li>
			<li class="fore2">2.填写核对订单信息<b></b></li>
			<li class="fore3">3.成功提交订单</li>
		</ul>
		<div class="clr"></div>
</div>
<div class="w" id="safeinfo"></div><!--父订单的ID-->
<div class="w main">
	<div class="m m3 msop">
        <div class="mt" id="success_tittle"><s class="icon-succ02"></s><h3 class="ftx-02">感谢您，订单提交成功！</h3>
		</div>
		<div class="mc" id="success_detail">	
		    <ul class="list-order">
			    <li class="li-st">
					<div class="fore1">订单号：<a href="javascript:void(0)">${order.orderId }</a></div>
					<!-- 货到付款 -->
					<div class="fore2">应付款：<strong class="ftx-01">${order.payment}元</strong></div>
					<div class="fore3">
					   	京淘快递 &nbsp; 送货所需时间: 预计次日达&nbsp;
					</div>
				</li>
			</ul>
		<!-- 在线支付按钮  -->
				<div id="bookDiv"></div>
 					<p class="i-tips01">
				            	您的订单已经在处理中，请及时支付订单，如有疑问欢迎咨询
             		</p>
		 </div>
		
		<div class="payment payment-new mt25" id="payMallPlatVm">
		    <div class="payment-change">
		        <div class="pc-wrap clearfix" :effect="{is:'animate', action:'enter'}">
		            <div class="pc-w-left" ms-for="($index, el) in @agencyChannelList">
		                <div class="pay-wrap">
			                <a href="http://www.jt.com/order/pay-zfb/${order.orderId}.html" ms-if="@el.channelId =='weixin' && @el.canUse" :click="@weixinConfirm($index)" style="cursor:pointer" psa="PCashier_wxzf">
			                    <i class="wechat-pay icon"><img src="/images/PAY-ZFB.png" ></i> 支付宝支付
			                </a>
			                <span ms-if="$index !== 0">|</span>
			                <a href="http://www.jt.com/order/pay-union/${order.orderId}.html" ms-if="@el.channelId =='unionpay' && @el.canUse" :click="@platConfirm($index)" style="cursor:pointer" psa="PCashier_zgyl">
			                    <i class="visa-pay icon"><img src="/images/PAY-UNION.png"></i>
			                   	 银联支付
			                    <i class="item-tips-content" ms-if="@el.marketingInfo" ms-text="{{@el.marketingInfo}}"></i>
			                </a>
		            	</div>
		            </div>
		        </div>
		    </div>
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