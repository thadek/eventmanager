
let idEventoUnico;
let usernameLogged;
let porcentajeOcup;
let evento;
$( document ).ready(function() {
   try{
       idEventoUnico = document.getElementById("idEventoUnico").innerHTML

   } catch(error) {
       console.log(error)}


    try{
        usernameLogged = document.getElementById("usdatos").innerHTML;
    }catch(error){

    }

    porcentajeOcup = 0;
    cargandoToggler(1050)
});



const Toast = Swal.mixin({
    toast: true,
    position: 'top-right',
    iconColor:'white',
    customClass:{
        popup: 'colored-toast'
    },
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})




async function  renderizarListaPublica(arrayEventos) {
    let body = '';
    let ejemploPorcentajes = [0.44, 0.55];

    let dias = '';

    if (arrayEventos) {

        for (let i = 0; i < arrayEventos.length; i++) {

            const idEv = arrayEventos[i].id;



            for(let j=0; j<arrayEventos[i].dias.length;j++){
                if(j === (arrayEventos[i].dias.length-1)){
                    dias+= `${capitalizeFirstLetter(arrayEventos[i].dias[j].toLowerCase())}. `
                }else{
                    dias+= `${capitalizeFirstLetter(arrayEventos[i].dias[j].toLowerCase())}, `
                }
            }

         //  console.log(dibujarCirculito(i))

            body += `<li>
     <a href="http://localhost:8080/eventos/ver/${arrayEventos[i].id}">
     <div class="row descripcionEvento">
       <div class="col-lg-2 col-md-4 circuloprogreso circuloprogreso-${arrayEventos[i].id}"><strong>${await getPorcentajeOcupacionEvento(arrayEventos[i].id,arrayEventos[i].modalidad)}%</strong>
         <p>Ocupacion</p>
       </div>
       <div class="col-lg-10 col-md-8">
         <h3 class="eventName">${arrayEventos[i].nombre}</h3>
         <p class="eventModalidad">${capitalizeFirstLetter(arrayEventos[i].modalidad.toLowerCase())}</p>
         <p class="eventProfessor">${arrayEventos[i].facilitador.nombre} ${arrayEventos[i].facilitador.apellido}</p>
         <h6 class="eventOverview">${dias} - ${formatearHora(arrayEventos[i].hora)}</h6>
       </div>

     </div>
       </a>
   </li> `
            dias= '';
        }

        document.getElementById("listaEvn").innerHTML = body;

      for (let i = 0; i < arrayEventos.length; i++) {

         let ocupacion = await getPorcentajeOcupacionEvento(arrayEventos[i].id,arrayEventos[i].modalidad)

          if(ocupacion===0){
              dibujarCirculito( 0,arrayEventos[i].id)
          }else{
              dibujarCirculito( ocupacion/100,arrayEventos[i].id)
          }


        }

    } else {
        body = `<li>  <h3>No hay eventos cargados en sistema.</h3></li>`
        document.getElementById("listaEvn").innerHTML = body;
    }


}


function cargarListaEventosPublica(){
    fetch('http://localhost:8080/api/eventos/vertodos').then(respuesta => respuesta.json()).then(datosEv=>{renderizarListaPublica(datosEv)})

}


function cargarListaEventosPrivada(){
    fetch(`http://localhost:8080/api/eventos/miseventos/${usernameLogged}`).then(respuesta => respuesta.json()).then(datosEv=>{
        renderizarListaPrivada(datosEv)
    })

}

async function getPorcentajeOcupacionEvento(idEvento,modalidad){

    if(modalidad === 'ONLINE'){
        const ocup = await fetch(`http://localhost:8080/api/eventos/ocupacion/${idEvento}/online`).then(respuesta => respuesta.json())
        return ocup.porcentaje
    }else if(modalidad === 'PRESENCIAL') {
        const ocup = await fetch(`http://localhost:8080/api/eventos/ocupacion/${idEvento}/presencial`).then(respuesta => respuesta.json())

        return ocup.porcentaje
    }
    else{
    const ocup = await fetch(`http://localhost:8080/api/eventos/ocupacion/${idEvento}/mixta`).then(respuesta => respuesta.json())
           return ocup.porcentaje;        

    }

}

