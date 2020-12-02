/**
 * nth-tabs
 * author:nethuige
 * version:1.0
 */
(function ($) {
    $.fn.nthTabs = function(options){
        //插件中的40为默认左边距
        var nthTabs = this;
        var defaults = {
            allowClose:true, //添加选项卡时是否允许关闭,默认值：是
            active:false, //添加选项卡时是否为活动状态,默认值：否
            rollWidth:nthTabs.width()-120, //可滚动的区域宽度，120即3个操作按钮的宽度
        };
        var settings = $.extend({},defaults,options);
        var template = '<div class="page-tabs"><a href="#" class="roll-nav roll-nav-left"><span class="fa fa-backward"></span></a><div class="content-tabs"><div class="content-tabs-container"><ul class="nav nav-tabs profile-navtabs"></ul></div></div><a href="#" class="roll-nav roll-nav-right"><span class="fa fa-forward"></span></a><div class="dropdown roll-nav right-nav-list"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="fa fa-chevron-down"></span></a><ul class="dropdown-menu"><li><a href="#" class="tab-location">定位当前选项卡</a></li><li><a href="#" class="tab-close-current">关闭当前选项卡</a></li><li role="separator" class="divider"></li><li><a href="#" class="tab-close-other">关闭其他选项卡</a></li><li><a href="#" class="tab-close-all">关闭全部选项卡</a></li><li role="separator" class="divider"></li><li class="scrollbar-outer tab-list-scrollbar"><div class="tab-list-container"><ul class="tab-list"></ul></div></li></ul></div></div><div class="tab-content">' +
            '<div class="panelcontent" id="conparentecharts1"></div>' +
            '<div class="panelcontent" id="conparentecharts2"></div>' +
            '<div class="panelcontent" id="conparentecharts3"></div>' +
            '<div class="panelcontent" id="conparentecharts4"></div>' +
            '<div class="panelcontent" id="conparentecharts5"></div>' +
            '<div class="panelcontent" id="conparentecharts6"></div>' +
            '</div>';
        //各种api
        var methods = {
            //初始化
            init:function () {
                nthTabs.html(template);
                methods.listen();
            },
            //事件监听
            listen:function(){
                event.onTabClose().onTabRollLeft().onTabRollRight().onTabList()
                    .onTabCloseOpt().onTabCloseAll().onTabCloseOther().onLocationTab();
            },
            //获取所有tab宽度
            getAllTabWidth:function(){
                var sum_width = 0;
                nthTabs.find('.profile-navtabs li').each(function(){
                    sum_width+=parseFloat($(this).width());
                });
                return sum_width;
            },
            //获取左右滑动步值
            getMarginStep:function () {
                return settings.rollWidth/2;
            },
            //获取当前活动状态选项卡ID
            getActiveId:function(){
                return nthTabs.find('li[class="active"]').find("a").attr("href").replace('#','');
            },
            //获取所有选项卡
            getTabList:function(){
                var tablist = [];
                nthTabs.find('.profile-navtabs li a').each(function(){
                    tablist.push({id:$(this).attr('href'),title:$(this).children('span').html()});
                });
                return tablist;
            },
            //添加一个选项卡
            addTab:function (options) {
                //nav-tab

                var tab = [];
                var active = options.active==undefined ? settings.active : options.active;
                var allowClose = options.allowClose==undefined ? settings.allowClose : options.allowClose;
                active = active ? 'active':'';
                tab.push('<li role="presentation" class="'+active+'">');
                var optionid = options.id;
                if(optionid.indexOf("parentecharts")!=-1){
                    tab.push('<a title="'+options.title+'" href="#con'+options.id+'" data-toggle="tab">');
                }else{
                    tab.push('<a title="'+options.title+'" href="#'+options.id+'" data-toggle="tab">');
                }
                tab.push('<span>'+options.title+'</span>');
                allowClose ? tab.push('<i class="fa fa-close tab-close"></i>'):'';
                tab.push('</a>');
                tab.push('</li>');
                nthTabs.find(".profile-navtabs").append(tab.join(''));
                //tab-content
                /*var tabContent = [];
                tabContent.push('<div class="tab-pane '+active+'" id="'+options.id+'">');
                tabContent.push(options.content);
                tabContent.push('</div>');
                nthTabs.find(".tab-content").append(tabContent.join(''));*/
                return methods;
            },
            //定位选项卡
            locationTab:function(tabId){

                tabId = tabId==undefined ? methods.getActiveId() : tabId;
                tabId = tabId.indexOf('#')>-1 ? tabId : '#'+tabId;
                var navTabA = nthTabs.find("[href='"+tabId+"']"); //当前活动标签对象
                //计算存在于当前活动选项卡之前的所有同级选项卡的宽度之和
                var width = 0;
                navTabA.parent().prevAll().each(function () {
                    width+=$(this).width();
                });
                //得到选项卡容器对象
                var contentTab = navTabA.parent().parent().parent();
                //情况1：之前同级选项卡宽度之和小于选项卡可视区域的70%则不做任何margin处理
                if(width<=settings.rollWidth*0.7){
                    margin_left_total = 40;
                }
                //情况2：之前同级选项卡宽度之和大于选项卡可视区域的70%且小于等于选项卡可视区域的则margin为可视区域的一半,使其更显眼
                else if(width<=settings.rollWidth){
                    margin_left_total = 40-settings.rollWidth/2;
                }
                //情况3：算法：a:当前活动选项卡的所有前面的同级选项卡的宽度之和；b:当前活动选项卡的宽度；c:选项卡可视区域的宽度；
                //即当前活动选项卡相对选项卡可视区域宽度的(余宽) = a+b-Math.floor(a/c)*c
                else{
                    var remaining = width+navTabA.parent().width()-(Math.floor(width/settings.rollWidth)*settings.rollWidth);
                    if(remaining<=settings.rollWidth*0.7){
                        //余宽小于选项卡可视区域宽度70%则边距为可视区域宽度的整数倍
                        margin_left_total = 40-Math.floor(width/settings.rollWidth)*settings.rollWidth;
                    }else{
                        //余宽大于选项卡可视区域宽度70%则边距为可视区域宽度的整数倍外加一半的边距，保证活动选项卡可视效果最佳
                        margin_left_total = 40-Math.floor(width/settings.rollWidth)*settings.rollWidth-settings.rollWidth/2;
                    }
                }
                contentTab.css("margin-left",margin_left_total);
                return methods;
            },
            //删除单个选项卡
            delTab:function (tabId) {

            	tabId = tabId == undefined ? methods.getActiveId() : tabId;
                tabId = tabId.indexOf('#')>-1 ? tabId : '#'+tabId;
                var navTabA = nthTabs.find("[href='"+tabId+"']");
                //如果关闭的是激活状态的选项卡
                if(navTabA.parent().attr('class')=='active'){
                    //激活选项卡，如果后面存在激活后面，否则激活前面
                    var activeNavTab = navTabA.parent().next();
                    var activeTabContent = $(tabId).next();
                    if(activeNavTab.length<1){
                        activeNavTab = navTabA.parent().prev();
                        activeTabContent = $(tabId).prev();
                    }
                    activeNavTab.addClass('active');
                    activeTabContent.addClass('active');
                }
                //移除旧选项卡
                navTabA.parent().remove();
                if (tabId.indexOf("parentecharts")!=-1){
                    $("#"+tabId.substring(4)).attr("data-type","tabAdd");
                    $("#"+tabId.substring(4)).attr("data-isopen","");
                }else {
                    $(tabId).attr("data-type","tabAdd");
                }
                
            	nthTabs.find(tabId).html("");
            	
            	if($(".profile-navtabs li").length>0){
                    var prevId;
                	$(".profile-navtabs li").each(function(){
                    	if($(this).hasClass("active")){
                    		prevId = $(this).find("a").attr("href");
                    	}
                    });
                    methods.setActTab(prevId).locationTab(prevId);
                }else{
                    nthTabs.find(".tab-content .panelcontent").html("");
                }

                return methods;
            },
            //删除其他选项卡
            delOtherTab:function(){
                //nthTabs.find(".tab-content div").not('[class="tab-pane active"]').remove();
                //nthTabs.find(".tab-content .panelcontent").not('[style="display: block;"]').html("");
                // nthTabs.find(".tab-content").find(".panelcontent").html("");
            	
                if($(".profile-navtabs li").length>0){
                    var prevId;
                    $(".profile-navtabs li").each(function(){
                        if(!$(this).hasClass("active")){
                            prevId = $(this).find("a").attr("href");
                            if (prevId.indexOf("parentecharts")!=-1){
                                $("#"+prevId.substring(4)).attr("data-type","tabAdd");
                                $("#"+prevId.substring(4)).attr("data-isopen","");
                            }else {
                                $(prevId).attr("data-type","tabAdd");
                            }
                        }
                    })
                }
                nthTabs.find(".profile-navtabs li").not('[class="active"]').remove();
                nthTabs.find(".tab-content .panelcontent").each(function() {
                    if (!$(this).hasClass("active")) {
                        $(this).html("");
                    }
                });

                return methods;
            },
            //删除全部选项卡
            delAllTab:function(){
                if($(".profile-navtabs li").length>0){
                    $(".profile-navtabs li").each(function(){
                        var prevId = $(this).find("a").attr("href");
                        if (prevId.indexOf("parentecharts")!=-1){
                            $("#"+prevId.substring(4)).attr("data-type", "tabAdd");
                            $("#"+prevId.substring(4)).attr("data-isopen", "");
                        }else {
                            $(prevId).attr("data-type","tabAdd");
                        }
                    })
                }

                nthTabs.find(".profile-navtabs li").remove();
                //nthTabs.find(".tab-content div").remove();
                nthTabs.find(".tab-content .panelcontent").html("");

                return methods;
            },
            //切换活动选项卡
            setActTab:function (tabId) {

            	if(tabId.indexOf("#")!=-1){
            		tabId = tabId.split('#')[1];
                    if (tabId.indexOf("parentecharts")!=-1){
                    	tabId = tabId.split('con')[1];
                    }
            	}

                tabId = tabId==undefined ? methods.getActiveId() : tabId;
                tabId = tabId.indexOf('#')>-1 ? tabId : '#'+tabId;
                nthTabs.find('.active').removeClass('active');
                if (tabId.indexOf("parentecharts")!=-1){
                	var tabIdsub = tabId.split('#')[1];
                	nthTabs.find("[href='#con"+tabIdsub+"']").parent().addClass('active');
                }else{
                	nthTabs.find("[href='"+tabId+"']").parent().addClass('active');
                }
                nthTabs.find(tabId).addClass('active');


                
                if (tabId.indexOf("parentecharts")!=-1){
                	
                	var tabIdsub = tabId.split('#')[1];

                    if (tabId.indexOf("parentecharts4")==-1){
                    	
                    	var subboolend = false;
                    	var flagHas =$("#"+tabIdsub).attr("data-isopen")//是否已经打开此标签页
                    	if($("#"+tabIdsub).attr("subType")=="parentecharts1时序网络"){

                    		$.ajax({
                				url:"/profile/groupprofiledensityJson",
                				data:{childrenChart: "parentecharts1时序网络",collectIds:localStorage.getItem('collectIds')},
                				success:function(data){
                	                 if(!subboolend){
                		                layer.open({
                	                        type: 1, //此处以iframe举例
                	                        title: '时序网络详情',
                	                        area: ['80%', '700px'],
                	                        shade: 0,
                	                        maxmin: true,
                	                        content: data,//将结果页面放入layer弹出层中
                	                        cancel: function(index, layero){
                	                            subboolend=false;
                	                        },
                	                        success: function(layero, index){
                	                            indexend=index;
                	                            subboolend=true;
                	                        }
                	                    });
                	                } 
                				}
                			})
                			var html = getData(tabIdsub);
                            $('#con'+tabIdsub).html(html);
                            $("#"+tabIdsub).attr("subType","")
                        }else if (flagHas=="true"&&$("#"+tabIdsub).attr("subType")&&$("#"+tabIdsub).attr("subType")!="parentecharts5身份共享"&&$("#"+tabIdsub).attr("subType")!="parentecharts5文件共享"&&$("#"+tabIdsub).attr("subType")!="parentecharts5快递共享"){

                            $('.panelcontent').each(function(){
                                if($(this).attr("id") == "con"+tabIdsub){
                                	$(this).addClass("active");
                                    $("#childchart-"+tabIdsub.substring(13,14)).show();
                                    var phone = $("#"+tabIdsub).attr("subType");
                                    var subtypename =phone.substring(14);
                                    extracted(phone,subtypename);
                                    var lines = $("#conparentecharts"+tabIdsub.substring(13,14)).find(".menuline");
                                    lines.each(function(){
                                        if($("#"+tabIdsub).attr("subType") == $(this).attr("data-index")){
                                            $(this).addClass("lineactive").siblings(".menuline");
                                        }
                                    });
                                }else{
                                	$(this).removeClass('active');
                                }
                            })
                            $("#"+tabIdsub).attr("subType","");
                            
                        } else if ($("#"+tabIdsub).attr("subType")&&$("#"+tabIdsub).attr("subType")!="parentecharts5身份共享"&&$("#"+tabIdsub).attr("subType")!="parentecharts5文件共享"&&$("#"+tabIdsub).attr("subType")!="parentecharts5快递共享"){
                            var html = getData(tabIdsub);
                            $('#con'+tabIdsub).html(html);
                            $('.panelcontent').each(function(){
                                if($(this).attr("id") == "con"+tabIdsub){
                                	$(this).addClass("active");
                                    $("#childchart-"+tabIdsub.substring(13,14)).show();
                                    var phone = $("#"+tabIdsub).attr("subType");
                                    var subtypename =$("#"+tabIdsub).attr("subType").substring(14);
                                    extracted(phone,subtypename);
                                    var lines = $("#conparentecharts"+tabIdsub.substring(13,14)).find(".menuline");
                                    lines.each(function(){
                                        if($("#"+tabIdsub).attr("subType") == $(this).attr("data-index")){
                                            $(this).addClass("lineactive").siblings(".menuline");
                                        }
                                    });
                                    $("#"+tabIdsub).attr("subType","")
                                }else{
                                	$(this).removeClass('active');
                                }
                            })
                        } else {
                            var html = getData(tabIdsub);
                            $('#con'+tabIdsub).html(html);
                        }

                    } else {
                        var html = getData(tabIdsub);
                        $('#con'+tabIdsub).html(html);
                        $("#"+tabIdsub).attr("subType","")
                    }

                    /*$('.panelcontent').each(function(){
                        if($(this).attr("id") == "con"+tabIdsub){
                            $(this).addClass("active");
                        }else{
                            $(this).removeClass('active');
                        }
                    })*/
                    $("#con"+tabIdsub).addClass("active").siblings().removeClass('active');
                    
                    return methods;
                    
                } else {
                	
                    if($(".profile-navtabs li").length>0){
                        var prevId;
                        $(".profile-navtabs li").each(function(){
                            prevId = $(this).find("a").attr("href");
                            if (tabId!=prevId) {
                                if (prevId.indexOf("specialOpen")!=-1){
                                    $(this).remove();
                                }
                            }
                        })
                    }
                    $('#conparentecharts4').html("")
                    $('#conparentecharts6').html("")
                    var html = getData(tabId.split('#')[1]);
                    $('#conparentecharts6').html(html);
                    $("#conparentecharts6").addClass("active").siblings().removeClass('active');

                    return methods;
                }

            }
        };
        //事件处理
        var event = {
            //定位选项卡
            onLocationTab:function(){
                nthTabs.on("click",'.tab-location',function () {

                    methods.locationTab();
                });
                return event;
            },
            //关闭选项卡按钮
            onTabClose:function () {
                nthTabs.on("click",'.tab-close',function () {
                	
                    var tabId = $(this).parent().attr('href');
                    methods.delTab(tabId);
                    return false;
                });
                return event;
            },
            //关闭当前选项卡操作
            onTabCloseOpt:function(){
                nthTabs.on("click",'.tab-close-current',function () {

                    methods.delTab();
                });
                return event;
            },
            //关闭其他选项卡
            onTabCloseOther:function(){
                nthTabs.on("click",'.tab-close-other',function () {

                    methods.delOtherTab();
                });
                return event;
            },
            //关闭全部选项卡
            onTabCloseAll:function(){
                nthTabs.on("click",'.tab-close-all',function () {

                    methods.delAllTab();
                });
                return event;
            },
            //左滑选项卡
            onTabRollLeft:function () {
                nthTabs.on("click",'.roll-nav-left',function () {

                    if(methods.getAllTabWidth()<=settings.rollWidth)return false; //未超出可视区域宽度,不可滑动
                    var contentTab = $(this).parent().find('.content-tabs-container');
                    var margin_left_origin = contentTab.css('marginLeft').replace('px','');
                    var margin_left_total = parseFloat(margin_left_origin) + methods.getMarginStep()+40;
                    contentTab.css("margin-left",margin_left_total>40 ? 40 : margin_left_total);
                });
                return event;
            },
            //右滑选项卡
            onTabRollRight:function () {
                nthTabs.on("click",'.roll-nav-right',function () {

                    if(methods.getAllTabWidth()<=settings.rollWidth)return false; //未超出可视区域宽度,不可滑动
                    var contentTab = $(this).parent().find('.content-tabs-container');
                    var margin_left_origin = contentTab.css('marginLeft').replace('px','');
                    var margin_left_total = parseFloat(margin_left_origin) - methods.getMarginStep();
                    if(methods.getAllTabWidth()-Math.abs(margin_left_origin)<=settings.rollWidth)return false; //已无隐藏无需滚动
                    contentTab.css("margin-left",margin_left_total);
                });
                return event;
            },
            //选项卡清单
            onTabList:function(){
                nthTabs.on("click",'.right-nav-list',function () {

                    var tablist = methods.getTabList();
                    var html = [];
                    $.each(tablist,function (key,val) {
                        html.push('<li class="toggle-tab" data-id="'+val.id+'">'+val.title+'</li>');
                    });
                    nthTabs.find(".tab-list").html( html.join(''));
                });
                nthTabs.find(".tab-list-scrollbar").scrollbar();
                event.onTabListToggle();
                return event;
            },
            //清单下切换选项卡
            onTabListToggle:function () {
                nthTabs.on("click",'.toggle-tab',function () {

                    var tabId = $(this).data("id");
                    methods.setActTab(tabId).locationTab(tabId);
                });
                return event;
            }
        };
        methods.init();
        return methods;
    }
})(jQuery);