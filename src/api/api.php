<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

require_once "LogicaMediciones.php";

$logica = new LogicaMediciones();
$endpoint = $_GET['endpoint'] ?? '';

if ($_SERVER["REQUEST_METHOD"] === "GET") {
    // Obtener últimas mediciones
    if ($endpoint === "mediciones") {
        $datos = $logica->obtenerMediciones();
        echo json_encode(["ok" => true, "mediciones" => $datos]);
    }
    // Endpoint de prueba
    elseif ($endpoint === "health") {
        echo json_encode(["ok" => true, "service" => "api-biometria", "ts" => time()]);
    }
    // Endpoint desconocido
    else {
        http_response_code(404);
        echo json_encode(["ok" => false, "error" => "Endpoint no encontrado"]);
    }
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Recibir JSON desde Android
    $input = json_decode(file_get_contents("php://input"), true);

    if (!$input || !isset($input["tipo"]) || !isset($input["valor"])) {
        http_response_code(400);
        echo json_encode(["ok" => false, "error" => "Datos incompletos"]);
        exit;
    }

    $ok = $logica->insertarMedicion($input["tipo"], $input["valor"]);
    if ($ok) {
        echo json_encode(["ok" => true]);
    } else {
        http_response_code(500);
        echo json_encode(["ok" => false, "error" => "Error insertando medición"]);
    }
}
?>
