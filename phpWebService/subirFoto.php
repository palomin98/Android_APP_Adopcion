<?php
	$server = "";
	$user = "";
	$pass = "";
	$bd = "adoptales";

	if($_SERVER['REQUEST_METHOD']=='POST'){
		$imagen = $_POST['foto'];
		$id = $_POST['id'];
		
		//Creamos la conexión
    	$con = mysqli_connect($server, $user, $pass,$bd) 
    	or die("Ha sucedido un error inexperado en la conexion de la base de datos");
		
		$sql ="SELECT id FROM imagenesAnimales ORDER BY id ASC";
		$res = mysqli_query($con,$sql);
		$id = 0;
		while($row = mysqli_fetch_array($res)){
			$id = $row['id'];
		}
		$id = $id + 1;
		$path = "fotos/$id.png";
		$actualpath = "https://www.losnaranjosdam.online/2dam/ruben/adoptales/fotos/$id.png";
		$sql = "INSERT INTO imagenesAnimales (foto,nombre) VALUES ('$actualpath','$id.png')";
		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($imagen));
			echo "Subio imagen Correctamente";
		}
		mysqli_close($con);
		}else{
		echo "Error";
	}
?>