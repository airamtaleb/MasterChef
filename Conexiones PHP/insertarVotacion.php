<?php
include 'conexion.php';
$registros=$_POST['registros'];

$obj = json_decode($registros,true);

foreach($obj as $val){
	$sql=$conexion->prepare("insert into Valoraciones(ID_juez, ID_evento, Nombre_equipo, Presentacion, Servicio, Sabor, Imagen, Triptico) 
		select ".$val[ID_juez].", ".$val[ID_evento].", '".$val[Nombre_equipo]."', ".$val[Presentacion].", ".$val[Servicio].", ".$val[Sabor].", ".$val[Imagen].", ".$val[Triptico]."
		where NOT exists(SELECT * FROM Valoraciones WHERE ID_juez=".$val[ID_juez]." AND ID_evento =".$val[ID_evento]." AND Nombre_equipo='".$val[Nombre_equipo]."')");

	$sql->execute();

}


$sql->close();
$conexion->close();
?>


