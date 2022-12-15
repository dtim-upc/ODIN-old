import{b as T,Q as L}from"./datasources.store.9808ef93.js";import{aT as A,r as S,ad as W,o as g,q as k,s as c,v as s,c as $,y as E,D as H,ao as X,a as j,B as i,aU as v,C as _,ah as Q,i as C,x as F,A as M,aO as Z,z as N,g as J,aB as K}from"./index.1dc04fa4.js";import{Q as Y}from"./QTooltip.ed3efd61.js";import{Q as ee}from"./QTable.569fc909.js";import{a as te,Q as z}from"./QCard.8b1340ac.js";import{a as D,Q as oe,b as re}from"./QSelect.c81869a2.js";import{C as x}from"./ClosePopup.421ca59b.js";import{u as le}from"./use-quasar.62a15c0a.js";import{a as I,Q as ae}from"./QForm.2920b7e6.js";import{Q as P}from"./QSpace.b853161e.js";import{Q as ne,a as h}from"./format.47a278a4.js";import{Q as B}from"./use-model-toggle.3d79aca0.js";import{Q as se}from"./QBtnDropdown.08322dfe.js";import"./use-prevent-scroll.ca3d1e41.js";import"./use-dark.de9a0581.js";import"./focus-manager.32f8d49a.js";import"./scroll.a88fb3a5.js";import"./QSeparator.db748d79.js";import"./use-key-composition.87cdc301.js";import"./uid.42677368.js";const ie={key:0},de={__name:"AddFolderForm",props:{showFormButtons:{type:Boolean,default:!0}},emits:["submitSuccess"],setup(e,{expose:t,emit:o}){const a=A(),r=S(null);t({form:r});const l=W({name:"",description:"",privacy:"private",color:"#dbe2e7"}),n=["private","public"],p=["#dbe2e7","#4e68f5"],u=()=>{l.name="",l.description="",l.privacy="private",l.color="#dbe2e7"},d=()=>{r.value.resetValidation(),o("submitSuccess")},b=()=>{a.createProject(l,d)};return(y,m)=>(g(),k(ae,{ref_key:"form",ref:r,onSubmit:b,onReset:u,class:"q-gutter-md"},{default:c(()=>[s(I,{filled:"",modelValue:l.name,"onUpdate:modelValue":m[0]||(m[0]=f=>l.name=f),label:"Project name","lazy-rules":"",rules:[f=>f&&f.length>0||"Please type a name"]},null,8,["modelValue","rules"]),s(I,{modelValue:l.description,"onUpdate:modelValue":m[1]||(m[1]=f=>l.description=f),filled:"",autogrow:"",label:"Description (Optional)"},null,8,["modelValue"]),s(D,{modelValue:l.privacy,"onUpdate:modelValue":m[2]||(m[2]=f=>l.privacy=f),options:n,label:"Privacy",class:"q-mt-none"},null,8,["modelValue"]),s(D,{modelValue:l.color,"onUpdate:modelValue":m[3]||(m[3]=f=>l.color=f),options:p,label:"Color",class:"q-mt-none"},null,8,["modelValue"]),e.showFormButtons?(g(),$("div",ie,[s(E,{label:"Submit",type:"submit",color:"primary"}),s(E,{label:"Cancel",type:"reset",color:"primary",flat:"",class:"q-ml-sm"})])):H("",!0)]),_:1},512))}},ce=/^rgb(a)?\((\d{1,3}),(\d{1,3}),(\d{1,3}),?([01]?\.?\d*?)?\)$/;function q({r:e,g:t,b:o,a}){const r=a!==void 0;if(e=Math.round(e),t=Math.round(t),o=Math.round(o),e>255||t>255||o>255||r&&a>100)throw new TypeError("Expected 3 numbers below 256 (and optionally one below 100)");return a=r?(Math.round(255*a/100)|1<<8).toString(16).slice(1):"","#"+(o|t<<8|e<<16|1<<24).toString(16).slice(1)+a}function R(e){if(typeof e!="string")throw new TypeError("Expected a string");e=e.replace(/^#/,""),e.length===3?e=e[0]+e[0]+e[1]+e[1]+e[2]+e[2]:e.length===4&&(e=e[0]+e[0]+e[1]+e[1]+e[2]+e[2]+e[3]+e[3]);const t=parseInt(e,16);return e.length>6?{r:t>>24&255,g:t>>16&255,b:t>>8&255,a:Math.round((t&255)/2.55)}:{r:t>>16,g:t>>8&255,b:t&255}}function ue({h:e,s:t,v:o,a}){let r,l,n;t=t/100,o=o/100,e=e/360;const p=Math.floor(e*6),u=e*6-p,d=o*(1-t),b=o*(1-u*t),y=o*(1-(1-u)*t);switch(p%6){case 0:r=o,l=y,n=d;break;case 1:r=b,l=o,n=d;break;case 2:r=d,l=o,n=y;break;case 3:r=d,l=b,n=o;break;case 4:r=y,l=d,n=o;break;case 5:r=o,l=d,n=b;break}return{r:Math.round(r*255),g:Math.round(l*255),b:Math.round(n*255),a}}function pe({r:e,g:t,b:o,a}){const r=Math.max(e,t,o),l=Math.min(e,t,o),n=r-l,p=r===0?0:n/r,u=r/255;let d;switch(r){case l:d=0;break;case e:d=t-o+n*(t<o?6:0),d/=6*n;break;case t:d=o-e+n*2,d/=6*n;break;case o:d=e-t+n*4,d/=6*n;break}return{h:Math.round(d*360),s:Math.round(p*100),v:Math.round(u*100),a}}function w(e){if(typeof e!="string")throw new TypeError("Expected a string");const t=e.replace(/ /g,""),o=ce.exec(t);if(o===null)return R(t);const a={r:Math.min(255,parseInt(o[2],10)),g:Math.min(255,parseInt(o[3],10)),b:Math.min(255,parseInt(o[4],10))};if(o[1]){const r=parseFloat(o[5]);a.a=Math.min(1,isNaN(r)===!0?1:r)*100}return a}function me(e,t){if(typeof e!="string")throw new TypeError("Expected a string as color");if(typeof t!="number")throw new TypeError("Expected a numeric percent");const o=w(e),a=t<0?0:255,r=Math.abs(t)/100,l=o.r,n=o.g,p=o.b;return"#"+(16777216+(Math.round((a-l)*r)+l)*65536+(Math.round((a-n)*r)+n)*256+(Math.round((a-p)*r)+p)).toString(16).slice(1)}function fe(e){if(typeof e!="string"&&(!e||e.r===void 0))throw new TypeError("Expected a string or a {r, g, b} object as color");const t=typeof e=="string"?w(e):e,o=t.r/255,a=t.g/255,r=t.b/255,l=o<=.03928?o/12.92:Math.pow((o+.055)/1.055,2.4),n=a<=.03928?a/12.92:Math.pow((a+.055)/1.055,2.4),p=r<=.03928?r/12.92:Math.pow((r+.055)/1.055,2.4);return .2126*l+.7152*n+.0722*p}function ge(e){if(typeof e!="string"&&(!e||e.r===void 0))throw new TypeError("Expected a string or a {r, g, b} object as color");const t=typeof e=="string"?w(e):e;return(t.r*299+t.g*587+t.b*114)/1e3}function _e(e,t){if(typeof e!="string"&&(!e||e.r===void 0))throw new TypeError("Expected a string or a {r, g, b[, a]} object as fgColor");if(typeof t!="string"&&(!t||t.r===void 0))throw new TypeError("Expected a string or a {r, g, b[, a]} object as bgColor");const o=typeof e=="string"?w(e):e,a=o.r/255,r=o.g/255,l=o.b/255,n=o.a!==void 0?o.a/100:1,p=typeof t=="string"?w(t):t,u=p.r/255,d=p.g/255,b=p.b/255,y=p.a!==void 0?p.a/100:1,m=n+y*(1-n),f=Math.round((a*n+u*y*(1-n))/m*255),U=Math.round((r*n+d*y*(1-n))/m*255),G=Math.round((l*n+b*y*(1-n))/m*255),V={r:f,g:U,b:G,a:Math.round(m*100)};return typeof e=="string"?q(V):V}function be(e,t){if(typeof e!="string")throw new TypeError("Expected a string as color");if(t===void 0||t<-1||t>1)throw new TypeError("Expected offset to be between -1 and 1");const{r:o,g:a,b:r,a:l}=w(e),n=l!==void 0?l/100:0;return q({r:o,g:a,b:r,a:Math.round(Math.min(1,Math.max(0,n+t))*100)})}function ye(e){if(typeof e!="string")throw new TypeError("Expected a string as color");const t=document.createElement("div");t.className=`text-${e} invisible fixed no-pointer-events`,document.body.appendChild(t);const o=getComputedStyle(t).getPropertyValue("color");return t.remove(),q(w(o))}var O={rgbToHex:q,hexToRgb:R,hsvToRgb:ue,rgbToHsv:pe,textToRgb:w,lighten:me,luminosity:fe,brightness:ge,blend:_e,changeAlpha:be,getPaletteColor:ye};const ve=i("div",{class:"paper"},null,-1),he=i("div",{class:"paper"},null,-1),we=i("div",{class:"paper"},null,-1),ke={class:"row no-wrap items-center q-pa-sm rounded-borders"},Me={style:{position:"absolute",bottom:"0",width:"100%"}},Ce={class:"row no-wrap items-center q-mt-md q-pa-sm rounded-borders"},Fe={__name:"Folder",props:{row:{type:Object},folderColor:{type:String,default:"#3dbb94"}},setup(e){const t=e,o=X(),a=S(""),r=j(()=>"background:"+O.lighten(t.folderColor,-10)+";"),l=j(()=>"background:"+t.folderColor+";"),n=p=>{o.push({name:"home",params:{id:p.id}})};return(p,u)=>(g(),$("div",{class:N(["folder",a.value==t.row.id?"active":""])},[i("div",{class:"folder__back",style:v(_(r))},[ve,he,we,i("div",{class:"folder__front",style:v(_(l))},null,4),i("div",{class:"folder__front right",style:v(_(l)),onClick:u[3]||(u[3]=d=>n(t.row))},[i("div",ke,[i("span",null,Q(t.row.name),1),s(P),s(se,{onShow:u[0]||(u[0]=d=>a.value=t.row.id),onBeforeHide:u[1]||(u[1]=d=>a.value=""),color:"primary",flat:"","dropdown-icon":"more_horiz","no-icon-animation":"",padding:"none","menu-anchor":"top right","menu-self":"top left",onClick:u[2]||(u[2]=Z(()=>{},["stop","prevent"]))},{default:c(()=>[s(ne,{dense:""},{default:c(()=>[C((g(),k(B,{clickable:"",onClick:p.onItemClick},{default:c(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:c(()=>[s(F,{color:"primary",name:"settings"})]),_:1}),s(h,null,{default:c(()=>[s(T,null,{default:c(()=>[M("Settings")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[x]]),C((g(),k(B,{clickable:"",onClick:p.onItemClick},{default:c(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:c(()=>[s(F,{color:"primary",name:"folder_copy"})]),_:1}),s(h,null,{default:c(()=>[s(T,null,{default:c(()=>[M("Clone")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[x]]),C((g(),k(B,{clickable:"",onClick:p.onItemClick},{default:c(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:c(()=>[s(F,{color:"primary",name:"join_full"})]),_:1}),s(h,null,{default:c(()=>[s(T,null,{default:c(()=>[M("Integrate")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[x]]),C((g(),k(B,{clickable:"",onClick:p.onItemClick},{default:c(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:c(()=>[s(F,{color:"primary",name:"delete"})]),_:1}),s(h,null,{default:c(()=>[s(T,null,{default:c(()=>[M("Delete")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[x]])]),_:1})]),_:1})]),i("div",Me,[i("div",Ce,[s(oe,{style:v(_(r)),"text-color":"white"},{default:c(()=>[M(Q(t.row.numberOfDS)+" files ",1)]),_:1},8,["style"]),s(P),i("span",null,Q(t.row.privacy),1)])])],4),i("div",{class:"folder__back_after",style:v(_(r))},null,4)],4)],2))}};const xe=i("div",{class:"paper"},null,-1),Ee=i("div",{class:"paper"},null,-1),Qe=i("div",{class:"paper"},null,-1),Se={class:"text-center flex flex-center",style:{height:"100%"}},$e={__name:"AddFolder",props:{row:{type:Object},folderColor:{type:String,default:"#F2E9E9"}},emits:["addFolder"],setup(e,{emit:t}){const o=e;S("");const a=j(()=>"background:"+O.lighten(o.folderColor,0)+";"),r=j(()=>"background:"+o.folderColor+";");return(l,n)=>(g(),$("div",{class:"folder",onClick:n[0]||(n[0]=p=>l.$emit("addFolder"))},[i("div",{class:"folder__back",style:v(_(a))},[xe,Ee,Qe,i("div",{class:"folder__front",style:v(_(r))},null,4),i("div",{class:"folder__front right",style:v(_(r))},[i("div",Se,[s(F,{size:"xl",name:"add"})])],4),i("div",{class:"folder__back_after",style:v(_(a))},null,4)],4)]))}},Te={class:"q-table__title"},Be={key:0,class:"q-pa-md col-xs-12 col-sm-6 col-md-2 col-lg-2"},je={class:"q-pa-md col-xs-12 col-sm-6 col-md-2 col-lg-2"},qe=i("div",{class:"full-width row flex-center text-accent q-gutter-sm q-pa-xl",style:{"flex-direction":"column"}},[i("svg",{width:"160px",height:"88px",viewBox:"0 0 216 120",fill:"none",xmlns:"http://www.w3.org/2000/svg",class:"sc-jIkXHa sc-ZOtfp fXAzWm jPTZgW"},[i("g",{opacity:"0.84","clip-path":"url(#EmptyDocuments_svg__clip0_1142_57509)"},[i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M189.25 19.646a7.583 7.583 0 010 15.166h-43.333a7.583 7.583 0 010 15.167h23.833a7.583 7.583 0 010 15.167h-11.022c-5.28 0-9.561 3.395-9.561 7.583 0 1.956 1.063 3.782 3.19 5.48 2.017 1.608 4.824 1.817 7.064 3.096a7.583 7.583 0 01-3.754 14.174H65.75a7.583 7.583 0 010-15.166H23.5a7.583 7.583 0 110-15.167h43.333a7.583 7.583 0 100-15.167H39.75a7.583 7.583 0 110-15.166h43.333a7.583 7.583 0 010-15.167H189.25zm0 30.333a7.583 7.583 0 110 15.166 7.583 7.583 0 010-15.166z",fill:"#D9D8FF","fill-opacity":"0.8"}),i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M132.561 19.646l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162zM73.162 26.33l4.97-.557-4.97.557z",fill:"#fff"}),i("path",{d:"M73.162 26.33l4.97-.557m54.429-6.127l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162z",stroke:"#7B79FF","stroke-width":"2.5"}),i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M129.818 24.27l9.122 66.608.82 6.682c.264 2.153-1.246 4.11-3.373 4.371l-56.812 6.976c-2.127.261-4.066-1.272-4.33-3.425l-8.83-71.908a2.167 2.167 0 011.887-2.415l7.028-.863",fill:"#F0F0FF"}),i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M135.331 5.833H85.978a2.97 2.97 0 00-2.107.873A2.97 2.97 0 0083 8.813v82.333c0 .823.333 1.567.872 2.106a2.97 2.97 0 002.107.873h63.917a2.97 2.97 0 002.106-.873 2.97 2.97 0 00.873-2.106V23.367a2.98 2.98 0 00-.873-2.107L137.437 6.705a2.98 2.98 0 00-2.106-.872z",fill:"#fff",stroke:"#7B79FF","stroke-width":"2.5"}),i("path",{d:"M135.811 7.082v12.564a3.25 3.25 0 003.25 3.25h8.595M94.644 78.146h28.167m-28.167-55.25h28.167-28.167zm0 13h46.584-46.584zm0 14.083h46.584-46.584zm0 14.084h46.584-46.584z",stroke:"#7B79FF","stroke-width":"2.5","stroke-linecap":"round","stroke-linejoin":"round"})]),i("defs",null,[i("clipPath",{id:"EmptyDocuments_svg__clip0_1142_57509"},[i("path",{fill:"#fff",d:"M0 0h216v120H0z"})])])]),i("p",{style:{color:"rgb(102, 102, 135)","font-weight":"500","font-size":"1rem","line-height":"1.25"}},"No alignments added. Check classes color fixed to system")],-1),Ve=i("div",{class:"text-h6"},"New project",-1),ze={__name:"TableProjects",props:{no_shadow:{type:Boolean,default:!1},alignments:{type:Array,default:[]}},setup(e){const t=A(),o=S(!0),a=S(!1),r=le(),l=[{name:"id",required:!0,label:"ID",align:"center",field:"id",sortable:!0},{name:"name",required:!0,label:"Name",align:"center",field:"name",sortable:!0},{name:"numberOfDS",required:!0,label:"# Datasources",align:"center",field:"numberOfDS",sortable:!0},{name:"createdBy",required:!0,label:"Created by",align:"center",field:"createdBy",sortable:!0},{name:"privacy",required:!0,label:"Privacy",align:"center",field:"privacy",sortable:!0}];J(()=>{t.getProjects()});const n="Projects";return(p,u)=>(g(),$(K,null,[s(ee,{grid:o.value,rows:_(t).projects,columns:l,class:N({"no-shadow":e.no_shadow}),"row-key":"id","no-data-label":"Consider adding some alignments to start the integration process","no-results-label":"The filter didn't uncover any results"},{"top-left":c(()=>[i("div",Te,[i("span",null,Q(n)),s(E,{padding:"none",color:"secondary",icon:"add",onClick:u[0]||(u[0]=d=>a.value=!0)})])]),"top-right":c(d=>[s(E,{unelevated:"",padding:"none",color:"primary700",icon:"remove",onClick:u[1]||(u[1]=b=>o.value=!o.value)}),s(E,{flat:"",round:"",dense:"",icon:d.inFullscreen?"fullscreen_exit":"fullscreen",onClick:d.toggleFullscreen},{default:c(()=>[C((g(),k(Y,{disable:_(r).platform.is.mobile},{default:c(()=>[M(Q(d.inFullscreen?"Exit Fullscreen":"Toggle Fullscreen"),1)]),_:2},1032,["disable"])),[[x]])]),_:2},1032,["icon","onClick"])]),item:c(d=>[d.rowIndex==0?(g(),$("div",Be,[s($e,{onAddFolder:u[2]||(u[2]=b=>a.value=!a.value)})])):H("",!0),i("div",je,[s(Fe,{row:d.row,folderColor:d.row.color},null,8,["row","folderColor"])])]),"no-data":c(({icon:d,message:b,filter:y})=>[qe]),_:1},8,["grid","rows","class"]),s(re,{modelValue:a.value,"onUpdate:modelValue":u[4]||(u[4]=d=>a.value=d)},{default:c(()=>[s(te,{flat:"",bordered:"",class:"my-card",style:{"min-width":"30vw"}},{default:c(()=>[s(z,null,{default:c(()=>[Ve]),_:1}),s(z,{class:"q-pt-none"},{default:c(()=>[s(de,{onSubmitSuccess:u[3]||(u[3]=d=>a.value=!1)})]),_:1})]),_:1})]),_:1},8,["modelValue"])],64))}},rt={__name:"Projects",setup(e){return(t,o)=>(g(),k(L,{class:"q-pa-md"},{default:c(()=>[s(ze)]),_:1}))}};export{rt as default};
