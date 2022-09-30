import{b as v,w as b,g as h,e as S,R as T,m as x,a6 as C}from"./index.1c9f1618.js";import{g as P,d as w,c as E}from"./scroll.8948f4bf.js";const{passive:m}=C,M=["both","horizontal","vertical"];var Q=v({name:"QScrollObserver",props:{axis:{type:String,validator:e=>M.includes(e),default:"vertical"},debounce:[String,Number],scrollTarget:{default:void 0}},emits:["scroll"],setup(e,{emit:p}){const t={position:{top:0,left:0},direction:"down",directionChanged:!1,delta:{top:0,left:0},inflectionPoint:{top:0,left:0}};let n=null,i,s;b(()=>e.scrollTarget,()=>{d(),u()});function c(){n!==null&&n();const r=Math.max(0,w(i)),a=E(i),o={top:r-t.position.top,left:a-t.position.left};if(e.axis==="vertical"&&o.top===0||e.axis==="horizontal"&&o.left===0)return;const g=Math.abs(o.top)>=Math.abs(o.left)?o.top<0?"up":"down":o.left<0?"left":"right";t.position={top:r,left:a},t.directionChanged=t.direction!==g,t.delta=o,t.directionChanged===!0&&(t.direction=g,t.inflectionPoint=t.position),p("scroll",{...t})}function u(){i=P(s,e.scrollTarget),i.addEventListener("scroll",l,m),l(!0)}function d(){i!==void 0&&(i.removeEventListener("scroll",l,m),i=void 0)}function l(r){if(r===!0||e.debounce===0||e.debounce==="0")c();else if(n===null){const[a,o]=e.debounce?[setTimeout(c,e.debounce),clearTimeout]:[requestAnimationFrame(c),cancelAnimationFrame];n=()=>{o(a),n=null}}}const{proxy:f}=x();return h(()=>{s=f.$el.parentNode,u()}),S(()=>{n!==null&&n(),d()}),Object.assign(f,{trigger:l,getPosition:()=>t}),T}});export{Q};
