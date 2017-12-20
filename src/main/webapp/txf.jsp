<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="http://www.zmpay.xyz/ticket/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://www.zmpay.xyz/ticket/css/client.css">
    <script src="http://www.zmpay.xyz/ticket/js/jquery.min.js"></script>
    <script src="http://www.zmpay.xyz/ticket/js/bootstrap.min.js"></script>
	<title>银联在线支付</title>
</head>
<body class="bg-info">
	<%
		String orderNo = request.getParameter("orderNo");
		String amount = request.getParameter("amount");
		String jspayUrl = "/xpay/jspay/" + orderNo;
	%>
    <div class="jumbotron text-center">
        <h2>银联在线支付</h2>
    </div>
    <div class="container">
   		 <div class="row">
            <div class="wrapper">
                <h5 class="text-center">订单金额：<%= amount%>元</h5>
            </div>
         </div>
    	<div class="row">
            <div class="wrapper">
                <h4 class="text-center">请选择支付方式和银行</h4>
                <br>
            </div>
         </div>  
         <form id="payForm" class="form-horizontal" action="<%= jspayUrl%>" onsubmit = "return checkInput();">
                    <div class="form-group">
                        <label for="name" class="col-xs-4 control-label text-right">支付方式：</label>
                        <div>
                            <select id="cardType" name="cardType" style="width:153px" required>  
  								<option value ="1">借记卡</option>  
  								<option value ="2">信用卡</option>  
 							</select> 	
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="phone" class="col-xs-4 control-label text-right">开户银行：</label>
                        <div>
                            <select id="bankId" name="bankId" style="width:153px" required>  
  								<option value ="1004">建设银行</option>  
   								<option value ="1002">农业银行</option>  
   								<option value ="1001">工商银行</option>  
  								<option value ="1003">中国银行</option>  
  								<option value ="1014">浦发银行</option>  
   								<option value ="1008">光大银行</option>
   								<option value ="1011">平安银行</option>  
  								<option value ="1013">兴业银行</option>  
   								<option value ="1006">邮政储蓄银行</option> 
   								<option value ="1007">中信银行</option>  
  								<option value ="1009">华夏银行</option>  
   								<option value ="1012">招商银行</option> 
   								<option value ="1017">广发银行</option>  
  								<option value ="1016">北京银行</option>  
   								<option value ="1025">上海银行</option> 
   								<option value ="1010">民生银行</option>  
  								<option value ="1005">交通银行</option>  
   								<option value ="1103">北京农村商业银行</option>  	
   								<option value ="6666">其他银行</option>  								
 							</select> 
                        </div>
                    </div>
                    <br>
               <button type="submit" class="btn btn-primary center-block">立即支付</button>
         </form>  
    </div>
    
<script>
    function checkUser(){
  		var cardType = document.getElementById("cardType").value;
   		var bank = document.getElementById("bankId").value;

  		if(cardType == ""  ){
     		alert("请选择支付方式");
     		return false;
   		}
   		if(bankId == ""  ){
    		alert("请选择银行");
     		return false;
   		}
  		document.getElementById("payForm").submit();
	}
</script>
</body>
</html>