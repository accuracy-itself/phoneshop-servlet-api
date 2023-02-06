<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Product List">
  <style>
    <%@include file="style.css"%>>
  </style>
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message}">
    <div class="success">
        ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="error">
      There was an error adding to cart. ${param.error}
    </div>
  </c:if>
  <br>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <a href="${pageContext.servletContext.contextPath}/advancedSearch">Advanced search</a>

  <jsp:include page="productTable.jsp"/>

  <jsp:include page="viewHistory.jsp"/>
</tags:master>