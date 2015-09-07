var selectedCategory = null;
var categoryButton = "<li><span class='folder' key='{1}'>{0}</span><div style='width: 100px;display: inline-block;padding-left:10px;'><a class='btn btn-mini btn-info editCategory newCategory'><i class='icon-edit'></i></a>&nbsp;<a class='btn btn-mini btn-danger deleteCategory newCategory'><i class='icon-remove'></i></a></div><li>";
var productBody = "<li><span class='file' key='{1}'>{0}</span><div style='width: 100px;display: inline-block;padding-left:10px;'><a class='btn btn-mini btn-danger deleteProduct newProduct'><i class='icon-remove'></i></a></div></li>";

function validateName(name){
    if(name == ""){
        alert("Name cannot be blank");
        return false;
    } else if(name.length > 255){
        alert("Name length cannot be more then 255");
        return false;
    }
    return true;
}

function initCategoryButtonClickActions(element){
    var edit = element == null ? $(".editCategory") : element.find(".editCategory");
    edit.click(function(event){
        event.preventDefault();
        var folder = $(this).closest("li").find("span:eq(0)");
        var name=prompt("Category Name", folder.text());
        if(!name)return;
        name = name.trim();
        if(!validateName(name)){
            return;
        }
        var nameExist = false;
        folder.closest("ul").find("li > .folder").each(function(){
            var dir = $(this);
            if(name == dir.text().trim() && dir.attr("key") != folder.attr("key"))
                nameExist = true;
        });
        if(name == ""){
            alert("Name cannot be blank");
            return;
        }
        var parentId = selectedCategory == null ? "" : "&category.parentId=" + selectedCategory.attr("key");
        var data = 'category.id=' + folder.attr("key") + '&category.name=' + encodeURIComponent(name) + parentId;
        $.ajax({
            type: 'POST',
            'url': contextPath + '/category.do?action=update',
            'data': data,
            'success': function(error){
                if(error != null && error.trim() != ""){
                    alert(error);
                    return;
                }
                folder.text(name);
            }, 'error':function(){
                alert("Error! Cannot update category")
            }});
    });
    var deleteEl = element == null ? $(".deleteCategory") : element.find(".deleteCategory");
    deleteEl.click(function(event){
        event.preventDefault();
        var folder = $(this).closest("li").find("span:eq(0)");
        var parent = folder.parent();
        var r=confirm("Delete Category '" + parent.find("span:eq(0)").text() + " ?");
        if(!r)return;
        $.ajax({
            type: 'GET',
            'url': contextPath + '/category.do?action=delete&id=' + folder.attr("key"),
            'success': function(){
                updateeeExpCollap(parent);
                parent.remove();
                initCategoriesActions();
            }, 'error':function(){
                alert("Error! Cannot delete category")
            }});
    });
}

function updateeeExpCollap(el){
    // if latest exist then remove
    if(el.closest("ul").find("li").length == 1) {
        var branch = el.parent().parent();
        branch.children(".hitarea").remove();
        branch.children(".folder").unbind('click');
        branch.removeClass("collapsable");
        branch.children("ul").remove();
    }
}

function initProductButtonClickActions(element){
    var deleteEl = element == null ? $(".deleteProduct") : element.find(".deleteProduct");
    deleteEl.click(function(event){
        event.preventDefault();
        var file = $(this).closest("li").find("span:eq(0)");
        var parent = file.parent();
        var r=confirm("Delete Product '" + parent.find("span:eq(0)").text() + " ?");
        if(!r)return;
        var folder = parent.closest("ul").closest("li").find(".folder:eq(0)");
        $.ajax({
            type: 'GET',
            'url': contextPath + '/category.do?action=removeProduct&productId=' + file.attr("key") + "&categoryId=" + folder.attr("key"),
            'success': function(){
                updateeeExpCollap(parent);
                var childrenGroup = parent.parent();
                parent.remove();
                childrenGroup.children("li:last").addClass("last");
                initCategoriesActions();
            }, 'error':function(){
                alert("Error! Cannot remove Product")
            }});
    });
}

function showHideDeleteButton(){
    $(".deleteCategory").each(function(){
        var $this = $(this);
        var childrenGroup = $this.closest("li").find("ul");
        if(childrenGroup.length == 0 || childrenGroup.find("li").length == 0){
            $this.show();
        } else {
            $this.hide();
        }
    });
}

function initCategoryTree(){
    $("#categoryTree").treeview({
        url: contextPath + "/category.do?action=loadData"
    });
}

function initCategoriesActions() {
    $("li:empty").remove();
    $(".folder").click(function(event){
        event.stopPropagation();
        $(".folder").removeClass("selected");
        selectedCategory = $(this);
        selectedCategory.addClass("selected");
    });

    showHideDeleteButton();
    initDroppable();

}

