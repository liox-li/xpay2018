<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>跳转中...</title>
<body>
<form id="form" action=" https://mobilegw.ips.com.cn/psfp-mgw/paymenth5.do " method="post">
  <input name="pGateWayReq" type="hidden" value="<c:out value="${pGateWayReq}" /> " />
</form>
<script>
window.onload= function(){
   document.getElementById('form').submit();
}
</script>
</body>
</html>