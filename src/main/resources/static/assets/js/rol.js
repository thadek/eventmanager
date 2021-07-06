verRoles();
togglers();

function togglers(){


    $('#togglerSeccion').click(function(){
         
        $('#containerRol').fadeToggle(150);
    
    

}
    )}


const mostrarDatos = (data) => {
    //console.log(data)
    let body =''
    for(let i=0; i<data.length; i++){
     body += `<tr><td>${data[i].idRol}</td><td>${data[i].nombreRol}</td> <td>
    <button class="btn btn-outline-success" onclick=mostrarEditarRol('${data[i].idRol}','${data[i].nombreRol}') >
    <i class="bi bi-pencil-square"></i> Editar</button>
    <button class="btn btn-outline-danger" onclick="document.getElementById('idRolConfirmar').innerHTML=${data[i].idRol}" data-bs-toggle="modal" data-bs-target="#modalEliminar">
    <i class="bi bi-trash"></i> Eliminar</button>
     </td></tr>`
    }

    document.getElementById('datos').innerHTML = body;
}
function verRoles(){
    let url = 'http://localhost:8080/api/roles/ver';
 fetch(url)
.then(respuesta=>respuesta.json())
.then(datos=>mostrarDatos(datos))
.catch(error=>console.log(error))

}

function eliminar(id){
    let url = 'http://localhost:8080/api/roles/eliminar'
    let bodyReq = {idRol:id};
    bodyReq = JSON.stringify(bodyReq);
   
    
    fetch(url,{
        method:'delete', headers:{
            'Content-Type': 'application/json'
        }, body:bodyReq
    }).then(respuesta => respuesta.json()).then(d=>{actualizar();
        console.log(d);
        $('#mensaje-notif').html(d.respuesta);
        $('#notificacion').toast('show');
    }).catch()
}



function actualizar(){
    $('#cargando').show(500).delay(500);
    $('#cargando').hide(500).delay(600);
    verRoles();
}


function editarRol(){   
    let bodyReq = {idRol: document.getElementById('formEditRolId').value, 
    nombreRol:document.getElementById('formEditRolnombre').value };
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/roles/modificar'
    fetch(url,{
        method:'post', headers:{
            'Content-Type': 'application/json'
        }, body:bodyReq
    }).then(respuesta => respuesta.json()).then(d=>{actualizar();
        console.log(d);
        $('#mensaje-notif').html(d.respuesta);
        $('#notificacion').toast('show');
    }).catch()
    

}


function guardarNuevoRol(){
    let nombre = document.getElementById('nombre-nuevo-rol').value;
    let bodyReq = {nombreRol: nombre};
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/roles/crear'
    fetch(url,{
        method:'post', headers:{
            'Content-Type': 'application/json'
        }, body:bodyReq
    }).then(respuesta => respuesta.json()).then(d=>{actualizar();
        $('#mensaje-notif').html(d.respuesta);
        $('#notificacion').toast('show');
    }).catch()
}

function nuevoRol(){
    $('#editarRol').hide();
    $('#crearRol').fadeIn();
    document.getElementById('titulo-form').innerHTML = 'Crear Rol';
}

function mostrarEditarRol(id, nombre){

    $('#crearRol').hide();
    $('#editarRol').fadeIn(); 
    document.getElementById('titulo-form').innerHTML = 'Modificar Rol';
     document.getElementById('formEditRolId').value = id;
     document.getElementById('formEditRolnombre').value = nombre;
    
}

