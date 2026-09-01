elemento.scrollIntoView({
    behavior: "smooth"
});
}
}

/* MOSTRAR FECHA ACTUAL */
/**
 * Muestra la fecha actual en un elemento HTML.
 *
 * @param {string} id identificador del elemento.
 */
function mostrarFechaActual(id) {
    const elemento = document.getElementById(id);
    if (!elemento) {
        return;
    }
    const fecha = new Date();
    elemento.textContent = fecha.toLocaleDateString("es-CO");
}

/* INICIALIZACIÓN */
document.addEventListener("DOMContentLoaded", function() {
        console.log("J&M Makeup - Aplicación iniciada correctamente.");
    }
);