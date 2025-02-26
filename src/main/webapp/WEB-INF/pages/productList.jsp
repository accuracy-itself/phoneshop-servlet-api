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
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink order="asc" sort="description"/>
          <tags:sortLink order="desc" sort="description"/>
        </td>
        <td>Quantity</td>
        <td class="price">
          Price
          <tags:sortLink order="asc" sort="price"/>
          <tags:sortLink order="desc" sort="price"/>
        </td>
        <td></td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}" alt="${product.description}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td class="quantity">
          <form id="addToCart${product.id}" method="post" >
            <input name="quantity" value="1" class="quantity">
          </form>
        </td>
        <td class="price-popup">
          <a href="#${product.id}">
          <fmt:formatNumber value="${product.price}" type="currency"
                            currencySymbol="${product.currency.symbol}" maxFractionDigits="0"/>
          </a>
          <div id="${product.id}" class="overlay">
            <div class="popup">
              <h2>Price viewHistory:</h2>
              <h3>${product.description}</h3>
              <a class="close" href="#">&times;</a>
              <div>
                <table class="popup-table">
                <c:forEach var="viewHistory" items="${product.histories}">
                  <tr>
                    <td><fmt:parseDate value="${viewHistory.date}" pattern="yyyy-MM-dd" var="historyDate" type="date"/>
                      <fmt:formatDate pattern="dd.MM.yyyy" value="${historyDate}"/></td>
                    <td><fmt:formatNumber value="${viewHistory.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}" maxFractionDigits="0"/></td>
                  </tr>
                </c:forEach>
                </table>
              </div>
            </div>
          </div>
        </td>
        <td>
            <button form="addToCart${product.id}" formaction="${pageContext.servletContext.contextPath}/addCartItem/${product.id}">Add to cart</button>
        </td>
      </tr>
    </c:forEach>
  </table>
  <jsp:include page="viewHistory.jsp"/>
</tags:master>