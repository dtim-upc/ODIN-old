import{b as v,w as b,g as h,e as S,a2 as T,m as x,O as C}from"./index.876f1426.js";import{g as O,c as P,b as w}from"./scroll.09115f1d.js";const{passive:m}=C,E=["both","horizontal","vertical"];var Q=v({name:"QScrollObserver",props:{axis:{type:String,validator:e=>E.includes(e),default:"vertical"},debounce:[String,Number],scrollTarget:{default:void 0}},emits:["scroll"],setup(e,{emit:p}){const t={position:{top:0,left:0},direction:"down",directionChanged:!1,delta:{top:0,left:0},inflectionPoint:{top:0,left:0}};let n=null,i,s;b(()=>e.scrollTarget,()=>{d(),u()});function c(){n!==null&&n();const r=Math.max(0,P(i)),a=w(i),o={top:r-t.position.top,left:a-t.position.left};if(e.axis==="vertical"&&o.top===0||e.axis==="horizontal"&&o.left===0)return;const g=Math.abs(o.top)>=Math.abs(o.left)?o.top<0?"up":"down":o.left<0?"left":"right";t.position={top:r,left:a},t.directionChanged=t.direction!==g,t.delta=o,t.directionChanged===!0&&(t.direction=g,t.inflectionPoint=t.position),p("scroll",{...t})}function u(){i=O(s,e.scrollTarget),i.addEventListener("scroll",l,m),l(!0)}function d(){i!==void 0&&(i.removeEventListener("scroll",l,m),i=void 0)}function l(r){if(r===!0||e.debounce===0||e.debounce==="0")c();else if(n===null){const[a,o]=e.debounce?[setTimeout(c,e.debounce),clearTimeout]:[requestAnimationFrame(c),cancelAnimationFrame];n=()=>{o(a),n=null}}}const{proxy:f}=x();return h(()=>{s=f.$el.parentNode,u()}),S(()=>{n!==null&&n(),d()}),Object.assign(f,{trigger:l,getPosition:()=>t}),T}});export{Q};