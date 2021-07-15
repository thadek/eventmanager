window.onload = function(){
    verificarSubscripcion()
    subscribeFromRedirect()
}



const btnInscribir=document.getElementById("btnsuscribir");

btnInscribir.addEventListener('click',function(){
    verificarSesionDesdeBoton()
})


function subscribeFromRedirect(){
    const parametrosUrl = new URLSearchParams(window.location.search);
    const suscribirParam = parametrosUrl.get('subscribe')
    if(suscribirParam!=null){
        iniciarSubscripcion()
    }
}

function verificarSesionDesdeBoton(){
   if(!usernameLogged){
       Swal.fire({
           title:'Inicia sesión para continuar.',
           icon:'info',
           confirmButtonText: `Ok`

       }).then((result)=>{
               window.location.replace(`http://localhost:8080/eventos/subscribe?ev=${idEventoUnico}`)
       })
   }else{
       togglerBotonLoad()
   }
}

function togglerBotonLoad(){

    if(btnInscribir.innerHTML===`<i class="spinner-border text-light"> </i>`){
      btnInscribir.innerHTML = "Inscribirse"
      }else{
        btnInscribir.innerHTML=`<i class="spinner-border text-light"> </i>`
        iniciarSubscripcion()
      }

}

async function verificarPerfil(){

    let url = `http://localhost:8080/api/perfil/ver/${usernameLogged}`;
    const perfil = await fetch(url).then(respuesta=>respuesta.json()).catch(error=>{})
        if(perfil){
            return perfil;
        }else{
            return false;
        }
}

async function cargarSuscripcion(perfil,evento){
   //Procesar datos

    let logo=`<div class="contenedor-logo">
<div class="coast">
    <div class="wave-rel-wrap">
      <div class="wave"></div>
    </div>
  </div>
  <div class="coast delay">
    <div class="wave-rel-wrap">
      <div class="wave delay"></div>
    </div>
  </div>
  
  <div class="logoAnim logoAnim-a">e</div>
  <div class="logoAnim logoAnim-v">v</div>
  <div class="logoAnim logoAnim-e">n</div>

</div><br>

`
    let inscripcion= { evento: evento, alumno:perfil,modalidad: evento.modalidad };
//let inscripcion= { evento: {id:evento.id}, alumno:{id:perfil.idPerfil,email:perfil.email},modalidad: evento.modalidad };


inscripcion = JSON.stringify(inscripcion);
console.log(inscripcion)

const inscribir = await fetch("http://localhost:8080/api/eventos/inscripciones/nueva",
    {method: 'post', headers: {
        'Content-Type': 'application/json'
    }, body: inscripcion}).then(resp=>resp.json());



Swal.fire({
    title:"Un momento.",
    html:logo,
    showConfirmButton: false,
    //timer:2200
}).then((resolve)=>{

    if(!(inscribir.error==="true")){
        //Tarjeta Bienvenida
        Swal.fire({
            title:`¡Te damos la bienvenida!`,
            html:`Tu inscripción a <br> <h3>${evento.nombre}</h3> 
              por <h4>${evento.facilitador.nombre} ${evento.facilitador.apellido}</h4>
              se registró exitosamente. <br>
              Recordá que la misma debe ser aprobada por un administrador al momento del pago.
         `,
            icon:'success'
        }).then((result)=>{verificarSubscripcion()})
    }else{
        Swal.fire({
            title: "Ocurrió un error.",
            html:`${inscribir.respuesta}`,
            icon:'error'
        })

    }



})






}


async function iniciarSubscripcion(){
    if(usernameLogged){

    const evento = await fetch(`http://localhost:8080/api/eventos/ver/${idEventoUnico}`).then(res=>res.json());
    if(evento) {

        const perfil = await verificarPerfil();
        Swal.fire({
            title: `Suscribirse a ${evento.nombre}`,
            text: "Verificando datos",
            showConfirmButton: false,
            timer: 800,
            willOpen: () => {
                Swal.showLoading()
                if (perfil) {
                    setTimeout(function(){
                    Swal.fire({
                        title:"Confirmar Inscripción",

                        html:
                        ` Te vas a inscribir a <br> <h3>${evento.nombre}</h3> dictado por <br>
                         <h4>${evento.facilitador.nombre} ${evento.facilitador.apellido}</h4> en modalidad <h4>${evento.modalidad}</h4> 
                        con tus datos de Perfil: <br> <h5>${perfil.nombre} ${perfil.apellido} - ${perfil.email} - ${perfil.tel}</h5> 
                        ¿Confirmar?`,
                        confirmButtonText:"Confirmar Inscripcion",
                        cancelButtonText:"Cancelar",
                        showCancelButton:true
                    }).then((result)=>{
                        if(result.isConfirmed){
                            cargarSuscripcion(perfil,evento)
                        }else{
                            togglerBotonLoad()
                        }

                    })
                        },400)
                } else {
                    //Timeout para que muestre la ventanita >:x
                    setTimeout(function(){
                        Swal.fire({
                            title: "No hay perfil creado",
                            icon: 'error',
                            text: "Necesitas un perfil creado para poder inscribirte en cualquier evento.",
                            confirmButtonText: "Crearme un perfil",
                            cancelButtonText: "Cancelar",
                            showCancelButton: true,

                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.replace(`http://localhost:8080/perfil`)
                            }
                            togglerBotonLoad()
                        })
                    },800)

                }

            }



        })


    }


    }


}

async function verificarSubscripcion(){
    const columnaBtn = document.getElementById("colBtnSub");
    const columnaBtnHidden = document.getElementById("colBtnHidden");
    if(usernameLogged){
       //Verifico Suscripcion

       const verInsc= await fetch(`http://localhost:8080/api/eventos/inscripciones/${idEventoUnico}/${usernameLogged}`).then(res=>res.json());
       console.log(verInsc)
       if(verInsc.respuesta){
           btnInscribir.disabled = true;
           btnInscribir.innerHTML ="Inscripto";
       }
    }

}
