var dataTable;
var tableRowButtons = "<div style='width: 120px;display: inline-block; '><a class='btn btn-small btn-info editProduct newProduct'><i class='icon-edit'></i></a>&nbsp;<a class='btn btn-small btn-danger deleteProduct newProduct'><i class='icon-remove'></i></a></div>";
var addEditContext = '<form id="addEditProduct" class="form-horizontal" action="' + contextPath + '/product.do?action={5}" method="post" style="position:relative;">' +
    '<input name="product.id" type="text" value="{0}" style="visibility: hidden;position: absolute;">'  +
    '<div class="control-group"><label class="control-label">Product ID:</label><div class="controls"><input name="product.productId" type="text" value="{1}"></div></div>' +
    '<div class="control-group"><label class="control-label">Name:</label><div class="controls"><input name="product.name" type="text" value="{2}"></div></div>' +
    '<div class="control-group"><label class="control-label">Description:</label><div class="controls"><input name="product.description" type="text" value="{3}"></div></div>' +
    '<div class="control-group"><label class="control-label">Price:</label><div class="controls"><input id="productPrice" name="product.price" type="text" value="{4}"></div></div>' +
    '</div></form>';

var dialogTemplate = '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>' +
    '<h3 id="myModalLabel">{0}</h3></div>' +
    '<div class="modal-body">{1}</div>' +
    '<div class="modal-footer"><button id="saveProduct" class="btn btn-primary">{2}</button><button class="btn" data-dismiss="modal" aria-hidden="true">Close</button></div>';

function getTdValues(row){
    var tdValue = [];
    row.find("td").each(function(index,value){
        tdValue[index] = $(value).text();
    });
    return tdValue;
}

function getData(){
    var form = $("#addEditProduct");
    var data = [form.find("input[name='product\\.productId']").val().trim(),
        form.find("input[name='product\\.name']").val().trim(),
        form.find("input[name='product\\.description']").val().trim(),
        form.find("input[name='product\\.price']").val()];
    data[3] = data[3].trim() == "" ? 0 : Number(data[3].trim());
    return data;
}

function allowOnlyNumbersForPrice(){
    $("#productPrice").keydown(function(event) {
        // Allow: backspace, delete, tab, escape, and enter
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
            // Allow: Ctrl+A
            (event.keyCode == 65 && event.ctrlKey === true) ||
            // Allow: home, end, left, right
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            // let it happen, don't do anything
            return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || (event.keyCode != 190 && event.keyCode > 105) )) {
                event.preventDefault();
            }
        }
    });
}

function productIdExist(row, productId){
    if(productId == null)return false;
    var exist = false;
    $("#productTable tr td:first-child").each(function(){
        var $this = $(this);
        if((row == null || row.attr("key") != $this.parent().attr("key"))
            && productId.trim() == $this.text().trim()){
            exist = true;
            return false;
        }
    });
    return exist;
}

function showDialog(values, header, button, row){

    var content = $("#addEditDialog");
    var data = addEditContext.format(values[0],values[1],values[2],values[3],values[4], row == null ? "create" : "update");
    content.html(dialogTemplate.format(header,data,button));
    content.modal();

    allowOnlyNumbersForPrice();

    $('#saveProduct').click(function(){
        var form = $("#addEditProduct");
        var data = getData();
        if(productIdExist(row, data[0])){
            alert("Product ID already exist.");
            return;
        }
        $.ajax({
            type: 'POST',
            'url': form.attr('action'),
            'data': form.serialize(),
            'success': function(res){
                if(row != null){
                    // if not json - then error
                    if(res != null && res.trim() != ""){
                        alert(res);
                        return;
                    }
                    row.find("td").each(function(index,value){
                        if(index < 4)
                            $(value).text(data[index]);
                    });
                    var t = $(row).find(".filtered").text().toLowerCase();
                    row.find('.indexColumn').text(t);
                } else {
                    var json
                    try{
                        json = $.parseJSON(res);
                    }catch(err){
                        alert(res);
                        return;
                    }
                    var rowTemplate = "<tr key='{0}'>";
                    for(i=1;i<7;i++){
                        rowTemplate += "<td " + (i == 1 || i == 2 ? "class='filtered'" : "") + ">{" + i + "}</td>";
                    }
                    var lastTr = $('#productTable').find('tbody tr:last');
                    var newRow = rowTemplate.format(json.id,
                        data[0], data[1], data[2], data[3],
                        json.created, tableRowButtons);
                    if(lastTr.length == 0)
                        $('#productTable tbody').append(newRow);
                    else
                        lastTr.after(newRow);
                    setAsFiltered(lastTr.next());
                    initButtonClickActions(lastTr.next());
                }
                setAsFiltered($('#productTable').find('tbody tr:last'));
                content.modal('hide');
            }, 'error':function(){
                alert("Error! Cannot save Product");
                content.modal('hide');
            }});
        return false;
    });
}

function initButtonClickActions(element){
    var edit = element == null ? $(".editProduct") : element.find(".editProduct");
    edit.click(function(){
        var row = $(this).parent().parent().parent();
        var tdValue = getTdValues(row);
        var data = [row.attr("key")];
        showDialog(data.concat(tdValue.slice(0, 4)), "Edit (" + tdValue[1] + ")", "Save", row);
    });
    var deleteEl = element == null ? $(".deleteProduct") : element.find(".deleteProduct");
    deleteEl.click(function(){
        var row = $(this).parent().parent().parent();
        var tdValue = getTdValues(row);
        var r=confirm("Delete Product '" + tdValue[1] + " ?");
        if(!r)return;
        $.ajax({
            type: 'GET',
            'url': contextPath + '/product.do?action=delete&id=' + row.attr('key'),
            'success': function(){
                row.remove();
            }, 'error':function(){
                alert("Error! Cannot delete Product");
            }});
    });
}

function setAsFiltered(element){
    var t = $(element).find(".filtered").text().toLowerCase();
    $("<td class='indexColumn'></td>").hide().text(t).appendTo(element);
}

$(document).ready(function() {
    $(".filterable tr:has(td)").each(function(){
        setAsFiltered(this);
    });
    $("#productFilter").val("").keyup(function(){
        var val = $(this).val();
        $(".filterable tr:hidden").show();
        if(val.replace(/\s/g, '') == ''){
            return;
        }
        var s = val.toLowerCase().split(" ");
        $.each(s, function(index, value){
            $(".filterable tr:visible .indexColumn:not(:contains('" + value + "'))").parent().hide();
        });
    });
    $("#createProduct").click(function(){
        var data = ["", "", "", "", 0];
        showDialog(data, "Create Product", "Add", null);
    });
    initButtonClickActions();

});