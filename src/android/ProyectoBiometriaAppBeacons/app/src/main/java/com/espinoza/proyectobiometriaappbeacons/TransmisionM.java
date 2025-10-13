package com.espinoza.proyectobiometriaappbeacons;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// --------------------------------------------------------------
 // Clase encargada de enviar las mediciones reales a la API REST del servidor.
 // Uso: llamar TransmitirMedidas.enviarMedida(medida) desde un hilo (no en UI thread).
// --------------------------------------------------------------
public class TransmisionM {

    // Cambia esta URL por la de tu servidor real (use https si es posible).
    // Para pruebas en emulador con servidor local usa: "http://10.0.2.2:8000/medida"
    private static final String API_URL = "https://jespcer.upv.edu.es/biometria/api.php/medida";

    private static final String TAG = "TransmitirMedidas";

    // --------------------------------------------------------------
    // Envía una medida al servidor mediante HTTP POST con cuerpo JSON.
    // Debe llamarse fuera del hilo UI (por ejemplo desde new Thread(...) o un Executor).
    // @param medida objeto Medida con tipo y valor
    // @return true si la petición obtiene un código HTTP 2xx, false en caso contrario
    // --------------------------------------------------------------
    public static boolean enviarMedida(Medida medida) {
        HttpURLConnection conn = null;
        try {
            // Construir URL y abrir conexión
            URL url = new URL(API_URL);
            conn = (HttpURLConnection) url.openConnection();

            // Configurar metodo y cabeceras
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10_000); // 10s
            conn.setReadTimeout(10_000);    // 10s
            conn.setDoOutput(true);         // porque vamos a escribir el body

            // Construir JSON a enviar (keys: "tipo" y "valor" para concordar con Medida.getValor())
            JSONObject json = new JSONObject();
            json.put("tipo", medida.getTipo());
            json.put("valor", medida.getValor());
            String jsonString = json.toString();

            // Enviar JSON
            byte[] out = jsonString.getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(out);
                os.flush();
            }

            // Leer código de respuesta y cuerpo (si lo hay)
            int code = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (code >= 400) ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder resp = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) resp.append(line);
            br.close();

            Log.d(TAG, "HTTP " + code + " ← " + resp.toString());

            // Consideramos éxito cualquier 2xx
            return (code >= 200 && code < 300);

        } catch (Exception e) {
            Log.e(TAG, "Error enviando medida real", e);
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
