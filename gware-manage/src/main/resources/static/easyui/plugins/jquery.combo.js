/**
 * EasyUI for jQuery 1.5.4.1
 * 
 * Copyright (c) 2009-2018 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
(function($){
$(function(){
$(document).unbind(".combo").bind("mousedown.combo mousewheel.combo",function(e){
var p=$(e.target).closest("span.combo,div.combo-p,div.menu");
if(p.length){
_1(p);
return;
}
$("body>div.combo-p>div.combo-panel:visible").panel("close");
});
});
function _2(_3){
var _4=$.data(_3,"combo");
var _5=_4.options;
if(!_4.panel){
_4.panel=$("<div class=\"combo-panel\"></div>").appendTo("body");
_4.panel.panel({minWidth:_5.panelMinWidth,maxWidth:_5.panelMaxWidth,minHeight:_5.panelMinHeight,maxHeight:_5.panelMaxHeight,doSize:false,closed:true,cls:"combo-p",style:{position:"absolute",zIndex:10},onOpen:function(){
var _6=$(this).panel("options").comboTarget;
var _7=$.data(_6,"combo");
if(_7){
_7.options.onShowPanel.call(_6);
}
},onBeforeClose:function(){
_1($(this).parent());
},onClose:function(){
var _8=$(this).panel("options").comboTarget;
var _9=$(_8).data("combo");
if(_9){
_9.options.onHidePanel.call(_8);
}
}});
}
var _a=$.extend(true,[],_5.icons);
if(_5.hasDownArrow){
_a.push({iconCls:"combo-arrow",handler:function(e){
_10(e.data.target);
}});
}
$(_3).addClass("combo-f").textbox($.extend({},_5,{icons:_a,onChange:function(){
}}));
$(_3).attr("comboName",$(_3).attr("textboxName"));
_4.combo=$(_3).next();
_4.combo.addClass("combo");
_4.panel.unbind(".combo");
for(var _b in _5.panelEvents){
_4.panel.bind(_b+".combo",{target:_3},_5.panelEvents[_b]);
}
};
function _c(_d){
var _e=$.data(_d,"combo");
var _f=_e.options;
var p=_e.panel;
if(p.is(":visible")){
p.panel("close");
}
if(!_f.cloned){
p.panel("destroy");
}
$(_d).textbox("destroy");
};
function _10(_11){
var _12=$.data(_11,"combo").panel;
if(_12.is(":visible")){
var _13=_12.combo("combo");
_14(_13);
if(_13!=_11){
$(_11).combo("showPanel");
}
}else{
var p=$(_11).closest("div.combo-p").children(".combo-panel");
$("div.combo-panel:visible").not(_12).not(p).panel("close");
$(_11).combo("showPanel");
}
$(_11).combo("textbox").focus();
};
function _1(_15){
$(_15).find(".combo-f").each(function(){
var p=$(this).combo("panel");
if(p.is(":visible")){
p.panel("close");
}
});
};
function _16(e){
var _17=e.data.target;
var _18=$.data(_17,"combo");
var _19=_18.options;
if(!_19.editable){
_10(_17);
}else{
var p=$(_17).closest("div.combo-p").children(".combo-panel");
$("div.combo-panel:visible").not(p).each(function(){
var _1a=$(this).combo("combo");
if(_1a!=_17){
_14(_1a);
}
});
}
};
function _1b(e){
var _1c=e.data.target;
var t=$(_1c);
var _1d=t.data("combo");
var _1e=t.combo("options");
_1d.panel.panel("options").comboTarget=_1c;
switch(e.keyCode){
case 38:
_1e.keyHandler.up.call(_1c,e);
break;
case 40:
_1e.keyHandler.down.call(_1c,e);
break;
case 37:
_1e.keyHandler.left.call(_1c,e);
break;
case 39:
_1e.keyHandler.right.call(_1c,e);
break;
case 13:
e.preventDefault();
_1e.keyHandler.enter.call(_1c,e);
return false;
case 9:
case 27:
_14(_1c);
break;
default:
if(_1e.editable){
if(_1d.timer){
clearTimeout(_1d.timer);
}
_1d.timer=setTimeout(function(){
var q=t.combo("getText");
if(_1d.previousText!=q){
_1d.previousText=q;
t.combo("showPanel");
_1e.keyHandler.query.call(_1c,q,e);
t.combo("validate");
}
},_1e.delay);
}
}
};
function _1f(_20){
var _21=$.data(_20,"combo");
var _22=_21.combo;
var _23=_21.panel;
var _24=$(_20).combo("options");
var _25=_23.panel("options");
_25.comboTarget=_20;
if(_25.closed){
_23.panel("panel").show().css({zIndex:($.fn.menu?$.fn.menu.defaults.zIndex++:($.fn.window?$.fn.window.defaults.zIndex++:99)),left:-999999});
_23.panel("resize",{width:(_24.panelWidth?_24.panelWidth:_22._outerWidth()),height:_24.panelHeight});
_23.panel("panel").hide();
_23.panel("open");
}
(function(){
if(_25.comboTarget==_20&&_23.is(":visible")){
_23.panel("move",{left:_26(),top:_27()});
setTimeout(arguments.callee,200);
}
})();
function _26(){
var _28=_22.offset().left;
if(_24.panelAlign=="right"){
_28+=_22._outerWidth()-_23._outerWidth();
}
if(_28+_23._outerWidth()>$(window)._outerWidth()+$(document).scrollLeft()){
_28=$(window)._outerWidth()+$(document).scrollLeft()-_23._outerWidth();
}
if(_28<0){
_28=0;
}
return _28;
};
function _27(){
var top=_22.offset().top+_22._outerHeight();
if(top+_23._outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=_22.offset().top-_23._outerHeight();
}
if(top<$(document).scrollTop()){
top=_22.offset().top+_22._outerHeight();
}
return top;
};
};
function _14(_29){
var _2a=$.data(_29,"combo").panel;
_2a.panel("close");
};
function _2b(_2c,_2d){
var _2e=$.data(_2c,"combo");
var _2f=$(_2c).textbox("getText");
if(_2f!=_2d){
$(_2c).textbox("setText",_2d);
}
_2e.previousText=_2d;
};
function _30(_31){
var _32=$.data(_31,"combo");
var _33=_32.options;
var _34=$(_31).next();
var _35=[];
_34.find(".textbox-value").each(function(){
_35.push($(this).val());
});
if(_33.multivalue){
return _35;
}else{
return _35.length?_35[0].split(_33.separator):_35;
}
};
function _36(_37,_38){
var _39=$.data(_37,"combo");
var _3a=_39.combo;
var _3b=$(_37).combo("options");
if(!$.isArray(_38)){
_38=_38.split(_3b.separator);
}
var _3c=_30(_37);
_3a.find(".textbox-value").remove();
if(_38.length){
if(_3b.multivalue){
for(var i=0;i<_38.length;i++){
_3d(_38[i]);
}
}else{
_3d(_38.join(_3b.separator));
}
}
function _3d(_3e){
var _3f=$(_37).attr("textboxName")||"";
var _40=$("<input type=\"hidden\" class=\"textbox-value\">").appendTo(_3a);
_40.attr("name",_3f);
if(_3b.disabled){
_40.attr("disabled","disabled");
}
_40.val(_3e);
};
var _41=(function(){
if(_3c.length!=_38.length){
return true;
}
for(var i=0;i<_38.length;i++){
if(_38[i]!=_3c[i]){
return true;
}
}
return false;
})();
if(_41){
$(_37).val(_38.join(_3b.separator));
if(_3b.multiple){
_3b.onChange.call(_37,_38,_3c);
}else{
_3b.onChange.call(_37,_38[0],_3c[0]);
}
$(_37).closest("form").trigger("_change",[_37]);
}
};
function _42(_43){
var _44=_30(_43);
return _44[0];
};
function _45(_46,_47){
_36(_46,[_47]);
};
function _48(_49){
var _4a=$.data(_49,"combo").options;
var _4b=_4a.onChange;
_4a.onChange=function(){
};
if(_4a.multiple){
_36(_49,_4a.value?_4a.value:[]);
}else{
_45(_49,_4a.value);
}
_4a.onChange=_4b;
};
$.fn.combo=function(_4c,_4d){
if(typeof _4c=="string"){
var _4e=$.fn.combo.methods[_4c];
if(_4e){
return _4e(this,_4d);
}else{
return this.textbox(_4c,_4d);
}
}
_4c=_4c||{};
return this.each(function(){
var _4f=$.data(this,"combo");
if(_4f){
$.extend(_4f.options,_4c);
if(_4c.value!=undefined){
_4f.options.originalValue=_4c.value;
}
}else{
_4f=$.data(this,"combo",{options:$.extend({},$.fn.combo.defaults,$.fn.combo.parseOptions(this),_4c),previousText:""});
if(_4f.options.multiple&&_4f.options.value==""){
_4f.options.originalValue=[];
}else{
_4f.options.originalValue=_4f.options.value;
}
}
_2(this);
_48(this);
});
};
$.fn.combo.methods={options:function(jq){
var _50=jq.textbox("options");
return $.extend($.data(jq[0],"combo").options,{width:_50.width,height:_50.height,disabled:_50.disabled,readonly:_50.readonly});
},cloneFrom:function(jq,_51){
return jq.each(function(){
$(this).textbox("cloneFrom",_51);
$.data(this,"combo",{options:$.extend(true,{cloned:true},$(_51).combo("options")),combo:$(this).next(),panel:$(_51).combo("panel")});
$(this).addClass("combo-f").attr("comboName",$(this).attr("textboxName"));
});
},combo:function(jq){
return jq.closest(".combo-panel").panel("options").comboTarget;
},panel:function(jq){
return $.data(jq[0],"combo").panel;
},destroy:function(jq){
return jq.each(function(){
_c(this);
});
},showPanel:function(jq){
return jq.each(function(){
_1f(this);
});
},hidePanel:function(jq){
return jq.each(function(){
_14(this);
});
},clear:function(jq){
return jq.each(function(){
$(this).textbox("setText","");
var _52=$.data(this,"combo").options;
if(_52.multiple){
$(this).combo("setValues",[]);
}else{
$(this).combo("setValue","");
}
});
},reset:function(jq){
return jq.each(function(){
var _53=$.data(this,"combo").options;
if(_53.multiple){
$(this).combo("setValues",_53.originalValue);
}else{
$(this).combo("setValue",_53.originalValue);
}
});
},setText:function(jq,_54){
return jq.each(function(){
_2b(this,_54);
});
},getValues:function(jq){
return _30(jq[0]);
},setValues:function(jq,_55){
return jq.each(function(){
_36(this,_55);
});
},getValue:function(jq){
return _42(jq[0]);
},setValue:function(jq,_56){
return jq.each(function(){
_45(this,_56);
});
}};
$.fn.combo.parseOptions=function(_57){
var t=$(_57);
return $.extend({},$.fn.textbox.parseOptions(_57),$.parser.parseOptions(_57,["separator","panelAlign",{panelWidth:"number",hasDownArrow:"boolean",delay:"number",reversed:"boolean",multivalue:"boolean",selectOnNavigation:"boolean"},{panelMinWidth:"number",panelMaxWidth:"number",panelMinHeight:"number",panelMaxHeight:"number"}]),{panelHeight:(t.attr("panelHeight")=="auto"?"auto":parseInt(t.attr("panelHeight"))||undefined),multiple:(t.attr("multiple")?true:undefined)});
};
$.fn.combo.defaults=$.extend({},$.fn.textbox.defaults,{inputEvents:{click:_16,keydown:_1b,paste:_1b,drop:_1b},panelEvents:{mousedown:function(e){
e.preventDefault();
e.stopPropagation();
}},panelWidth:null,panelHeight:200,panelMinWidth:null,panelMaxWidth:null,panelMinHeight:null,panelMaxHeight:null,panelAlign:"left",reversed:false,multiple:false,multivalue:true,selectOnNavigation:true,separator:",",hasDownArrow:true,delay:200,keyHandler:{up:function(e){
},down:function(e){
},left:function(e){
},right:function(e){
},enter:function(e){
},query:function(q,e){
}},onShowPanel:function(){
},onHidePanel:function(){
},onChange:function(_58,_59){
}});
})(jQuery);

