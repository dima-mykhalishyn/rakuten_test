<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>


<html>
<head>
    <title>
        <tiles:getAsString name="title" ignore="true" />
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- ================================================================== -->
    <link href="${pageContext.request.contextPath}/css/test.css" rel="stylesheet" media="screen">
    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/css/jquery.treeview.css" rel="stylesheet" media="screen">

    <!-- ================================================================== -->
    <!-- jquery -->
    <script src="${pageContext.request.contextPath}/js/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery/jquery-ui-1.10.3.custom.min.js"></script>
    <!-- bootstrap -->
    <script src="${pageContext.request.contextPath}/js/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap/bootstrap-modal.js"></script>
    <!-- tree -->
    <script src="${pageContext.request.contextPath}/js/tree/jquery.treeview.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/tree/jquery.treeview.edit.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/tree/jquery.treeview.async.js" type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/js/test/global.js" type="text/javascript"></script>
</head>
<body>
<script type="text/javascript">
    var contextPath = '${pageContext.request.contextPath}';
</script>
<tiles:insert attribute="header" ignore="true"/>
<div class="container">
    <div style="padding-top: 60px; padding-bottom: 60px;">
        <tiles:insert attribute="body" ignore="true"/>
    </div>
</div>
<tiles:insert attribute="footer" ignore="true"/>
</body>
</html>