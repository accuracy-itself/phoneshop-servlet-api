<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<h3>
    Recently viewed
</h3>
<table>
    <tr>
        <c:forEach var="item" items="${viewHistory.items}">
            <td>
                <p>
                    <img class="product-tile" src="${item.product.imageUrl}" alt="${item.product.description}">
                </p>
                <p>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                            ${item.product.description}
                    </a>
                </p>
                <p>
                    <fmt:formatNumber value="${item.product.price}" type="currency"
                                      currencySymbol="${item.product.currency.symbol}"/>
                </p>
            </td>
        </c:forEach>
    </tr>
</table>

