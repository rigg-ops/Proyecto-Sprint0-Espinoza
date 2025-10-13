 **Sistema de Monitorización Ambiental – Sprint 0 (2025)**  
 *Universitat Politècnica de València – GTI 3A*  
 *Desarrollado por:* **Judit Espinoza Cervera**

---

###  Enlace del Proyecto (Plesk)
[https://jespcer.upv.edu.es](https://jespcer.upv.edu.es)

Este proyecto integra diferentes tecnologías para crear un sistema de medición ambiental inteligente.  
Permite capturar, enviar y visualizar datos de **CO₂** y **temperatura** usando sensores, Bluetooth Low Energy (BLE) y una API web.  

Desarrollado dentro de la asignatura **Proyecto de Aplicaciones de Biometría y Medio Ambiente** del Grado en Tecnologías Interactivas.

---

##  Estructura General del Sistema

- ** Módulo Arduino (C++)** → Emite datos simulados mediante anuncios **iBeacon BLE**, con un UUID único (`EPSG-GTI-PROY-3A`).  
  Envía periódicamente las medidas de temperatura y CO₂.

- ** Aplicación Android (Java)** → Escanea dispositivos BLE cercanos, filtra por nombre **“BioBeacon”**,  
  interpreta las tramas recibidas e informa al servidor mediante peticiones **HTTP POST** en formato JSON.

- ** Servidor Web (PHP + MySQL)** → Recibe y almacena las mediciones a través de una **API RESTful**.  
  Ofrece endpoints como:
  - `GET  api.php?endpoint=mediciones` → Devuelve las mediciones más recientes.  
  - `POST api.php` → Inserta una nueva medida (`{ tipo, valor }`).  
  - `GET  api.php?endpoint=health` → Indica el estado del servicio.

- ** Interfaz Web (HTML + JS)** → Muestra los datos guardados en una tabla dinámica actualizada en tiempo real  
  gracias al uso de la **Fetch API**.

- ** Validación (Node.js + Mocha + Request)** →  
  Ejecuta pruebas automáticas que comprueban la conexión, la inserción de datos y la respuesta de la API REST.

---

##  Herramientas y Tecnologías

| Componente | Tecnología empleada |
|-------------|--------------------|
| Emisión BLE | Arduino IDE – C++ |
| Recepción BLE | Android Studio – Java |
| Backend REST | PHP + MySQL (Plesk UPV) |
| Interfaz Web | HTML5 + CSS + JavaScript |
| Testing | Node.js con Mocha y Request |
| Control de versiones | Git y GitHub |

---

##  Despliegue del Sistema

El proyecto está alojado y funcionando en el entorno **Plesk (UPV)**:

-  **API REST:** `https://jespcer.upv.edu.es/biometria/api.php`  
-  **Interfaz Web:** `https://jespcer.upv.edu.es/biometria/index.html`  
-  **Base de datos:** MySQL (creada automáticamente por PHP)

---

##  Instrucciones de Uso – Aplicación Android

1. Abrir el proyecto en **Android Studio**.  
2. Activar **Bluetooth** y conceder permisos de **ubicación**.  
3. Pulsar **“Buscar dispositivo”** en la interfaz.  
4. La aplicación localizará el beacon **“BioBeacon”** (UUID `EPSG-GTI-PROY-3A`).  
5. Los datos detectados se enviarán automáticamente al servidor remoto.  

---

##  Ejecución de Pruebas Automáticas

El sistema incluye una carpeta `/test/` con las pruebas unitarias de la API REST.  
Estas verifican que el servidor responde correctamente al registrar y consultar mediciones.

###  Pasos para ejecutarlas:
```bash
npm install        # Instala dependencias necesarias
npm test           # Ejecuta la suite de pruebas
```
### Ejemplo de resultado:
```bash
 Pruebas API - Sistema de Monitorización Ambiental
  ✓ Inserta una nueva medida correctamente (POST)
  ✓ Devuelve la lista de mediciones (GET)
  ✓ Confirma estado del servidor (health)

3 passing (1s)
