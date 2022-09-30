import{b as te,r as C,a as f,w as E,e as x,L as H,M as A,h as M,T as oe,m as ae,f as ne,I as ie}from"./index.1c9f1618.js";import{c as le,d as se,v as j,e as re,f as ue,u as ce,g as de,h as fe,i as ve,j as me,r as q,s as he,p as D,k as ge}from"./use-prevent-scroll.eb632cfc.js";import{u as Te,a as ye,b as pe,f as L}from"./format.31819496.js";import{g as be}from"./scroll.8948f4bf.js";var ke=te({name:"QTooltip",inheritAttrs:!1,props:{...le,...Te,...se,maxHeight:{type:String,default:null},maxWidth:{type:String,default:null},transitionShow:{default:"jump-down"},transitionHide:{default:"jump-up"},anchor:{type:String,default:"bottom middle",validator:j},self:{type:String,default:"top middle",validator:j},offset:{type:Array,default:()=>[14,14],validator:re},scrollTarget:{default:void 0},delay:{type:Number,default:0},hideDelay:{type:Number,default:0}},emits:[...ye],setup(e,{slots:Q,emit:y,attrs:v}){let i,l;const m=ae(),{proxy:{$q:a}}=m,s=C(null),r=C(!1),W=f(()=>D(e.anchor,a.lang.rtl)),I=f(()=>D(e.self,a.lang.rtl)),N=f(()=>e.persistent!==!0),{registerTick:R,removeTick:p}=ue(),{registerTimeout:d,removeTimeout:h}=ce(),{transition:_,transitionStyle:B}=de(e,r),{localScrollTarget:b,changeScrollEvent:U,unconfigureScrollTarget:V}=fe(e,O),{anchorEl:o,canShow:$,anchorEvents:u}=ve({showing:r,configureAnchorEl:Y}),{show:z,hide:g}=pe({showing:r,canShow:$,handleShow:G,handleHide:J,hideOnRouteChange:N,processOnMount:!0});Object.assign(u,{delayShow:K,delayHide:X});const{showPortal:S,hidePortal:P,renderPortal:F}=me(m,s,ee);if(a.platform.is.mobile===!0){const t={anchorEl:o,innerRef:s,onClickOutside(n){return g(n),n.target.classList.contains("q-dialog__backdrop")&&ie(n),!0}},T=f(()=>e.modelValue===null&&e.persistent!==!0&&r.value===!0);E(T,n=>{(n===!0?ge:q)(t)}),x(()=>{q(t)})}function G(t){p(),h(),S(),R(()=>{l=new MutationObserver(()=>c()),l.observe(s.value,{attributes:!1,childList:!0,characterData:!0,subtree:!0}),c(),O()}),i===void 0&&(i=E(()=>a.screen.width+"|"+a.screen.height+"|"+e.self+"|"+e.anchor+"|"+a.lang.rtl,c)),d(()=>{S(!0),y("show",t)},e.transitionDuration)}function J(t){p(),h(),P(),w(),d(()=>{P(!0),y("hide",t)},e.transitionDuration)}function w(){l!==void 0&&(l.disconnect(),l=void 0),i!==void 0&&(i(),i=void 0),V(),H(u,"tooltipTemp")}function c(){const t=s.value;o.value===null||!t||he({el:t,offset:e.offset,anchorEl:o.value,anchorOrigin:W.value,selfOrigin:I.value,maxHeight:e.maxHeight,maxWidth:e.maxWidth})}function K(t){if(a.platform.is.mobile===!0){L(),document.body.classList.add("non-selectable");const T=o.value,n=["touchmove","touchcancel","touchend","click"].map(k=>[T,k,"delayHide","passiveCapture"]);A(u,"tooltipTemp",n)}d(()=>{z(t)},e.delay)}function X(t){h(),a.platform.is.mobile===!0&&(H(u,"tooltipTemp"),L(),setTimeout(()=>{document.body.classList.remove("non-selectable")},10)),d(()=>{g(t)},e.hideDelay)}function Y(){if(e.noParentEvent===!0||o.value===null)return;const t=a.platform.is.mobile===!0?[[o.value,"touchstart","delayShow","passive"]]:[[o.value,"mouseenter","delayShow","passive"],[o.value,"mouseleave","delayHide","passive"]];A(u,"anchor",t)}function O(){if(o.value!==null||e.scrollTarget!==void 0){b.value=be(o.value,e.scrollTarget);const t=e.noParentEvent===!0?c:g;U(b.value,t)}}function Z(){return r.value===!0?M("div",{...v,ref:s,class:["q-tooltip q-tooltip--style q-position-engine no-pointer-events",v.class],style:[v.style,B.value],role:"complementary"},ne(Q.default)):null}function ee(){return M(oe,{name:_.value,appear:!0},Z)}return x(w),Object.assign(m.proxy,{updatePosition:c}),F}});export{ke as Q};
