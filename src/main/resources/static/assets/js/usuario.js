verUsuarios();
togglers();
var selectorDeRol;

$(document).ready(function () {
    $('#formEditRol').selectize({
        sortField: 'text'

    });

    $('#rol').selectize({
        sortField: 'text'

    });

});

function togglers() {
    $('#togglerSeccion').click(function () {
        $('#containerUsuario').fadeToggle(150);
    }
    )
}


const mostrarDatos = (data) => {
    let body = ''
//console.log(data)
    for (let i = 0; i < data.length; i++) {

        body +=
            `<tr>
            <td>${data[i].username}</td>
            <td>${data[i].rol.nombreRol}</td>
            <td>
                <a class="btn btn-outline-warning" href="/adm/perfil/ver/${data[i].username}" >
                     <i class="bi bi-person-square"></i>
                        Perfil
                </a>

            </td>
            
            <td>
                <button class="btn btn-outline-success" onclick=mostrarEditarUsuario("${data[i].username}") >
                    <i class="bi bi-pencil-square"></i> 
                        Editar
                </button>
        
                <button class="btn btn-outline-danger" onclick=confirmarBorrar("${data[i].username}") >
                    <i class="bi bi-trash"></i> 
                    Eliminar
                </button>


            </td>
        </tr>`

}
    document.getElementById('datos').innerHTML = body;
}


function selectorDeRoles(valor) {
    selectorDeRol=valor;
    
}

function confirmarBorrar(usuario) {
    alertify.confirm('Confirmar Eliminación', 'No hay vuelta atrás', function(){eliminar(usuario)}, function(){alertify.error('Cancelado')});
}


async function verUsuarios() {
    let url = 'http://localhost:8080/api/usuario/ver';
    await fetch(url)
        .then(respuesta => respuesta.json())
        .then(datosUsuario => {
           mostrarDatos(datosUsuario)
        }
            )
        .catch(error => console.log(error))

}

function eliminar(id) {
    let url = 'http://localhost:8080/api/usuario/eliminar'
    let bodyReq = { username: id };
    bodyReq = JSON.stringify(bodyReq);

    fetch(url, {
        method: 'delete', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();
//        console.log(d);
//        $('#mensaje-notif').html(d.respuesta);
//        $('#notificacion').toast('show');
        alertify.success(d.respuesta);
    }).catch()
}



function actualizar() {
    $('#cargando').show(500).delay(500);
    $('#cargando').hide(500).delay(600);
    verUsuarios();
}


function editarUsuario() {

    let bodyReq = {
        username: document.getElementById('formEditUsernameId').value,
        password: document.getElementById('formEditPassword').value,

        rol:{
            idRol: selectorDeRol
        }
    };


    console.log(bodyReq)
    bodyReq = JSON.stringify(bodyReq); 
 

    let url = 'http://localhost:8080/api/usuario/modificar'
    fetch(url, {
        method: 'post', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();
        console.log(d);
        alertify.success(d.respuesta);
    }).catch()

}


function guardarNuevoUsuario() {


    let bodyReq = {

        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        rol:{
            idRol: selectorDeRol
        }
    };
    //{atributoClase : let local}
    bodyReq = JSON.stringify(bodyReq);
    let url = 'http://localhost:8080/api/usuario/crear'
    fetch(url, {
        method: 'post', headers: {
            'Content-Type': 'application/json'
        }, body: bodyReq
    }).then(respuesta => respuesta.json()).then(d => {
        actualizar();
        alertify.success(d.respuesta);
    }).catch()
}

function nuevoUsuario() {
    $('#editarUsuario').hide();
    $('#crearUsuario').fadeIn();
    document.getElementById('titulo-form').innerHTML = 'Crear Usuario';
}

function mostrarEditarUsuario(username) {
    $('#crearUsuario').hide();
    $('#editarUsuario').fadeIn();
    selectorDeRol=rol;
    document.getElementById('titulo-form').innerHTML = 'Modificar Usuario';
    document.getElementById('formEditUsernameId').value = username;
    document.getElementById('formEditPassword').value = password;


}

