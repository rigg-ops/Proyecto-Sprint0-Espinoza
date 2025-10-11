// ----------------------------------------------------------
// Proyecto Biometría - GTI 3A
// Archivo: logica.js
// Descripción: Obtiene las mediciones desde la API REST y
//              las muestra en la tabla HTML.
// ----------------------------------------------------------

document.addEventListener("DOMContentLoaded", async () => {
  const tabla = document.getElementById("tabla");
  const contenido = document.getElementById("contenido-tabla");
  const loading = document.getElementById("loading");

  try {
    // Petición a la API REST
    const respuesta = await fetch("api.php?endpoint=mediciones");
    const datos = await respuesta.json();

    // Si hay datos
    if (datos.ok && datos.mediciones.length > 0) {
      datos.mediciones.forEach(medicion => {
        const fila = `
          <tr>
            <td>${medicion.id}</td>
            <td>${medicion.tipo}</td>
            <td>${medicion.valor}</td>
            <td>${medicion.fecha}</td>
          </tr>`;
        contenido.innerHTML += fila;
      });

      // Mostrar tabla y ocultar texto de carga
      tabla.style.display = "table";
      loading.style.display = "none";
    } else {
      loading.textContent = "No hay mediciones registradas todavía.";
    }
  } catch (error) {
    console.error("Error al conectar con la API:", error);
    loading.textContent = "Error al cargar los datos. Ver consola.";
  }
});
