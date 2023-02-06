<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Advanced Search Page">
  <style>
    <%@include file="style.css"%>>
  </style>
  <h2>Advanced search</h2>
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
  <form method="post">
    <table class="search-table">
      <tr>
        <td>Description</td>
        <td>
          <input name="description" value="${param.description}">
          <c:set var="error" value="${errors['description']}"/>
          <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
          </c:if>
        </td>
        <td>
          <select name="wordMatching" style="width: 100%">
            <option value="true" ${param['wordMatching'] == "ALL_WORDS" ? 'selected="selected"' : ''}>all words</option>
            <option value="false" ${param['wordMatching'] == "ANY_WORD" ? 'selected="selected"' : ''}>any word</option>
          </select>
          <c:set var="error" value="${errors['wordMatching']}"/>
          <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
          </c:if>
        </td>
      </tr>
      <tr>
        <td>Min Price</td>
        <td>
          <input name="minPrice" value="${param.minPrice}">
          <c:set var="error" value="${errors['minPrice']}"/>
          <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
          </c:if>
        </td>
      </tr>
      <tr>
        <td>Max Price</td>
        <td>
          <input name="maxPrice" value="${param.maxPrice}">
          <c:set var="error" value="${errors['maxPrice']}"/>
          <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
          </c:if>
        </td>
      </tr>
    </table>
    <br>
    <button>Search</button>
  </form>

  <jsp:include page="productTable.jsp"/>

  <jsp:include page="viewHistory.jsp"/>
</tags:master>