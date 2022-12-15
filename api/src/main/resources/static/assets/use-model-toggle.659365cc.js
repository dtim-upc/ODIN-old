import{u as R,a as T}from"./use-dark.9ce0ddae.js";import{b as B,F as K,G as P,r as x,a as h,h as C,H as U,I as A,J as F,m as M,w as L,K as I,g as $,n as E}from"./index.876f1426.js";var H=B({name:"QItem",props:{...R,...K,tag:{type:String,default:"div"},active:{type:Boolean,default:null},clickable:Boolean,dense:Boolean,insetLevel:Number,tabindex:[String,Number],focused:Boolean,manualFocus:Boolean},emits:["click","keyup"],setup(t,{slots:m,emit:c}){const{proxy:{$q:d}}=M(),b=T(t,d),{hasRouterLink:p,hasLink:f,linkProps:l,linkClass:a,linkTag:k,navigateToRouterLink:n}=P(),r=x(null),i=x(null),v=h(()=>t.clickable===!0||f.value===!0||t.tag==="label"),o=h(()=>t.disable!==!0&&v.value===!0),q=h(()=>"q-item q-item-type row no-wrap"+(t.dense===!0?" q-item--dense":"")+(b.value===!0?" q-item--dark":"")+(f.value===!0&&t.active===null?a.value:t.active===!0?`${t.activeClass!==void 0?` ${t.activeClass}`:""} q-item--active`:"")+(t.disable===!0?" disabled":"")+(o.value===!0?" q-item--clickable q-link cursor-pointer "+(t.manualFocus===!0?"q-manual-focusable":"q-focusable q-hoverable")+(t.focused===!0?" q-manual-focusable--focused":""):"")),g=h(()=>{if(t.insetLevel===void 0)return null;const u=d.lang.rtl===!0?"Right":"Left";return{["padding"+u]:16+t.insetLevel*56+"px"}});function y(u){o.value===!0&&(i.value!==null&&(u.qKeyEvent!==!0&&document.activeElement===r.value?i.value.focus():document.activeElement===i.value&&r.value.focus()),p.value===!0&&n(u),c("click",u))}function e(u){if(o.value===!0&&U(u,13)===!0){A(u),u.qKeyEvent=!0;const V=new MouseEvent("click",u);V.qKeyEvent=!0,r.value.dispatchEvent(V)}c("keyup",u)}function s(){const u=F(m.default,[]);return o.value===!0&&u.unshift(C("div",{class:"q-focus-helper",tabindex:-1,ref:i})),u}return()=>{const u={ref:r,class:q.value,style:g.value,onClick:y,onKeyup:e};return o.value===!0?(u.tabindex=t.tabindex||"0",Object.assign(u,l.value)):v.value===!0&&(u["aria-disabled"]="true"),C(k.value,u,s())}}});const Q={modelValue:{type:Boolean,default:null},"onUpdate:modelValue":[Function,Array]},j=["before-show","show","before-hide","hide"];function N({showing:t,canShow:m,hideOnRouteChange:c,handleShow:d,handleHide:b,processOnMount:p}){const f=M(),{props:l,emit:a,proxy:k}=f;let n;function r(e){t.value===!0?o(e):i(e)}function i(e){if(l.disable===!0||e!==void 0&&e.qAnchorHandled===!0||m!==void 0&&m(e)!==!0)return;const s=l["onUpdate:modelValue"]!==void 0;s===!0&&(a("update:modelValue",!0),n=e,E(()=>{n===e&&(n=void 0)})),(l.modelValue===null||s===!1)&&v(e)}function v(e){t.value!==!0&&(t.value=!0,a("before-show",e),d!==void 0?d(e):a("show",e))}function o(e){if(l.disable===!0)return;const s=l["onUpdate:modelValue"]!==void 0;s===!0&&(a("update:modelValue",!1),n=e,E(()=>{n===e&&(n=void 0)})),(l.modelValue===null||s===!1)&&q(e)}function q(e){t.value!==!1&&(t.value=!1,a("before-hide",e),b!==void 0?b(e):a("hide",e))}function g(e){l.disable===!0&&e===!0?l["onUpdate:modelValue"]!==void 0&&a("update:modelValue",!1):e===!0!==t.value&&(e===!0?v:q)(n)}L(()=>l.modelValue,g),c!==void 0&&I(f)===!0&&L(()=>k.$route.fullPath,()=>{c.value===!0&&t.value===!0&&o()}),p===!0&&$(()=>{g(l.modelValue)});const y={show:i,hide:o,toggle:r};return Object.assign(k,y),y}export{H as Q,j as a,N as b,Q as u};