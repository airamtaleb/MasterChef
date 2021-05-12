<?php
include 'conexion.php';
$idjuez=$_POST['idjuez'];
$idevento=$_POST['idevento'];

$sentencia=$conexion->prepare("SELECT * FROM Solicitudes WHERE ID_juez=$idjuez AND ID_evento=$idevento");
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
?>