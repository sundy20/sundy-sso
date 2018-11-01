//$(function () {
/*二级菜单*/
$('#navDrop').hover(function () {
    $(this).find('.nav-item').stop(true).slideDown(600);
}, function () {
    $(this).find('.nav-item').stop(true).slideUp(600);
});
/*子项列表*/
$('#navDrop .nav-item>li').hover(function () {
    $(this).addClass('active').siblings().removeClass('active')
});

$('.scroll-absolute').on('click', function () {
    $(this).parents('.scroll-contain').toggleClass('abs');
});
//var massages_01 = $.ajax({cache: false,url: "http://192.168.1.149:10600/rest/marketIndex/getMarketIndexs?names=",type: "get",dataType: "json"});
//    滚动数据的调用
var massages_01 = $.ajax({
    cache: false,
    url: "/rest/marketIndex/getMarketIndexs?names=SHMETX",
    type: "get",
    dataType: "json"
});

var massages_02 = $.ajax({
    cache: false,
    url: "/rest/marketIndex/getMarketIndexs?names=Shibor(O/N),Shibor(1W),Shibor(1M),Shibor(3M)",
    type: "get",
    dataType: "json"
});

var massages_06 = $.ajax({
    cache: false,
    url: "/rest/marketIndex/getMarketIndexs?names=Libor(O/N),Libor(1W),Libor(1M),Libor(3M)",
    type: "get",
    dataType: "json"
});

var massages_10 = $.ajax({
    cache: false,
    url: "/rest/marketIndex/getMarketIndexs?names=Hibor(O/N),Hibor(1W),Hibor(1M),Hibor(3M)",
    type: "get",
    dataType: "json"
});

var massages_14 = $.ajax({
    cache: false,
    url: "/rest/marketIndex/getMarketIndexs?names=BDI",
    type: "get",
    dataType: "json"
});

/*获取列表*/
massages_01.then(function (res) {
        if (res.code === '000000') {
            for (var i = 0; i < res.data.length; i++) {
                var kaitem = $("#scroll_li_01").html();
                var kdata = {}
                kdata = res.data[i];
                var conReg = /(?:\{)(\w*)(?:\})/g;
                var source = kaitem.replace(conReg, function (node, key) {
                    return kdata[key];
                });
                $("#scroll_no1").append(source);
                if (kdata.updown < 0) {
                    $('.scroll_01 li').eq(i).find('span.change_nums').addClass('text-down down');
                    $('.scroll_01 li').eq(i).find('span.change-up').addClass('text-down ');
                }
                ;
                //动画调用你
                //scrollTitle($('.scroll_01'),3000);
            }
        }
    },
    function () {
        //alert_msg("获取列表信息异常，请刷新重试")
    });
massages_02.then(function (res) {
        if (res.code === '000000') {
            for (var i = 0; i < res.data.length; i++) {
                var kaitem = $("#scroll_li_02").html();
                var kdata = {}
                kdata = res.data[i];
                var conReg = /(?:\{)(\w*)(?:\})/g;
                var source = kaitem.replace(conReg, function (node, key) {
                    return kdata[key];
                });
                $("#scroll_no2").append(source);
                if (kdata.updown < 0) {
                    $('.scroll_02 li').eq(i).find('span.change_num').addClass('text-down ');
                    $('.scroll_02 li').eq(i).find('span.change-up').addClass('text-down down');
                }
                ;
                //动画调用你
                //scrollTitle($('.scroll_02'));
            }
        }
    },
    function () {
        //alert_msg("获取列表信息异常，请刷新重试")
    });
massages_06.then(function (res) {
        if (res.code === '000000') {
            for (var i = 0; i < res.data.length; i++) {
                var kaitem = $("#scroll_li_03").html();
                var kdata = {}
                kdata = res.data[i];
                var conReg = /(?:\{)(\w*)(?:\})/g;
                var source = kaitem.replace(conReg, function (node, key) {
                    return kdata[key];
                });
                $("#scroll_no3").append(source);
                if (kdata.updown < 0) {
                    $('.scroll_03 li').eq(i).find('span.change_num').addClass('text-down ');
                    $('.scroll_03 li').eq(i).find('span.change-up').addClass('text-down down');
                }
                ;
                //动画调用你
                //scrollTitle($('.scroll_03'));
            }
        }
    },
    function () {
        //alert_msg("获取列表信息异常，请刷新重试")
    });

massages_10.then(function (res) {
        if (res.code === '000000') {
            for (var i = 0; i < res.data.length; i++) {
                var kaitem = $("#scroll_li_04").html();
                var kdata = {}
                kdata = res.data[i];
                var conReg = /(?:\{)(\w*)(?:\})/g;
                var source = kaitem.replace(conReg, function (node, key) {
                    return kdata[key];
                });
                $("#scroll_no4").append(source);
                if (kdata.updown < 0) {
                    $('.scroll_04 li').eq(i).find('span.change_num').addClass('text-down ');
                    $('.scroll_04 li').eq(i).find('span.change-up').addClass('text-down down');
                }
                ;
                //动画调用你
                //scrollTitle($('.scroll_04'));
            }
        }
    },
    function () {
        //alert_msg("获取列表信息异常，请刷新重试")
    });
massages_14.then(function (res) {
        if (res.code === '000000') {
            for (var i = 0; i < res.data.length; i++) {
                var kaitem = $("#scroll_li_05").html();
                var kdata = {}
                kdata = res.data[i];
                var conReg = /(?:\{)(\w*)(?:\})/g;
                var source = kaitem.replace(conReg, function (node, key) {
                    return kdata[key];
                });
                $("#scroll_no5").append(source);
                if (kdata.updown < 0) {
                    $('.scroll_05 li').eq(i).find('span.change_nums').addClass('text-down down');
                    $('.scroll_05 li').eq(i).find('span.change-up').addClass('text-down ');
                }
                ;
            }
        }
    },
    function () {
        //alert_msg("获取列表信息异常，请刷新重试")
    });
//}
$(function () {
    /*setTimeout(function(){
     scrollTitle($('.scroll_02'),3000);
     scrollTitle($('.scroll_03'),3200);
     scrollTitle($('.scroll_04'),3300);
     },500);*/

//    公用头部的nav高亮问题
    var nav_active = $('body').attr('data_nav');
    $('#nav_01').removeClass('active');
    $('#' + nav_active + '').addClass('active');
//
});
//    一键返回顶部
$(window).scroll(function () {
    var height = $(window).scrollTop();
    if (height > 500) {
        $('.footer .fixed_right').css('display', 'block');
    } else {
        $('.footer .fixed_right').css('display', 'none');
    }
})
$('.footer .fixed_right').click(function () {
    $('body,html').animate({scrollTop: 0}, 500);
});
