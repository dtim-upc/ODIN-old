import{b as G,h as o,f as ue,L as mt,x as qe,m as W,a as d,r as D,w as $,ag as Ae,g as Ie,a8 as St,a7 as ht,e as Ne,S as Le,a9 as Qe,ak as yt,aj as _t,aG as Q,K as Me,M as wt,ae as Ee,aH as we,aI as $e,aJ as qt,n as He,aK as Ct,aA as U,y as re}from"./index.d68f4a5a.js";import{Q as Pt}from"./QSeparator.a128976e.js";import{Q as kt,g as xt}from"./QList.a7d2d4d7.js";import{u as Ce,a as Pe}from"./use-dark.cac0eb06.js";import{u as Rt,d as Tt,e as ze,b as Vt,Q as pt}from"./QSelect.b26dbc07.js";import{d as Bt,h as Ot}from"./use-key-composition.b19a4bbf.js";var Ft=G({name:"QTh",props:{props:Object,autoWidth:Boolean},emits:["click"],setup(e,{slots:a,emit:l}){const c=W(),{proxy:{$q:r}}=c,f=i=>{l("click",i)};return()=>{if(e.props===void 0)return o("th",{class:e.autoWidth===!0?"q-table--col-auto-width":"",onClick:f},ue(a.default));let i,s;const v=c.vnode.key;if(v){if(i=e.props.colsMap[v],i===void 0)return}else i=e.props.col;if(i.sortable===!0){const n=i.align==="right"?"unshift":"push";s=mt(a.default,[]),s[n](o(qe,{class:i.__iconClass,name:r.iconSet.table.arrowUp}))}else s=ue(a.default);const S={class:i.__thClass+(e.autoWidth===!0?" q-table--col-auto-width":""),style:i.headerStyle,onClick:n=>{i.sortable===!0&&e.props.sort(i),f(n)}};return o("th",S,s)}}});const Lt=["horizontal","vertical","cell","none"];var Mt=G({name:"QMarkupTable",props:{...Ce,dense:Boolean,flat:Boolean,bordered:Boolean,square:Boolean,wrapCells:Boolean,separator:{type:String,default:"horizontal",validator:e=>Lt.includes(e)}},setup(e,{slots:a}){const l=W(),c=Pe(e,l.proxy.$q),r=d(()=>`q-markup-table q-table__container q-table__card q-table--${e.separator}-separator`+(c.value===!0?" q-table--dark q-table__card--dark q-dark":"")+(e.dense===!0?" q-table--dense":"")+(e.flat===!0?" q-table--flat":"")+(e.bordered===!0?" q-table--bordered":"")+(e.square===!0?" q-table--square":"")+(e.wrapCells===!1?" q-table--no-wrap":""));return()=>o("div",{class:r.value},[o("table",{class:"q-table"},ue(a.default))])}});function Ue(e,a){return o("div",e,[o("table",{class:"q-table"},a)])}const Et={list:kt,table:Mt},$t=["list","table","__qtable"];var jt=G({name:"QVirtualScroll",props:{...Rt,type:{type:String,default:"list",validator:e=>$t.includes(e)},items:{type:Array,default:()=>[]},itemsFn:Function,itemsSize:Number,scrollTarget:{default:void 0}},setup(e,{slots:a,attrs:l}){let c;const r=D(null),f=d(()=>e.itemsSize>=0&&e.itemsFn!==void 0?parseInt(e.itemsSize,10):Array.isArray(e.items)?e.items.length:0),{virtualScrollSliceRange:i,localResetVirtualScroll:s,padVirtualScroll:v,onVirtualScrollEvt:S}=Tt({virtualScrollLength:f,getVirtualScrollTarget:C,getVirtualScrollEl:P}),n=d(()=>{if(f.value===0)return[];const p=(O,x)=>({index:i.value.from+x,item:O});return e.itemsFn===void 0?e.items.slice(i.value.from,i.value.to).map(p):e.itemsFn(i.value.from,i.value.to-i.value.from).map(p)}),m=d(()=>"q-virtual-scroll q-virtual-scroll"+(e.virtualScrollHorizontal===!0?"--horizontal":"--vertical")+(e.scrollTarget!==void 0?"":" scroll")),w=d(()=>e.scrollTarget!==void 0?{}:{tabindex:0});$(f,()=>{s()}),$(()=>e.scrollTarget,()=>{y(),_()});function P(){return r.value.$el||r.value}function C(){return c}function _(){c=xt(P(),e.scrollTarget),c.addEventListener("scroll",S,Le.passive)}function y(){c!==void 0&&(c.removeEventListener("scroll",S,Le.passive),c=void 0)}function V(){let p=v(e.type==="list"?"div":"tbody",n.value.map(a.default));return a.before!==void 0&&(p=a.before().concat(p)),Qe(a.after,p)}return Ae(()=>{s()}),Ie(()=>{_()}),St(()=>{_()}),ht(()=>{y()}),Ne(()=>{y()}),()=>{if(a.default===void 0){console.error("QVirtualScroll: default scoped slot is required for rendering");return}return e.type==="__qtable"?Ue({ref:r,class:"q-table__middle "+m.value},V()):o(Et[e.type],{...l,ref:r,class:[l.class,m.value],...w.value},V)}}});function Dt(e,a){const l=D(null),c=d(()=>e.disable===!0?null:o("span",{ref:l,class:"no-outline",tabindex:-1}));function r(f){const i=a.value;f!==void 0&&f.type.indexOf("key")===0?i!==null&&document.activeElement!==i&&i.contains(document.activeElement)===!0&&i.focus():l.value!==null&&(f===void 0||i!==null&&i.contains(f.target)===!0)&&l.value.focus()}return{refocusTargetEl:c,refocusTarget:r}}var At={xs:30,sm:35,md:40,lg:50,xl:60};const It={...Ce,..._t,...Bt,modelValue:{required:!0,default:null},val:{},trueValue:{default:!0},falseValue:{default:!1},indeterminateValue:{default:null},checkedIcon:String,uncheckedIcon:String,indeterminateIcon:String,toggleOrder:{type:String,validator:e=>e==="tf"||e==="ft"},toggleIndeterminate:Boolean,label:String,leftLabel:Boolean,color:String,keepColor:Boolean,dense:Boolean,disable:Boolean,tabindex:[String,Number]},Nt=["update:modelValue"];function Qt(e,a){const{props:l,slots:c,emit:r,proxy:f}=W(),{$q:i}=f,s=Pe(l,i),v=D(null),{refocusTargetEl:S,refocusTarget:n}=Dt(l,v),m=yt(l,At),w=d(()=>l.val!==void 0&&Array.isArray(l.modelValue)),P=d(()=>{const q=Q(l.val);return w.value===!0?l.modelValue.findIndex(L=>Q(L)===q):-1}),C=d(()=>w.value===!0?P.value>-1:Q(l.modelValue)===Q(l.trueValue)),_=d(()=>w.value===!0?P.value===-1:Q(l.modelValue)===Q(l.falseValue)),y=d(()=>C.value===!1&&_.value===!1),V=d(()=>l.disable===!0?-1:l.tabindex||0),p=d(()=>`q-${e} cursor-pointer no-outline row inline no-wrap items-center`+(l.disable===!0?" disabled":"")+(s.value===!0?` q-${e}--dark`:"")+(l.dense===!0?` q-${e}--dense`:"")+(l.leftLabel===!0?" reverse":"")),O=d(()=>{const q=C.value===!0?"truthy":_.value===!0?"falsy":"indet",L=l.color!==void 0&&(l.keepColor===!0||(e==="toggle"?C.value===!0:_.value!==!0))?` text-${l.color}`:"";return`q-${e}__inner relative-position non-selectable q-${e}__inner--${q}${L}`}),x=d(()=>{const q={type:"checkbox"};return l.name!==void 0&&Object.assign(q,{"^checked":C.value===!0?"checked":void 0,name:l.name,value:w.value===!0?l.val:l.trueValue}),q}),T=Ot(x),A=d(()=>{const q={tabindex:V.value,role:"checkbox","aria-label":l.label,"aria-checked":y.value===!0?"mixed":C.value===!0?"true":"false"};return l.disable===!0&&(q["aria-disabled"]="true"),q});function H(q){q!==void 0&&(Me(q),n(q)),l.disable!==!0&&r("update:modelValue",z(),q)}function z(){if(w.value===!0){if(C.value===!0){const q=l.modelValue.slice();return q.splice(P.value,1),q}return l.modelValue.concat([l.val])}if(C.value===!0){if(l.toggleOrder!=="ft"||l.toggleIndeterminate===!1)return l.falseValue}else if(_.value===!0){if(l.toggleOrder==="ft"||l.toggleIndeterminate===!1)return l.trueValue}else return l.toggleOrder!=="ft"?l.trueValue:l.falseValue;return l.indeterminateValue}function J(q){(q.keyCode===13||q.keyCode===32)&&Me(q)}function F(q){(q.keyCode===13||q.keyCode===32)&&H(q)}const j=a(C,y);return Object.assign(f,{toggle:H}),()=>{const q=j();l.disable!==!0&&T(q,"unshift",` q-${e}__native absolute q-ma-none q-pa-none`);const L=[o("div",{class:O.value,style:m.value},q)];S.value!==null&&L.push(S.value);const X=l.label!==void 0?Qe(c.default,[l.label]):ue(c.default);return X!==void 0&&L.push(o("div",{class:`q-${e}__label q-anchor--skip`},X)),o("div",{ref:v,class:p.value,...A.value,onClick:H,onKeydown:J,onKeyup:F},L)}}const Ht=o("div",{key:"svg",class:"q-checkbox__bg absolute"},[o("svg",{class:"q-checkbox__svg fit absolute-full",viewBox:"0 0 24 24","aria-hidden":"true"},[o("path",{class:"q-checkbox__truthy",fill:"none",d:"M1.73,12.91 8.1,19.28 22.79,4.59"}),o("path",{class:"q-checkbox__indet",d:"M4,14H20V10H4"})])]);var _e=G({name:"QCheckbox",props:It,emits:Nt,setup(e){function a(l,c){const r=d(()=>(l.value===!0?e.checkedIcon:c.value===!0?e.indeterminateIcon:e.uncheckedIcon)||null);return()=>r.value!==null?[o("div",{key:"icon",class:"q-checkbox__icon-container absolute-full flex flex-center no-wrap"},[o(qe,{class:"q-checkbox__icon",name:r.value})])]:[Ht]}return Qt("checkbox",a)}});let K=0;const zt={fullscreen:Boolean,noRouteFullscreenExit:Boolean},Ut=["update:fullscreen","fullscreen"];function Kt(){const e=W(),{props:a,emit:l,proxy:c}=e;let r,f,i;const s=D(!1);wt(e)===!0&&$(()=>c.$route.fullPath,()=>{a.noRouteFullscreenExit!==!0&&n()}),$(()=>a.fullscreen,m=>{s.value!==m&&v()}),$(s,m=>{l("update:fullscreen",m),l("fullscreen",m)});function v(){s.value===!0?n():S()}function S(){s.value!==!0&&(s.value=!0,i=c.$el.parentNode,i.replaceChild(f,c.$el),document.body.appendChild(c.$el),K++,K===1&&document.body.classList.add("q-body--fullscreen-mixin"),r={handler:n},Ee.add(r))}function n(){s.value===!0&&(r!==void 0&&(Ee.remove(r),r=void 0),i.replaceChild(c.$el,f),s.value=!1,K=Math.max(0,K-1),K===0&&(document.body.classList.remove("q-body--fullscreen-mixin"),c.$el.scrollIntoView!==void 0&&setTimeout(()=>{c.$el.scrollIntoView()})))}return Ae(()=>{f=document.createElement("span")}),Ie(()=>{a.fullscreen===!0&&S()}),Ne(n),Object.assign(c,{toggleFullscreen:v,setFullscreen:S,exitFullscreen:n}),{inFullscreen:s,toggleFullscreen:v}}function Gt(e,a){return new Date(e)-new Date(a)}const Wt={sortMethod:Function,binaryStateSort:Boolean,columnSortOrder:{type:String,validator:e=>e==="ad"||e==="da",default:"ad"}};function Jt(e,a,l,c){const r=d(()=>{const{sortBy:s}=a.value;return s&&l.value.find(v=>v.name===s)||null}),f=d(()=>e.sortMethod!==void 0?e.sortMethod:(s,v,S)=>{const n=l.value.find(P=>P.name===v);if(n===void 0||n.field===void 0)return s;const m=S===!0?-1:1,w=typeof n.field=="function"?P=>n.field(P):P=>P[n.field];return s.sort((P,C)=>{let _=w(P),y=w(C);return _==null?-1*m:y==null?1*m:n.sort!==void 0?n.sort(_,y,P,C)*m:we(_)===!0&&we(y)===!0?(_-y)*m:$e(_)===!0&&$e(y)===!0?Gt(_,y)*m:typeof _=="boolean"&&typeof y=="boolean"?(_-y)*m:([_,y]=[_,y].map(V=>(V+"").toLocaleString().toLowerCase()),_<y?-1*m:_===y?0:m)})});function i(s){let v=e.columnSortOrder;if(qt(s)===!0)s.sortOrder&&(v=s.sortOrder),s=s.name;else{const m=l.value.find(w=>w.name===s);m!==void 0&&m.sortOrder&&(v=m.sortOrder)}let{sortBy:S,descending:n}=a.value;S!==s?(S=s,n=v==="da"):e.binaryStateSort===!0?n=!n:n===!0?v==="ad"?S=null:n=!1:v==="ad"?n=!0:S=null,c({sortBy:S,descending:n,page:1})}return{columnToSort:r,computedSortMethod:f,sort:i}}const Xt={filter:[String,Object],filterMethod:Function};function Yt(e,a){const l=d(()=>e.filterMethod!==void 0?e.filterMethod:(c,r,f,i)=>{const s=r?r.toLowerCase():"";return c.filter(v=>f.some(S=>{const n=i(S,v)+"";return(n==="undefined"||n==="null"?"":n.toLowerCase()).indexOf(s)!==-1}))});return $(()=>e.filter,()=>{He(()=>{a({page:1},!0)})},{deep:!0}),{computedFilterMethod:l}}function Zt(e,a){for(const l in a)if(a[l]!==e[l])return!1;return!0}function je(e){return e.page<1&&(e.page=1),e.rowsPerPage!==void 0&&e.rowsPerPage<1&&(e.rowsPerPage=0),e}const el={pagination:Object,rowsPerPageOptions:{type:Array,default:()=>[5,7,10,15,20,25,50,0]},"onUpdate:pagination":[Function,Array]};function tl(e,a){const{props:l,emit:c}=e,r=D(Object.assign({sortBy:null,descending:!1,page:1,rowsPerPage:l.rowsPerPageOptions.length>0?l.rowsPerPageOptions[0]:5},l.pagination)),f=d(()=>{const n=l["onUpdate:pagination"]!==void 0?{...r.value,...l.pagination}:r.value;return je(n)}),i=d(()=>f.value.rowsNumber!==void 0);function s(n){v({pagination:n,filter:l.filter})}function v(n={}){He(()=>{c("request",{pagination:n.pagination||f.value,filter:n.filter||l.filter,getCellValue:a})})}function S(n,m){const w=je({...f.value,...n});if(Zt(f.value,w)===!0){i.value===!0&&m===!0&&s(w);return}if(i.value===!0){s(w);return}l.pagination!==void 0&&l["onUpdate:pagination"]!==void 0?c("update:pagination",w):r.value=w}return{innerPagination:r,computedPagination:f,isServerSide:i,requestServerInteraction:v,setPagination:S}}function ll(e,a,l,c,r,f){const{props:i,emit:s,proxy:{$q:v}}=e,S=d(()=>c.value===!0?l.value.rowsNumber||0:f.value),n=d(()=>{const{page:x,rowsPerPage:T}=l.value;return(x-1)*T}),m=d(()=>{const{page:x,rowsPerPage:T}=l.value;return x*T}),w=d(()=>l.value.page===1),P=d(()=>l.value.rowsPerPage===0?1:Math.max(1,Math.ceil(S.value/l.value.rowsPerPage))),C=d(()=>m.value===0?!0:l.value.page>=P.value),_=d(()=>(i.rowsPerPageOptions.includes(a.value.rowsPerPage)?i.rowsPerPageOptions:[a.value.rowsPerPage].concat(i.rowsPerPageOptions)).map(T=>({label:T===0?v.lang.table.allRows:""+T,value:T})));$(P,(x,T)=>{if(x===T)return;const A=l.value.page;x&&!A?r({page:1}):x<A&&r({page:x})});function y(){r({page:1})}function V(){const{page:x}=l.value;x>1&&r({page:x-1})}function p(){const{page:x,rowsPerPage:T}=l.value;m.value>0&&x*T<S.value&&r({page:x+1})}function O(){r({page:P.value})}return i["onUpdate:pagination"]!==void 0&&s("update:pagination",{...l.value}),{firstRowIndex:n,lastRowIndex:m,isFirstPage:w,isLastPage:C,pagesNumber:P,computedRowsPerPageOptions:_,computedRowsNumber:S,firstPage:y,prevPage:V,nextPage:p,lastPage:O}}const al={selection:{type:String,default:"none",validator:e=>["single","multiple","none"].includes(e)},selected:{type:Array,default:()=>[]}},nl=["update:selected","selection"];function ol(e,a,l,c){const r=d(()=>{const C={};return e.selected.map(c.value).forEach(_=>{C[_]=!0}),C}),f=d(()=>e.selection!=="none"),i=d(()=>e.selection==="single"),s=d(()=>e.selection==="multiple"),v=d(()=>l.value.length>0&&l.value.every(C=>r.value[c.value(C)]===!0)),S=d(()=>v.value!==!0&&l.value.some(C=>r.value[c.value(C)]===!0)),n=d(()=>e.selected.length);function m(C){return r.value[C]===!0}function w(){a("update:selected",[])}function P(C,_,y,V){a("selection",{rows:_,added:y,keys:C,evt:V});const p=i.value===!0?y===!0?_:[]:y===!0?e.selected.concat(_):e.selected.filter(O=>C.includes(c.value(O))===!1);a("update:selected",p)}return{hasSelectionMode:f,singleSelection:i,multipleSelection:s,allRowsSelected:v,someRowsSelected:S,rowsSelectedNumber:n,isRowSelected:m,clearSelection:w,updateSelection:P}}function De(e){return Array.isArray(e)?e.slice():[]}const rl={expanded:Array},il=["update:expanded"];function ul(e,a){const l=D(De(e.expanded));$(()=>e.expanded,i=>{l.value=De(i)});function c(i){return l.value.includes(i)}function r(i){e.expanded!==void 0?a("update:expanded",i):l.value=i}function f(i,s){const v=l.value.slice(),S=v.indexOf(i);s===!0?S===-1&&(v.push(i),r(v)):S!==-1&&(v.splice(S,1),r(v))}return{isRowExpanded:c,setExpanded:r,updateExpanded:f}}const sl={visibleColumns:Array};function cl(e,a,l){const c=d(()=>{if(e.columns!==void 0)return e.columns;const s=e.rows[0];return s!==void 0?Object.keys(s).map(v=>({name:v,label:v.toUpperCase(),field:v,align:we(s[v])?"right":"left",sortable:!0})):[]}),r=d(()=>{const{sortBy:s,descending:v}=a.value;return(e.visibleColumns!==void 0?c.value.filter(n=>n.required===!0||e.visibleColumns.includes(n.name)===!0):c.value).map(n=>{const m=n.align||"right",w=`text-${m}`;return{...n,align:m,__iconClass:`q-table__sort-icon q-table__sort-icon--${m}`,__thClass:w+(n.headerClasses!==void 0?" "+n.headerClasses:"")+(n.sortable===!0?" sortable":"")+(n.name===s?` sorted ${v===!0?"sort-desc":""}`:""),__tdStyle:n.style!==void 0?typeof n.style!="function"?()=>n.style:n.style:()=>null,__tdClass:n.classes!==void 0?typeof n.classes!="function"?()=>w+" "+n.classes:P=>w+" "+n.classes(P):()=>w}})}),f=d(()=>{const s={};return r.value.forEach(v=>{s[v.name]=v}),s}),i=d(()=>e.tableColspan!==void 0?e.tableColspan:r.value.length+(l.value===!0?1:0));return{colList:c,computedCols:r,computedColsMap:f,computedColspan:i}}const ie="q-table__bottom row items-center",Ke={};ze.forEach(e=>{Ke[e]={}});var Sl=G({name:"QTable",props:{rows:{type:Array,default:()=>[]},rowKey:{type:[String,Function],default:"id"},columns:Array,loading:Boolean,iconFirstPage:String,iconPrevPage:String,iconNextPage:String,iconLastPage:String,title:String,hideHeader:Boolean,grid:Boolean,gridHeader:Boolean,dense:Boolean,flat:Boolean,bordered:Boolean,square:Boolean,separator:{type:String,default:"horizontal",validator:e=>["horizontal","vertical","cell","none"].includes(e)},wrapCells:Boolean,virtualScroll:Boolean,virtualScrollTarget:{default:void 0},...Ke,noDataLabel:String,noResultsLabel:String,loadingLabel:String,selectedRowsLabel:Function,rowsPerPageLabel:String,paginationLabel:Function,color:{type:String,default:"grey-8"},titleClass:[String,Array,Object],tableStyle:[String,Array,Object],tableClass:[String,Array,Object],tableHeaderStyle:[String,Array,Object],tableHeaderClass:[String,Array,Object],cardContainerClass:[String,Array,Object],cardContainerStyle:[String,Array,Object],cardStyle:[String,Array,Object],cardClass:[String,Array,Object],hideBottom:Boolean,hideSelectedBanner:Boolean,hideNoData:Boolean,hidePagination:Boolean,onRowClick:Function,onRowDblclick:Function,onRowContextmenu:Function,...Ce,...zt,...sl,...Xt,...el,...rl,...al,...Wt},emits:["request","virtual-scroll",...Ut,...il,...nl],setup(e,{slots:a,emit:l}){const c=W(),{proxy:{$q:r}}=c,f=Pe(e,r),{inFullscreen:i,toggleFullscreen:s}=Kt(),v=d(()=>typeof e.rowKey=="function"?e.rowKey:t=>t[e.rowKey]),S=D(null),n=D(null),m=d(()=>e.grid!==!0&&e.virtualScroll===!0),w=d(()=>" q-table__card"+(f.value===!0?" q-table__card--dark q-dark":"")+(e.square===!0?" q-table--square":"")+(e.flat===!0?" q-table--flat":"")+(e.bordered===!0?" q-table--bordered":"")),P=d(()=>`q-table__container q-table--${e.separator}-separator column no-wrap`+(e.grid===!0?" q-table--grid":w.value)+(f.value===!0?" q-table--dark":"")+(e.dense===!0?" q-table--dense":"")+(e.wrapCells===!1?" q-table--no-wrap":"")+(i.value===!0?" fullscreen scroll":"")),C=d(()=>P.value+(e.loading===!0?" q-table--loading":""));$(()=>e.tableStyle+e.tableClass+e.tableHeaderStyle+e.tableHeaderClass+P.value,()=>{m.value===!0&&n.value!==null&&n.value.reset()});const{innerPagination:_,computedPagination:y,isServerSide:V,requestServerInteraction:p,setPagination:O}=tl(c,I),{computedFilterMethod:x}=Yt(e,O),{isRowExpanded:T,setExpanded:A,updateExpanded:H}=ul(e,l),z=d(()=>{let t=e.rows;if(V.value===!0||t.length===0)return t;const{sortBy:u,descending:g}=y.value;return e.filter&&(t=x.value(t,e.filter,M.value,I)),Je.value!==null&&(t=Xe.value(e.rows===t?t.slice():t,u,g)),t}),J=d(()=>z.value.length),F=d(()=>{let t=z.value;if(V.value===!0)return t;const{rowsPerPage:u}=y.value;return u!==0&&(Z.value===0&&e.rows!==t?t.length>ee.value&&(t=t.slice(0,ee.value)):t=t.slice(Z.value,ee.value)),t}),{hasSelectionMode:j,singleSelection:q,multipleSelection:L,allRowsSelected:X,someRowsSelected:ke,rowsSelectedNumber:se,isRowSelected:ce,clearSelection:Ge,updateSelection:Y}=ol(e,l,F,v),{colList:We,computedCols:M,computedColsMap:xe,computedColspan:Re}=cl(e,y,j),{columnToSort:Je,computedSortMethod:Xe,sort:de}=Jt(e,y,We,O),{firstRowIndex:Z,lastRowIndex:ee,isFirstPage:ve,isLastPage:fe,pagesNumber:te,computedRowsPerPageOptions:Ye,computedRowsNumber:le,firstPage:ge,prevPage:be,nextPage:me,lastPage:Se}=ll(c,_,y,V,O,J),Ze=d(()=>F.value.length===0),et=d(()=>{const t={};return ze.forEach(u=>{t[u]=e[u]}),t.virtualScrollItemSize===void 0&&(t.virtualScrollItemSize=e.dense===!0?28:48),t});function tt(){m.value===!0&&n.value.reset()}function lt(){if(e.grid===!0)return gt();const t=e.hideHeader!==!0?Oe:null;if(m.value===!0){const g=a["top-row"],b=a["bottom-row"],h={default:R=>Ve(R.item,a.body,R.index)};if(g!==void 0){const R=o("tbody",g({cols:M.value}));h.before=t===null?()=>R:()=>[t()].concat(R)}else t!==null&&(h.before=t);return b!==void 0&&(h.after=()=>o("tbody",b({cols:M.value}))),o(jt,{ref:n,class:e.tableClass,style:e.tableStyle,...et.value,scrollTarget:e.virtualScrollTarget,items:F.value,type:"__qtable",tableColspan:Re.value,onVirtualScroll:nt},h)}const u=[ot()];return t!==null&&u.unshift(t()),Ue({class:["q-table__middle scroll",e.tableClass],style:e.tableStyle},u)}function at(t,u){if(n.value!==null){n.value.scrollTo(t,u);return}t=parseInt(t,10);const g=S.value.querySelector(`tbody tr:nth-of-type(${t+1})`);if(g!==null){const b=S.value.querySelector(".q-table__middle.scroll"),h=g.offsetTop-e.virtualScrollStickySizeStart,R=h<b.scrollTop?"decrease":"increase";b.scrollTop=h,l("virtual-scroll",{index:t,from:0,to:_.value.rowsPerPage-1,direction:R})}}function nt(t){l("virtual-scroll",t)}function Te(){return[o(pt,{class:"q-table__linear-progress",color:e.color,dark:f.value,indeterminate:!0,trackColor:"transparent"})]}function Ve(t,u,g){const b=v.value(t),h=ce(b);if(u!==void 0)return u(pe({key:b,row:t,pageIndex:g,__trClass:h?"selected":""}));const R=a["body-cell"],k=M.value.map(B=>{const ne=a[`body-cell-${B.name}`],oe=ne!==void 0?ne:R;return oe!==void 0?oe(rt({key:b,row:t,pageIndex:g,col:B})):o("td",{class:B.__tdClass(t),style:B.__tdStyle(t)},I(B,t))});if(j.value===!0){const B=a["body-selection"],ne=B!==void 0?B(it({key:b,row:t,pageIndex:g})):[o(_e,{modelValue:h,color:e.color,dark:f.value,dense:e.dense,"onUpdate:modelValue":(oe,bt)=>{Y([b],[t],oe,bt)}})];k.unshift(o("td",{class:"q-table--col-auto-width"},ne))}const E={key:b,class:{selected:h}};return e.onRowClick!==void 0&&(E.class["cursor-pointer"]=!0,E.onClick=B=>{l("RowClick",B,t,g)}),e.onRowDblclick!==void 0&&(E.class["cursor-pointer"]=!0,E.onDblclick=B=>{l("RowDblclick",B,t,g)}),e.onRowContextmenu!==void 0&&(E.class["cursor-pointer"]=!0,E.onContextmenu=B=>{l("RowContextmenu",B,t,g)}),o("tr",E,k)}function ot(){const t=a.body,u=a["top-row"],g=a["bottom-row"];let b=F.value.map((h,R)=>Ve(h,t,R));return u!==void 0&&(b=u({cols:M.value}).concat(b)),g!==void 0&&(b=b.concat(g({cols:M.value}))),o("tbody",b)}function pe(t){return he(t),t.cols=t.cols.map(u=>{const g={...u};return U(g,"value",()=>I(u,t.row)),g}),t}function rt(t){return he(t),U(t,"value",()=>I(t.col,t.row)),t}function it(t){return he(t),t}function he(t){Object.assign(t,{cols:M.value,colsMap:xe.value,sort:de,rowIndex:Z.value+t.pageIndex,color:e.color,dark:f.value,dense:e.dense}),j.value===!0&&U(t,"selected",()=>ce(t.key),(u,g)=>{Y([t.key],[t.row],u,g)}),U(t,"expand",()=>T(t.key),u=>{H(t.key,u)})}function I(t,u){const g=typeof t.field=="function"?t.field(u):u[t.field];return t.format!==void 0?t.format(g,u):g}const N=d(()=>({pagination:y.value,pagesNumber:te.value,isFirstPage:ve.value,isLastPage:fe.value,firstPage:ge,prevPage:be,nextPage:me,lastPage:Se,inFullscreen:i.value,toggleFullscreen:s}));function ut(){const t=a.top,u=a["top-left"],g=a["top-right"],b=a["top-selection"],h=j.value===!0&&b!==void 0&&se.value>0,R="q-table__top relative-position row items-center";if(t!==void 0)return o("div",{class:R},[t(N.value)]);let k;if(h===!0?k=b(N.value).slice():(k=[],u!==void 0?k.push(o("div",{class:"q-table-control"},[u(N.value)])):e.title&&k.push(o("div",{class:"q-table__control"},[o("div",{class:["q-table__title",e.titleClass]},e.title)]))),g!==void 0&&(k.push(o("div",{class:"q-table__separator col"})),k.push(o("div",{class:"q-table__control"},[g(N.value)]))),k.length!==0)return o("div",{class:R},k)}const Be=d(()=>ke.value===!0?null:X.value);function Oe(){const t=st();return e.loading===!0&&a.loading===void 0&&t.push(o("tr",{class:"q-table__progress"},[o("th",{class:"relative-position",colspan:Re.value},Te())])),o("thead",t)}function st(){const t=a.header,u=a["header-cell"];if(t!==void 0)return t(ye({header:!0})).slice();const g=M.value.map(b=>{const h=a[`header-cell-${b.name}`],R=h!==void 0?h:u,k=ye({col:b});return R!==void 0?R(k):o(Ft,{key:b.name,props:k},()=>b.label)});if(q.value===!0&&e.grid!==!0)g.unshift(o("th",{class:"q-table--col-auto-width"}," "));else if(L.value===!0){const b=a["header-selection"],h=b!==void 0?b(ye({})):[o(_e,{color:e.color,modelValue:Be.value,dark:f.value,dense:e.dense,"onUpdate:modelValue":Fe})];g.unshift(o("th",{class:"q-table--col-auto-width"},h))}return[o("tr",{class:e.tableHeaderClass,style:e.tableHeaderStyle},g)]}function ye(t){return Object.assign(t,{cols:M.value,sort:de,colsMap:xe.value,color:e.color,dark:f.value,dense:e.dense}),L.value===!0&&U(t,"selected",()=>Be.value,Fe),t}function Fe(t){ke.value===!0&&(t=!1),Y(F.value.map(v.value),F.value,t)}const ae=d(()=>{const t=[e.iconFirstPage||r.iconSet.table.firstPage,e.iconPrevPage||r.iconSet.table.prevPage,e.iconNextPage||r.iconSet.table.nextPage,e.iconLastPage||r.iconSet.table.lastPage];return r.lang.rtl===!0?t.reverse():t});function ct(){if(e.hideBottom===!0)return;if(Ze.value===!0){if(e.hideNoData===!0)return;const g=e.loading===!0?e.loadingLabel||r.lang.table.loading:e.filter?e.noResultsLabel||r.lang.table.noResults:e.noDataLabel||r.lang.table.noData,b=a["no-data"],h=b!==void 0?[b({message:g,icon:r.iconSet.table.warning,filter:e.filter})]:[o(qe,{class:"q-table__bottom-nodata-icon",name:r.iconSet.table.warning}),g];return o("div",{class:ie+" q-table__bottom--nodata"},h)}const t=a.bottom;if(t!==void 0)return o("div",{class:ie},[t(N.value)]);const u=e.hideSelectedBanner!==!0&&j.value===!0&&se.value>0?[o("div",{class:"q-table__control"},[o("div",[(e.selectedRowsLabel||r.lang.table.selectedRecords)(se.value)])])]:[];if(e.hidePagination!==!0)return o("div",{class:ie+" justify-end"},vt(u));if(u.length>0)return o("div",{class:ie},u)}function dt(t){O({page:1,rowsPerPage:t.value})}function vt(t){let u;const{rowsPerPage:g}=y.value,b=e.paginationLabel||r.lang.table.pagination,h=a.pagination,R=e.rowsPerPageOptions.length>1;if(t.push(o("div",{class:"q-table__separator col"})),R===!0&&t.push(o("div",{class:"q-table__control"},[o("span",{class:"q-table__bottom-item"},[e.rowsPerPageLabel||r.lang.table.recordsPerPage]),o(Vt,{class:"q-table__select inline q-table__bottom-item",color:e.color,modelValue:g,options:Ye.value,displayValue:g===0?r.lang.table.allRows:g,dark:f.value,borderless:!0,dense:!0,optionsDense:!0,optionsCover:!0,"onUpdate:modelValue":dt})])),h!==void 0)u=h(N.value);else if(u=[o("span",g!==0?{class:"q-table__bottom-item"}:{},[g?b(Z.value+1,Math.min(ee.value,le.value),le.value):b(1,J.value,le.value)])],g!==0&&te.value>1){const k={color:e.color,round:!0,dense:!0,flat:!0};e.dense===!0&&(k.size="sm"),te.value>2&&u.push(o(re,{key:"pgFirst",...k,icon:ae.value[0],disable:ve.value,onClick:ge})),u.push(o(re,{key:"pgPrev",...k,icon:ae.value[1],disable:ve.value,onClick:be}),o(re,{key:"pgNext",...k,icon:ae.value[2],disable:fe.value,onClick:me})),te.value>2&&u.push(o(re,{key:"pgLast",...k,icon:ae.value[3],disable:fe.value,onClick:Se}))}return t.push(o("div",{class:"q-table__control"},u)),t}function ft(){const t=e.gridHeader===!0?[o("table",{class:"q-table"},[Oe()])]:e.loading===!0&&a.loading===void 0?Te():void 0;return o("div",{class:"q-table__middle"},t)}function gt(){const t=a.item!==void 0?a.item:u=>{const g=u.cols.map(h=>o("div",{class:"q-table__grid-item-row"},[o("div",{class:"q-table__grid-item-title"},[h.label]),o("div",{class:"q-table__grid-item-value"},[h.value])]));if(j.value===!0){const h=a["body-selection"],R=h!==void 0?h(u):[o(_e,{modelValue:u.selected,color:e.color,dark:f.value,dense:e.dense,"onUpdate:modelValue":(k,E)=>{Y([u.key],[u.row],k,E)}})];g.unshift(o("div",{class:"q-table__grid-item-row"},R),o(Pt,{dark:f.value}))}const b={class:["q-table__grid-item-card"+w.value,e.cardClass],style:e.cardStyle};return(e.onRowClick!==void 0||e.onRowDblclick!==void 0)&&(b.class[0]+=" cursor-pointer",e.onRowClick!==void 0&&(b.onClick=h=>{l("RowClick",h,u.row,u.pageIndex)}),e.onRowDblclick!==void 0&&(b.onDblclick=h=>{l("RowDblclick",h,u.row,u.pageIndex)})),o("div",{class:"q-table__grid-item col-xs-12 col-sm-6 col-md-4 col-lg-3"+(u.selected===!0?" q-table__grid-item--selected":"")},[o("div",b,g)])};return o("div",{class:["q-table__grid-content row",e.cardContainerClass],style:e.cardContainerStyle},F.value.map((u,g)=>t(pe({key:v.value(u),row:u,pageIndex:g}))))}return Object.assign(c.proxy,{requestServerInteraction:p,setPagination:O,firstPage:ge,prevPage:be,nextPage:me,lastPage:Se,isRowSelected:ce,clearSelection:Ge,isRowExpanded:T,setExpanded:A,sort:de,resetVirtualScroll:tt,scrollTo:at,getCellValue:I}),Ct(c.proxy,{filteredSortedRows:()=>z.value,computedRows:()=>F.value,computedRowsNumber:()=>le.value}),()=>{const t=[ut()],u={ref:S,class:C.value};return e.grid===!0?t.push(ft()):Object.assign(u,{class:[u.class,e.cardClass],style:e.cardStyle}),t.push(lt(),ct()),e.loading===!0&&a.loading!==void 0&&t.push(a.loading()),o("div",u,t)}}});export{Sl as Q,Nt as a,Qt as b,_e as c,It as u};
