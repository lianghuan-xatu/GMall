
//我的京东下拉
$(".header_wdjd").hover(function() {
    $(this).children(".header_wdjd_txt").stop(true).show(100)
    $(this).css({
        background: "#fff"
    })
}, function() {
    $(this).css({
        background: "#E3E4E5"
    });
    $(this).children(".header_wdjd_txt").stop(true).hide(100)
});
//客户服务下拉
$(".header_wdjd1").hover(function() {
    $(this).children(".header_wdjd_txt").stop(true).show(100);
    $(this).css({
        background: "#fff"
    })
}, function() {
    $(this).css({
        background: "#E3E4E5"
    });
    $(this).children(".header_wdjd_txt").stop(true).hide(100)
});
//网站导航下拉
$(".header_wzdh").hover(function() {
    $(this).children(".header_wzdh_txt").stop(true).show(100);
    $(this).css({
        background: "#fff"
    })
}, function() {
    $(this).css({
        background: "#E3E4E5"
    });
    $(this).children(".header_wzdh_txt").stop(true).hide(100)
});
//手机京东下拉
$(".header_sjjd").hover(function() {
    $(this).children(".header_sjjd_div").stop(true).show(50)

}, function() {
    $(this).children(".header_sjjd_div").stop(true).hide(50)
});
//地理位置下拉
$(".header_head_p_a").hover(function() {
    $(".header_head_p_cs").stop(true).show(100);
    $(this).css({
        background: "#fff"
    })
}, function() {
    $(this).css({
        background: "#E3E4E5"
    });
    $(".header_head_p_cs").stop(true).hide(100)
});
$(".header_head_p_cs").hover(function(){
   $(this).stop(true).show(100);
},function(){
    $(this).stop(true).hide(100)
});
$(".header_head_p_cs a").hover(function(){
    $(this).css({background:"#f0f0f0"});
    $(".header_head_p_cs a:nth-child(1)").css({background:"#c81623"})
},function(){
    $(this).css({background:"#fff"});
    $(".header_head_p_cs a:nth-child(1)").css({background:"#c81623"})
});
//购物车下拉
$('.header_gw').hover(function() {
    $(this).css({background:"#fff"}).next('.header_ko').stop(true).fadeIn(100)
}, function() {
    $(this).css({background:"#f9f9f9"}).next('.header_ko').stop(true).fadeOut(100)
});



