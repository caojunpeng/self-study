(function (a) {
    $.fn.dataTable.ext.errMode = 'none'; //屏蔽Datatables错误弹框
    /**
     * 是否为数字
     */
    utils.isNum = function(para){
        if(/^\d+$/.test(para)){
            return true;
        }else{
            return false;
        }
    }
    utils.text = function(object){
        alert(object);
    }
    /**
     * 是否为全中文
     */
    utils.china = function(para){
        if(!para){
            return false;
        }
        var reg=new RegExp(/^([\u4E00-\u9FA5]+,?)+$/);
        if(reg.test(para)){
            return true;
        }else{
            return false;
        }
    }
	/* table、聊天信息、思维导图右键菜单 */
    document.oncontextmenu=function(e){
    	debugger
        alert("右键菜单");
		return false;
	};
    function formatDateTime(timeStamp) {
        var date = new Date();
        date.setTime(timeStamp );
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
    };
    utils.formatDateTimes = function formatDateTime(timeStamp) {
        var date = new Date();
        date.setTime(timeStamp );
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d;
    };
    utils.caseMonth = function(val){
        var res ;
        switch (val) {
            case (1):res = '一月';break;
            case (2):res = '二月';break;
            case (3):res = '三月';break;
            case (4):res = '四月';break;
            case (5):res = '五月';break;
            case (6):res = '六月';break;
            case (7):res = '七月';break;
            case (8):res = '八月';break;
            case (9):res = '九月';break;
            case (10):res = '十月';break;
            case (11):res = '十一月';break;
            case (12):res = '十二月';break;
            default:res = null;
        }
        return res;
    }
    /**
     * 随机获取颜色  用于时序图
     */
    utils.matchColor = function(){
        var color = new Array("#00CBB5","#0098BE","#FFCC95","#003300","#00336F","#0033F6","#333300","#33335A","#3333A5","#333366"
            ,"#3333CC","#660033","#6600C5","#6600FB","#6600E1","#66009C","#660074","#0080ff","#008104","#0080B0","#008111");
        return color[Math.floor( Math.random() * 20 )];
    }
    //阻止冒泡
    function cancelBubble(){
        var e=getEvent();
        if(window.event){
            //e.returnValue=false;//阻止自身行为
            e.cancelBubble=true;//阻止冒泡
        }else if(e.preventDefault){
            //e.preventDefault();//阻止自身行为
            e.stopPropagation();//阻止冒泡
        }
    }
    //阻止事件向上冒泡
    utils.stopBubble = function(){
    	debugger
        if(event && event.stopPropagation){
        	event.stopPropagation();
        }else{
        	event.cancelBubble = true;
        }
    }
}(window.utils = (window.utils || {})));


