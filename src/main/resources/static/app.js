"use strict";
const $ = s => document.querySelector(s);
const escapeHtml = value => String(value ?? "").replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
let auth="", user=null, screen="productos", products=[], people=[], filters={};
const titles={productos:"Productos y alertas",movimientos:"Registrar movimiento",historial:"Historial y ajustes",empleados:"Empleados"};
const admin=()=>user?.rol==="ADMIN";
function message(value){$("#mensaje").textContent=value}
async function api(path,method="GET",data){
 const response=await fetch("/api/"+path,{method,headers:{Authorization:"Basic "+auth,...(data?{"Content-Type":"application/json"}:{})},body:data?JSON.stringify(data):undefined,cache:"no-store"});
 const raw=await response.text();
 let result; try{result=raw?JSON.parse(raw):null}catch{throw Error("Respuesta inesperada del servidor ("+response.status+")")}
 if(!response.ok){if(response.status===401 && user)login(); throw Error(result?.error||result?.message||("Error "+response.status))}
 return result;
}
function field(name,label,type="text",value="",attr="required"){
 return '<label>'+label+'<input name="'+name+'" type="'+type+'" value="'+escapeHtml(value)+'" '+attr+'></label>';
}
function submit(label){return '<button type="submit">'+label+'</button>'}
function form(id,body){return '<form id="'+id+'">'+body+'</form>'}
function table(headers,rows,empty){
 if(!rows.length)return '<p>'+empty+'</p>';
 return '<div class="scroll"><table><thead><tr>'+headers.map(x=>'<th>'+x+'</th>').join("")+'</tr></thead><tbody>'+rows.map(row=>'<tr>'+row.map(x=>'<td>'+x+'</td>').join("")+'</tr>').join("")+'</tbody></table></div>';
}
function btn(action,id,label){return '<button class="alt" data-action="'+action+'" data-id="'+Number(id)+'">'+label+'</button>'}
function bind(id,handler){
 const el=document.getElementById(id); if(!el)return;
 el.onsubmit=async event=>{
  event.preventDefault(); const button=event.submitter; if(button.disabled)return; button.disabled=true;
  try{await handler(Object.fromEntries(new FormData(el)))}catch(e){if($("#modal").open)$("#modalError").textContent=e.message;else message(e.message)}finally{button.disabled=false}
 };
}
function login(){
 auth=""; user=null; products=[]; people=[]; filters={}; if($("#modal").open)$("#modal").close();
 $("#identidad").textContent=""; $("#salir").hidden=true; $("nav").hidden=true; message("");
 $("#vista").innerHTML='<section class="login"><h1>Iniciar sesión</h1>'+form("login",field("username","Usuario","text","","required autocomplete=username")+field("password","Contraseña","password","","required autocomplete=current-password")+submit("Entrar"))+'</section>';
 bind("login",async d=>{
  auth=btoa(Array.from(new TextEncoder().encode(d.username+":"+d.password),b=>String.fromCharCode(b)).join(""));
  try{user=await api("me")}catch(e){auth="";throw e}
  $("#identidad").textContent=user.nombre||user.username; $("#salir").hidden=false; $("nav").hidden=false; await navigate("productos");
 });
}
function productForm(p={}){
 return field("nombre","Nombre","text",p.nombre,"required maxlength=120")+
 (p.id?"":field("unidad","Unidad (litros, kg, unidades)","text","","required maxlength=30"))+
 field("stockMinimo","Stock mínimo","number",p.stockMinimo??0,"required min=0 step=any")+
 field("cantidadObjetivo","Cantidad objetivo","number",p.cantidadObjetivo??"","required min=0 step=any")+submit("Guardar");
}
function selectProduct(all=false){
 const options=products.filter(p=>all||p.activo).map(p=>'<option value="'+p.id+'" '+(all&&String(p.id)===filters.productoId?"selected":"")+'>'+escapeHtml(p.nombre)+' · '+p.stock+' '+escapeHtml(p.unidad)+'</option>').join("");
 return '<label>Producto<select name="productoId" '+(all?"":"required")+'><option value="">'+(all?"Todos":"Seleccionar producto")+'</option>'+options+'</select></label>';
}
async function saved(path,method,data,notice){
 await api(path,method,data); if($("#modal").open)$("#modal").close(); await navigate(screen,notice);
}
function renderProducts(alerts){
 const rows=products.map(p=>[escapeHtml(p.nombre),escapeHtml(p.unidad),p.stock,p.stockMinimo,p.cantidadObjetivo,p.activo?"Activo":"Inactivo",...(admin()?[btn("editar",p.id,"Editar")+btn("estado",p.id,p.activo?"Desactivar":"Reactivar")+btn("borrar",p.id,"Eliminar")]:[])]);
 return '<section class="alert"><h2>Alertas de compra</h2>'+table(["Producto","Disponible","Compra sugerida"],alerts.map(p=>[escapeHtml(p.nombre),escapeHtml(p.stock+" "+p.unidad),escapeHtml(p.compraSugerida+" "+p.unidad)]),"Sin alertas de compra.")+'</section>'+
 (admin()?'<details><summary>Crear producto</summary>'+form("crear",productForm())+'</details>':"")+
 '<section><h2>Catálogo</h2>'+table(["Producto","Unidad","Stock","Mínimo","Objetivo","Estado",...(admin()?["Acciones"]:[])],rows,"Todavía no hay productos.")+'</section>';
}
function movement(){return '<section>'+form("mov",selectProduct()+'<label>Tipo<select name="tipo"><option>ENTRADA</option><option>SALIDA</option></select></label>'+field("cantidad","Cantidad","number","","required min=0 step=any")+field("motivo","Motivo","text","","required maxlength=500")+submit("Registrar movimiento"))+'</section>'}
function history(rows){
 const names=new Map(products.map(p=>[p.id,p.nombre]));
 return '<section><h2>Filtrar movimientos</h2>'+form("filtrar",selectProduct(true)+field("desde","Desde (UTC)","date",filters.desde,"")+field("hasta","Hasta (UTC)","date",filters.hasta,"")+submit("Aplicar"))+'</section><section>'+table(["Fecha local","Producto","Tipo","Anterior","Resultante","Motivo","Usuario ID"],rows.map(m=>[escapeHtml(new Date(m.fecha).toLocaleString("es-EC")),escapeHtml(names.get(m.productoId)||m.productoId),escapeHtml(m.tipo),m.stockAnterior,m.stockResultante,escapeHtml(m.motivo),m.usuarioId]),"Sin movimientos para estos filtros.")+'</section>'+
 (admin()?'<details><summary>Ajustar inventario</summary>'+form("ajuste",selectProduct()+field("nuevoStock","Nuevo stock","number","","required min=0 step=any")+field("motivo","Motivo","text","","required maxlength=500")+submit("Guardar ajuste"))+'</details>':"");
}
function employees(){
 return '<details><summary>Crear empleado</summary>'+form("altaEmpleado",field("nombre","Nombre")+field("username","Usuario")+field("password","Contraseña","password")+submit("Crear empleado"))+'</details><section>'+table(["Nombre","Usuario","Rol","Estado","Acciones"],people.map(p=>[escapeHtml(p.nombre),escapeHtml(p.username),escapeHtml(p.rol),p.activo?"Activo":"Inactivo",p.rol==="EMPLEADO"?btn("empleadoEstado",p.id,p.activo?"Desactivar":"Reactivar")+btn("clave",p.id,"Cambiar contraseña"):"—"]),"Sin empleados.")+'</section>';
}
let version=0;
async function navigate(next,notice=""){
 const current=++version; screen=next; message(notice);
 $("nav").innerHTML=Object.entries(titles).filter(([key])=>key!=="empleados"||admin()).map(([key,title])=>'<button data-page="'+key+'" aria-current="'+(key===screen)+'">'+title+'</button>').join("");
 $("#vista").textContent="Cargando…";
 try{
  let body;
  if(next==="empleados"){const list=await api("empleados");if(current!==version||!user)return;people=list;body=employees()}
  else{
   const params=new URLSearchParams(Object.entries(filters).filter(([,v])=>v));
   const [list,extra]=await Promise.all([api("productos"),next==="productos"?api("alertas"):next==="historial"?api("movimientos?"+params):null]);
   if(current!==version||!user)return;products=list;
   body=next==="productos"?renderProducts(extra):next==="historial"?history(extra):movement();
  }
  $("#vista").innerHTML='<h1>'+titles[next]+'</h1>'+body;
  bind("crear",d=>saved("productos","POST",d,"Producto creado."));
  bind("altaEmpleado",d=>saved("empleados","POST",d,"Empleado creado."));
  bind("mov",async d=>{await api("movimientos","POST",d);await navigate("productos","Movimiento registrado. Existencias actualizadas.")});
  bind("ajuste",d=>saved("movimientos/ajustes","POST",d,"Ajuste registrado."));
  bind("filtrar",async d=>{filters=d;await navigate("historial")});
 }catch(e){if(current===version){if(user)$("#vista").textContent="";message(e.message)}}
}
function modal(title,html,callback){$("#modalTitulo").textContent=title;$("#modalError").textContent="";$("#modalBody").innerHTML=form("editarDatos",html);$("#modal").showModal();bind("editarDatos",callback)}
document.addEventListener("click",e=>{
 const b=e.target.closest("button");if(!b)return;
 if(b.dataset.page){navigate(b.dataset.page);return}
 if(!b.dataset.action)return;
 const id=Number(b.dataset.id), staff=["clave","empleadoEstado"].includes(b.dataset.action);
 const p=(staff?people:products).find(x=>x.id===id);if(!p)return;
 const path=(staff?"empleados/":"productos/")+id;
 if(b.dataset.action==="editar")return modal("Editar "+p.nombre,productForm(p),d=>saved(path,"PUT",d,"Producto actualizado."));
 if(b.dataset.action==="clave")return modal("Cambiar contraseña",field("password","Nueva contraseña","password")+submit("Guardar"),d=>saved(path+"/password","PUT",d,"Contraseña actualizada."));
 if(b.dataset.action==="borrar")return modal("Eliminar "+p.nombre,"<p>Esta acción elimina el producto del catálogo.</p>"+submit("Confirmar eliminación"),()=>saved(path,"DELETE",undefined,"Producto eliminado."));
 const action=p.activo?"desactivar":"reactivar";
 modal((p.activo?"Desactivar ":"Reactivar ")+p.nombre,submit("Confirmar"),()=>saved(path+"/"+action,"PATCH",undefined,"Estado actualizado."));
});
$("#cerrar").onclick=()=>$("#modal").close();
$("#salir").onclick=()=>{version++;login()};
login();
