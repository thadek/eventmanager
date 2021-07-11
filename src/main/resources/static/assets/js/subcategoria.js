verSubcategorias();
togglers();

function togglers() {
    $('#togglerSeccion').click(function () {
        $('#containerSubcategoria').fadeToggle(150);
    }
    )
}


const mostrarDatos = (data) => {
    let body = ''


    for (let i = 0; i < data.length; i++) {
        //Muestro la lista de categorias ordenadas en un solo String.
        let listaCategorias = '';
        for (let j = 0; j < data[i].categorias.length; j++) {
            if (j === (data[i].categorias.length - 1)) {
                listaCategorias += `${data[i].categorias[j].nombre}. `
            } else {
                listaCategorias += `${data[i].categorias[j].nombre}, `
            }

        }

        

        body +=
            `<tr>
            <td>${data[i].idSubcategoria}</td>
            <td>${data[i].nombre}</td>
            <td>${listaCategorias}</td>
            <td>${data[i].descripcion}</td>

            
            <td>
                <button class="btn btn-outline-success" onclick=mostrarEditarSubcategoria(${data[i].idSubcategoria},"${encodeURIComponent(data[i].nombre)}") >
                    <i class="bi bi-pencil-square"></i> 
                        Editar
                </button>
        
                <button class="btn btn-outline-danger" onclick="document.getElementById('idSubcategoriaConfirmar').innerHTML=${data[i].idSubcategoria}"
                    data-bs-toggle="modal" data-bs-target="#modalEliminar">
                    <i class="bi bi-trash"></i> 
                    Eliminar
                </button>
            </td>
        </tr>`

}

    console.log(body)

    document.getElementById('datos').innerHTML = body;
}


function verSubcategorias() {
    let url = 'http://localhost:8080/api/subcategorias/ver';
    fetch(url)
        .then(respuesta => respuesta.json())
        .then(datos => mostrarDatos(datos))
        .catch(error => console.log(error))

}

function eliminar(id) {
    let url = 'http://localhost:8080/api/subcategorias/eliminar'
    let bodyReq = { idSubcategoria: id };
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
    verSubcategorias();
}


function editarSubcategoria() {
    let bodyReq = {
        idSubcategoria: document.getElementById('formEditSubcategoriaId').value,
        nombre: document.getElementById('formEditSubcategoriaNombre').value
    };
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/subcategorias/modificar'
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


function guardarNuevaSubcategoria() {
    let nombre = document.getElementById('nombre-nueva-subcategoria').value;
    //{atributoClase : let local}
    let bodyReq = { nombre: nombre };
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/subcategorias/crear'
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

function nuevaSubcategoria() {
    $('#editarSubcategoria').hide();
    $('#crearSubcategoria').fadeIn();
    document.getElementById('titulo-form').innerHTML = 'Crear Subcategoria';
}

function mostrarEditarSubcategoria(id, nombre) {
    $('#crearSubcategoria').hide();
    $('#editarSubcategoria').fadeIn();
    document.getElementById('titulo-form').innerHTML = 'Modificar Subcategoria';
    document.getElementById('formEditSubcategoriaId').value = id;
    document.getElementById('formEditSubcategoriaNombre').value = decodeURIComponent(nombre);

}

