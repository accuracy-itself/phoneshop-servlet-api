<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true"  type="com.es.phoneshop.model.product.order.Order"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="name" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr>
    <td>${label}</td>
    <td>
        ${order[name]}
    </td>
</tr>