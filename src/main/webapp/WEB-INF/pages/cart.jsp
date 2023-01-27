<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.product.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <style>
    <%@include file="style.css"%>
  </style>
  <p>
    Cart : ${cart}
  </p>
  <c:if test="${not empty param.message and empty error}">
    <div class="success">
        ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty errors}">
    <div class="errors">
        There were errors updating cart.
    </div>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>Description</td>
          <td class="quantity">Quantity</td>
          <td>Price</td>
          <td></td>
        </tr>
      </thead>
      <c:forEach var="item" items="${cart.items}" varStatus="status">
        <tr>
          <td>
            <img class="product-tile" src="${item.product.imageUrl}" alt="${item.product.description}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
              ${item.product.description}
            </a>
          </td>
          <td class="quantity">
            <fmt:formatNumber value="${item.quantity}" var="quantity"/>
            <c:set var="error" value="${errors[item.product.id]}"/>
            <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : quantity}" class="quantity"/>
            <c:if test="${not empty error}">
              <div class="error">
                  ${errors[item.product.id]}
              </div>
            </c:if>
            <input type="hidden" name="productId" value="${item.product.id}"/>
          </td>
          <td class="price-popup">
            <a href="#${item.product.id}">
            <fmt:formatNumber value="${item.product.price}" type="currency"
                              currencySymbol="${item.product.currency.symbol}" maxFractionDigits="0"/>
            </a>
            <div id="${item.product.id}" class="overlay">
              <div class="popup">
                <h2>Price viewHistory:</h2>
                <h3>${item.product.description}</h3>
                <a class="close" href="#">&times;</a>
                <div>
                  <table class="popup-table">
                  <c:forEach var="viewHistory" items="${item.product.histories}">
                    <tr>
                      <td><fmt:parseDate value="${viewHistory.date}" pattern="yyyy-MM-dd" var="historyDate" type="date"/>
                        <fmt:formatDate pattern="dd.MM.yyyy" value="${historyDate}"/></td>
                      <td><fmt:formatNumber value="${viewHistory.price}" type="currency"
                                            currencySymbol="${item.product.currency.symbol}" maxFractionDigits="0"/></td>
                    </tr>
                  </c:forEach>
                  </table>
                </div>
              </div>
            </div>
          </td>
          <td>
            <button form="deleteCartItem"
                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
      <tr>
        <td></td>
        <td>Total</td>
        <td class="quantity">${cart.totalQuantity}</td>
        <td class="price">${cart.totalCost}</td>
      </tr>
    </table>
    <p>
      <button>Update</button>
    </p>
  </form>
  <form id="deleteCartItem" method="post">
  </form>
  <form action="${pageContext.servletContext.contextPath}/checkout">
    <button>Checkout</button>
  </form>
  <jsp:include page="viewHistory.jsp"/>
</tags:master>