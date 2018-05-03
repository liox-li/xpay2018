<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>环迅更新用户信息</title>
<body>
<form id="form" action="https://ebp.ips.com.cn/fpms-access/action/user/update.html" method="post">
  <input name="ipsRequest" type="hidden" value="<c:out value="${ipsRequest}" /> " />
</form>
<script>
window.onload= function(){
   document.getElementById('form').submit();
}
</script>
</body>
</html>