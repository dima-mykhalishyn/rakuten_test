<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<button id="createCategory" class="btn btn-info" type="button">Create Category</button>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span7">
            <fieldset>
                <legend>Category:</legend>
                <ul id="categoryTree" class="filetree">
                    <c:forEach var="treeValue" items="${categoryForm.treeElements}">
                    <li id="${treeValue.id}" <c:if test="${treeValue.hasChildElements}">class="hasChildren"</c:if>><span class='folder' key='${treeValue.id}'>${treeValue.name}</span><div style='width: 100px;display: inline-block;padding-left:10px;'><a class='btn btn-mini btn-info editCategory'><i class='icon-edit'></i></a>&nbsp;<a class='btn btn-mini btn-danger deleteCategory' key='${treeValue.id}'><i class='icon-remove'></i></a></div>
                        <c:if test="${treeValue.hasChildElements}">
                        <ul>
                            <li><span class="placeholder">&nbsp;</span></li>
                        </ul>
                        </c:if>
                    <li>
                        </c:forEach>
                </ul>
            </fieldset>
        </div>
        <div class="span3">
            <fieldset>
                <legend>Products:</legend>
                <ul id="productBlock" class="nav nav-tabs nav-stacked" style="overflow: auto;height: 400px;">
                    <c:forEach var="productValue" items="${categoryForm.products}">
                        <li class="product" key="${productValue.id}"><a href="#">${productValue.productId}</a></li>
                    </c:forEach>
                </ul>
            </fieldset>
        </div>
    </div>
</div>




<script src="${pageContext.request.contextPath}/js/test/category.js" type="text/javascript"></script>