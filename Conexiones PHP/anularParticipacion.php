<?php
include 'conexion.php';
$idjuez=$_POST['idjuez'];
$idevento=$_POST['idevento'];

$sql=$conexion->prepare("delete from solicitudes WHERE ID_juez=$idjuez AND ID_evento =$idevento");

$sql->execute();

$sql->close();
$conexion->close();
?>