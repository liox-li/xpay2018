<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<html>
<header>
<title>出错啦！</title>
<body>
<pre>
<%
	String error = (String)request.getAttribute("javax.servlet.error.message");
    error = (error == null || error.length() == 0)?"出错啦！":error;
	out.println(error);
%>
</pre>