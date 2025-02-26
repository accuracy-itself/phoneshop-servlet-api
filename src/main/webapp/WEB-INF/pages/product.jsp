<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <style>
        <%@include file="style.css"%>>
    </style>
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message and empty error}">
        <div class="success">
            ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
                There was an error adding to cart.
        </div>
    </c:if>
    <p>
        ${product.description}
    </p>
    <form method="post">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}" alt="${product.description}">
                </td>
            </tr>
            <tr>
                <td>code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>quantity</td>
                <td class="quantity">
                    <input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity">
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
    <jsp:include page="viewHistory.jsp"/>
</tags:master>