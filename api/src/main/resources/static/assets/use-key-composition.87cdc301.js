import{w as A,g as G,e as z,k as oe,m as I,as as te,r as p,a as i,a6 as le,az as re,aw as ne,I as ue,n as ae,a7 as ie,a8 as se,ac as de,h as a,N as B,x as Q,Q as fe,f as k,T as ce,X as ve}from"./index.1dc04fa4.js";import{u as pe,a as me}from"./use-dark.de9a0581.js";import{u as ge}from"./uid.42677368.js";import{b as be,c as he}from"./focus-manager.32f8d49a.js";function Ce({validate:e,resetValidation:o,requiresQForm:l}){const r=oe(te,!1);if(r!==!1){const{props:f,proxy:s}=I();Object.assign(s,{validate:e,resetValidation:o}),A(()=>f.disable,n=>{n===!0?(typeof o=="function"&&o(),r.unbindComponent(s)):r.bindComponent(s)}),G(()=>{f.disable!==!0&&r.bindComponent(s)}),z(()=>{f.disable!==!0&&r.unbindComponent(s)})}else l===!0&&console.error("Parent QForm not found on useFormChild()!")}const K=/^#[0-9a-fA-F]{3}([0-9a-fA-F]{3})?$/,Z=/^#[0-9a-fA-F]{4}([0-9a-fA-F]{4})?$/,J=/^#([0-9a-fA-F]{3}|[0-9a-fA-F]{4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$/,$=/^rgb\(((0|[1-9][\d]?|1[\d]{0,2}|2[\d]?|2[0-4][\d]|25[0-5]),){2}(0|[1-9][\d]?|1[\d]{0,2}|2[\d]?|2[0-4][\d]|25[0-5])\)$/,E=/^rgba\(((0|[1-9][\d]?|1[\d]{0,2}|2[\d]?|2[0-4][\d]|25[0-5]),){2}(0|[1-9][\d]?|1[\d]{0,2}|2[\d]?|2[0-4][\d]|25[0-5]),(0|0\.[0-9]+[1-9]|0\.[1-9]+|1)\)$/,T={date:e=>/^-?[\d]+\/[0-1]\d\/[0-3]\d$/.test(e),time:e=>/^([0-1]?\d|2[0-3]):[0-5]\d$/.test(e),fulltime:e=>/^([0-1]?\d|2[0-3]):[0-5]\d:[0-5]\d$/.test(e),timeOrFulltime:e=>/^([0-1]?\d|2[0-3]):[0-5]\d(:[0-5]\d)?$/.test(e),email:e=>/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(e),hexColor:e=>K.test(e),hexaColor:e=>Z.test(e),hexOrHexaColor:e=>J.test(e),rgbColor:e=>$.test(e),rgbaColor:e=>E.test(e),rgbOrRgbaColor:e=>$.test(e)||E.test(e),hexOrRgbColor:e=>K.test(e)||$.test(e),hexaOrRgbaColor:e=>Z.test(e)||E.test(e),anyColor:e=>J.test(e)||$.test(e)||E.test(e)},qe=[!0,!1,"ondemand"],ye={modelValue:{},error:{type:Boolean,default:null},errorMessage:String,noErrorIcon:Boolean,rules:Array,reactiveRules:Boolean,lazyRules:{type:[Boolean,String],validator:e=>qe.includes(e)}};function _e(e,o){const{props:l,proxy:r}=I(),f=p(!1),s=p(null),n=p(null);Ce({validate:C,resetValidation:_});let m=0,h;const w=i(()=>l.rules!==void 0&&l.rules!==null&&l.rules.length>0),g=i(()=>l.disable!==!0&&w.value===!0),y=i(()=>l.error===!0||f.value===!0),P=i(()=>typeof l.errorMessage=="string"&&l.errorMessage.length>0?l.errorMessage:s.value);A(()=>l.modelValue,()=>{F()}),A(()=>l.reactiveRules,b=>{b===!0?h===void 0&&(h=A(()=>l.rules,()=>{F(!0)})):h!==void 0&&(h(),h=void 0)},{immediate:!0}),A(e,b=>{b===!0?n.value===null&&(n.value=!1):n.value===!1&&(n.value=!0,g.value===!0&&l.lazyRules!=="ondemand"&&o.value===!1&&x())});function _(){m++,o.value=!1,n.value=null,f.value=!1,s.value=null,x.cancel()}function C(b=l.modelValue){if(g.value!==!0)return!0;const S=++m,O=o.value!==!0?()=>{n.value=!0}:()=>{},q=(d,c)=>{d===!0&&O(),f.value=d,s.value=c||null,o.value=!1},R=[];for(let d=0;d<l.rules.length;d++){const c=l.rules[d];let v;if(typeof c=="function"?v=c(b,T):typeof c=="string"&&T[c]!==void 0&&(v=T[c](b)),v===!1||typeof v=="string")return q(!0,v),!1;v!==!0&&v!==void 0&&R.push(v)}return R.length===0?(q(!1),!0):(o.value=!0,Promise.all(R).then(d=>{if(d===void 0||Array.isArray(d)===!1||d.length===0)return S===m&&q(!1),!0;const c=d.find(v=>v===!1||typeof v=="string");return S===m&&q(c!==void 0,c),c===void 0},d=>(S===m&&(console.error(d),q(!0)),!1)))}function F(b){g.value===!0&&l.lazyRules!=="ondemand"&&(n.value===!0||l.lazyRules!==!0&&b!==!0)&&x()}const x=le(C,0);return z(()=>{h!==void 0&&h(),x.cancel()}),Object.assign(r,{resetValidation:_,validate:C}),re(r,"hasError",()=>y.value),{isDirtyModel:n,hasRules:w,hasError:y,errorMessage:P,validate:C,resetValidation:_}}const X=/^on[A-Z]/;function xe(e,o){const l={listeners:p({}),attributes:p({})};function r(){const f={},s={};for(const n in e)n!=="class"&&n!=="style"&&X.test(n)===!1&&(f[n]=e[n]);for(const n in o.props)X.test(n)===!0&&(s[n]=o.props[n]);l.attributes.value=f,l.listeners.value=s}return ne(r),r(),l}function M(e){return e===void 0?`f_${ge()}`:e}function Se(e){return e!=null&&(""+e).length>0}const Ee={...pe,...ye,label:String,stackLabel:Boolean,hint:String,hideHint:Boolean,prefix:String,suffix:String,labelColor:String,color:String,bgColor:String,filled:Boolean,outlined:Boolean,borderless:Boolean,standout:[Boolean,String],square:Boolean,loading:Boolean,labelSlot:Boolean,bottomSlots:Boolean,hideBottomSpace:Boolean,rounded:Boolean,dense:Boolean,itemAligned:Boolean,counter:Boolean,clearable:Boolean,clearIcon:String,disable:Boolean,readonly:Boolean,autofocus:Boolean,for:String,maxlength:[Number,String]},Ie=["update:modelValue","clear","focus","blur","popup-show","popup-hide"];function Pe(){const{props:e,attrs:o,proxy:l,vnode:r}=I();return{isDark:me(e,l.$q),editable:i(()=>e.disable!==!0&&e.readonly!==!0),innerLoading:p(!1),focused:p(!1),hasPopupOpen:!1,splitAttrs:xe(o,r),targetUid:p(M(e.for)),rootRef:p(null),targetRef:p(null),controlRef:p(null)}}function Oe(e){const{props:o,emit:l,slots:r,attrs:f,proxy:s}=I(),{$q:n}=s;let m;e.hasValue===void 0&&(e.hasValue=i(()=>Se(o.modelValue))),e.emitValue===void 0&&(e.emitValue=t=>{l("update:modelValue",t)}),e.controlEvents===void 0&&(e.controlEvents={onFocusin:D,onFocusout:U}),Object.assign(e,{clearValue:j,onControlFocusin:D,onControlFocusout:U,focus:c}),e.computedCounter===void 0&&(e.computedCounter=i(()=>{if(o.counter!==!1){const t=typeof o.modelValue=="string"||typeof o.modelValue=="number"?(""+o.modelValue).length:Array.isArray(o.modelValue)===!0?o.modelValue.length:0,u=o.maxlength!==void 0?o.maxlength:o.maxValues;return t+(u!==void 0?" / "+u:"")}}));const{isDirtyModel:h,hasRules:w,hasError:g,errorMessage:y,resetValidation:P}=_e(e.focused,e.innerLoading),_=e.floatingLabel!==void 0?i(()=>o.stackLabel===!0||e.focused.value===!0||e.floatingLabel.value===!0):i(()=>o.stackLabel===!0||e.focused.value===!0||e.hasValue.value===!0),C=i(()=>o.bottomSlots===!0||o.hint!==void 0||w.value===!0||o.counter===!0||o.error!==null),F=i(()=>o.filled===!0?"filled":o.outlined===!0?"outlined":o.borderless===!0?"borderless":o.standout?"standout":"standard"),x=i(()=>`q-field row no-wrap items-start q-field--${F.value}`+(e.fieldClass!==void 0?` ${e.fieldClass.value}`:"")+(o.rounded===!0?" q-field--rounded":"")+(o.square===!0?" q-field--square":"")+(_.value===!0?" q-field--float":"")+(S.value===!0?" q-field--labeled":"")+(o.dense===!0?" q-field--dense":"")+(o.itemAligned===!0?" q-field--item-aligned q-item-type":"")+(e.isDark.value===!0?" q-field--dark":"")+(e.getControl===void 0?" q-field--auto-height":"")+(e.focused.value===!0?" q-field--focused":"")+(g.value===!0?" q-field--error":"")+(g.value===!0||e.focused.value===!0?" q-field--highlighted":"")+(o.hideBottomSpace!==!0&&C.value===!0?" q-field--with-bottom":"")+(o.disable===!0?" q-field--disabled":o.readonly===!0?" q-field--readonly":"")),b=i(()=>"q-field__control relative-position row no-wrap"+(o.bgColor!==void 0?` bg-${o.bgColor}`:"")+(g.value===!0?" text-negative":typeof o.standout=="string"&&o.standout.length>0&&e.focused.value===!0?` ${o.standout}`:o.color!==void 0?` text-${o.color}`:"")),S=i(()=>o.labelSlot===!0||o.label!==void 0),O=i(()=>"q-field__label no-pointer-events absolute ellipsis"+(o.labelColor!==void 0&&g.value!==!0?` text-${o.labelColor}`:"")),q=i(()=>({id:e.targetUid.value,editable:e.editable.value,focused:e.focused.value,floatingLabel:_.value,modelValue:o.modelValue,emitValue:e.emitValue})),R=i(()=>{const t={for:e.targetUid.value};return o.disable===!0?t["aria-disabled"]="true":o.readonly===!0&&(t["aria-readonly"]="true"),t});A(()=>o.for,t=>{e.targetUid.value=M(t)});function d(){const t=document.activeElement;let u=e.targetRef!==void 0&&e.targetRef.value;u&&(t===null||t.id!==e.targetUid.value)&&(u.hasAttribute("tabindex")===!0||(u=u.querySelector("[tabindex]")),u&&u!==t&&u.focus({preventScroll:!0}))}function c(){be(d)}function v(){he(d);const t=document.activeElement;t!==null&&e.rootRef.value.contains(t)&&t.blur()}function D(t){clearTimeout(m),e.editable.value===!0&&e.focused.value===!1&&(e.focused.value=!0,l("focus",t))}function U(t,u){clearTimeout(m),m=setTimeout(()=>{document.hasFocus()===!0&&(e.hasPopupOpen===!0||e.controlRef===void 0||e.controlRef.value===null||e.controlRef.value.contains(document.activeElement)!==!1)||(e.focused.value===!0&&(e.focused.value=!1,l("blur",t)),u!==void 0&&u())})}function j(t){ue(t),n.platform.is.mobile!==!0?(e.targetRef!==void 0&&e.targetRef.value||e.rootRef.value).focus():e.rootRef.value.contains(document.activeElement)===!0&&document.activeElement.blur(),o.type==="file"&&(e.inputRef.value.value=null),l("update:modelValue",null),l("clear",o.modelValue),ae(()=>{P(),n.platform.is.mobile!==!0&&(h.value=!1)})}function W(){const t=[];return r.prepend!==void 0&&t.push(a("div",{class:"q-field__prepend q-field__marginal row no-wrap items-center",key:"prepend",onClick:B},r.prepend())),t.push(a("div",{class:"q-field__control-container col relative-position row no-wrap q-anchor--skip"},Y())),g.value===!0&&o.noErrorIcon===!1&&t.push(V("error",[a(Q,{name:n.iconSet.field.error,color:"negative"})])),o.loading===!0||e.innerLoading.value===!0?t.push(V("inner-loading-append",r.loading!==void 0?r.loading():[a(fe,{color:o.color})])):o.clearable===!0&&e.hasValue.value===!0&&e.editable.value===!0&&t.push(V("inner-clearable-append",[a(Q,{class:"q-field__focusable-action",tag:"button",name:o.clearIcon||n.iconSet.field.clear,tabindex:0,type:"button","aria-hidden":null,role:null,onClick:j})])),r.append!==void 0&&t.push(a("div",{class:"q-field__append q-field__marginal row no-wrap items-center",key:"append",onClick:B},r.append())),e.getInnerAppend!==void 0&&t.push(V("inner-append",e.getInnerAppend())),e.getControlChild!==void 0&&t.push(e.getControlChild()),t}function Y(){const t=[];return o.prefix!==void 0&&o.prefix!==null&&t.push(a("div",{class:"q-field__prefix no-pointer-events row items-center"},o.prefix)),e.getShadowControl!==void 0&&e.hasShadow.value===!0&&t.push(e.getShadowControl()),e.getControl!==void 0?t.push(e.getControl()):r.rawControl!==void 0?t.push(r.rawControl()):r.control!==void 0&&t.push(a("div",{ref:e.targetRef,class:"q-field__native row",tabindex:-1,...e.splitAttrs.attributes.value,"data-autofocus":o.autofocus===!0||void 0},r.control(q.value))),S.value===!0&&t.push(a("div",{class:O.value},k(r.label,o.label))),o.suffix!==void 0&&o.suffix!==null&&t.push(a("div",{class:"q-field__suffix no-pointer-events row items-center"},o.suffix)),t.concat(k(r.default))}function ee(){let t,u;g.value===!0?y.value!==null?(t=[a("div",{role:"alert"},y.value)],u=`q--slot-error-${y.value}`):(t=k(r.error),u="q--slot-error"):(o.hideHint!==!0||e.focused.value===!0)&&(o.hint!==void 0?(t=[a("div",o.hint)],u=`q--slot-hint-${o.hint}`):(t=k(r.hint),u="q--slot-hint"));const H=o.counter===!0||r.counter!==void 0;if(o.hideBottomSpace===!0&&H===!1&&t===void 0)return;const N=a("div",{key:u,class:"q-field__messages col"},t);return a("div",{class:"q-field__bottom row items-start q-field__bottom--"+(o.hideBottomSpace!==!0?"animated":"stale"),onClick:B},[o.hideBottomSpace===!0?N:a(ce,{name:"q-transition--field-message"},()=>N),H===!0?a("div",{class:"q-field__counter"},r.counter!==void 0?r.counter():e.computedCounter.value):null])}function V(t,u){return u===null?null:a("div",{key:t,class:"q-field__append q-field__marginal row no-wrap items-center q-anchor--skip"},u)}let L=!1;return ie(()=>{L=!0}),se(()=>{L===!0&&o.autofocus===!0&&s.focus()}),G(()=>{de.value===!0&&o.for===void 0&&(e.targetUid.value=M()),o.autofocus===!0&&s.focus()}),z(()=>{clearTimeout(m)}),Object.assign(s,{focus:c,blur:v}),function(){const u=e.getControl===void 0&&r.control===void 0?{...e.splitAttrs.attributes.value,"data-autofocus":o.autofocus===!0||void 0,...R.value}:R.value;return a("label",{ref:e.rootRef,class:[x.value,f.class],style:f.style,...u},[r.before!==void 0?a("div",{class:"q-field__before q-field__marginal row no-wrap items-center",onClick:B},r.before()):null,a("div",{class:"q-field__inner relative-position col self-stretch"},[a("div",{ref:e.controlRef,class:b.value,tabindex:-1,...e.controlEvents},W()),C.value===!0?ee():null]),r.after!==void 0?a("div",{class:"q-field__after q-field__marginal row no-wrap items-center",onClick:B},r.after()):null])}}const Te={name:String};function Me(e={}){return(o,l,r)=>{o[l](a("input",{class:"hidden"+(r||""),...e.value}))}}function ze(e){return i(()=>e.name||e.for)}const Re=/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/,Ae=/[\u4e00-\u9fff\u3400-\u4dbf\u{20000}-\u{2a6df}\u{2a700}-\u{2b73f}\u{2b740}-\u{2b81f}\u{2b820}-\u{2ceaf}\uf900-\ufaff\u3300-\u33ff\ufe30-\ufe4f\uf900-\ufaff\u{2f800}-\u{2fa1f}]/u,Be=/[\u3131-\u314e\u314f-\u3163\uac00-\ud7a3]/,we=/[a-z0-9_ -]$/i;function De(e){return function(l){if(l.type==="compositionend"||l.type==="change"){if(l.target.qComposing!==!0)return;l.target.qComposing=!1,e(l)}else l.type==="compositionupdate"&&l.target.qComposing!==!0&&typeof l.data=="string"&&(ve.is.firefox===!0?we.test(l.data)===!1:Re.test(l.data)===!0||Ae.test(l.data)===!0||Be.test(l.data)===!0)===!0&&(l.target.qComposing=!0)}}export{Te as a,Ie as b,Pe as c,ze as d,Oe as e,Se as f,De as g,Me as h,Ee as u};