function toggler() {
    var CLASSES = $.treeview.classes;
    $(this).parent()
        // swap classes for hitarea
        .find(">.hitarea")
        .swapClass( CLASSES.collapsableHitarea, CLASSES.expandableHitarea )
        .swapClass( CLASSES.lastCollapsableHitarea, CLASSES.lastExpandableHitarea )
        .end()
        // swap classes for parent li
        .swapClass( CLASSES.collapsable, CLASSES.expandable )
        .swapClass( CLASSES.lastCollapsable, CLASSES.lastExpandable )
        // find child lists
        .find( ">ul" )
        // toggle them
        .heightToggle();
}

function initDroppable(){
    $(".folder").droppable({
        tolerance: 'touch',
        over: function() {
            $(this).addClass('dropOver');
        },
        out: function() {
            $(this).removeClass('dropOver');
        },
        drop: function(event, ui) {
            var productEl = ui.draggable;
            var productId = productEl.attr('key');
            var $this = $(this);
            if($this.parent().children("ul").children("li").children('.file[key="' + productId + '"]').length != 0){
                $(".folder").removeClass('dropOver');
                return;
            }
            $(".folder").removeClass('dropOver');
            $.ajax({
                type: 'GET',
                'url': contextPath + '/category.do?action=addProduct&categoryId=' + $this.attr("key") + "&productId=" + productId,
                'success': function(){
                    var branch;
                    var parent = $this.parent();
                    var childrenGroup = parent.children("ul");
                    var product = productBody.format(productEl.find("a").text(), productId);
                    if(childrenGroup.length == 0) {
                        parent.addClass("collapsable");
                        $("<div class='hitarea hasChildren-hitarea collapsable-hitarea' />").appendTo(parent).click(toggler);
                        parent.children(".folder").click(toggler);
                        var ul = $("<ul/>").appendTo(parent);
                        branch = $(product).appendTo(ul).addClass("last");
                    } else {
                        childrenGroup.children("li").removeClass("last");
                        var branch = $(product).appendTo(childrenGroup).addClass("last");
                    }
                    initProductButtonClickActions(branch);
                    showHideDeleteButton();

                }, 'error':function(){
                    $(".folder").removeClass('dropOver');
                }});

        }
    });
};

$(document).ready(function() {
    initCategoryTree();
    $(".product").draggable({
        revertDuration: 1,
        revert: true,
        helper:'clone'
    });
    $('html').click(function() {
        $(".folder").removeClass("selected");
        selectedCategory = null;
    });
    initCategoriesActions();
    initCategoryButtonClickActions();
    initProductButtonClickActions();

    $("#createCategory").click(function() {
        var name=prompt("Please enter Category Name");
        if(!name)return;
        name = name.trim();
        if(!validateName(name)){
            return;
        }
        var selected = selectedCategory;
        var catParent = selectedCategory == null ? $("#categoryTree") : selectedCategory.parent().children("ul");
        var nameExist = false;
        catParent.children("li").children('.folder').each(function(){
            if($(this).text().trim() == name){
                nameExist = true;
                return false;
            }
        });
        if(nameExist){
            alert("Category with current name already exist.");
            return;
        }
        var parentId = selectedCategory == null ? "" : "&category.parentId=" + selectedCategory.attr("key");
        var data = 'category.name=' + encodeURIComponent(name) + parentId;
        $.ajax({
            type: 'POST',
            'url': contextPath + '/category.do?action=create',
            'data': data,
            'success': function(res){
                var json
                try{
                    json = $.parseJSON(res);
                }catch(err){
                    alert(res);
                    return;
                }
                var category = categoryButton.format(name, json.id);
                var branch;
                if(selected == null){
                    branch = $(category).appendTo($("#categoryTree"));
                } else{
                    var parent = selected.parent();
                    var childrenGroup = parent.children("ul");
                    if(childrenGroup.length == 0) {
                        $("<div class='hitarea hasChildren-hitarea collapsable-hitarea'/>").prependTo(parent).click(toggler);
                        selected.click(toggler);
                        var ul = $("<ul/>").appendTo(parent);
                        branch = $(category).appendTo(ul);
                        branch.addClass("last");
                    } else {
                        var lastChildCategody = childrenGroup.children("li").children(".folder:last");
                        var childrenSize = childrenGroup.children("li").length;
                        if(lastChildCategody.length == 0){
                            branch = $(category).prependTo(childrenGroup);
                        } else {
                            var childParent = lastChildCategody.parent();
                            branch = $(category).insertAfter(childParent);
                        }
                        if(childrenSize == 0)branch.addClass("last");
                    }
                }
                initCategoriesActions();
                initCategoryButtonClickActions(branch);
            }, 'error':function(){
                alert("Error! Cannot save category")
            }});

    });
});