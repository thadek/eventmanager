cargandoToggler(900);


function renderizarListaPublica(arrayEventos) {
    let body = '';
    let ejemploPorcentajes = [0.44, 0.55, 0.66, 0.70, 0.80];
    let ejemploModalidad = ["Online", "Presencial", "Online", "Presencial", "Online"]

    if (arrayEventos) {

        for (let i = 0; i < 5; i++) {
            body += `<li>
     <a href="/">
     <div class="row descripcionEvento">
       <div class="col-lg-2 col-md-4 circuloprogreso circuloprogreso-${i}"><strong>${Math.round(ejemploPorcentajes[i] * 100)}%</strong>
         <p>Ocupacion</p>
       </div>
       <div class="col-lg-10 col-md-8">
         <h3 class="eventName">Hatha Yoga</h3>
         <p class="eventModalidad">${ejemploModalidad[i]}</p>
         <p class="eventProfessor">Profesor: Romina</p>
         <h6 class="eventOverview">Lunes y Jueves 20hs - Zoom</h6>
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

function cargandoToggler(tiempo) {
    $('.loader-anim').show();

    setTimeout(function () {
        $('.loader-anim').hide();
        renderizarListaPublica("1");
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

