verCategorias();
togglers();

function togglers() {
    $('#togglerSeccion').click(function () {
        $('#containerCategoria').fadeToggle(150);
    }
    )
}


const mostrarDatos = (data) => {
    //console.log(data)
    let body = ''
    for (let i = 0; i < data.length; i++) {
        body += 
        `<tr>
            <td>${data[i].idCategoria}</td>
            <td>${data[i].nombre}</td> 
            <td>${data[i].nombreSubCategoria}</td> 

            
            <td>
                <button class="btn btn-outline-success" onclick=mostrarEditarCategoria('${data[i].idCategoria}','${data[i].nombreCategoria}') >
                    <i class="bi bi-pencil-square"></i> 
                        Editar
                </button>
        
                <button class="btn btn-outline-danger" onclick="document.getElementById('idCategoriaConfirmar').innerHTML=${data[i].idCategoria}" 
                    data-bs-toggle="modal" data-bs-target="#modalEliminar">
                    <i class="bi bi-trash"></i> 
                    Eliminar
                </button>
            </td>
        </tr>`
    }

    document.getElementById('datos').innerHTML = body;
}


function verCategorias() {
    let url = 'http://localhost:8080/api/categorias/ver';
    fetch(url)
        .then(respuesta => respuesta.json())
        .then(datos => mostrarDatos(datos))
        .catch(error => console.log(error))

}

function eliminar(id) {
    let url = 'http://localhost:8080/api/categorias/eliminar'
    let bodyReq = { idCategoria: id };
    bodyReq = JSON.stringify(bodyReq);


    fetch(url, {
        method: 'delete', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();
        console.log(d);
        $('#mensaje-notif').html(d.respuesta);
        $('#notificacion').toast('show');
    }).catch()
}



function actualizar() {
    $('#cargando').show(500).delay(500);
    $('#cargando').hide(500).delay(600);
    verCategorias();
}


function editarCategoria() {
    let bodyReq = {
        idCategoria: document.getElementById('formEditCategoriaId').value,
        nombre: document.getElementById('formEditCategoriaNombre').value
    };
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/categorias/modificar'
    fetch(url, {
        method: 'post', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();
        console.log(d);
        $('#mensaje-notif').html(d.respuesta);
        $('#notificacion').toast('show');
    }).catch()


}


function guardarNuevaCategoria() {
    let nombre = document.getElementById('nombre-nueva-categoria').value;
    //{atributoClase : let local}
    let bodyReq = { nombre: nombre };
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/categorias/crear'
    fetch(url, {
        method: 'post', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();
        $('#mensaje-notif').html(d.respuesta);
        $('#notificacion').toast('show');
    }).catch()
}

function nuevaCategoria() {
    $('#editarCategoria').hide();
    $('#crearCategoria').fadeIn();
    document.getElementById('titulo-form').innerHTML = 'Crear Categoria';
}

function mostrarEditarCategoria(id, nombre) {
    $('#crearCategoria').hide();
    $('#editarCategoria').fadeIn();
    document.getElementById('titulo-form').innerHTML = 'Modificar Categoria';
    document.getElementById('formEditCategoriaId').value = id;
    document.getElementById('formEditCategoriaNombre').value = nombre;

}

