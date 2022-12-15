import{aC as J,b as W,F as Q,r as L,a as i,w as x,e as X,h as n,m as Y,i as Z,aD as p,f as ee,x as _,I as te}from"./index.1dc04fa4.js";import{u as ne,a as ae,b as oe,Q as ie}from"./use-model-toggle.3d79aca0.js";import{a as I}from"./format.47a278a4.js";import{b as k}from"./datasources.store.9808ef93.js";import{Q as le}from"./QSlideTransition.7a7f0646.js";import{Q as C}from"./QSeparator.db748d79.js";import{u as ue,a as re}from"./use-dark.de9a0581.js";import{u as y}from"./uid.42677368.js";const u=J({}),ce=Object.keys(Q);var xe=W({name:"QExpansionItem",props:{...Q,...ne,...ue,icon:String,label:String,labelLines:[Number,String],caption:String,captionLines:[Number,String],dense:Boolean,toggleAriaLabel:String,expandIcon:String,expandedIcon:String,expandIconClass:[Array,String,Object],duration:Number,headerInsetLevel:Number,contentInsetLevel:Number,expandSeparator:Boolean,defaultOpened:Boolean,hideExpandIcon:Boolean,expandIconToggle:Boolean,switchToggleSide:Boolean,denseToggle:Boolean,group:String,popup:Boolean,headerStyle:[Array,String,Object],headerClass:[Array,String,Object]},emits:[...ae,"click","after-show","after-hide"],setup(e,{slots:g,emit:v}){const{proxy:{$q:c}}=Y(),f=re(e,c),a=L(e.modelValue!==null?e.modelValue:e.defaultOpened),m=L(null),h=y(),{show:A,hide:S,toggle:b}=oe({showing:a});let l,r;const B=i(()=>`q-expansion-item q-item-type q-expansion-item--${a.value===!0?"expanded":"collapsed"} q-expansion-item--${e.popup===!0?"popup":"standard"}`),O=i(()=>{if(e.contentInsetLevel===void 0)return null;const t=c.lang.rtl===!0?"Right":"Left";return{["padding"+t]:e.contentInsetLevel*56+"px"}}),d=i(()=>e.disable!==!0&&(e.href!==void 0||e.to!==void 0&&e.to!==null&&e.to!=="")),P=i(()=>{const t={};return ce.forEach(o=>{t[o]=e[o]}),t}),j=i(()=>d.value===!0||e.expandIconToggle!==!0),E=i(()=>e.expandedIcon!==void 0&&a.value===!0?e.expandedIcon:e.expandIcon||c.iconSet.expansionItem[e.denseToggle===!0?"denseIcon":"icon"]),N=i(()=>e.disable!==!0&&(d.value===!0||e.expandIconToggle===!0)),D=i(()=>({expanded:a.value===!0,detailsId:e.targetUid,toggle:b,show:A,hide:S})),T=i(()=>{const t=e.toggleAriaLabel!==void 0?e.toggleAriaLabel:c.lang.label[a.value===!0?"collapse":"expand"](e.label);return{role:"button","aria-expanded":a.value===!0?"true":"false","aria-owns":h,"aria-controls":h,"aria-label":t}});x(()=>e.group,t=>{r!==void 0&&r(),t!==void 0&&q()});function R(t){d.value!==!0&&b(t),v("click",t)}function H(t){t.keyCode===13&&w(t,!0)}function w(t,o){o!==!0&&m.value!==null&&m.value.focus(),b(t),te(t)}function $(){v("after-show")}function G(){v("after-hide")}function q(){l===void 0&&(l=y()),a.value===!0&&(u[e.group]=l);const t=x(a,s=>{s===!0?u[e.group]=l:u[e.group]===l&&delete u[e.group]}),o=x(()=>u[e.group],(s,z)=>{z===l&&s!==void 0&&s!==l&&S()});r=()=>{t(),o(),u[e.group]===l&&delete u[e.group],r=void 0}}function K(){const t={class:[`q-focusable relative-position cursor-pointer${e.denseToggle===!0&&e.switchToggleSide===!0?" items-end":""}`,e.expandIconClass],side:e.switchToggleSide!==!0,avatar:e.switchToggleSide},o=[n(_,{class:"q-expansion-item__toggle-icon"+(e.expandedIcon===void 0&&a.value===!0?" q-expansion-item__toggle-icon--rotated":""),name:E.value})];return N.value===!0&&(Object.assign(t,{tabindex:0,...T.value,onClick:w,onKeyup:H}),o.unshift(n("div",{ref:m,class:"q-expansion-item__toggle-focus q-icon q-focus-helper q-focus-helper--rounded",tabindex:-1}))),n(I,t,()=>o)}function M(){let t;return g.header!==void 0?t=[].concat(g.header(D.value)):(t=[n(I,()=>[n(k,{lines:e.labelLines},()=>e.label||""),e.caption?n(k,{lines:e.captionLines,caption:!0},()=>e.caption):null])],e.icon&&t[e.switchToggleSide===!0?"push":"unshift"](n(I,{side:e.switchToggleSide===!0,avatar:e.switchToggleSide!==!0},()=>n(_,{name:e.icon})))),e.disable!==!0&&e.hideExpandIcon!==!0&&t[e.switchToggleSide===!0?"unshift":"push"](K()),t}function U(){const t={ref:"item",style:e.headerStyle,class:e.headerClass,dark:f.value,disable:e.disable,dense:e.dense,insetLevel:e.headerInsetLevel};return j.value===!0&&(t.clickable=!0,t.onClick=R,Object.assign(t,d.value===!0?P.value:T.value)),n(ie,t,M)}function V(){return Z(n("div",{key:"e-content",class:"q-expansion-item__content relative-position",style:O.value,id:h},ee(g.default)),[[p,a.value]])}function F(){const t=[U(),n(le,{duration:e.duration,onShow:$,onHide:G},V)];return e.expandSeparator===!0&&t.push(n(C,{class:"q-expansion-item__border q-expansion-item__border--top absolute-top",dark:f.value}),n(C,{class:"q-expansion-item__border q-expansion-item__border--bottom absolute-bottom",dark:f.value})),t}return e.group!==void 0&&q(),X(()=>{r!==void 0&&r()}),()=>n("div",{class:B.value},[n("div",{class:"q-expansion-item__container relative-position"},F())])}});export{xe as Q};
