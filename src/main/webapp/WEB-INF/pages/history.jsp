<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<h3>
    Recently viewed
</h3>
<table>
    <tr>
        <c:forEach var="item" items="${history.products}">
            <td>
                <p>
                    <img class="product-tile" src="${item.imageUrl}" alt="${item.description}">
                </p>
                <p>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.id}">
                            ${item.description}
                    </a>
                </p>
                <p>
                    <fmt:formatNumber value="${item.price}" type="currency"
                                      currencySymbol="${item.currency.symbol}"/>
                </p>
            </td>
        </c:forEach>
    </tr>
</table>

