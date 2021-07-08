try{
   verPerfil($("#usuario").attr("value")); 
}catch(e){
   
}
cargarFotito();
var usuarioCargado = false;

const mostrarDatos = (data) => {
    $('#cargando-card').hide();
    $('#contenido').show();
    let perfil = data; 
    usuarioCargado=true;
    listenerGuardar(perfil);
    //console.log(perfil);
    //Mostrar datos normal
    document.getElementById('nombrePerfil').innerHTML = perfil.nombre + " " + perfil.apellido;
    document.getElementById('email').innerHTML = perfil.email;
    document.getElementById('tel').innerHTML = perfil.tel;
    document.getElementById('fechaNac').innerHTML = perfil.fechaNac;
    document.getElementById('descripcionPerfil').innerHTML = perfil.descripcion;
    document.getElementById('fotoPerfil').src=perfil.fotoURL;

    //Mostrar datos para edición
    document.getElementById('nombreInput').value = perfil.nombre;
    document.getElementById('apellidoInput').value= perfil.apellido
    document.getElementById('emailInput').value = perfil.email;
    document.getElementById('telInput').value = perfil.tel;
    document.getElementById('fechaInput').value = perfil.fechaNac;
    document.getElementById('descripcionInput').value = perfil.descripcion;
    document.getElementById('fotoPerfilEdit').src=perfil.fotoURL;
}

function noExistePerfil (){
    $('#cargando-card').hide();
    $('#contenido').show();
    document.getElementById('cardPerfil').style.opacity = 0.2;
    $("#noPerfil").show();
}

function verPerfil(username){
$('#contenido').hide();
$('#cargando-card').show();


    let url = `http://localhost:8080/api/perfil/ver/${username}`;
 fetch(url)
.then(respuesta=>respuesta.json())
.then(datos=>{
    if(datos){     
        setTimeout(function(){mostrarDatos(datos);},400);
                
    } 
})
.catch(error=>noExistePerfil())}

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


function cancelar(){
    $("#formulario-edit").hide();
    $("#contenido").show(); 
}

function actualizarPerfilButton(){

  $("#contenido").hide(100);
  $("#formulario-edit").show(100);
    document.getElementById('nombrePerfil');   
    document.getElementById('email')
    document.getElementById('tel')
    document.getElementById('fechaNac')
    document.getElementById('descripcionPerfil')
    document.getElementById('fotoPerfil')
}

function cargarFotito(){
    const fotoinput = document.getElementById("inputFoto")
        const img = document.getElementById("fotoPerfilEdit")      
        fotoinput.addEventListener("change", ev => {
            const formdata = new FormData()       
            //Si pongo algo, hago, sino no hago nada
            if(ev.target.files[0]){
            $('#cargando').show();
                formdata.append("image", ev.target.files[0])
            fetch("https://api.imgur.com/3/image/", {
                method: "post",
                headers: {
                    Authorization: "Client-ID e03bb4be3d61985"
                },
                body: formdata
            }).then(data => data.json()).then(data => {
                $('#cargando').hide();
                img.src = data.data.link               
            })
            }else{
                return;
            }
            
        })
}

function nuevoPerfil(){
    $("#noPerfil").hide();
    $("#contenido").hide(100);
    $("#btnCancelar").hide();
    $("#formulario-edit").show(100);

    document.getElementById('tituloForm').innerHTML = 'Crear Perfil';
    document.getElementById('cardPerfil').style.opacity = 1;
    crearPerfilNuevo($("#usuario").attr("value"));
}

function actualizar(){
    verPerfil($("#usuario").attr("value")); 
    $("#formulario-edit").hide(100);
    $("#contenido").show(100);
}

function listenerGuardar(perfil){

    //Evaluo si guardar nuevo perfil o modificar existente
    
      //Guardo el perfil sin modificar
   const perfilSinModificar = JSON.parse(JSON.stringify(perfil)); 
   //Guardo la variable que va a cambiar y compararse
   let perfilModificado = JSON.parse(JSON.stringify(perfil));

   const btnGuardar = document.getElementById('btnGuardar');
   btnGuardar.addEventListener('click',function(){
       if (usuarioCargado){
           perfilModificado.nombre = document.getElementById('nombreInput').value;
           perfilModificado.apellido = document.getElementById('apellidoInput').value;
           perfilModificado.email = document.getElementById('emailInput').value;
           perfilModificado.tel = document.getElementById('telInput').value;
           perfilModificado.fechaNac = document.getElementById('fechaInput').value;
           perfilModificado.descripcion = document.getElementById('descripcionInput').value;
           perfilModificado.fotoURL = document.getElementById('fotoPerfilEdit').src;

           //Si hay cambios, guardo, sino no hago nada
          if(!(JSON.stringify(perfilModificado)===JSON.stringify(perfilSinModificar))){
             
             modificarDatos(perfilModificado);
             
          }else{        
              cancelar();
          }

       } 

   })
 
function modificarDatos(perfil){
    let bodyReq = JSON.stringify(perfil);   
    let url = `http://localhost:8080/api/perfil/modificar/${perfil.usuario.username}`
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
   


 




   
}

function crearPerfilNuevo(username){

    let perfilTemplate = JSON.parse(`{"email":"ejemplo","nombre":"ejemplo","apellido":"ejemplo","tel":"000","fechaNac":"1991-01-27","descripcion":"ejemplo","usuario":{"username":"${username}"},"fotoURL":"https://i.imgur.com/tRLkAZB.png"}`);
    const perfilSinModificar = JSON.parse(JSON.stringify(perfilTemplate)); 
    //Guardo la variable que va a cambiar y compararse
    let perfilModificado = JSON.parse(JSON.stringify(perfilTemplate));
   
    
    const btnGuardar = document.getElementById('btnGuardar');
    btnGuardar.addEventListener('click',function(){

   // console.log(perfilModificado.nombre)
   perfilModificado.nombre = document.getElementById('nombreInput').value;
   perfilModificado.apellido = document.getElementById('apellidoInput').value;
   perfilModificado.email = document.getElementById('emailInput').value;
   perfilModificado.tel = document.getElementById('telInput').value;
   perfilModificado.fechaNac = document.getElementById('fechaInput').value;
   perfilModificado.descripcion = document.getElementById('descripcionInput').value;
   perfilModificado.fotoURL = document.getElementById('fotoPerfilEdit').src;


    if(perfilModificado.nombre!="ejemplo" && perfilModificado.nombre){
        //Si pasa esta validación, persistir al backend.
        guardarPerfilNuevo(perfilModificado);
    }else{
        alert("nombre es un campo requerido.");
    }

    })

    function guardarPerfilNuevo(perfil){
            let bodyReq = JSON.stringify(perfil);   
            let url = `http://localhost:8080/api/perfil/crear/${perfil.usuario.username}`
            fetch(url,{
                method:'post', headers:{
                    'Content-Type': 'application/json'
                }, body:bodyReq
            }).then(respuesta => respuesta.json()).then(d=>{
                actualizar();
                $("#btnCancelar").show();         
                $('#mensaje-notif').html(d.respuesta);
                $('#notificacion').toast('show');
            }).catch()
        



    }


   

   
}


