import{Q as c,b as i}from"./datasources.store.1a690bc7.js";import{r as n,o as m,q as p,s as e,B as o,v as a,y as _,aV as d,an as h}from"./index.2aa9959a.js";import{Q as f,a as u}from"./format.21ab1503.js";import{Q as g}from"./QScrollArea.7a809c5c.js";import{_ as Q}from"./Graph.e996763a.js";import"./use-dark.174efb17.js";import"./QResizeObserver.1f98e7c3.js";import"./QScrollObserver.82bca2c2.js";import"./scroll.dc5aaab7.js";import"./touch.70a9dd44.js";import"./use-quasar.307c716c.js";const v={class:"col-2",style:{background:"white"}},b=o("h5",{class:"q-pa-md"},"Schema",-1),w={class:"col-10"},J={__name:"ViewGraph",setup(y){const s=n(""),r=h(),l=()=>{console.log("setting global schema view"),d.prueba().then(t=>{console.log("response",t.data),s.value=JSON.stringify(t.data)}).catch(t=>{console.log("error addding ds: ",t),r.negative("Something went wrong in the server.")})};return(t,S)=>(m(),p(c,{class:"row items-stretch"},{default:e(()=>[o("div",v,[a(g,{class:"fit"},{default:e(()=>[a(f,null,{default:e(()=>[a(u,null,{default:e(()=>[a(i,null,{default:e(()=>[b]),_:1}),a(_,{color:"primary",label:"Get",onClick:l})]),_:1})]),_:1})]),_:1})]),o("div",w,[a(Q,{graphical:s.value},null,8,["graphical"])])]),_:1}))}};export{J as default};