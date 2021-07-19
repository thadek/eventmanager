var usernameLogged;

togglers();

$( document ).ready(function() {
    try{
        usernameLogged = document.getElementById("usdatos").innerHTML;

    }catch(error){

    }
    actualizar();

});

function togglers() {
    $('#togglerSeccion').click(function () {
            $('#containerUsuario').fadeToggle(150);
        }
    )
    $('#togglerInscPorEvento').click(function () {
            $('#containerporEvento').fadeToggle(150);
        }
    )

}

async function cargarEventoSeleccionado(){
    $('#cargando').show(900).delay(500);
    $('#cargando').hide(900).delay(600);
    let select = document.getElementById("listaTodosLosEventos")
    let valorSeleccionado = select.options[select.selectedIndex].value;
    if(valorSeleccionado==='Selecciona un evento...'){
      Toast.fire({
          icon:'info', title: 'No te hagas el vivo, elegi algun evento a renderizar..'
      })
    }else{
         const evento = await fetch(`http://localhost:8080/api/eventos/ver/${valorSeleccionado}`).then(res=>res.json());
        mostrarEventoEspecifico(evento)
     }



}

const mostrarEventoEspecifico = (data) => {
    let body = ''
console.log(data.inscripciones)
    if(document.getElementById('datosEvGenerico')){

        for (let i = 0; i < data.inscripciones.length; i++) {

            body +=
                `<tr>
            <td>${data.inscripciones[i].idInscripcion}</td>
            <td>${data.nombre} - ${data.facilitador.nombre} ${data.facilitador.apellido} - ${data.modalidad}</td>
            <td>${data.inscripciones[i].modalidad}</td>
            <td>${data.inscripciones[i].alumno.nombre} ${data.inscripciones[i].alumno.apellido}</td>
            <td>${data.inscripciones[i].estado}</td>              
            
            <td>
                
                <button class="btn btn-outline-danger"  onclick=confirmarEliminacion(${data.inscripciones[i].idInscripcion}) >
                    <i class="bi bi-trash"></i> 
                    Eliminar Inscripcion
                </button>


            </td>
        </tr>`



        }

        document.getElementById('datosEvGenerico').innerHTML = body;
    }


//console.log(data)

}



async function mostrarListadeEventos(){
  const eventos = await fetch('http://localhost:8080/api/eventos/vertodos').then(respuesta => respuesta.json())
    let selector='<option>Selecciona un evento...</option> ';

    for(i=0;i<eventos.length;i++){
        selector+= ` <option value="${eventos[i].id}"> ${eventos[i].nombre} - ${eventos[i].facilitador.nombre} ${eventos[i].facilitador.apellido} - ${eventos[i].modalidad} </option>
           `
    }
   document.getElementById('listaTodosLosEventos').innerHTML=selector

    $('#listaTodosLosEventos').selectize({
        sortField:'text'
    });

}




function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);

}
const mostrarDatos = (data) => {
    let body = ''
    if(document.getElementById('datos')){
        for (let i = 0; i < data.length; i++) {


            body +=
                `<tr>
            <td>${data[i].idInscripcion}</td>
            <td>${data[i].evento.nombre} - ${data[i].evento.facilitador.nombre} ${data[i].evento.facilitador.apellido} - ${data[i].evento.modalidad}</td>
            <td>${data[i].modalidad}</td>
            <td>${data[i].alumno.nombre} ${data[i].alumno.apellido}</td>
            <td>${data[i].estado}</td>              
            
            <td>
                <button class="btn btn-outline-success" onclick=confirmarInscripcion(${data[i].idInscripcion}) >
                    <i class="bi bi-pencil-square"></i> 
                        Confirmar
                </button>
        
                <button class="btn btn-outline-danger"  onclick=confirmarEliminacion(${data[i].idInscripcion}) >
                    <i class="bi bi-trash"></i> 
                    Eliminar Inscripcion
                </button>


            </td>
        </tr>`



        }

        document.getElementById('datos').innerHTML = body;
    }else{
        setTimeout(function(){
            console.log("entro a mostrardatos")
            notificacionAdm(data);
        },500)
    }

//console.log(data)

}

async function actualizar() {
    $('#cargando').show(900).delay(500);
    $('#cargando').hide(900).delay(600);
    await verInscripcionesPendientes();
    await mostrarListadeEventos();
}

async function verInscripcionesPendientes() {
    let url = 'http://localhost:8080/api/eventos/inscripciones/pendientes';
    await fetch(url)
        .then(respuesta => respuesta.json())
        .then(datos => {
                mostrarDatos(datos)
            }
        )
        .catch(error => console.log(error))

}

function cerrarNotif(){
    $('#notificacionadm').hide();
}

function notificacionAdm(datos){
    if(datos.length>0){
        document.getElementById("mensajenotif").innerHTML=`Hola ${capitalizeFirstLetter(usernameLogged.toLowerCase())}! Hay ${datos.length} inscripciones por confirmar!`
        $('#notificacionadm').fadeIn();
       // console.log(`hay ${datos.length} inscripciones sin confirmar`)
    }

}




async function eliminarInscripcion(data){

    let bodyReq = JSON.stringify(data);
    const res = await fetch(`http://localhost:8080/api/eventos/inscripciones/eliminar`, {
        method: 'delete', headers: {
            'Content-Type': 'application/json'
        },body:bodyReq}).
    then((respuesta) => respuesta.json())

    if(!(res.error)){
        Toast.fire({
            icon:'success', title: res.respuesta
        })
    }else{
        Toast.fire({
            icon:'error', title: res.error
        })
    }
    actualizar();

}


async function enviarConfirmacion(id){
   const res = await fetch(`http://localhost:8080/api/eventos/inscripciones/confirmar/${id}`, {
        method: 'post', headers: {
            'Content-Type': 'application/json'
        }}).
   then((respuesta) => respuesta.json())

    if(!(res.error)){
        Toast.fire({
            icon:'success', title: res.respuesta
        })
    }else{
        Toast.fire({
            icon:'error', title: res.error
        })
    }
    actualizar()


}

async function confirmarInscripcion(id){

  const data = await fetch(`http://localhost:8080/api/eventos/inscripciones/buscarporid/${id}`).then(res=>res.json())

    //console.log(data)
   await Swal.fire({
       title: `Confirmar Inscripcion de  ${data.alumno.nombre} ${data.alumno.apellido} a ${data.evento.nombre}`,
       showCancelButton:true,
       confirmButtonText:"Confirmar",
       cancelButtonText:"Cancelar"}).then((resultado) => {
           if(resultado.isConfirmed){
                enviarConfirmacion(id);
           }
   })
}

async function confirmarEliminacion(id){

    const data = await fetch(`http://localhost:8080/api/eventos/inscripciones/buscarporid/${id}`).then(res=>res.json())


    await Swal.fire({
        title: `¿Eliminar la Inscripción de  ${data.alumno.nombre} ${data.alumno.apellido} a ${data.evento.nombre}?`,
        showCancelButton:true,
        confirmButtonText:"Eliminar",
        cancelButtonText:"Cancelar"}).then((resultado) => {
        if(resultado.isConfirmed){
            eliminarInscripcion(data);
        }
    })
}