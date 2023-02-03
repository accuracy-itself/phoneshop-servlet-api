<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Overview">
    <style>
        <%@include file="style.css"%>
    </style>
    <h1>Order overview</h1>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="quantity">Quantity</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
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
                        ${item.quantity}
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
                                            <td><fmt:parseDate value="${viewHistory.date}" pattern="yyyy-MM-dd"
                                                               var="historyDate" type="date"/>
                                                <fmt:formatDate pattern="dd.MM.yyyy" value="${historyDate}"/></td>
                                            <td><fmt:formatNumber value="${viewHistory.price}" type="currency"
                                                                  currencySymbol="${item.product.currency.symbol}"
                                                                  maxFractionDigits="0"/></td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td>Subtotal</td>
            <td class="quantity">${order.totalQuantity}</td>
            <td class="price">${order.subTotal}</td>
        </tr>
        <tr>
            <td></td>
            <td>Delivery cost</td>
            <td></td>
            <td class="price">${order.deliveryCost}</td>
        </tr>
        <tr>
            <td></td>
            <td>Total</td>
            <td class="quantity">${order.totalQuantity}</td>
            <td class="price">${order.totalCost}</td>
        </tr>
    </table>
    <h2>Your details</h2>
    <table>
        <tags:orderOverviewRow name="firstName" order="${order}" label="First Name"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="lastName" order="${order}" label="Last Name"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="phone" order="${order}" label="Phone"></tags:orderOverviewRow>
        <td>Delivery Date</td>
        <td>
            <fmt:parseDate value="${order.deliveryDate}" pattern="yyyy-MM-dd"
                           var="deliveryDate" type="date"/>
            <fmt:formatDate pattern="dd.MM.yyyy" value="${deliveryDate}"/>
        </td>
        <tags:orderOverviewRow name="deliveryAddress" order="${order}" label="Delivery address"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="paymentMethod" order="${order}" label="Payment Method"></tags:orderOverviewRow>
    </table>
    <br>
    <jsp:include page="viewHistory.jsp"/>
</tags:master>