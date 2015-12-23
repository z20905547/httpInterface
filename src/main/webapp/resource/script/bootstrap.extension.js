/*
 * 扩展bootstrap的js
 */
(function($) {

    // -----------------------------------树控件开始-----------------------------------//

    var addGlyphicon = function(o,autoExpand) {
        var element = $(o);
        var node = element.find("> .node");
        var a = $("<a>").addClass("btn btn-default btn-sm tree-node-button");
        var icon = $("<span>").addClass("glyphicon");
        a.append(icon).after(" ");

        if (element.find("ul").length > 0) {
            if (autoExpand) {
                icon.addClass("glyphicon-minus-sign");
            } else {
                icon.addClass("glyphicon-plus-sign");
                element.find("> ul").hide();
            }
        } else {
            icon.addClass("glyphicon-leaf");
            a.addClass("disabled");
        }

        node.before(a);

        $.each(element.find("> ul > li"),function(i,o){
            addGlyphicon(o,autoExpand);
        });
    };

    var Tree = function(element, options) {
        this.$element = $(element);
        this.options = $.extend({}, Tree.DEFAULTS, options);
        var ul = this.$element.find("> ul");
        $.each(ul.find("> li"),function(i,o){
            addGlyphicon(o,options.autoExpand);
        });
    };

    Tree.DEFAULTS = {
        autoExpand : true,
        cascade: false,
        queryDefer:500
    };

    Tree.prototype.query = function(q) {
        var queryContainer = this.$element.find("ul.filter-result");
        queryContainer.empty();
        var li = this.$element.find("li");

        if ($.isEmpty(q)) {
            li.show();
            queryContainer.empty();
            return ;
        }

        li.hide();

        var result = li.find(".node:contains('"+q+"')").parent().clone();
        if (queryContainer.length == 0) {
            queryContainer = $("<ul>").addClass("filter-result");
            this.$element.append(queryContainer);
        }
        result.find(".tree-node-button").addClass("disabled").find("span").attr("class","glyphicon glyphicon-leaf");
        queryContainer.append(result.show());
    };

    Tree.prototype.expandAll = function() {
        this.$element.find("ul:hidden").show();
        this.$element.find("> ul > li a.tree-node-button span").addClass("glyphicon-minus-sign").removeClass("glyphicon-plus-sign");
    };

    Tree.prototype.collapseAll = function() {
        this.$element.find("> ul > li ul:not(:hidden)").hide();
        this.$element.find("> ul > li a.tree-node-button span").addClass("glyphicon-plus-sign").removeClass("glyphicon-minus-sign");
    };

    Tree.prototype.checkAll = function() {
        this.$element.find(".node input[type='checkbox']").prop("checked",true);
    };

    Tree.prototype.uncheckAll = function() {
        this.$element.find(".node input[type='checkbox']").prop("checked",false);
    }

    var old = $.fn.tree;

    $.fn.tree = function(option) {
        return this.each(function() {
            var $this = $(this);
            var data = $this.data("bs.tree");
            var options = $.extend({}, Tree.DEFAULTS, $this.data(), typeof option == "object" && option);

            if (!data) $this.data("bs.tree", (data = new Tree(this, options)));
        });
    };

    $.fn.tree.Constructor = Tree;

    $.fn.tree.noConflict = function() {
        $.fn.tree = old;
        return this;
    };

    $(document).on("click.bs.tree.node",".tree .tree-node-button",function(e) {
        var target = $(e.currentTarget);
        var children = target.parent("li").find("> ul");
        if (!children.is(":hidden")) {
            children.hide();
            $(this).find("> span").addClass("glyphicon-plus-sign").removeClass("glyphicon-minus-sign");
        } else {
            children.show();
            $(this).find("> span").addClass("glyphicon-minus-sign").removeClass("glyphicon-plus-sign");
        }
        e.stopPropagation();
    });

    $(document).on("click.bs.tree.checkbox",".tree .node input[type='checkbox']",function(e) {
        var target = $(e.currentTarget);
        var data = target.parents(".tree").data('bs.tree');
        if (target.is(":checked") && data.options.cascade) {
            var parents = target.parents("li");
            for ( var i = 1; i < parents.length; i++) {
                $(parents.get(i)).find(" > .node input[type='checkbox']").prop("checked", true);
            }
            var li = $(target.parents("li").get(0));
            li.find("ul li .node input[type='checkbox']").prop("checked", true);
        }
        if (!target.is(":checked") && data.options.cascade) {
            var li = $(target.parents("li").get(0));
            li.find("ul li .node input[type='checkbox']").prop("checked", false);
        }
        e.stopPropagation();
    });

    $(document).on("keyup.bs.tree.filter", ".tree input[data-toggle='filter']",function(e) {
        var target = $(e.currentTarget);
        var query = target.val();
        var data = target.parents(".tree").data('bs.tree');
        data.query.defer(data.options.queryDefer, data, [query]);

        e.stopPropagation();
    });

    $(document).on("click.bs.tree.expandAll", ".tree button[data-toggle='expandAll']",function(e) {
        var target = $(e.currentTarget);
        var data = target.parents(".tree").data('bs.tree');
        data.expandAll();
        e.stopPropagation();
    });

    $(document).on("click.bs.tree.collapseAll", ".tree button[data-toggle='collapseAll']",function(e) {
        var target = $(e.currentTarget);
        var data = target.parents(".tree").data('bs.tree');
        data.collapseAll();
        e.stopPropagation();
    });

    $(document).on("click.bs.tree.checkAll", ".tree button[data-toggle='checkAll']",function(e) {
        var target = $(e.currentTarget);
        var data = target.parents(".tree").data('bs.tree');
        data.checkAll();
        e.stopPropagation();
    });

    $(document).on("click.bs.tree.uncheckedAll", ".tree button[data-toggle='uncheckedAll']",function(e) {
        var target = $(e.currentTarget);
        var data = target.parents(".tree").data('bs.tree');
        data.uncheckAll();
        e.stopPropagation();
    });

    //-----------------------------------树控件结束-----------------------------------//

})(jQuery);