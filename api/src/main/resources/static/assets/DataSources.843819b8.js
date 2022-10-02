import{b as z,u as M,Q}from"./datasources.store.aceb95f7.js";import{b as q,a as S,h as T,a9 as N,r as w,ax as $,o as n,c as H,v as o,aF as I,s as a,y as c,B as l,A as u,ag as _,q as d,D as k,C as f,x as j,i as E,z as G}from"./index.876f1426.js";import{j as P}from"./QForm.5803165a.js";import{Q as A}from"./QTooltip.4e6178e1.js";import{C as L,Q as x}from"./ClosePopup.5e0eec82.js";import{Q as y}from"./QTd.64061ba1.js";import{Q as W}from"./QTable.c360863b.js";import{_ as R}from"./FormNewDataSource.5b074d24.js";import"./use-dark.9ce0ddae.js";import"./uid.42677368.js";import"./focus-manager.32f8d49a.js";import"./use-prevent-scroll.1ba9ffd6.js";import"./format.30be34e3.js";import"./use-model-toggle.659365cc.js";import"./scroll.09115f1d.js";import"./QSeparator.075b25f8.js";const U=["top","middle","bottom"];var X=q({name:"QBadge",props:{color:String,textColor:String,floating:Boolean,transparent:Boolean,multiLine:Boolean,outline:Boolean,rounded:Boolean,label:[Number,String],align:{type:String,validator:e=>U.includes(e)}},setup(e,{slots:m}){const g=S(()=>e.align!==void 0?{verticalAlign:e.align}:null),r=S(()=>{const i=e.outline===!0&&e.color||e.textColor;return`q-badge flex inline items-center no-wrap q-badge--${e.multiLine===!0?"multi":"single"}-line`+(e.outline===!0?" q-badge--outline":e.color!==void 0?` bg-${e.color}`:"")+(i!==void 0?` text-${i}`:"")+(e.floating===!0?" q-badge--floating":"")+(e.rounded===!0?" q-badge--rounded":"")+(e.transparent===!0?" q-badge--transparent":"")});return()=>T("div",{class:r.value,style:g.value,role:"alert","aria-label":e.label},N(m.default,e.label!==void 0?[e.label]:[]))}});const Z={class:"q-pa-md"},O={class:"q-table__title"},J=l("div",{class:"full-width row flex-center text-accent q-gutter-sm q-pa-xl",style:{"flex-direction":"column"}},[l("svg",{width:"160px",height:"88px",viewBox:"0 0 216 120",fill:"none",xmlns:"http://www.w3.org/2000/svg",class:"sc-jIkXHa sc-ZOtfp fXAzWm jPTZgW"},[l("g",{opacity:"0.84","clip-path":"url(#EmptyDocuments_svg__clip0_1142_57509)"},[l("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M189.25 19.646a7.583 7.583 0 010 15.166h-43.333a7.583 7.583 0 010 15.167h23.833a7.583 7.583 0 010 15.167h-11.022c-5.28 0-9.561 3.395-9.561 7.583 0 1.956 1.063 3.782 3.19 5.48 2.017 1.608 4.824 1.817 7.064 3.096a7.583 7.583 0 01-3.754 14.174H65.75a7.583 7.583 0 010-15.166H23.5a7.583 7.583 0 110-15.167h43.333a7.583 7.583 0 100-15.167H39.75a7.583 7.583 0 110-15.166h43.333a7.583 7.583 0 010-15.167H189.25zm0 30.333a7.583 7.583 0 110 15.166 7.583 7.583 0 010-15.166z",fill:"#D9D8FF","fill-opacity":"0.8"}),l("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M132.561 19.646l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162zM73.162 26.33l4.97-.557-4.97.557z",fill:"#fff"}),l("path",{d:"M73.162 26.33l4.97-.557m54.429-6.127l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162z",stroke:"#7B79FF","stroke-width":"2.5"}),l("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M129.818 24.27l9.122 66.608.82 6.682c.264 2.153-1.246 4.11-3.373 4.371l-56.812 6.976c-2.127.261-4.066-1.272-4.33-3.425l-8.83-71.908a2.167 2.167 0 011.887-2.415l7.028-.863",fill:"#F0F0FF"}),l("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M135.331 5.833H85.978a2.97 2.97 0 00-2.107.873A2.97 2.97 0 0083 8.813v82.333c0 .823.333 1.567.872 2.106a2.97 2.97 0 002.107.873h63.917a2.97 2.97 0 002.106-.873 2.97 2.97 0 00.873-2.106V23.367a2.98 2.98 0 00-.873-2.107L137.437 6.705a2.98 2.98 0 00-2.106-.872z",fill:"#fff",stroke:"#7B79FF","stroke-width":"2.5"}),l("path",{d:"M135.811 7.082v12.564a3.25 3.25 0 003.25 3.25h8.595M94.644 78.146h28.167m-28.167-55.25h28.167-28.167zm0 13h46.584-46.584zm0 14.083h46.584-46.584zm0 14.084h46.584-46.584z",stroke:"#7B79FF","stroke-width":"2.5","stroke-linecap":"round","stroke-linejoin":"round"})]),l("defs",null,[l("clipPath",{id:"EmptyDocuments_svg__clip0_1142_57509"},[l("path",{fill:"#fff",d:"M0 0h216v120H0z"})])])]),l("span",{style:{color:"rgb(102, 102, 135)","font-weight":"500","font-size":"1rem","line-height":"1.25"}},"Data sources not found."),l("span",{style:{color:"rgb(102, 102, 135)","font-weight":"500","font-size":"1rem","line-height":"1.25"}},"To integrate data sources with the project, please add at least two sources.")],-1),K={__name:"TableDataSources",props:{no_shadow:{type:Boolean,default:!1},view:{type:String,default:"datasources"}},setup(e){const m=e,g=w(!1),r=z(),i=M();$(()=>{r.setProject(),i.init()});const C=[{name:"id",label:"Id",align:"center",field:"id",sortable:!0},{name:"Name",label:"Name",align:"center",field:"name",sortable:!0},{name:"Type",label:"Type",align:"center",field:"type",sortable:!0},{name:"View Metadata",label:"View Metadata",align:"center",field:"View Metadata",sortable:!1},{name:"View_Source_Graph",label:"Source Graph",align:"center",field:"View Source Graph",sortable:!1},{name:"actions",label:"actions",align:"center",field:"actions",sortable:!1}],B={integration:["Name","Type"],datasources:["Name","Type","#Wrappers","View Metadata","View_Source_Graph","actions"]},F="Data Sources",h=w(""),D=B[m.view],v=w(!1),V=b=>{r.deleteDataSource(b.row)};return(b,s)=>(n(),H("div",Z,[o(W,{grid:g.value,ref:"tableRef",rows:f(r).datasources,columns:C,filter:h.value,class:G({"no-shadow":e.no_shadow}),"row-key":"id","no-data-label":"I didn't find anything for you. Consider creating a new data source.","no-results-label":"The filter didn't uncover any results","visible-columns":f(D)},I({"top-left":a(()=>[l("div",O,[u(_(F)+" "),e.view==="datasources"?(n(),d(c,{key:0,unelevated:"",padding:"none",color:"primary700",icon:"add",onClick:s[0]||(s[0]=t=>v.value=!0)})):k("",!0)])]),"top-right":a(t=>[f(i).isDSEmpty?k("",!0):(n(),d(c,{key:0,outline:"",color:"primary",label:"Finish pending sources",class:"q-mr-xs",to:{name:"dsIntegration"}},{default:a(()=>[o(X,{color:"orange",floating:""},{default:a(()=>[u(_(f(i).datasources.length),1)]),_:1})]),_:1})),o(P,{outlined:"",dense:"",debounce:"400",color:"primary",modelValue:h.value,"onUpdate:modelValue":s[1]||(s[1]=p=>h.value=p)},{append:a(()=>[o(j,{name:"search"})]),_:1},8,["modelValue"]),o(c,{flat:"",round:"",dense:"",icon:t.inFullscreen?"fullscreen_exit":"fullscreen",onClick:t.toggleFullscreen},{default:a(()=>[E((n(),d(A,{disable:b.$q.platform.is.mobile},{default:a(()=>[u(_(t.inFullscreen?"Exit Fullscreen":"Toggle Fullscreen"),1)]),_:2},1032,["disable"])),[[L]])]),_:2},1032,["icon","onClick"])]),"body-cell-status":a(t=>[o(y,{props:t},{default:a(()=>[t.row.graphicalGraph===""?(n(),d(x,{key:0,"text-color":"white",color:"accent"},{default:a(()=>[u(" Missing Data Sources ")]),_:1})):(n(),d(x,{key:1,"text-color":"white",color:"blue"},{default:a(()=>[u(" Completed")]),_:1}))]),_:2},1032,["props"])]),"body-cell-View_Source_Graph":a(t=>[o(y,{props:t},{default:a(()=>[o(c,{dense:"",round:"",flat:"",color:"grey",icon:"download",onClick:p=>f(r).downloadSource(t.row.id)},null,8,["onClick"])]),_:2},1032,["props"])]),"no-data":a(({icon:t,message:p,filter:Y})=>[J]),_:2},[e.view==="datasources"?{name:"body-cell-actions",fn:a(t=>[o(y,{props:t},{default:a(()=>[o(c,{dense:"",round:"",flat:"",color:"grey",onClick:p=>V(t),icon:"delete"},null,8,["onClick"])]),_:2},1032,["props"])]),key:"0"}:void 0]),1032,["grid","rows","filter","class","visible-columns"]),o(R,{show:v.value,"onUpdate:show":s[2]||(s[2]=t=>v.value=t)},null,8,["show"])]))}},he={__name:"DataSources",setup(e){return(m,g)=>(n(),d(Q,null,{default:a(()=>[o(K)]),_:1}))}};export{he as default};
