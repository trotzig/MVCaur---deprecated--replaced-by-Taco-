<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MVCaur</title>
</head>
<body>
	<h1><c:out value="${mvcaur.name}"/></h1>
	<p>
		Type: <c:out value="${mvcaur.inner.type}"/> 
	</p>
</body>
</html>