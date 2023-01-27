<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <style>
        <%@include file="style.css"%>
    </style>
    <br>
    <c:if test="${not empty param.message and empty errors}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There were errors placing order.
        </div>
    </c:if>
    <br>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
            <tags:orderFormRow name="firstName" order="${order}" label ="First Name" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" order="${order}" label ="Last Name" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" order="${order}" label ="Phone" errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>Delivery Date<span style="color:red">*</span></td>
                <td>
                    <c:set var="error" value="${errors['deliveryDate']}"/>
                    <input name="deliveryDate" type=date value="${not empty error ? param['deliveryDate'] : order.deliveryDate}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
            <tags:orderFormRow name="deliveryAddress" order="${order}" label ="Delivery address" errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>Payment method</td>
                <td>
                    <select name="paymentMethod" style="width: 100%">
                        <option value="CASH" ${param['paymentMethod'] == "CASH" ? 'selected="selected"' : ''}>Cash</option>
                        <option value="CREDIT_CARD" ${param['paymentMethod'] == "CREDIT_CARD" ? 'selected="selected"' : ''}>Credit card</option>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <br>
        <button>Place order</button>
    <jsp:include page="viewHistory.jsp"/>
</tags:master>