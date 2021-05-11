<?php
include 'conexion.php';
$nombre=$_POST['nombre'];
$apellidos=$_POST['apellidos'];
$departamento=$_POST['departamento'];
$intolerancia=$_POST['intolerancia'];
$correo=$_POST['correo'];
$password=$_POST['password'];

$sql=$conexion->prepare("insert into Jueces (nombre, apellidos, departamento, intolerancia, correo, clave) values (?, ?, ?, ?, ?, ?)");
$sql->bind_param('ssssss',$nombre, $apellidos, $departamento, $intolerancia, $correo, $password);
$sql->execute();

$sql->close();
$conexion->close();
?>