async function renderizarListaPrivada(arrayEventos) {
    let body = '';
    //let ejemploPorcentajes = [0.2, 0.66, 0.80];

    let dias = '';


    if (arrayEventos) {
        for (let i = 0; i < arrayEventos.length; i++) {

           const porc = await getPorcentajeOcupacionEvento(arrayEventos[i].id,arrayEventos[i].modalidad)

            for(let j=0; j<arrayEventos[i].dias.length;j++){
                if(j === (arrayEventos[i].dias.length-1)){

                    dias+= `${capitalizeFirstLetter(arrayEventos[i].dias[j].toLowerCase())}. `
                }else{
                    dias+= `${capitalizeFirstLetter(arrayEventos[i].dias[j].toLowerCase())}, `
                }
            }

            //console.log("LOG LISTA PRIVADA",porcentajeOcup)
           // dibujarCirculito(i)


            body += `<li>
         <a href="http://localhost:8080/eventos/ver/${arrayEventos[i].id}">
         <div class="row descripcionEvento">
           <div class="col-lg-2 col-md-4 circuloprogreso circuloprogreso-${arrayEventos[i].id}"><strong>${porc}%</strong>
             <p>Ocupacion</p>
           </div>
           <div class="col-lg-10 col-md-8">
             <h3 class="eventName">${arrayEventos[i].nombre}</h3>
             <p class="eventModalidad">${capitalizeFirstLetter(arrayEventos[i].modalidad.toLowerCase())}</p>
             <p class="eventProfessor">${arrayEventos[i].facilitador.nombre} ${arrayEventos[i].facilitador.apellido}</p>
             <h6 class="eventOverview">${dias} - ${formatearHora(arrayEventos[i].hora)}</h6>
           </div>
    
         </div>
           </a>
       </li> `

            dias= '';

        }

        document.getElementById("listaMisEventos").innerHTML = body;

        for (let i = 0; i < arrayEventos.length; i++) {

            let ocupacion = await getPorcentajeOcupacionEvento(arrayEventos[i].id,arrayEventos[i].modalidad)

            if(ocupacion===0){
                dibujarCirculito( 0,arrayEventos[i].id)
            }else{
                dibujarCirculito( ocupacion/100,arrayEventos[i].id)
            }


        }


    } else {
        body = `<li>  <h3>No hay eventos cargados en tu usuario.</h3></li>`
        document.getElementById("listaMisEventos").innerHTML = body;
    }

}

function formatearHora(hora){
   const fecha = `1990/05/05 ${hora}`
   const f = new Date(fecha);
   //correccion los primeros 10 min de la hora al parsear
   if(f.getMinutes()<10){
       minutos="0"+f.getMinutes();
   }else{
       minutos=f.getMinutes()
   }
   const nuevaHora = `${f.getHours()}:${minutos}`;
   return nuevaHora
}


function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);

}





async function renderizarEvento(){
    //Cargar Evento Individual

    const evento = await fetch(`http://localhost:8080/api/eventos/ver/${idEventoUnico}`).then(res=>res.json());


    document.getElementById("modalidadEvento").innerHTML = capitalizeFirstLetter(evento.modalidad.toLowerCase());
    let dias =  "";
    for(let i=0; i<evento.dias.length;i++){
        if(i === (evento.dias.length-1)){
            dias+= `${capitalizeFirstLetter(evento.dias[i].toLowerCase())}. `
        }else{
            dias+= `${capitalizeFirstLetter(evento.dias[i].toLowerCase())}, `
        }
    }
    let fechasEvento = '';
    if (!evento.fechaFin){
        fechasEvento=evento.fechaInicio
    }else{
        fechasEvento= `${evento.fechaInicio} - ${evento.fechaFin}`
    }

    document.getElementById("diasEvento").innerHTML =  fechasEvento;
    document.getElementById("dias").innerHTML = dias;
    let ocupacion = await getPorcentajeOcupacionEvento(evento.id,evento.modalidad)
    if(evento.modalidad=='ONLINE'){
        document.getElementById("ocupacionOnline").innerHTML = `${ocupacion}%`
        document.getElementById("ocupacionPresencial").remove();
    }else if(evento.modalidad=='PRESENCIAL'){
        document.getElementById("ocupacionPresencial").innerHTML = `${ocupacion}%`

    }else{
        document.getElementById("ocupacionPresencial").innerHTML = `${ocupacion}%`
        document.getElementById("ocupacionOnline").remove();
    }
    document.getElementById("hora").innerHTML=`${formatearHora(evento.hora)}hs`
    document.getElementById("duracion").innerHTML= `${evento.duracion} mins`;
    document.getElementById("precio").innerHTML = `$${evento.valor}`
    document.getElementById("descripcionEvento").innerHTML = evento.descripcion;
    document.getElementById("fotitoProfe").src=evento.facilitador.fotoURL
    document.getElementById("fotitoProfe2").src=evento.facilitador.fotoURL
    document.getElementById("nombreProfesor").innerHTML = `${evento.facilitador.nombre} ${evento.facilitador.apellido}`

setTimeout( function(){
    $('.loader-anim').hide();
    $('.descripcionCompletaEv').show();
    $('.informacionContainer').show();
    $('.bodyEvent').show();
    $('.contenidoEvento').show();
},200)

}


