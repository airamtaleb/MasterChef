<?php
include 'conexion.php';
$idevento=$_GET['idevento'];
$idjuez=$_GET['idjuez'];
$equipo=$_GET['equipo'];

$sentencia=$conexion->prepare("SELECT * FROM Valoraciones WHERE ID_evento=$idevento AND ID_juez=$idjuez AND Nombre_equipo=$equipo");
$sentencia->execute();

$resultado = $sentencia->get_result();

//$fila = $resultado->fetch_all(MYSQLI_ASSOC); //Devuelve array de elementos
//echo json_encode($fila); 


$data = [];
while ($fila = $resultado->fetch_assoc()) {
    $data[] = $fila;
}   
echo json_encode($data,JSON_UNESCAPED_UNICODE); 


$sentencia->close();
$conexion->close();

?>