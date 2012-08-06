<%@page import="ua.pp.bizon.persentcounter.web.dao.StatementProxy"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Statement</title>
</head>
<body>
<body>
<%
    StatementProxy statementDAO = (StatementProxy) request.getAttribute("statementDao");
%>
    <div><%=statementDAO.getSessionId()%></div>

    <form method="POST" action="" enctype="multipart/form-data">
        Date from: <input type="file" name="from" /> 
        <input type="text" value="<%= statementDAO.getSessionId() %>" name="id" style="visibility: hidden;"/>
        <% if (statementDAO.getSessionId() != null) request.setAttribute("id", statementDAO.getSessionId()); %>
        <input
            type="submit" value="Add" />
    </form>

    <hr>
    <ol>
        <li><%=":" + statementDAO.getStatement().toString() + ":"%></li>

    </ol>
    <hr>


</body>
</html>