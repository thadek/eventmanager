verSubcategorias();
togglers();
var selectordeCat;

$(document).ready(function () {
    $('#formEditCategoria').selectize({
        sortField: 'text'

    });

    $('#categorias').selectize({
        sortField: 'text'

    });

});

function togglers() {
    $('#togglerSeccion').click(function () {
        $('#containerSubcategoria').fadeToggle(150);
    }
    )
}


const mostrarDatos = (data) => {
    let body = ''
//console.log(data)
    for (let i = 0; i < data.length; i++) {

        body +=
            `<tr>
            <td>${data[i].idSubcategoria}</td>
            <td>${data[i].nombre}</td>
            <td>${data[i].categoria.nombre}</td>
            <td>${data[i].descripcion}</td>


            <td>
                <button class="btn btn-outline-success" onclick=mostrarEditarSubcategoria(${data[i].idSubcategoria},"${encodeURIComponent(data[i].nombre)}","${encodeURIComponent(data[i].descripcion)}",${data[i].categoria.idCategoria}) >

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
    document.getElementById('datos').innerHTML = body;
}


function selectorDeCategoria(valor) {
    selectordeCat=valor;

}



async function verSubcategorias() {
    let url = 'http://localhost:8080/api/subcategorias/ver';
    await fetch(url)
        .then(respuesta => respuesta.json())
        .then(datosSubcategoria => {
          mostrarDatos(datosSubcategoria)
//             console.log(datosSubcategoria)
        }
            )
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
        nombre: document.getElementById('formEditSubcategoriaNombre').value,
        descripcion: document.getElementById('formEditSubcategoriaDescripcion').value,
        categoria:{
            idCategoria: selectordeCat
        }
    };


    console.log(bodyReq)
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


    let bodyReq = {

        nombre: document.getElementById('nombre-nueva-subcategoria').value,
        descripcion: document.getElementById('descripcion-subcategoria').value,
        categoria:{
            idCategoria: selectordeCat
        }
    };

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

function mostrarEditarSubcategoria(id, nombre, descripcion,categoria) {
    $('#crearSubcategoria').hide();
    $('#editarSubcategoria').fadeIn();
    selectordeCat=categoria;
    document.getElementById('titulo-form').innerHTML = 'Modificar Subcategoria';
    document.getElementById('formEditSubcategoriaId').value = id;
    document.getElementById('formEditSubcategoriaNombre').value = decodeURIComponent(nombre);
    document.getElementById('formEditSubcategoriaDescripcion').value = decodeURIComponent(descripcion);

}
