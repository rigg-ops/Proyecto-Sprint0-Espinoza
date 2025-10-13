 **Proyecto Biometría Sprint 0 – 2025**  
 *Universitat Politècnica de València – Grado en Tecnologías Interactivas (GTI 3A)*  
 *Autora:* **Judit Espinoza Cervera**

---

**Web del proyecto (Plesk):**  
[https://jespcer.upv.edu.es](https://jespcer.upv.edu.es)

Sistema completo de monitorización de datos ambientales (**CO₂** y **temperatura**) desarrollado para la asignatura  
**Proyecto Aplicaciones de Biometría y Medio Ambiente**.  
El sistema integra hardware, aplicación Android, servidor web y pruebas automáticas.

---

##  Contenido del Proyecto

-  **Arduino (C++)** → Emite tramas **BLE iBeacon** con el identificador `EPSG-GTI-PROY-3A`.  
  Envía valores simulados de CO₂ y temperatura mediante anuncios periódicos.  

-  **Android (Java)** → Escanea dispositivos BLE, filtra por nombre **"BioBeacon"**,  
  interpreta las tramas iBeacon y envía las mediciones al servidor mediante **HTTP POST (JSON)**.

-  **Servidor (PHP + MySQL)** → Implementa una **API REST** con los endpoints:
  - `GET  api.php?endpoint=mediciones` → Devuelve las últimas mediciones.  
  - `POST api.php` → Inserta una nueva medición (`{ tipo, valor }`).  
  - `GET  api.php?endpoint=health` → Estado del servicio.

-  **Web (HTML + JS)** → Muestra las mediciones almacenadas en una tabla dinámica  
  obtenida de la API REST usando **Fetch API**.  

-  **Tests automáticos (Node.js + Mocha + Request)** →  
  Validan la conexión con la API (inserción, consulta y estado del servidor).

---

##  Tecnologías y Herramientas

| Componente | Tecnología |
|-------------|-------------|
| Emisora BLE | Arduino IDE – C++ |
| Receptor BLE | Android Studio – Java |
| Servidor REST | PHP + MySQL (Plesk UPV) |
| Frontend | HTML5 + CSS + JavaScript |
| Tests | Node.js, Mocha, Request |
| Control de versiones | Git + GitHub |

---

##  Despliegue

El sistema está desplegado en el servidor Plesk de la UPV:

- **API REST:** `https://jespcer.upv.edu.es/biometria/api.php`  
- **Web:** `https://jespcer.upv.edu.es/biometria/index.html`  
- **Base de datos:** MySQL (creada y gestionada automáticamente por PHP)

---

##  Uso de la Aplicación Android

1. Abre el proyecto en **Android Studio**.  
2. Permite el uso de **Bluetooth** y **ubicación**.  
3. Pulsa el botón **“Buscar nuestro dispositivo”**.  
4. El sistema detectará el beacon **"BioBeacon"** con UUID `EPSG-GTI-PROY-3A`.  
5. Los datos capturados se enviarán automáticamente a la API REST.  

---

##  Ejecución de Tests

Los tests automáticos se encuentran en la carpeta `/test/` y comprueban el correcto funcionamiento de la API REST.

### 📍 Pasos:
```bash
npm install        # Instalar dependencias
npm test           # Ejecutar la suite de pruebas
```
 Resultado esperado:
 Pruebas API Proyecto Biometría
  ✓ POST /api.php inserta una medida válida
  ✓ GET /api.php?endpoint=mediciones devuelve lista de mediciones
  ✓ GET /api.php?endpoint=health responde correctamente

3 passing (1s)
