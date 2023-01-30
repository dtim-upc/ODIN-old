import{b as S,Q as L}from"./datasources.store.11e61b1c.js";import{aT as H,r as M,ad as W,o as v,q as k,s as u,v as s,c as E,y as V,D as N,ah as X,a as j,B as i,aU as _,C as y,aF as T,i as $,x,z as F,aO as Z,A as R,g as J,aC as K}from"./index.d68f4a5a.js";import{Q as Y}from"./QTable.10ad9361.js";import{Q as ee,a as P}from"./QCard.fada2e12.js";import{b as D,a as te,c as oe}from"./QSelect.b26dbc07.js";import{u as re}from"./use-quasar.0d998acc.js";import{a as I,Q as ae}from"./QForm.e3d83383.js";import{Q as A}from"./QSpace.21d5efeb.js";import{Q as le,a as h}from"./QList.a7d2d4d7.js";import{Q}from"./use-model-toggle.6c9b3b17.js";import{Q as ne}from"./QBtnDropdown.819533f6.js";import{C as B}from"./ClosePopup.ec66148b.js";import"./QTooltip.7e51ce49.js";import"./QSeparator.a128976e.js";import"./use-dark.cac0eb06.js";import"./use-key-composition.b19a4bbf.js";import"./uid.42677368.js";import"./focus-manager.32f8d49a.js";import"./QMenu.6dc407a1.js";import"./use-prevent-scroll.8ee261ba.js";import"./format.2a3572e1.js";const se={key:0},ie={__name:"AddFolderForm",props:{showFormButtons:{type:Boolean,default:!0}},emits:["submitSuccess","cancelForm"],setup(e,{expose:t,emit:o}){const a=H(),r=M(null);t({form:r});const l=W({name:"",description:"",privacy:"private",color:"#dbe2e7"}),n=["private","public"],p=["#dbe2e7","#4e68f5"],c=()=>{l.name="",l.description="",l.privacy="private",l.color="#dbe2e7"},d=()=>{r.value.resetValidation(),o("submitSuccess")},b=()=>{r.value.resetValidation(),o("cancelForm")},g=()=>{a.createProject(l,d)};return(C,f)=>(v(),k(ae,{ref_key:"form",ref:r,onSubmit:g,onReset:c,class:"q-gutter-md"},{default:u(()=>[s(I,{filled:"",modelValue:l.name,"onUpdate:modelValue":f[0]||(f[0]=m=>l.name=m),label:"Project name","lazy-rules":"",rules:[m=>m&&m.length>0||"Please type a name"]},null,8,["modelValue","rules"]),s(I,{modelValue:l.description,"onUpdate:modelValue":f[1]||(f[1]=m=>l.description=m),filled:"",autogrow:"",label:"Description (Optional)"},null,8,["modelValue"]),s(D,{modelValue:l.privacy,"onUpdate:modelValue":f[2]||(f[2]=m=>l.privacy=m),options:n,label:"Privacy",class:"q-mt-none"},null,8,["modelValue"]),s(D,{modelValue:l.color,"onUpdate:modelValue":f[3]||(f[3]=m=>l.color=m),options:p,label:"Color",class:"q-mt-none"},null,8,["modelValue"]),e.showFormButtons?(v(),E("div",se,[s(V,{label:"Submit",type:"submit",color:"primary"}),s(V,{label:"Cancel",type:"reset",color:"primary",flat:"",class:"q-ml-sm",onClick:f[4]||(f[4]=m=>b())})])):N("",!0)]),_:1},512))}},de=/^rgb(a)?\((\d{1,3}),(\d{1,3}),(\d{1,3}),?([01]?\.?\d*?)?\)$/;function q({r:e,g:t,b:o,a}){const r=a!==void 0;if(e=Math.round(e),t=Math.round(t),o=Math.round(o),e>255||t>255||o>255||r&&a>100)throw new TypeError("Expected 3 numbers below 256 (and optionally one below 100)");return a=r?(Math.round(255*a/100)|1<<8).toString(16).slice(1):"","#"+(o|t<<8|e<<16|1<<24).toString(16).slice(1)+a}function O(e){if(typeof e!="string")throw new TypeError("Expected a string");e=e.replace(/^#/,""),e.length===3?e=e[0]+e[0]+e[1]+e[1]+e[2]+e[2]:e.length===4&&(e=e[0]+e[0]+e[1]+e[1]+e[2]+e[2]+e[3]+e[3]);const t=parseInt(e,16);return e.length>6?{r:t>>24&255,g:t>>16&255,b:t>>8&255,a:Math.round((t&255)/2.55)}:{r:t>>16,g:t>>8&255,b:t&255}}function ce({h:e,s:t,v:o,a}){let r,l,n;t=t/100,o=o/100,e=e/360;const p=Math.floor(e*6),c=e*6-p,d=o*(1-t),b=o*(1-c*t),g=o*(1-(1-c)*t);switch(p%6){case 0:r=o,l=g,n=d;break;case 1:r=b,l=o,n=d;break;case 2:r=d,l=o,n=g;break;case 3:r=d,l=b,n=o;break;case 4:r=g,l=d,n=o;break;case 5:r=o,l=d,n=b;break}return{r:Math.round(r*255),g:Math.round(l*255),b:Math.round(n*255),a}}function ue({r:e,g:t,b:o,a}){const r=Math.max(e,t,o),l=Math.min(e,t,o),n=r-l,p=r===0?0:n/r,c=r/255;let d;switch(r){case l:d=0;break;case e:d=t-o+n*(t<o?6:0),d/=6*n;break;case t:d=o-e+n*2,d/=6*n;break;case o:d=e-t+n*4,d/=6*n;break}return{h:Math.round(d*360),s:Math.round(p*100),v:Math.round(c*100),a}}function w(e){if(typeof e!="string")throw new TypeError("Expected a string");const t=e.replace(/ /g,""),o=de.exec(t);if(o===null)return O(t);const a={r:Math.min(255,parseInt(o[2],10)),g:Math.min(255,parseInt(o[3],10)),b:Math.min(255,parseInt(o[4],10))};if(o[1]){const r=parseFloat(o[5]);a.a=Math.min(1,isNaN(r)===!0?1:r)*100}return a}function pe(e,t){if(typeof e!="string")throw new TypeError("Expected a string as color");if(typeof t!="number")throw new TypeError("Expected a numeric percent");const o=w(e),a=t<0?0:255,r=Math.abs(t)/100,l=o.r,n=o.g,p=o.b;return"#"+(16777216+(Math.round((a-l)*r)+l)*65536+(Math.round((a-n)*r)+n)*256+(Math.round((a-p)*r)+p)).toString(16).slice(1)}function me(e){if(typeof e!="string"&&(!e||e.r===void 0))throw new TypeError("Expected a string or a {r, g, b} object as color");const t=typeof e=="string"?w(e):e,o=t.r/255,a=t.g/255,r=t.b/255,l=o<=.03928?o/12.92:Math.pow((o+.055)/1.055,2.4),n=a<=.03928?a/12.92:Math.pow((a+.055)/1.055,2.4),p=r<=.03928?r/12.92:Math.pow((r+.055)/1.055,2.4);return .2126*l+.7152*n+.0722*p}function fe(e){if(typeof e!="string"&&(!e||e.r===void 0))throw new TypeError("Expected a string or a {r, g, b} object as color");const t=typeof e=="string"?w(e):e;return(t.r*299+t.g*587+t.b*114)/1e3}function ge(e,t){if(typeof e!="string"&&(!e||e.r===void 0))throw new TypeError("Expected a string or a {r, g, b[, a]} object as fgColor");if(typeof t!="string"&&(!t||t.r===void 0))throw new TypeError("Expected a string or a {r, g, b[, a]} object as bgColor");const o=typeof e=="string"?w(e):e,a=o.r/255,r=o.g/255,l=o.b/255,n=o.a!==void 0?o.a/100:1,p=typeof t=="string"?w(t):t,c=p.r/255,d=p.g/255,b=p.b/255,g=p.a!==void 0?p.a/100:1,C=n+g*(1-n),f=Math.round((a*n+c*g*(1-n))/C*255),m=Math.round((r*n+d*g*(1-n))/C*255),G=Math.round((l*n+b*g*(1-n))/C*255),z={r:f,g:m,b:G,a:Math.round(C*100)};return typeof e=="string"?q(z):z}function ve(e,t){if(typeof e!="string")throw new TypeError("Expected a string as color");if(t===void 0||t<-1||t>1)throw new TypeError("Expected offset to be between -1 and 1");const{r:o,g:a,b:r,a:l}=w(e),n=l!==void 0?l/100:0;return q({r:o,g:a,b:r,a:Math.round(Math.min(1,Math.max(0,n+t))*100)})}function be(e){if(typeof e!="string")throw new TypeError("Expected a string as color");const t=document.createElement("div");t.className=`text-${e} invisible fixed no-pointer-events`,document.body.appendChild(t);const o=getComputedStyle(t).getPropertyValue("color");return t.remove(),q(w(o))}var U={rgbToHex:q,hexToRgb:O,hsvToRgb:ce,rgbToHsv:ue,textToRgb:w,lighten:pe,luminosity:me,brightness:fe,blend:ge,changeAlpha:ve,getPaletteColor:be};const ye=i("div",{class:"paper"},null,-1),_e=i("div",{class:"paper"},null,-1),he=i("div",{class:"paper"},null,-1),we={class:"row no-wrap items-center q-pa-sm rounded-borders"},ke={style:{position:"absolute",bottom:"0",width:"100%"}},Me={class:"row no-wrap items-center q-mt-md q-pa-sm rounded-borders"},Ce={__name:"Folder",props:{row:{type:Object},folderColor:{type:String,default:"#3dbb94"}},setup(e){const t=e,o=X(),a=M(""),r=j(()=>"background:"+U.lighten(t.folderColor,-10)+";"),l=j(()=>"background:"+t.folderColor+";"),n=p=>{o.push({name:"home",params:{id:p.id}})};return(p,c)=>(v(),E("div",{class:R(["folder",a.value==t.row.id?"active":""])},[i("div",{class:"folder__back",style:_(y(r))},[ye,_e,he,i("div",{class:"folder__front",style:_(y(l))},null,4),i("div",{class:"folder__front right",style:_(y(l)),onClick:c[3]||(c[3]=d=>n(t.row))},[i("div",we,[i("span",null,T(t.row.name),1),s(A),s(ne,{onShow:c[0]||(c[0]=d=>a.value=t.row.id),onBeforeHide:c[1]||(c[1]=d=>a.value=""),color:"primary",flat:"","dropdown-icon":"more_horiz","no-icon-animation":"",padding:"none","menu-anchor":"top right","menu-self":"top left",onClick:c[2]||(c[2]=Z(()=>{},["stop","prevent"]))},{default:u(()=>[s(le,{dense:""},{default:u(()=>[$((v(),k(Q,{clickable:"",onClick:p.onItemClick},{default:u(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:u(()=>[s(x,{color:"primary",name:"settings"})]),_:1}),s(h,null,{default:u(()=>[s(S,null,{default:u(()=>[F("Settings")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[B]]),$((v(),k(Q,{clickable:"",onClick:p.onItemClick},{default:u(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:u(()=>[s(x,{color:"primary",name:"folder_copy"})]),_:1}),s(h,null,{default:u(()=>[s(S,null,{default:u(()=>[F("Clone")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[B]]),$((v(),k(Q,{clickable:"",onClick:p.onItemClick},{default:u(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:u(()=>[s(x,{color:"primary",name:"join_full"})]),_:1}),s(h,null,{default:u(()=>[s(S,null,{default:u(()=>[F("Integrate")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[B]]),$((v(),k(Q,{clickable:"",onClick:p.onItemClick},{default:u(()=>[s(h,{avatar:"",style:{"min-width":"30px",padding:"0"}},{default:u(()=>[s(x,{color:"primary",name:"delete"})]),_:1}),s(h,null,{default:u(()=>[s(S,null,{default:u(()=>[F("Delete")]),_:1})]),_:1})]),_:1},8,["onClick"])),[[B]])]),_:1})]),_:1})]),i("div",ke,[i("div",Me,[s(te,{style:_(y(r)),"text-color":"white"},{default:u(()=>[F(T(t.row.numberOfDS)+" files ",1)]),_:1},8,["style"]),s(A),i("span",null,T(t.row.privacy),1)])])],4),i("div",{class:"folder__back_after",style:_(y(r))},null,4)],4)],2))}};const Fe=i("div",{class:"paper"},null,-1),xe=i("div",{class:"paper"},null,-1),Ee=i("div",{class:"paper"},null,-1),Se={class:"text-center flex flex-center",style:{height:"100%"}},$e={__name:"AddFolder",props:{row:{type:Object},folderColor:{type:String,default:"#fff"}},emits:["addFolder"],setup(e,{emit:t}){const o=e;M("");const a=j(()=>"background:"+U.lighten(o.folderColor,0)+";"),r=j(()=>"background:"+o.folderColor+";");return(l,n)=>(v(),E("div",{class:"folder",onClick:n[0]||(n[0]=p=>l.$emit("addFolder"))},[i("div",{class:"folder__back",style:_(y(a))},[Fe,xe,Ee,i("div",{class:"folder__front",style:_(y(r))},null,4),i("div",{class:"folder__front right",style:_(y(r))},[i("div",Se,[s(x,{size:"xl",name:"add"})])],4),i("div",{class:"folder__back_after",style:_(y(a))},null,4)],4)]))}},Qe={class:"q-table__title"},Be={key:0,class:"q-pa-md col-xs-12 col-sm-6 col-md-2 col-lg-2"},Te={class:"q-pa-md col-xs-12 col-sm-6 col-md-2 col-lg-2"},Ve=i("div",{class:"full-width row flex-center text-accent q-gutter-sm q-pa-xl",style:{"flex-direction":"column"}},[i("svg",{width:"160px",height:"88px",viewBox:"0 0 216 120",fill:"none",xmlns:"http://www.w3.org/2000/svg",class:"sc-jIkXHa sc-ZOtfp fXAzWm jPTZgW"},[i("g",{opacity:"0.84","clip-path":"url(#EmptyDocuments_svg__clip0_1142_57509)"},[i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M189.25 19.646a7.583 7.583 0 010 15.166h-43.333a7.583 7.583 0 010 15.167h23.833a7.583 7.583 0 010 15.167h-11.022c-5.28 0-9.561 3.395-9.561 7.583 0 1.956 1.063 3.782 3.19 5.48 2.017 1.608 4.824 1.817 7.064 3.096a7.583 7.583 0 01-3.754 14.174H65.75a7.583 7.583 0 010-15.166H23.5a7.583 7.583 0 110-15.167h43.333a7.583 7.583 0 100-15.167H39.75a7.583 7.583 0 110-15.166h43.333a7.583 7.583 0 010-15.167H189.25zm0 30.333a7.583 7.583 0 110 15.166 7.583 7.583 0 010-15.166z",fill:"#D9D8FF","fill-opacity":"0.8"}),i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M132.561 19.646l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162zM73.162 26.33l4.97-.557-4.97.557z",fill:"#fff"}),i("path",{d:"M73.162 26.33l4.97-.557m54.429-6.127l10.077 73.496.906 7.374a4.334 4.334 0 01-3.773 4.829l-63.44 7.789a4.333 4.333 0 01-4.83-3.772l-9.767-79.547a2.166 2.166 0 011.91-2.417l5.262-.59 63.655-7.162z",stroke:"#7B79FF","stroke-width":"2.5"}),i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M129.818 24.27l9.122 66.608.82 6.682c.264 2.153-1.246 4.11-3.373 4.371l-56.812 6.976c-2.127.261-4.066-1.272-4.33-3.425l-8.83-71.908a2.167 2.167 0 011.887-2.415l7.028-.863",fill:"#F0F0FF"}),i("path",{"fill-rule":"evenodd","clip-rule":"evenodd",d:"M135.331 5.833H85.978a2.97 2.97 0 00-2.107.873A2.97 2.97 0 0083 8.813v82.333c0 .823.333 1.567.872 2.106a2.97 2.97 0 002.107.873h63.917a2.97 2.97 0 002.106-.873 2.97 2.97 0 00.873-2.106V23.367a2.98 2.98 0 00-.873-2.107L137.437 6.705a2.98 2.98 0 00-2.106-.872z",fill:"#fff",stroke:"#7B79FF","stroke-width":"2.5"}),i("path",{d:"M135.811 7.082v12.564a3.25 3.25 0 003.25 3.25h8.595M94.644 78.146h28.167m-28.167-55.25h28.167-28.167zm0 13h46.584-46.584zm0 14.083h46.584-46.584zm0 14.084h46.584-46.584z",stroke:"#7B79FF","stroke-width":"2.5","stroke-linecap":"round","stroke-linejoin":"round"})]),i("defs",null,[i("clipPath",{id:"EmptyDocuments_svg__clip0_1142_57509"},[i("path",{fill:"#fff",d:"M0 0h216v120H0z"})])])]),i("p",{style:{color:"rgb(102, 102, 135)","font-weight":"500","font-size":"1rem","line-height":"1.25"}},"No alignments added. Check classes color fixed to system")],-1),je=i("div",{class:"text-h6"},"New project",-1),qe={__name:"TableProjects",props:{no_shadow:{type:Boolean,default:!1},alignments:{type:Array,default:[]}},setup(e){const t=H(),o=M(!0),a=M(!1),r=M({rowsPerPage:0});re();const l=[{name:"id",required:!0,label:"ID",align:"center",field:"id",sortable:!0},{name:"name",required:!0,label:"Name",align:"center",field:"name",sortable:!0},{name:"numberOfDS",required:!0,label:"# Datasources",align:"center",field:"numberOfDS",sortable:!0},{name:"createdBy",required:!0,label:"Created by",align:"center",field:"createdBy",sortable:!0},{name:"privacy",required:!0,label:"Privacy",align:"center",field:"privacy",sortable:!0}];J(()=>{t.getProjects()});const n="Projects";return(p,c)=>(v(),E(K,null,[s(Y,{grid:o.value,rows:y(t).projects,columns:l,class:R({"no-shadow":e.no_shadow}),style:{height:"600px"},"virtual-scroll":"",pagination:r.value,"onUpdate:pagination":c[3]||(c[3]=d=>r.value=d),"rows-per-page-options":[0],"row-key":"id","no-data-label":"Consider adding some alignments to start the integration process","no-results-label":"The filter didn't uncover any results"},{"top-left":u(()=>[i("div",Qe,[i("span",null,T(n)),s(V,{padding:"none",color:"secondary",icon:"add",onClick:c[0]||(c[0]=d=>a.value=!0)})])]),"top-right":u(d=>[s(V,{unelevated:"",padding:"none",color:"primary700",icon:"list",onClick:c[1]||(c[1]=b=>o.value=!o.value)})]),item:u(d=>[d.rowIndex==0?(v(),E("div",Be,[s($e,{onAddFolder:c[2]||(c[2]=b=>a.value=!a.value)})])):N("",!0),i("div",Te,[s(Ce,{row:d.row,folderColor:d.row.color},null,8,["row","folderColor"])])]),"no-data":u(({icon:d,message:b,filter:g})=>[Ve]),_:1},8,["grid","rows","class","pagination"]),s(oe,{modelValue:a.value,"onUpdate:modelValue":c[6]||(c[6]=d=>a.value=d)},{default:u(()=>[s(ee,{flat:"",bordered:"",class:"my-card",style:{"min-width":"30vw"}},{default:u(()=>[s(P,null,{default:u(()=>[je]),_:1}),s(P,{class:"q-pt-none"},{default:u(()=>[s(ie,{onSubmitSuccess:c[4]||(c[4]=d=>a.value=!1),onCancelForm:c[5]||(c[5]=d=>a.value=!1)})]),_:1})]),_:1})]),_:1},8,["modelValue"])],64))}},rt={__name:"Projects",setup(e){return(t,o)=>(v(),k(L,{class:"q-pa-md"},{default:u(()=>[s(qe)]),_:1}))}};export{rt as default};
