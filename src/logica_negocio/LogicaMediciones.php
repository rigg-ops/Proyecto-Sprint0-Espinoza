<?php
// -------------------------------------------------------------
// Clase: LogicaMediciones
// Descripción: Lógica del sistema biométrico (crea tabla, inserta, obtiene datos)
// -------------------------------------------------------------

class LogicaMediciones {
    private $conn;

    function __construct() {
        $host = "localhost:3306";
        $user = "jespcer";        
        $pass = "Jespcervera";     
        $db   = "jespcer_biometria";

        $this->conn = new mysqli($host, $user, $pass, $db);
        if ($this->conn->connect_error) {
            http_response_code(500);
            die(json_encode(["ok" => false, "error" => $this->conn->connect_error]));
        }

        $this->crearTabla();
    }

    private function crearTabla() {
        $sql = "CREATE TABLE IF NOT EXISTS mediciones (
            id INT AUTO_INCREMENT PRIMARY KEY,
            tipo VARCHAR(30),
            valor FLOAT,
            fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )";
        $this->conn->query($sql);
    }

    public function insertarMedicion($tipo, $valor) {
        $tiposPermitidos = ["CO2", "TEMP", "HUMEDAD"];
        if (!in_array(strtoupper($tipo), $tiposPermitidos) || !is_numeric($valor)) {
            return ["ok" => false, "error" => "Datos inválidos"];
        }

        $stmt = $this->conn->prepare("INSERT INTO mediciones (tipo, valor) VALUES (?, ?)");
        $stmt->bind_param("sd", $tipo, $valor);
        return ["ok" => $stmt->execute()];
    }

    public function obtenerMediciones() {
        $result = $this->conn->query("SELECT * FROM mediciones ORDER BY fecha DESC LIMIT 20");
        $data = [];
        while ($row = $result->fetch_assoc()) $data[] = $row;
        return ["ok" => true, "mediciones" => $data];
    }
}
?>
