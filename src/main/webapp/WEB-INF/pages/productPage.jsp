<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="test" uri="/WEB-INF/struts/struts-layout.tld"  %>
<fieldset>
    <legend>Products:</legend>
    <button id="createProduct" class="btn btn-info" type="button">Create Product</button>
    <div class="input-prepend" style="position:absolute;right:150px;">
        <span class="add-on" style="height: 20px;">Filter:</span>
        <input id="productFilter" style="height: 30px;"/>
    </div>
    <table id="productTable" class="table table-bordered filterable" style="width: 100%;" >
        <thead>
        <tr>
            <th>Product ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th style="width: 200px;">Created</th>
            <th style="width: 120px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="productValue" items="${productForm.productList}" varStatus="status">
            <tr key="${productValue.id}">
                <td class="filtered">${productValue.productId}</td>
                <td class="filtered">${productValue.name}</td>
                <td>${productValue.description}</td>
                <td>${productValue.price}</td>
                <td><test:dateFormatter format="yyyy-MM-dd HH:mm:ss" dateValue="${productValue.created}"/></td>
                <td>
                    <div style='width: 120px;display: inline-block; '>
                        <a class='btn btn-small btn-info editProduct'><i class='icon-edit'></i></a>&nbsp;
                        <a class='btn btn-small btn-danger deleteProduct'><i class='icon-remove'></i></a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</fieldset>

<div id="addEditDialog" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>

<script src="${pageContext.request.contextPath}/js/test/product.js" type="text/javascript"></script>