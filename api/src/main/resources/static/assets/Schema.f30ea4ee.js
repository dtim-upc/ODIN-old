import{Q as c}from"./use-model-toggle.21d80c23.js";import{Q as n,a as S}from"./format.21ab1503.js";import{r as i,g as b,o as m,q as _,s as a,B as u,v as e,y as f,z as h,c as Q,aA as w,C as x,aB as B}from"./index.2aa9959a.js";import{Q as g}from"./QExpansionItem.77b210b3.js";import{Q as k}from"./QScrollArea.7a809c5c.js";import{u as C,Q as G}from"./datasources.store.1a690bc7.js";import{_ as j}from"./Graph.e996763a.js";import"./use-dark.174efb17.js";import"./QSlideTransition.c9ba564f.js";import"./QSeparator.2e830016.js";import"./uid.42677368.js";import"./QResizeObserver.1f98e7c3.js";import"./QScrollObserver.82bca2c2.js";import"./scroll.dc5aaab7.js";import"./touch.70a9dd44.js";import"./use-quasar.307c716c.js";const y={class:"col-2 columnHeader"},I=u("h5",null,"Schema",-1),L={class:"col-10"},W={__name:"Schema",setup($){i(!0);const l=C(),s=i(""),t=i(""),v=r=>{t.value=r.id,s.value=r.graphicalSchema},d=()=>{console.log("setting global schema view"),console.log(l.getGlobalSchema),t.value="project",s.value=l.getGlobalSchema};return b(()=>{l.datasources.length>0&&d()}),(r,p)=>(m(),_(G,{class:"row items-stretch"},{default:a(()=>[u("div",y,[e(k,{class:"fit"},{default:a(()=>[e(n,null,{default:a(()=>[e(S,null,{default:a(()=>[e(c,null,{default:a(()=>[I]),_:1})]),_:1}),e(g,{label:"Global schema","expand-icon":"arrow_drop_down","default-opened":""},{default:a(()=>[e(n,{dense:""},{default:a(()=>[e(c,null,{default:a(()=>[e(f,{flat:"",padding:"xs",label:"project",class:h(["full-width",t.value=="project"?"activebg":""]),align:"left",onClick:p[0]||(p[0]=o=>d())},null,8,["class"])]),_:1})]),_:1})]),_:1}),e(g,{label:"Local schemata","expand-icon":"arrow_drop_down"},{default:a(()=>[e(n,{dense:""},{default:a(()=>[(m(!0),Q(B,null,w(x(l).datasources,o=>(m(),_(c,null,{default:a(()=>[e(f,{flat:"",padding:"xs",label:o.name,class:h(["full-width",t.value==o.id?"activebg":""]),align:"left",onClick:z=>v(o)},null,8,["label","class","onClick"])]),_:2},1024))),256))]),_:1})]),_:1})]),_:1})]),_:1})]),u("div",L,[e(j,{graphical:s.value},null,8,["graphical"])])]),_:1}))}};export{W as default};