var listaSubcategorias = {}
var listaModalidades = ['ONLINE','MIXTA','PRESENCIAL'];
var dias = ['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'];
var profesores = {};
let nuevoEvento = new Object();


async function cargarRecursos(){
    //Cargo las subcategorias
    await fetch('http://localhost:8080/api/subcategorias/ver').then(respuesta=> respuesta.json()).then(data=>{
        for(let i=0; i<data.length;i++){
            listaSubcategorias[data[i].idSubcategoria]=data[i].nombre;
        }
        }
    )

    await fetch('http://localhost:8080/api/perfil/profesores').then(respuesta=> respuesta.json()).then(data=>{
        for(let i=0; i<data.length;i++){
            profesores[data[i].email]=data[i].nombre+' '+data[i].apellido;
        }
    })

}


async function crearEvento(){
    await cargarRecursos();

const pasos = ['1','2','3','4','5','6','7']

const swalQueue = Swal.mixin({
        progressSteps: pasos,
        confirmButtonText: 'Siguiente',
    })


    // 1 - Mostrar y elegir subcategoria
    const { value: subcat } = await swalQueue.fire({
        title: 'Seleccioná una subcategoría de evento',
        input: 'select',
        inputOptions:listaSubcategorias,
        inputPlaceholder: 'Selecciona una Subcategoría',
        currentProgressStep:0
    })

    if (subcat) {
        nuevoEvento.subcategoria={idSubcategoria:subcat};
    }





   // 2 - Elegir modalidad
    const { value: modalidad } = await swalQueue.fire({
        title: 'Selecciona la modalidad',
        input: 'select',
        inputOptions:listaModalidades,
        inputPlaceholder: 'Selecciona modalidad',
        showCancelButton: true,
        currentProgressStep:1,
    })


    if(modalidad){ nuevoEvento.modalidad = listaModalidades[modalidad]

        if(nuevoEvento.modalidad == 'PRESENCIAL'){

            const {value:modalidadCupoPres} = await swalQueue.fire({
                title: `Elegi el cupo disponible para modalidad ${nuevoEvento.modalidad}`,
                 html:`<input id="cupoPresencial" class="swal2-input" type="number"> 
                        <p>Cupo Presencial</p>`,

                currentProgressStep:2,
                focusConfirm: false,
                preConfirm: () => {
                    return [
                        document.getElementById('cupoPresencial').value
                    ]
                    }
        })

            nuevoEvento.cupoPresencial = modalidadCupoPres[0];
            nuevoEvento.cupoVirtual = 0;

        }else if(nuevoEvento.modalidad == 'ONLINE'){

            const {value:modalidadCupoOnline} = await swalQueue.fire({
                title: `Elegi el cupo disponible para modalidad ${nuevoEvento.modalidad}`,
                html:`<input id="cupoOnline" class="swal2-input" type="number"> 
                        <p>Cupo Online</p>`,

                currentProgressStep:2,
                focusConfirm: false,
                preConfirm: () => {
                    return [
                        document.getElementById('cupoOnline').value
                    ]
                }
            })

            nuevoEvento.cupoVirtual = modalidadCupoOnline[0];
            nuevoEvento.cupoPresencial = 0;
        }else{

            const {value:modalidadCupoMixta} = await swalQueue.fire({
                title: `Elegi el cupo disponible para modalidad ${nuevoEvento.modalidad}`,
                html:`<input id="cupoPresencial" class="swal2-input" type="number"> 
                     <p>Cupo Presencial</p>
                     <input id="cupoOnline" class="swal2-input" type="number"> 
                    <p>Cupo Online</p>`,

                currentProgressStep:2,
                focusConfirm: false,
                preConfirm: () => {
                    return [
                        document.getElementById('cupoPresencial').value,
                        document.getElementById('cupoOnline').value

                    ]
                }
            })


            nuevoEvento.cupoPresencial = modalidadCupoMixta[0]
           nuevoEvento.cupoVirtual = modalidadCupoMixta[1]

        }

    }



    //Cupo Modalidad






 // 3 - Elegir Profesor
    const { value: profesor } = await swalQueue.fire({
        title: ' Seleccioná un profesor',
        input: 'select',
        inputOptions:profesores,
        inputPlaceholder: 'Selecciona Profe',
        currentProgressStep:3
    })

    if (profesor) {
        nuevoEvento.facilitador={email:profesor};
    }

// 4 - Nombre evento
    const { value: nombreEv } = await swalQueue.fire({
        title: 'Ahora, el nombre del evento',
        input: 'text',
        inputPlaceholder: 'Nombre del evento',
        currentProgressStep:4
    })

    if (nombreEv) {
        nuevoEvento.nombre = nombreEv;
    }

// 5 - Dias
const { value:diasEv} = await swalQueue.fire({
    title: 'Dias de la semana que se va a realizar',
    html:
        ' <select id="diasEvento" multiple="multiple" type="text">' +
        ' <option value="LUNES">Lunes</option>' +
        '   <option value="MARTES">Martes</option>' +
        '  <option value="MIERCOLES">Miercoles</option>' +
        '  <option value="JUEVES">Jueves</option>' +
        ' <option value="VIERNES">Viernes</option>' +
        ' <option value="SABADO">Sabado</option>' +
        ' <option value="DOMINGO">Domingo</option>'+
        '</select>',
    didOpen(){
        $('#diasEvento').selectize({
            sortField: 'text'});
        document.getElementById("diasEvento").style.zIndex="1000";
    }
    , currentProgressStep:5,
    focusConfirm: false,
    customClass:{
        confirmButton:'botonDias'
    },
    buttonsStyling: false,
    preConfirm: () => {
        return[
            [...document.getElementById('diasEvento').options].filter(option => option.selected).map(option => option.value)
        ]
    }
})

    if(diasEv){
        nuevoEvento.dias = diasEv[0];
    }


    //6 -  Demas
    const { value: formValues } = await swalQueue.fire({
        title: ' Ya casi! solo faltan estos detalles:',
        html:

            `
            <input id="fechaInicio" class="form-control"  type="date">
            <p>Fecha Inicio</p>
            <input id="fechaFin" class="form-control" type="date"> 
            <p>Fecha Fin</p>
                <input id="hora" class="form-control" type="time"> 
            <p>Hora</p>
            <input id="duracionEvento"  class="form-control"type="number">
            <p>Duracion en minutos</p>
            <input id="valorNuevoEvento" class="form-control" type="number">
            <p>Valor</p>                                                   
            <div class="form-floating">
              <textarea class="form-control"  id="descripcionNuevoEvento"></textarea>
              <label for="descripcionNuevoEvento">Descripción</label>
            </div>  `

        ,currentProgressStep:6,
        focusConfirm: false,
        customClass:{
            content:'cartelito-sweetAlert',
            input:'cartelito-sweetAlert'

        },
        preConfirm: () => {
            return [

                document.getElementById('fechaInicio').value,
                document.getElementById('fechaFin').value,
                document.getElementById('hora').value,
                document.getElementById('duracionEvento').value,
                document.getElementById('valorNuevoEvento').value,
                document.getElementById('descripcionNuevoEvento').value


            ]
        }
    })

 // console.log(profesores[nuevoEvento.facilitador.email])

    if (formValues) {


        nuevoEvento.fechaInicio = formValues[0];
        nuevoEvento.fechaFin = formValues[1];
        nuevoEvento.hora = formValues[2];
        nuevoEvento.duracion = formValues[3];
        nuevoEvento.valor = formValues[4];
        nuevoEvento.descripcion = formValues[5];
    }

     let datosIngresados = `<strong>Nombre evento:</strong> ${nuevoEvento.nombre}<br>  
                             <strong>Modalidad:</strong> ${nuevoEvento.modalidad}<br> 
                             <strong> Profe:</strong> ${profesores[nuevoEvento.facilitador.email]}<br>                        
                             <strong> Dias:</strong> ${nuevoEvento.dias} <br>
                              <strong>Subcategoria:</strong> ${listaSubcategorias[nuevoEvento.subcategoria.idSubcategoria]}<br> 
                              <strong>Valor:</strong> ${nuevoEvento.valor} <br>
                              <strong> Hora:</strong> ${nuevoEvento.hora} <br>
                              <strong> Fecha Inicio:</strong> ${nuevoEvento.fechaInicio} <br>
                              <strong>Fecha Fin:</strong> ${nuevoEvento.fechaFin}<br>
                              <strong>Duracion:</strong> ${nuevoEvento.duracion}<br> 
                              <strong>Descripcion:</strong> ${nuevoEvento.descripcion}<br>
                               <strong>Cupo Virtual:</strong> ${nuevoEvento.cupoVirtual}<br> 
                               <strong>Cupo Presencial:</strong> ${nuevoEvento.cupoPresencial}
      `
    /*console.log(datosIngresados)
    console.log(nuevoEvento)*/

    const { value:confirmacionCrear } = await Swal.fire({
        title: ' Confirmar datos',
        html:
            `${datosIngresados}`
        ,
        currentProgressStep:7,
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Descartar cambios'})
        .then((resultado) => {
        if(resultado.isConfirmed){


            let timerInterval
            Swal.fire({
                title: 'Procesando',
                timer: 5000,
                timerProgressBar: true,
                didOpen: () => {
                    Swal.showLoading()
                    timerInterval = setInterval(() => {}, 100)
                },
                willClose: () => {
                    clearInterval(timerInterval)
                }
            }).then((result) => {


            })





//MANDAR EVENTO
            let bodyReq = JSON.stringify(nuevoEvento);
        //    console.log(bodyReq)

            let url = 'http://localhost:8080/api/eventos/crear'
            fetch(url, {
                method: 'post', headers: {
                    'Content-Type': 'application/json'
                }, body: bodyReq
            }).then(respuesta => respuesta.json()).then(d => {

                if(!(d.error)){
                    Toast.fire({
                        icon:'success', title: d.respuesta
                    })
                }else{
                    Toast.fire({
                        icon:'error', title: d.respuesta
                    })
                }
            }).catch()

        }
    })













   // console.log(nuevoEvento)








}



function cargandoToggler(tiempo) {
    $('.loader-anim').show();


    setTimeout(async function () {

        if(idEventoUnico){
//Ralentizar carga de evento?
            renderizarEvento()


        }else{
            $('.loader-anim').hide();
            if(usernameLogged){
                $('#miseventos').show();

                cargarListaEventosPrivada();
            }else{


            }
            cargarListaEventosPublica();
        }

    }, tiempo);

}



function dibujarCirculito(valor,clase) {


    $(`.circuloprogreso-${clase}`)
        .circleProgress({
            value:valor ,
            fill: {
                gradient: ['#13FCFC95', '#8919C695'] // or color: '#3aeabb', or image: 'http://i.imgur.com/pT0i89v.png'
            },
        });

}


function filtrarLista()
 {
    let input, filter, ul, li, a, i, txtValue;
    input = document.getElementById("buscador");
    filter = input.value.toUpperCase();
    //lista publica
    ul = document.getElementById("listaEvn");
    li = ul.getElementsByTagName("li");
    let contadorOcultos=0;
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
            contadorOcultos--;

        } else {
            li[i].style.display = "none";
           contadorOcultos++;

        }

     if(contadorOcultos==li.length){
        if(!(Swal.isVisible())){
            Toast.fire({
                icon:'info', title: "No hay resultados de tu búsqueda. :("
            })
        }
     }

    }


    //Lista de eventos suscriptos
    ul2 = document.getElementById("listaMisEventos");
    li2 = ul2.getElementsByTagName("li");
    for (i = 0; i < li2.length; i++) {
        a = li2[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li2[i].style.display = "";
            document.getElementById("listaMisEventos").innerHTML = ""
        } else {            
            li2[i].style.display = "none";

        }
    }
}


