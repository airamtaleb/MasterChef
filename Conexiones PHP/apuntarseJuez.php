<?php
include 'conexion.php';
$idjuez=$_POST['idjuez'];
$idevento=$_POST['idevento'];

$sql=$conexion->prepare("insert into Solicitudes(ID_juez, ID_evento, Solicitud) select $idjuez,$idevento,'Denegado' where NOT exists(SELECT * FROM Solicitudes WHERE ID_juez=$idjuez AND ID_evento =$idevento)");

$sql->execute();

$sql->close();
$conexion->close();
?>