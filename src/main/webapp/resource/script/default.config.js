/**
 * 通过jquery重写所有js插件的默认值和设置一些属性让页面更好的展示
 */
(function($){

    //删除背景遮罩
    var unmask = function(el){
        //获取元素中的data-backdrop属性，该属性指向这背景遮罩的div id
        var backdrop = $(el.attr("data-backdrop"));
        //如果在元素没有使用背景遮罩，可能在元素的某些地方有。
        if (backdrop.length === 0) {
            backdrop = el;
        }
        //查询出div有元素id的div，全部删除。
        backdrop.find("div[for='"+el.attr("id")+"']").remove();
    };

    //重写ajaxForm的设置
    $.defaultAjaxFormSetting = {
        //重写提交form前的方法，提交前添加一个背景遮罩到form中，挡住所有表单的内容
        beforeSubmit:function(formData, jqForm, options){
            //获取提交前是否要执行某个js方法
            var fn = jqForm.attr("onbeforesubmit");
            //如果没有要执行的方法，或者执行了方法后返回false，将不提交form
            if ($.isNotEmpty(fn) && window[fn](formData, jqForm, options) === false) {
                return false;
            }
            //获取当前form的data-backdrop属性
            var backdrop = $(jqForm.attr("data-backdrop"));
            //如果没有，将添加一个div做背景遮罩
            if (backdrop.length === 0) {
                backdrop = jqForm;
            }

            var mask = $("<div>").addClass("modal-backdrop fade in").attr("for",jqForm.attr("id"));
            backdrop.append(mask);

        },
        //重写提交成功方法，当完成提交后，将该form的背景遮罩div删除
        success:function(responseText, statusText, xhr, form){
            //删除背景遮罩
            unmask(form);
            //如果存在提交完成后要执行某个函数，就去执行该函数
            var fn = form.attr("onsuccess");
            if ($.isNotEmpty(fn)) {
                window[fn](responseText, statusText, xhr, form);
            }
        },
        //重写提交错误方法，当提交错误后，将该form的背景遮罩div删除
        error:function(response, statusText, message, form) {
            //删除背景遮罩
            unmask(form);
            //如果存在提交错误后要执行某个函数，就去执行该函数
            var fn = form.attr("onerror");
            if ($.isNotEmpty(fn)) {
                window[fn](response, statusText, message, form);
            }
        }
    };

    //初始化所有bootstrap控件，当document ready时
    var initComponent = function(){

        setInterval(function(){
            var date = new Date();
            $("#date").text(date.toLocaleString());
        },100);

        //初始化bootbox,设置语言为中文
        bootbox.setDefaults({
            locale:"zh_CN"
        });

        //如果存在modal控件，当展示modal时将焦点放到form表单的第一个文本域中
        $(".modal").on("shown.bs.modal",function(){
            $(this).find("form :input:not(:disabled):not(:hidden)").first().focus();
        });
        //如果存在tree控件,初始化tree控件
        $(".tree").tree();
        //如果存在form表单，将定义自己的form表单属性
        //当一个form的install属性没有，或者为false时，最做each下面的动作
        $("form[install!='true']").each(function(i,o){
            var temp = $(o);
            //将交单放到表单的第一个文本域中
            temp.find("input:not(:disabled):not(:hidden)").first().focus();
            //判断是否有id，如果没有id将会用action的内容，做转换,如aciont="a/b/c"那么id为"a-b-c"
            var id = temp.attr("id") || temp.attr("action").substring(temp.attr("action").indexOf("/") + 1,temp.attr("action").length);
            temp.attr("id",id.replace(/\//g,"-"));
            //判断该form是否需要ajax提交。如果是，将使用submitHandler方式进行验证，否则就按常规去做验证
            if ($.value(temp.attr("data-ajax-submit"),"false").booleanValue()) {
                temp.validate({
                    submitHandler : function(form) {
                        temp.ajaxSubmit($.defaultAjaxFormSetting);
                    }
                });
            } else {
                temp.validate();
            }

            //如果该form存在reset按钮，将自己去reset这一切
            temp.find("button[type='reset']").click(function(){

                temp.find("input:not(:disabled):not(:hidden)").each(function(i,field){
                    $(field).val("");
                });

                temp.find("select:not(:disabled):not(:hidden)").each(function(i,field){
                    $(field).val("");
                });

                temp.find("textarea:not(:disabled):not(:hidden)").each(function(i,field){
                    $(field).val("");
                });

                return false;
            });

            temp.attr("install",true);
        });
    };

    /*
     * Translated default messages for the jQuery validation plugin.
     * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
     */
    $.extend($.validator.messages, {
        required: "必须填写",
        remote: "该值已被占用",
        email: "请输入有效的电子邮件",
        url: "请输入有效的网址",
        date: "请输入有效的日期",
        dateISO: "请输入有效的日期 (YYYY-MM-DD)",
        number: "请输入正确的数字",
        digits: "只可输入数字",
        creditcard: "请输入有效的信用卡号码",
        equalTo: "你的输入不相同",
        extension: "请输入有效的后缀",
        maxlength: $.validator.format("最多 {0} 个字"),
        minlength: $.validator.format("最少 {0} 个字"),
        rangelength: $.validator.format("请输入长度为 {0} 至 {1} 之間的字串"),
        range: $.validator.format("请输入 {0} 至 {1} 之间的数值"),
        max: $.validator.format("请输入不大于 {0} 的数值"),
        min: $.validator.format("请输入不小于 {0} 的数值")
    });

    //重写验证控件的展示错误
    $.validator.setDefaults({
        //重写验证出错函数
        showErrors : function(errorMap, errorList) {
            //如果存在验证成功的集合，将删除has-error和tooltop
            $.each(this.successList,function(i,e){
                var element = $(e);
                element.parent("div").removeClass("has-error");
                element.tooltip("destroy");
            });
            //如果存在错误字段，将做each下的动作
            $.each(errorList, function(i, e) {
                //获取当前错误的元素
                var element = $(e.element);
                //获取当前元素对应的tooltip
                var tip = element.data('bs.tooltip');
                //如果没有，将创建一个tooltip,如果存在了，就直接改变tooltip的html内容
                if ($.isNotEmpty(tip)) {
                    tip.options.title = e.message;
                    //设置tooltip的信息
                    element.next().children(".tooltip-inner").html(e.message);
                } else {
                    element.parent("div").addClass("has-error");
                    element.tooltip({
                        title:e.message,
                        trigger:"focus"
                    });
                }
            });
        }
    });

    $(document).ready(function(){
        //判断浏览器版本，如果太低，就不能做任何事情
        if ($.browser.msie && $.browser.version < 8) {
            $(".main-container").html(
                '<div class="server-exception"><center>' +
                '<p><img class="" src="data:image/png;base64,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"></p>' +
                '<p>&nbsp;</p>' +
                '<h3>浏览器不兼容</h3>' +
                '<p>您的刘浏览器版本过低，<a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie">请升级IE到最新版本</a></p>' +
                '</center></div>'
            );
        } else {
            initComponent();
        }
    });

})(jQuery);
