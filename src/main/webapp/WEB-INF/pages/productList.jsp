<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <style>
    .overlay {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      background: rgba(0, 0, 0, 0.7);
      transition: opacity 500ms;
      display: none;
      opacity: 0;
    }
    .overlay:target {
      display:block;
      opacity: 1;
    }

    .popup {
      margin: 70px auto;
      padding: 20px;
      background: #fff;
      border-radius: 5px;
      width: 30%;
      position: relative;
      transition: all 5s ease-in-out;
    }

    .popup .close {
      position: absolute;
      top: 20px;
      right: 30px;
      transition: all 200ms;
    }

    .popup-table, .popup-table > tbody > tr > td{
      border: none;
    }
  </style>

  <p>
    Welcome to Expert-Soft training!
  </p>
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
        <td class="price">
          Price
          <tags:sortLink order="asc" sort="price"/>
          <tags:sortLink order="desc" sort="price"/>
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
        <td class="price-popup">
          <a href="#${product.id}">
          <fmt:formatNumber value="${product.price}" type="currency"
                            currencySymbol="${product.currency.symbol}" maxFractionDigits="0"/>
          </a>
          <div id="${product.id}" class="overlay">
            <div class="popup">
              <h2>Price history:</h2>
              <h3>${product.description}</h3>
              <a class="close" href="#">&times;</a>
              <div>
                <table class="popup-table">
                <c:forEach var="history" items="${product.histories}">
                  <tr>
                    <td><fmt:formatDate value="${history.date}" pattern="yyyy-MM-dd"/></td>
                    <td><fmt:formatNumber value="${history.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}" maxFractionDigits="0"/></td>
                  </tr>
                </c:forEach>
                </table>
              </div>
            </div>
          </div>

        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>