cargandoToggler(900);


function renderizarListaPublica(arrayEventos) {
    let body = '';
    let ejemploPorcentajes = [0.44, 0.55, 0.66, 0.70, 0.80];

    let dias = '';

    if (arrayEventos) {

        for (let i = 0; i < arrayEventos.length; i++) {


            for(let j=0; j<arrayEventos[i].dias.length;j++){
                if(j === (arrayEventos[i].dias.length-1)){
                    dias+= `${arrayEventos[i].dias[j]}. `
                }else{
                    dias+= `${arrayEventos[i].dias[j]}, `
                }
            }


            body += `<li>
     <a href="http://localhost:8080/eventos/ver">
     <div class="row descripcionEvento">
       <div class="col-lg-2 col-md-4 circuloprogreso circuloprogreso-${i}"><strong>${Math.round(ejemploPorcentajes[i] * 100)}%</strong>
         <p>Ocupacion</p>
       </div>
       <div class="col-lg-10 col-md-8">
         <h3 class="eventName">${arrayEventos[i].nombre}</h3>
         <p class="eventModalidad">${arrayEventos[i].modalidad}</p>
         <p class="eventProfessor">${arrayEventos[i].facilitador.nombre}</p>
         <h6 class="eventOverview">${dias} - ${arrayEventos[i].hora}</h6>
       </div>

     </div>
       </a>
   </li> `

        }

        document.getElementById("listaEvn").innerHTML = body;
        for (let i = 0; i < 5; i++) {
            dibujarCirculito(ejemploPorcentajes[i], i)
        }

    } else {
        body = `<li>  <h3>No hay eventos cargados en sistema.</h3></li>`
        document.getElementById("listaEvn").innerHTML = body;
    }


}


function cargarListaEventosPublica(){
    fetch('http://localhost:8080/api/eventos/vertodos').then(respuesta => respuesta.json()).then(datosEv=>{renderizarListaPublica(datosEv)})

}

function renderizarListaPrivada(arrayEventos) {
    let body = '';
    let ejemploPorcentajes = [0.2, 0.66, 0.80];


    if (arrayEventos) {
        for (let i = 0; i < ejemploPorcentajes.length; i++) {
            body += `<li>
         <a href="/">
         <div class="row descripcionEvento">
           <div class="col-lg-2 col-md-4 circuloprogreso circuloprogreso-${i}"><strong>${Math.round(ejemploPorcentajes[i] * 100)}%</strong>
             <p>Ocupacion</p>
           </div>
           <div class="col-lg-10 col-md-8">
             <h3 class="eventName">Hatha Yoga</h3>
             <p class="eventModalidad">Online</p>
             <p class="eventProfessor">Profesor: Romina</p>
             <h6 class="eventOverview">Lunes y Jueves 20hs - Zoom</h6>
           </div>
    
         </div>
           </a>
       </li> `

        }

        document.getElementById("listaMisEventos").innerHTML = body;
        for (let i = 0; i < ejemploPorcentajes.length; i++) {
            dibujarCirculito(ejemploPorcentajes[i], i)
        }

    } else {
        body = `<li>  <h3>No hay eventos cargados en tu usuario.</h3></li>`
        document.getElementById("listaMisEventos").innerHTML = body;
    }

}


function renderizarEvento(){
    $('.bodyEvent').show();
    $('.contenidoEvento').show();
}


var listaSubcategorias = {}
var listaModalidades = ['ONLINE','MIXTA','PRESENCIAL'];
var dias = ['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'];

let nuevoEvento = new Object();


async function cargarRecursos(){
    //Cargo las subcategorias
    await fetch('http://localhost:8080/api/subcategorias/ver').then(respuesta=> respuesta.json()).then(data=>{
        for(let i=0; i<data.length;i++){
            listaSubcategorias[data[i].idSubcategoria]=data[i].nombre;
        }
        }
    )
}


