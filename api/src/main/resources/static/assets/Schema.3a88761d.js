import{a as C,b as le,Q as oe}from"./datasources.store.2c9687c1.js";import{u as ie,a as se,b as ce,d as Q,e as I,Q as L}from"./format.31819496.js";import{az as ue,b as re,F as D,r as _,a as c,w as T,e as de,h as a,m as ge,i as fe,aA as me,f as ve,x as j,I as he,g as be,o as q,q as O,s as o,B as y,v as l,y as N,z as P,c as _e,aB as xe,C as Se,aC as Ie}from"./index.1c9f1618.js";import{Q as we,_ as ke}from"./Graph.3008c9a5.js";import{Q as E}from"./QSeparator.09e62405.js";import{u as Le,a as Te}from"./use-dark.1f812307.js";import{u as G}from"./uid.42677368.js";import{Q as qe}from"./QScrollArea.5d6604a8.js";import"./use-quasar.2adc34b0.js";import"./QResizeObserver.873c0018.js";import"./QScrollObserver.735d81f1.js";import"./scroll.8948f4bf.js";import"./touch.70a9dd44.js";const f=ue({}),Ce=Object.keys(D);var $=re({name:"QExpansionItem",props:{...D,...ie,...Le,icon:String,label:String,labelLines:[Number,String],caption:String,captionLines:[Number,String],dense:Boolean,toggleAriaLabel:String,expandIcon:String,expandedIcon:String,expandIconClass:[Array,String,Object],duration:Number,headerInsetLevel:Number,contentInsetLevel:Number,expandSeparator:Boolean,defaultOpened:Boolean,hideExpandIcon:Boolean,expandIconToggle:Boolean,switchToggleSide:Boolean,denseToggle:Boolean,group:String,popup:Boolean,headerStyle:[Array,String,Object],headerClass:[Array,String,Object]},emits:[...se,"click","after-show","after-hide"],setup(e,{slots:u,emit:d}){const{proxy:{$q:i}}=ge(),b=Te(e,i),n=_(e.modelValue!==null?e.modelValue:e.defaultOpened),g=_(null),m=G(),{show:v,hide:w,toggle:k}=ce({showing:n});let r,h;const R=c(()=>`q-expansion-item q-item-type q-expansion-item--${n.value===!0?"expanded":"collapsed"} q-expansion-item--${e.popup===!0?"popup":"standard"}`),H=c(()=>{if(e.contentInsetLevel===void 0)return null;const t=i.lang.rtl===!0?"Right":"Left";return{["padding"+t]:e.contentInsetLevel*56+"px"}}),x=c(()=>e.disable!==!0&&(e.href!==void 0||e.to!==void 0&&e.to!==null&&e.to!=="")),M=c(()=>{const t={};return Ce.forEach(s=>{t[s]=e[s]}),t}),V=c(()=>x.value===!0||e.expandIconToggle!==!0),z=c(()=>e.expandedIcon!==void 0&&n.value===!0?e.expandedIcon:e.expandIcon||i.iconSet.expansionItem[e.denseToggle===!0?"denseIcon":"icon"]),K=c(()=>e.disable!==!0&&(x.value===!0||e.expandIconToggle===!0)),U=c(()=>({expanded:n.value===!0,detailsId:e.targetUid,toggle:k,show:v,hide:w})),B=c(()=>{const t=e.toggleAriaLabel!==void 0?e.toggleAriaLabel:i.lang.label[n.value===!0?"collapse":"expand"](e.label);return{role:"button","aria-expanded":n.value===!0?"true":"false","aria-owns":m,"aria-controls":m,"aria-label":t}});T(()=>e.group,t=>{h!==void 0&&h(),t!==void 0&&A()});function F(t){x.value!==!0&&k(t),d("click",t)}function J(t){t.keyCode===13&&p(t,!0)}function p(t,s){s!==!0&&g.value!==null&&g.value.focus(),k(t),he(t)}function W(){d("after-show")}function X(){d("after-hide")}function A(){r===void 0&&(r=G()),n.value===!0&&(f[e.group]=r);const t=T(n,S=>{S===!0?f[e.group]=r:f[e.group]===r&&delete f[e.group]}),s=T(()=>f[e.group],(S,ne)=>{ne===r&&S!==void 0&&S!==r&&w()});h=()=>{t(),s(),f[e.group]===r&&delete f[e.group],h=void 0}}function Y(){const t={class:[`q-focusable relative-position cursor-pointer${e.denseToggle===!0&&e.switchToggleSide===!0?" items-end":""}`,e.expandIconClass],side:e.switchToggleSide!==!0,avatar:e.switchToggleSide},s=[a(j,{class:"q-expansion-item__toggle-icon"+(e.expandedIcon===void 0&&n.value===!0?" q-expansion-item__toggle-icon--rotated":""),name:z.value})];return K.value===!0&&(Object.assign(t,{tabindex:0,...B.value,onClick:p,onKeyup:J}),s.unshift(a("div",{ref:g,class:"q-expansion-item__toggle-focus q-icon q-focus-helper q-focus-helper--rounded",tabindex:-1}))),a(I,t,()=>s)}function Z(){let t;return u.header!==void 0?t=[].concat(u.header(U.value)):(t=[a(I,()=>[a(C,{lines:e.labelLines},()=>e.label||""),e.caption?a(C,{lines:e.captionLines,caption:!0},()=>e.caption):null])],e.icon&&t[e.switchToggleSide===!0?"push":"unshift"](a(I,{side:e.switchToggleSide===!0,avatar:e.switchToggleSide!==!0},()=>a(j,{name:e.icon})))),e.disable!==!0&&e.hideExpandIcon!==!0&&t[e.switchToggleSide===!0?"unshift":"push"](Y()),t}function ee(){const t={ref:"item",style:e.headerStyle,class:e.headerClass,dark:b.value,disable:e.disable,dense:e.dense,insetLevel:e.headerInsetLevel};return V.value===!0&&(t.clickable=!0,t.onClick=F,Object.assign(t,x.value===!0?M.value:B.value)),a(Q,t,Z)}function te(){return fe(a("div",{key:"e-content",class:"q-expansion-item__content relative-position",style:H.value,id:m},ve(u.default)),[[me,n.value]])}function ae(){const t=[ee(),a(we,{duration:e.duration,onShow:W,onHide:X},te)];return e.expandSeparator===!0&&t.push(a(E,{class:"q-expansion-item__border q-expansion-item__border--top absolute-top",dark:b.value}),a(E,{class:"q-expansion-item__border q-expansion-item__border--bottom absolute-bottom",dark:b.value})),t}return e.group!==void 0&&A(),de(()=>{h!==void 0&&h()}),()=>a("div",{class:R.value},[a("div",{class:"q-expansion-item__container relative-position"},ae())])}});const Qe={class:"col-2",style:{background:"white"}},ye=y("h5",null,"Schema 2",-1),Be={class:"col-10"},Ve={__name:"Schema",setup(e){_(!0);const u=le(),d=_(""),i=_(""),b=g=>{i.value=g.id,d.value=g.graphicalSchema},n=()=>{console.log("setting global schema view"),console.log(u.getGlobalSchema),i.value="project",d.value=u.getGlobalSchema};return be(()=>{u.datasources.length>0&&n()}),(g,m)=>(q(),O(oe,{class:"row items-stretch"},{default:o(()=>[y("div",Qe,[l(qe,{class:"fit"},{default:o(()=>[l(L,{class:"q-pa-md"},{default:o(()=>[l(I,null,{default:o(()=>[l(C,null,{default:o(()=>[ye]),_:1})]),_:1}),l($,{label:"Global schema","expand-icon":"arrow_drop_down","default-opened":""},{default:o(()=>[l(L,{dense:""},{default:o(()=>[l(Q,null,{default:o(()=>[l(N,{flat:"",padding:"xs",label:"project",class:P(["full-width",i.value=="project"?"activebg":""]),align:"left",onClick:m[0]||(m[0]=v=>n())},null,8,["class"])]),_:1})]),_:1})]),_:1}),l($,{label:"Local schemata","expand-icon":"arrow_drop_down"},{default:o(()=>[l(L,{dense:""},{default:o(()=>[(q(!0),_e(Ie,null,xe(Se(u).datasources,v=>(q(),O(Q,null,{default:o(()=>[l(N,{flat:"",padding:"xs",label:v.name,class:P(["full-width",i.value==v.id?"activebg":""]),align:"left",onClick:w=>b(v)},null,8,["label","class","onClick"])]),_:2},1024))),256))]),_:1})]),_:1})]),_:1})]),_:1})]),y("div",Be,[l(ke,{graphical:d.value},null,8,["graphical"])])]),_:1}))}};export{Ve as default};