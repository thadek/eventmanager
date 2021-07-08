verPerfil($("#usuario").attr("value"));



const mostrarDatos = (data) => {
    var perfil = data;
    console.log(perfil);
    document.getElementById('nombrePerfil').innerHTML = perfil.nombre + " " + perfil.apellido;
    document.getElementById('email').innerHTML = perfil.email;
    document.getElementById('tel').innerHTML = perfil.tel;
    document.getElementById('fechaNac').innerHTML = perfil.fechaNac;
    document.getElementById('descripcionPerfil').innerHTML = perfil.descripcion;
    document.getElementById('fotoPerfil').src=perfil.fotoURL;
}

function noExistePerfil (){
    document.getElementById('cardPerfil').style.opacity = 0.2;
    $("#noPerfil").show();
}

function verPerfil(username){
    let url = `http://localhost:8080/api/perfil/ver/${username}`;
 fetch(url)
.then(respuesta=>respuesta.json())
.then(datos=>{
    if(datos){
        mostrarDatos(datos);        
    } else {

    }
})
.catch(error=>noExistePerfil()


)}

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