async function crearEvento(){
    await cargarRecursos();



    // 1 - Mostrar y elegir subcategoria
    const { value: subcat } = await Swal.fire({
        title: ' 1 - Seleccioná una subcategoría de evento',
        input: 'select',
        inputOptions:listaSubcategorias,
        inputPlaceholder: 'Selecciona una Subcategoría',
        showCancelButton: true,
        progressSteps:["1"],
    })

    if (subcat) {
        nuevoEvento.subcategoria={idSubcategoria:subcat};
    }





   // 2 - Elegir modalidad
    const { value: modalidad } = await Swal.fire({
        title: '2 - Selecciona la modalidad',
        input: 'select',
        inputOptions:listaModalidades,
        inputPlaceholder: 'Selecciona modalidad',
        showCancelButton: true,
        progressSteps:["2"],
    })

    if(modalidad){ nuevoEvento.modalidad = listaModalidades[modalidad]

    }

    console.log(nuevoEvento)






   // 3 -  Demas
    const { value: formValues } = await Swal.fire({
        title: ' 3 - Detalles Evento',
        html:

            '<input id="nombreNuevoEvento" class="swal2-input" type="text">' +
            '<p>Nombre del Evento</p>'+
            '<input id="fechaInicio" class="swal2-input" type="date">' +
            '<p>Fecha Inicio</p>' +
            ' <select id="diasEvento" multiple="multiple" type="text">' +
            ' <option value="LUNES">Lunes</option>' +
            '   <option value="MARTES">Martes</option>' +
            '  <option value="MIERCOLES">Miercoles</option>' +
            '  <option value="JUEVES">Jueves</option>' +
            '<option value="VIERNES">Viernes</option>' +
            ' <option value="SABADO">Sabado</option>'+
            ' <option value="DOMINGO">Domingo</option>'+
            '</select>'+
            '<p>Dias Evento</p>' +

            '<input id="fechaFin" class="swal2-input"type="date">' +
            '<p>Fecha Fin</p>' +
            '<input id="hora" class="swal2-input" type="time">' +
            '<p>Hora</p>' +
            '<input id="duracionEvento" class="swal2-input" type="number">' +
            '<p>Duracion</p>'+
            '<input id="cupoPresencial" class="swal2-input" type="number">' +
            '<p>Cupo Presencial</p>'+
            '<input id="cupoOnline" class="swal2-input" type="number">' +
            '<p>Cupo Online</p>'+
            '<input id="valorNuevoEvento" class="swal2-input" type="number">' +
            '<p>Valor</p>'+
            '<input id="descripcionNuevoEvento" class="swal2-input" type="text">'+
            '<p>Descripción</p>'

        , didOpen(){
                $('#diasEvento').selectize({
                    sortField: 'text'})
            }

        ,
        focusConfirm: false,
        preConfirm: () => {
            return [
                document.getElementById('nombreNuevoEvento').value,
                document.getElementById('fechaInicio').value,
                document.getElementById('fechaFin').value,
                document.getElementById('hora').value,
                document.getElementById('duracionEvento').value,
                document.getElementById('cupoPresencial').value,
                document.getElementById('cupoOnline').value,
                document.getElementById('valorNuevoEvento').value,
                document.getElementById('descripcionNuevoEvento').value,
                [...document.getElementById('diasEvento').options].filter(option => option.selected).map(option => option.value)



            ]
        }
    })


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






    if (formValues) {

        nuevoEvento.nombre=formValues[0];
        nuevoEvento.fechaInicio=formValues[1];
        nuevoEvento.fechaFin=formValues[2];
        nuevoEvento.hora = formValues[3];
        nuevoEvento.duracion = formValues[4];
        nuevoEvento.cupoPresencial =formValues[5];
        nuevoEvento.cupoVirtual = formValues[6];
        nuevoEvento.valor = formValues[7];
        nuevoEvento.descripcion= formValues[8];
        nuevoEvento.dias = formValues[9];
        nuevoEvento.facilitador = { idPerfil : 3, email:'alejandro@enindigo.com.ar'}


        let bodyReq = JSON.stringify(nuevoEvento);
        console.log(bodyReq)

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

    console.log(nuevoEvento)








}






function cargandoToggler(tiempo) {
    $('.loader-anim').show();

    setTimeout(function () {
        $('.loader-anim').hide();
        renderizarEvento();
        cargarListaEventosPublica();
        renderizarListaPrivada("");


    }, tiempo);

}



function dibujarCirculito(valor, clase) {

    $(`.circuloprogreso-${clase}`)
        .circleProgress({
            value: valor,
            fill: {
                gradient: ['#3aeabb', '#fdd250'] // or color: '#3aeabb', or image: 'http://i.imgur.com/pT0i89v.png'
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
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";           
        } else {            
            li[i].style.display = "none";
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
        } else {            
            li2[i].style.display = "none";
        }
    }
}

