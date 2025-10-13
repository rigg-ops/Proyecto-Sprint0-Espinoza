// -----------------------------------------------------------
// Test automático de la API (Node.js + Mocha + Request)
// -----------------------------------------------------------

import assert from "assert";
import request from "request";

// URL base de tu API
const BASE_URL = "https://jespcer.upv.edu.es/biometria/api.php";

describe("Pruebas API Proyecto Biometría", () => {

  // ---------------------------------------------------------
  // POST → Inserta una nueva medición
  // ---------------------------------------------------------
  it("POST /api.php inserta una medida válida", (done) => {
    const data = { tipo: "CO2", valor: 450.5 };

    request.post(
      {
        url: `${BASE_URL}`,
        qs: { endpoint: "mediciones" },
        json: data,
      },
      (err, res, body) => {
        assert.strictEqual(res.statusCode, 200, "Código de respuesta incorrecto");
        assert.ok(body.ok, "La respuesta no contiene 'ok: true'");
        done();
      }
    );
  });

  // ---------------------------------------------------------
  // GET → Devuelve las mediciones registradas
  // ---------------------------------------------------------
  it("GET /api.php?endpoint=mediciones devuelve lista de mediciones", (done) => {
    request.get(
      `${BASE_URL}?endpoint=mediciones`,
      (err, res, body) => {
        assert.strictEqual(res.statusCode, 200, "Código HTTP distinto de 200");
        const data = JSON.parse(body);
        assert.ok(data.ok, "No se recibió ok:true");
        assert.ok(Array.isArray(data.mediciones), "No devolvió un array");
        done();
      }
    );
  });

  // ---------------------------------------------------------
  // GET → Endpoint de salud
  // ---------------------------------------------------------
  it("GET /api.php?endpoint=health responde correctamente", (done) => {
    request.get(`${BASE_URL}?endpoint=health`, (err, res, body) => {
      assert.strictEqual(res.statusCode, 200);
      const data = JSON.parse(body);
      assert.strictEqual(data.ok, true);
      assert.strictEqual(data.service, "api-biometria");
      done();
    });
  });

});
