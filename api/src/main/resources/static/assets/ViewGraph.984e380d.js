import{Q as c,b as i}from"./datasources.store.11e61b1c.js";import{r as n,o as m,q as p,s as e,B as o,v as a,y as _,aV as d,au as h}from"./index.d68f4a5a.js";import{Q as u,a as f}from"./QList.a7d2d4d7.js";import{Q as g}from"./QScrollArea.b3a0f22c.js";import{_ as Q}from"./Graph.b4659d0f.js";import"./use-dark.cac0eb06.js";import"./QResizeObserver.483c420e.js";import"./QScrollObserver.478033f2.js";import"./touch.70a9dd44.js";import"./format.2a3572e1.js";import"./use-quasar.0d998acc.js";const v={class:"col-2",style:{background:"white"}},b=o("h5",{class:"q-pa-md"},"Schema",-1),w={class:"col-10"},J={__name:"ViewGraph",setup(y){const s=n(""),r=h(),l=()=>{console.log("setting global schema view"),d.prueba().then(t=>{console.log("response",t.data),s.value=JSON.stringify(t.data)}).catch(t=>{console.log("error addding ds: ",t),r.negative("Something went wrong in the server.")})};return(t,S)=>(m(),p(c,{class:"row items-stretch"},{default:e(()=>[o("div",v,[a(g,{class:"fit"},{default:e(()=>[a(u,null,{default:e(()=>[a(f,null,{default:e(()=>[a(i,null,{default:e(()=>[b]),_:1}),a(_,{color:"primary",label:"Get",onClick:l})]),_:1})]),_:1})]),_:1})]),o("div",w,[a(Q,{graphical:s.value},null,8,["graphical"])])]),_:1}))}};export{J as default};
