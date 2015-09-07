/*
 * Async Treeview 0.1 - Lazy-loading extension for Treeview
 * 
 * http://bassistance.de/jquery-plugins/jquery-plugin-treeview/
 *
 * Copyright (c) 2007 Jörn Zaefferer
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id$
 *
 */
// NOTE: this is updated Treeview async functionality with changes for our project
;(function($) {
    function load(settings, root, child, container) {
        function createNode(parent) {
            // --------------------------------------------------------------------
            var content ="<span class='{2}' key='{0}'>{1}</span><div style='width: 100px;display: inline-block;padding-left:10px;'>";
            var deleteClass = "deleteProduct";
            if(this.type == 'CATEGORY'){
                content += "<a class='btn btn-mini btn-info editCategory'><i class='icon-edit'></i></a>&nbsp;";
                deleteClass = "deleteCategory"
            }
            content += "<a class='btn btn-mini btn-danger " + deleteClass + "' key='{0}'><i class='icon-remove'></i></a></div>";
            var current = $("<li/>").attr("id", this.id || "").html(content.format(this.id, this.text, this.type == 'CATEGORY' ? "folder" : "file")).appendTo(parent);
            // --------------------------------------------------------------------
            if (this.classes) {
                current.children("span").addClass(this.classes);
            }
            if (this.expanded) {
                current.addClass("open");
            }
            if (this.hasChildren || this.children && this.children.length) {
                var branch = $("<ul/>").appendTo(current);
                if (this.hasChildren) {
                    current.addClass("hasChildren");
                    createNode.call({
                        classes: "placeholder",
                        text: "&nbsp;",
                        children:[]
                    }, branch);
                }
                if (this.children && this.children.length) {
                    $.each(this.children, createNode, [branch])
                }
            }
        }
        $.ajax($.extend(true, {
            url: settings.url,
            data: {
                categoryId: root
            },
            success: function(response) {
                var data = $.parseJSON(response);
                child.empty();
                $.each(data, createNode, [child]);
                $(container).treeview({add: child});
                initCategoriesActions();
                initCategoryButtonClickActions(child);
                initProductButtonClickActions(child);
            }
        }, settings.ajax));
    }

    var proxied = $.fn.treeview;
    $.fn.treeview = function(settings) {
        if (!settings.url) {
            return proxied.apply(this, arguments);
        }
        var container = this;
        if (!container.children().size())
            load(settings, null, this, container);
        var userToggle = settings.toggle;
        return proxied.call(this, $.extend({}, settings, {
            collapsed: true,
            toggle: function() {
                var $this = $(this);
                if ($this.hasClass("hasChildren")) {
                    var childList = $this.removeClass("hasChildren").find("ul");
                    load(settings, this.id, childList, container);
                }
                if (userToggle) {
                    userToggle.apply(this, arguments);
                }
            }
        }));
    };

})(jQuery);