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

async function cargarSuscripcion(perfil,evento,modalidad){
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
    console.log(perfil)
    //let inscripcion= { evento: evento, alumno:,modalidad: evento.modalidad };
let inscripcion= { evento: {id:evento.id}, alumno:{id:perfil.idPerfil,email:perfil.email},modalidad: modalidad };

console.log("inscripcion predata",inscripcion)
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
    timer:2200
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





async function dardeBaja(){

    const evento = await fetch(`http://localhost:8080/api/eventos/ver/${idEventoUnico}`).then(res=>res.json());
    const verInsc= await fetch(`http://localhost:8080/api/eventos/inscripciones/${idEventoUnico}`).then(res=>res.json());


    const btnBaja = document.getElementById("btnBaja")
    btnBaja.innerHTML=`<i class="spinner-border text-light"> </i>`;

    await Swal.fire({
        title: `¿Confirmar Baja?`,
        text: `Te vas a dar de baja de ${evento.nombre}.`,
        showCancelButton:true,
        confirmButtonText:"Confirmar",
        cancelButtonText:"Cancelar"}).then((resultado) => {
        if(resultado.isConfirmed){
            enviarBaja()
        }
    })

    async function enviarBaja(){
        if(usernameLogged){
            for(let i=0; i<verInsc.length;i++){
                if(verInsc[i].alumno.usuario.username===usernameLogged){
                    const borrarInscripcion = await fetch(`http://localhost:8080/api/eventos/baja/${idEventoUnico}/${usernameLogged}`,{
                        method:'delete'
                    }).then(data=>data.json());
console.log(borrarInscripcion);
                    if(borrarInscripcion.error==="true"){
                        Toast.fire({
                            icon:'error', title: borrarInscripcion.respuesta
                        })
                    }else{
                        Swal.fire({
                            title:"Nos vemos",
                            imageUrl:"https://i.imgur.com/orim1C3.gif",
                            imageHeight:112,
                            text:borrarInscripcion.respuesta,
                            icon:'success',
                            confirmButtonText: `Ok`

                        }).then((result)=>{
                            location.reload();
                        })
                    }

                }

            }

        }
    }



}


var listaModalidades = ['ONLINE','MIXTA','PRESENCIAL'];

async function iniciarSubscripcion(){

    const listaModalidades = ['ONLINE','PRESENCIAL'];

    if(usernameLogged){


    const evento = await fetch(`http://localhost:8080/api/eventos/ver/${idEventoUnico}`).then(res=>res.json());
    if(evento) {

        const perfil = await verificarPerfil();

        let modalidad = ""

        Swal.fire({
            title: `Suscribirse a ${evento.nombre}`,
            text: "Verificando datos",
            showConfirmButton: false,
            timer: 800,
            willOpen: () => {
                Swal.showLoading()
                if (perfil) {
                    setTimeout(async function(){

                        if(evento.modalidad==="MIXTA"){
                            const {value:modalidadCasoMixto} = await Swal.fire({
                                input:'select',
                                title:`Elegir modalidad`,
                                inputOptions:listaModalidades,
                                inputPlaceholder:'Elegí la modalidad'
                            })
                            modalidad = listaModalidades[modalidadCasoMixto]

                        }else{
                            modalidad  = evento.modalidad;
                        }


                       await Swal.fire({
                        title:"Confirmar Inscripción",

                        html:
                        ` Te vas a inscribir a <br> <h3>${evento.nombre}</h3> dictado por <br>
                         <h4>${evento.facilitador.nombre} ${evento.facilitador.apellido}</h4> en modalidad <h4>${modalidad}</h4> 
                        con tus datos de Perfil: <br> <h5>${perfil.nombre} ${perfil.apellido} - ${perfil.email} - ${perfil.tel}</h5> 
                        ¿Confirmar?`,
                        confirmButtonText:"Confirmar Inscripcion",
                        cancelButtonText:"Cancelar",
                        showCancelButton:true
                    }).then((result)=>{
                        if(result.isConfirmed){
                            console.log(modalidad)
                            cargarSuscripcion(perfil,evento,modalidad)
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
    const columnaBtnHidden = document.getElementById("colBtnHidden");
    const btnBaja = document.getElementById("btnBaja")
    if(usernameLogged){
       //Verifico Suscripcion

       const verInsc= await fetch(`http://localhost:8080/api/eventos/inscripciones/${idEventoUnico}/${usernameLogged}`).then(res=>res.json());
     //  console.log(verInsc)
       if(verInsc.respuesta==="ok"){
           btnInscribir.disabled = true;
           btnInscribir.innerHTML =`${capitalizeFirstLetter(verInsc.estado.toLowerCase())}`;

           if(verInsc.estado==="PENDIENTE"){
             document.getElementById("mensajitobtn").setAttribute("title","Tu inscripción esta pendiente de confirmacion por un admin. Te avisaremos por mail cuando este confirmada.")
               let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
               let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                   return new bootstrap.Tooltip(tooltipTriggerEl)
               })
               btnBaja.style.display="";
           }else if(verInsc.estado==="CONFIRMADO"){
               document.getElementById("mensajitobtn").setAttribute("title","Tu inscripción esta confirmada!")
               let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
               let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                   return new bootstrap.Tooltip(tooltipTriggerEl)
               })
               btnBaja.style.display="";
           }

       }
    }

}
