<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>跳转中...</title>
<body>
<form id="form" action="https://thumbpay.e-years.com/psfp-webscan/onlinePay.do" method="post">
  <input name="wxPayReq" type="hidden" value="<c:out value="${wxPayReq}" /> " />
</form>
<script>
window.onload= function(){
   document.getElementById('form').submit();
}
</script>
</body>
</html>