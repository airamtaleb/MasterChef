 <?php
include 'conexion.php';
$idevento=$_POST['id_evento'];

$sentencia=$conexion->prepare("SELECT * FROM Equipos WHERE ID_evento=$idevento");
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