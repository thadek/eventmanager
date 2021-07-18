verEventos()

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



const mostrarDatos = (data) => {
    let body = ''
//console.log(data)
    let dias= ''
    for (let i = 0; i < data.length; i++) {

        for(let j=0; j<data[i].dias.length;j++){
            if(j === (data[i].dias.length-1)){
                dias+= `${data[i].dias[j]}. `
            }else{
                dias+= `${data[i].dias[j]}, `
            }
        }


        body +=
            `<tr>
            <td>${data[i].id}</td>
            <td>${data[i].nombre}</td>
             <td>${data[i].subcategoria.nombre}</td>
             <td>${data[i].subcategoria.categoria.nombre}</td>
            <td> ${data[i].facilitador.nombre} ${data[i].facilitador.apellido}</td>
             <td>${data[i].fechaInicio}</td>
            <td>${data[i].fechaFin}</td>
             <td>${data[i].hora}</td>
              <td>${data[i].duracion}</td>
             <td>${data[i].modalidad}</td>
               <td>${dias}</td>
            <td>${data[i].cupoPresencial}</td>
             <td>${data[i].cupoVirtual}</td>
            <td>${data[i].valor}</td>
            
           
            
            <td>
                <button class="btn btn-outline-success" onclick=editarEvento(${data[i].id}) >
                    <i class="bi bi-pencil-square"></i> 
                        Editar
                </button>
        
                <button class="btn btn-outline-danger" onclick=confirmarBorrar(${data[i].id}) >
                    <i class="bi bi-trash"></i> 
                    Eliminar
                </button>


            </td>
        </tr>`

        dias=''

    }

    document.getElementById('datos').innerHTML = body;
}



function confirmarBorrar(evento) {
    alertify.confirm('Confirmar Eliminación', 'No hay vuelta atrás', function(){eliminar(evento)}, function(){
        Toast.fire({
            icon:'error', title:"Eliminacion Cancelada"
        })
    });
}

var listaSubcategorias = {}
var listaModalidades = ['ONLINE','MIXTA','PRESENCIAL'];
var dias = ['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'];
var profesores = {};
let nuevoEvento = new Object();
async function verEventos() {
    let url = 'http://localhost:8080/api/eventos/vertodos';
    await fetch(url)
        .then(respuesta => respuesta.json())
        .then(datosEvento => {
                mostrarDatos(datosEvento)
            }
        )
        .catch(error => console.log(error))

}

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
            '<option value="VIERNES">Viernes</option>' +
            ' <option value="SABADO">Sabado</option>'+
            ' <option value="DOMINGO">Domingo</option>'+
            '</select>',
        didOpen(){
            $('#diasEvento').selectize({
                sortField: 'text'});
                document.getElementById("diasEvento").style.zIndex="1000";
        }
        ,currentProgressStep:5,
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
            </div>
`

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

    console.log(profesores[nuevoEvento.facilitador.email])

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
                        actualizar();
                    }else{
                        Toast.fire({
                            icon:'error', title: d.respuesta
                        })
                    }
                }).catch()

            }
        })






}

function renderizarListasRecursos(){



}



async function editarEvento(id){

    const evento = await fetch(`http://localhost:8080/api/eventos/ver/${id}`).then(res=>res.json());

    selectProfesores=''
    selectSubcategorias=''
    await fetch('http://localhost:8080/api/subcategorias/ver').then(respuesta=> respuesta.json()).then(data=>{
            for(let i=0; i<data.length;i++){

                if(evento.subcategoria.idSubcategoria===data[i].idSubcategoria){
                     selectSubcategorias+= `<option value=${data[i].idSubcategoria} selected>${data[i].nombre}</option>`
                }else{
                    selectSubcategorias+=`<option value=${data[i].idSubcategoria}>${data[i].nombre}</option>`
                }

            }
        }
    )

    await fetch('http://localhost:8080/api/perfil/profesores').then(respuesta=> respuesta.json()).then(data=>{
        for(let i=0; i<data.length;i++){

            if(evento.facilitador.email===data[i].email){
                selectProfesores+=`<option value=${data[i].email} selected>${data[i].nombre} ${data[i].apellido}</option>`
            }else{
                selectProfesores+=`<option value=${data[i].email}>${data[i].nombre} ${data[i].apellido}</option>`
            }

        }
    })


    const { value: formValues } = await Swal.fire({
        title: ' Edición del evento ',
        html:
            `<p>Nombre Evento</p>
            <input id="nombreEvento" class="form-control" type="text">
            <p>Dias del Evento</p>
            
            
      
             <select id="diasEvento" multiple="multiple" type="text">
            <option value="LUNES">Lunes</option>
               <option value="MARTES">Martes</option>
              <option value="MIERCOLES">Miercoles</option>
              <option value="JUEVES">Jueves</option>
            <option value="VIERNES">Viernes</option>
             <option value="SABADO">Sabado</option>
             <option value="DOMINGO">Domingo</option>
            </select>
            
            <select id="profesor">${selectProfesores}</select>

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
            </div>
`

        ,
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


}

function eliminar(eventId) {
    let url = 'http://localhost:8080/api/eventos/eliminar'
    let bodyReq = { id: eventId };
    bodyReq = JSON.stringify(bodyReq);

    fetch(url, {
        method: 'delete', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();

        Toast.fire({
            icon:'success', title:d.respuesta
        })
    }).catch()
}



function actualizar() {
    $('#cargando').show(500).delay(500);
    $('#cargando').hide(500).delay(600);
    verEventos();
}









