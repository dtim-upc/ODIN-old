import{b as A,aQ as Q,r as b,a as w,w as h,g as D,h as o,x as k,f as x,y as s,m as V,a5 as I}from"./index.876f1426.js";import{Q as M}from"./use-quasar.a85fc028.js";import{Q as R}from"./use-prevent-scroll.1ba9ffd6.js";import{u as _}from"./uid.42677368.js";var L=A({name:"QBtnDropdown",props:{...Q,modelValue:Boolean,split:Boolean,dropdownIcon:String,contentClass:[Array,String,Object],contentStyle:[Array,String,Object],cover:Boolean,persistent:Boolean,noRouteDismiss:Boolean,autoClose:Boolean,menuAnchor:{type:String,default:"bottom end"},menuSelf:{type:String,default:"top end"},menuOffset:Array,disableMainBtn:Boolean,disableDropdown:Boolean,noIconAnimation:Boolean,toggleAriaLabel:String},emits:["update:modelValue","click","before-show","show","before-hide","hide"],setup(e,{slots:l,emit:t}){const{proxy:i}=V(),u=b(e.modelValue),a=b(null),d=_(),c=w(()=>{const n={"aria-expanded":u.value===!0?"true":"false","aria-haspopup":"true","aria-controls":d,"aria-owns":d,"aria-label":e.toggleAriaLabel||i.$q.lang.label[u.value===!0?"collapse":"expand"](e.label)};return(e.disable===!0||e.split===!1&&e.disableMainBtn===!0||e.disableDropdown===!0)&&(n["aria-disabled"]="true"),n}),m=w(()=>"q-btn-dropdown__arrow"+(u.value===!0&&e.noIconAnimation===!1?" rotate-180":"")+(e.split===!1?" q-btn-dropdown__arrow-container":""));h(()=>e.modelValue,n=>{a.value!==null&&a.value[n?"show":"hide"]()}),h(()=>e.split,r);function g(n){u.value=!0,t("before-show",n)}function v(n){t("show",n),t("update:modelValue",!0)}function B(n){u.value=!1,t("before-hide",n)}function q(n){t("hide",n),t("update:modelValue",!1)}function C(n){t("click",n)}function S(n){I(n),r(),t("click",n)}function y(n){a.value!==null&&a.value.toggle(n)}function f(n){a.value!==null&&a.value.show(n)}function r(n){a.value!==null&&a.value.hide(n)}return Object.assign(i,{show:f,hide:r,toggle:y}),D(()=>{e.modelValue===!0&&f()}),()=>{const n=[o(k,{class:m.value,name:e.dropdownIcon||i.$q.iconSet.arrow.dropdown})];return e.disableDropdown!==!0&&n.push(o(R,{ref:a,id:d,class:e.contentClass,style:e.contentStyle,cover:e.cover,fit:!0,persistent:e.persistent,noRouteDismiss:e.noRouteDismiss,autoClose:e.autoClose,anchor:e.menuAnchor,self:e.menuSelf,offset:e.menuOffset,separateClosePopup:!0,onBeforeShow:g,onShow:v,onBeforeHide:B,onHide:q},l.default)),e.split===!1?o(s,{class:"q-btn-dropdown q-btn-dropdown--simple",...e,disable:e.disable===!0||e.disableMainBtn===!0,noWrap:!0,round:!1,...c.value,onClick:C},{default:()=>x(l.label,[]).concat(n),loading:l.loading}):o(M,{class:"q-btn-dropdown q-btn-dropdown--split no-wrap q-btn-item",outline:e.outline,flat:e.flat,rounded:e.rounded,square:e.square,push:e.push,unelevated:e.unelevated,glossy:e.glossy,stretch:e.stretch},()=>[o(s,{class:"q-btn-dropdown--current",...e,disable:e.disable===!0||e.disableMainBtn===!0,noWrap:!0,iconRight:e.iconRight,round:!1,onClick:S},{default:l.label,loading:l.loading}),o(s,{class:"q-btn-dropdown__arrow-container q-anchor--skip",...c.value,disable:e.disable===!0||e.disableDropdown===!0,outline:e.outline,flat:e.flat,rounded:e.rounded,push:e.push,size:e.size,color:e.color,textColor:e.textColor,dense:e.dense,ripple:e.ripple},()=>n)])}}});export{L as Q};
