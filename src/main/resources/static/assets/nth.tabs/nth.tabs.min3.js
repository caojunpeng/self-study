(function(a) {
    a.fn.nthTabs = function(c) {
        var h = this;
        var g = {
            allowClose: true,
            active: false,
            rollWidth: h.width() - 120,
        };
        var e = a.extend({}, g, c);
        var d = '<div class="page-tabs"><a href="#" class="roll-nav roll-nav-left"><span class="fa fa-backward"></span></a><div class="content-tabs"><div class="content-tabs-container"><ul class="nav nav-tabs"></ul></div></div><a href="#" class="roll-nav roll-nav-right" style="right:0px;"><span class="fa fa-forward"></span></a></div><div class="tab-content" style="padding:20px"></div>';
        var b = {
            init: function() {
                h.html(d);
                b.listen()
            },
            listen: function() {
                f.onTabClose().onTabRollLeft().onTabRollRight().onTabList().onTabCloseOpt().onTabCloseAll().onTabCloseOther().onLocationTab()
            },
            getAllTabWidth: function() {
                var i = 0;
                h.find(".nav-tabs li").each(function() {
                    i += parseFloat(a(this).width())
                });
                return i
            },
            getMarginStep: function() {
                return e.rollWidth / 2
            },
            getActiveId: function() {
                return h.find('li[class="active"]').find("a").attr("href").replace("#", "")
            },
            getTabList: function() {
                var i = [];
                h.find(".nav-tabs li a").each(function() {
                    i.push({
                        id: a(this).attr("href"),
                        title: a(this).children("span").html()
                    })
                });
                return i
            },
            addTab: function(j) {
                var k = [];
                var m = j.active == undefined ? e.active : j.active;
                var i = j.allowClose == undefined ? e.allowClose : j.allowClose;
                m = m ? "active" : "";
                k.push('<li role="presentation" class="' + m + '">');
                k.push('<a href="#' + j.id + '" data-toggle="tab">');
                k.push("<span>" + j.title + "</span>");
                i ? k.push('<i class="fa fa-close tab-close"></i>') : "";
                k.push("</a>");
                k.push("</li>");
                h.find(".nav-tabs").append(k.join(""));
                var l = [];
                l.push('<div class="tab-pane ' + m + '" id="' + j.id + '">');
                l.push(j.content);
                l.push("</div>");
                h.find(".tab-content").append(l.join(""));
                return b
            },
            locationTab: function(j) {
                j = j == undefined ? b.getActiveId() : j;
                j = j.indexOf("#") > -1 ? j : "#" + j;
                var m = h.find("[href='" + j + "']");
                var l = 0;
                m.parent().prevAll().each(function() {
                    l += a(this).width()
                });
                var i = m.parent().parent().parent();
                if (l <= e.rollWidth * 0.7) {
                    margin_left_total = 40
                } else {
                    if (l <= e.rollWidth) {
                        margin_left_total = 40 - e.rollWidth / 2
                    } else {
                        var k = l + m.parent().width() - (Math.floor(l / e.rollWidth) * e.rollWidth);
                        if (k <= e.rollWidth * 0.7) {
                            margin_left_total = 40 - Math.floor(l / e.rollWidth) * e.rollWidth
                        } else {
                            margin_left_total = 40 - Math.floor(l / e.rollWidth) * e.rollWidth - e.rollWidth / 2
                        }
                    }
                }
                i.css("margin-left", margin_left_total);
                return b
            },
            delTab: function(j) {
            	
            	/*var arrfind=new Array();*/
                j = j == undefined ? b.getActiveId() : j;
                j = j.indexOf("#") > -1 ? j : "#" + j;
                var k = h.find("[href='" + j + "']");
                if (k.parent().attr("class") == "active") {
                	
                    var i = k.parent().next();
                    /*var l = a(j).next();*/
                    if (i.length < 1) {
                        i = k.parent().prev();
                        /*l = a(j).prev()*/
                    }
                    i.addClass("active");
                   /* l.addClass("active")*/
                }
               
                var arrfind = utils.removeByValueas(localStorage.getItem('arrfind').split(','),j.split('#')[1]);
                
         		 utils.refreshPersonCollectsArrayas(arrfind);//去除批量中id
                k.parent().remove();
               /* a(j).remove();*/
                var id;
                var friendIds=new Array();
                for(var c=0;c<$('.nav-tabs>li').length;c++){
                	
                	if($('.nav-tabs>li').eq(c).hasClass('active')){

                		 id=$('.nav-tabs>li').eq(c).children().attr("href").split('#')[1];
                		friendIds[0]=$('.nav-tabs>li').eq(c).children().attr("href").split('#')[1]
                	}
                }
                $("#friendIds").attr("value",friendIds);
            	friendIds.length=0;
            	if(id==localStorage.getItem('namesgi')){
            		
            		return false;
            	}else{
            		
            		
            		$("#personimGroupmsgWeixintables").DataTable().draw();
            	}
            	$('.chat-discussion>.chat2').remove();
            	$('.chat-discussion>.chat1').remove();
            	localStorage.setItem('namesgi',id);
            	
                return b
            },
            delOtherTab: function() {
                h.find(".nav-tabs li").not('[class="active"]').remove();
                h.find(".tab-content div").not('[class="tab-pane active"]').remove();
                return b
            },
            delAllTab: function() {
                h.find(".nav-tabs li").remove();
                h.find(".tab-content div").remove();
                return b
            },
            setActTab: function(i) {
            	
                i = i == undefined ? b.getActiveId() : i;
                i = i.indexOf("#") > -1 ? i : "#" + i;
                h.find(".active").removeClass("active");
                h.find("[href='" + i + "']").parent().addClass("active");
                /*h.find(i).addClass("active");*/
               
                var friendIds=new Array(); 
                $('.nav-tabs>li>a').unbind('click').click(function () {
            		
            		$(this).parent().addClass('active').siblings().removeClass('active');
            		$('.loader').fadeIn();
    	            $('.allbgmain').fadeIn();
          		   friendIds[0]=$(this).attr("href").split('#')[1];
          		   
          		  $("#friendIds").attr("value",friendIds);
          		   localStorage.setItem('scorrl',1);
          		 localStorage.setItem('namesgi',null);
                	friendIds.length=0;
                	$("#personimGroupmsgWeixintables").DataTable().draw();
                	$('.chat-discussion>.chat2').remove();
                	   $('.chat-discussion>.chat1').remove();
                	   var codefalse=function(){
                		   $('.loader').fadeOut();
                		     $('.allbgmain').fadeOut();
                	   }
                	   setTimeout(codefalse,1000);
          	     })
          	     
          	   
                return b
            },
        };
        var f = {
            onLocationTab: function() {
            	
                h.on("click", ".tab-location", function() {
                    b.locationTab()
                });
                return f
            },
            onTabClose: function() {
                h.on("click", ".tab-close", function() {
                    var i = a(this).parent().attr("href");
                    b.delTab(i)
                });
                return f
            },
            onTabCloseOpt: function() {
                h.on("click", ".tab-close-current", function() {
                    b.delTab()
                });
                return f
            },
            onTabCloseOther: function() {
                h.on("click", ".tab-close-other", function() {
                    b.delOtherTab()
                });
                return f
            },
            onTabCloseAll: function() {
                h.on("click", ".tab-close-all", function() {
                    b.delAllTab()
                });
                return f
            },
            onTabRollLeft: function() {
                h.on("click", ".roll-nav-left", function() {
                    if (b.getAllTabWidth() <= e.rollWidth) {
                        return false
                    }
                    var i = a(this).parent().find(".content-tabs-container");
                    var j = i.css("marginLeft").replace("px", "");
                    var k = parseFloat(j) + b.getMarginStep() + 40;
                    i.css("margin-left", k > 40 ? 40 : k)
                });
                return f
            },
            onTabRollRight: function() {
                h.on("click", ".roll-nav-right", function() {
                    if (b.getAllTabWidth() <= e.rollWidth) {
                        return false
                    }
                    var i = a(this).parent().find(".content-tabs-container");
                    var j = i.css("marginLeft").replace("px", "");
                    var k = parseFloat(j) - b.getMarginStep();
                    if (b.getAllTabWidth() - Math.abs(j) <= e.rollWidth) {
                        return false
                    }
                    i.css("margin-left", k)
                });
                return f
            },
            onTabList: function() {
                h.on("click", ".right-nav-list", function() {
                    var i = b.getTabList();
                    var j = [];
                    a.each(i, function(k, l) {
                        j.push('<li class="toggle-tab" data-id="' + l.id + '">' + l.title + "</li>")
                    });
                    h.find(".tab-list").html(j.join(""))
                });
                h.find(".tab-list-scrollbar").scrollbar();
                f.onTabListToggle();
                return f
            },
            onTabListToggle: function() {
                h.on("click", ".toggle-tab", function() {
                    var i = a(this).data("id");
                    b.setActTab(i).locationTab(i)
                });
                return f
            }
        };
        b.init();
        return b
    }
}

)(jQuery);
