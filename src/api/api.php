<?php
header("Content-Type: application/json");
require_once "../logica_negocio/LogicaMediciones.php";

$logica = new LogicaMediciones();
$endpoint = $_GET['endpoint'] ?? '';

if ($_SERVER["REQUEST_METHOD"] === "GET") {
    if ($endpoint === "mediciones") {
        echo json_encode([
            "ok" => true,
            "mediciones" => $logica->obtenerMediciones()
        ]);
    } elseif ($endpoint === "health") {
        echo json_encode(["ok" => true, "service" => "api-biometria", "ts" => time()]);
    } else {
        http_response_code(404);
        echo json_encode(["ok" => false, "error" => "Endpoint no encontrado"]);
    }
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $input = json_decode(file_get_contents("php://input"), true);
    if ($logica->insertarMedicion($input["tipo"], $input["valor"])) {
        echo json_encode(["ok" => true]);
    } else {
        http_response_code(500);
        echo json_encode(["ok" => false, "error" => "Error insertando medición"]);
    }
}
?>
