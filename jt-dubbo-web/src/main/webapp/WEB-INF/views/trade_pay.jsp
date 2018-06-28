<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	String path = request.getRequestURI();
	String orderId = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
    request.setAttribute("orderId", orderId);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>支付宝当面付信息确认</title>
<link rel="icon shortcut bookmark" type="image/png" href="/favicon.ico">
<link type="text/css" rel="stylesheet" href="/css/base.css" />
<link rel="stylesheet" type="text/css" href="/css/purchase.base.2012.css" />
<link rel="stylesheet" type="text/css" href="/css/purchase.sop.css" />
<!-- <link rel="stylesheet" type="text/css" href="/css/main.css"> -->
<link rel="stylesheet" type="text/css" href="/css/bankList.css">
<link rel="stylesheet" type="text/css" href="/css/pay.css">
<link rel="stylesheet" type="text/css" href="/css/zfb.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/jquery-1.2.6.min.js"></script>

<script type="text/javascript" src="/js/base-2011.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"
	charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="/bootstrap-4.0.0-dist/css/bootstrap.css">
<script type="text/javascript" src="/js/jt.js" charset="utf-8"></script>

</head>
<body id="mainframe">
	<!--shortcut start-->
	<jsp:include page="../commons/shortcut.jsp" />
	<!--shortcut end-->
	<div class="w" id="headers">
		<div id="logo">
			<a href="www.jt.com/index.html"><img alt="京淘商城"
				src="/images/jt-logo.png"></a>
		</div>
		<div class="clr"></div>
	</div>
	<div id="main" class="container-fluid">
		<div class="cashier-nav">
			<ol>
				<li class="current">1、确认信息 →</li>
				<li>2、点击确认 →</li>
				<li class="last">3、确认完成</li>
			</ol>
		</div>
		<form class="form-horizontal" action="http://www.jt.com/order/zfbConfirm/${order.orderId }.html"
			method="post">
			<div class="form-group">
				<label for="order-id" class="col-sm-2 control-label" >商户订单号</label>
				<div class="col-sm-10">
					<input class="form-control" id="order-id" name="outTradeNo" type="text" placeholder="${order.orderId }" readonly>
				</div>
			</div>
			<div class="form-group">
				<label for="pay-money" class="col-sm-2 control-label">付款金额</label>
				<div class="col-sm-10">
					<input class="form-control" id="pay-money" name="totalAmount" type="text" placeholder="${order.payment}" readonly>
				</div>
			</div>
			<div class="form-group">
				<label for="discount" class="col-sm-2 control-label">不打折金额</label>
				<div class="col-sm-10">
					<input class="form-control" id="discount" name="undiscountableAmount" type="text" placeholder="0.00">
				</div>
			</div>
			<div class="form-group">
				<label for="barCode" class="col-sm-2 control-label">付款条码</label>
				<div class="col-sm-10">
					<input class="form-control" id="barCode" name="authCode" type="text" placeholder="${code}" readonly>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary active">确 认</button>
				</div>
			</div>
		</form>
	</div>
	<div class="w">
		<!-- links start -->
		<jsp:include page="../commons/footer-links.jsp"></jsp:include>
		<!-- links end -->
	</div>
</body>
</html>