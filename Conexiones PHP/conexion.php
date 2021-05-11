<?php
$hostname='politecnico-estella.ddns.net:3306';
$database='MasterChef1';
$username='politecnico-masterchef-1';
$password='Masterchef_2021!';

$conexion=new mysqli($hostname,$username,$password,$database);
if($conexion->connect_errno){
    echo "El sitio web está experimentado problemas";
}
?>