<?php 

	$nombre = $_GET['nombre'];
	$tipo = $_GET['tipo'];
	$raza = $_GET['raza'];
	$descripcion = $_GET['descripcion'];
	$imganimal = $_GET['imganimal'];
	$edad= $_GET['edad'];
	$iduser = $_GET['iduser'];
	$vacuna = $_GET['vacuna'];
	$chip = $_GET['chip'];
	$genero = $_GET['genero'];

	$server = "";
	$user = "";
	$pass = "";
	$bd = "adoptales";

    //Creamos la conexión
    $conexion = mysqli_connect($server, $user, $pass,$bd) 
    or die("Ha sucedido un error inexperado en la conexion de la base de datos");

	$sql ="SELECT id FROM imagenesAnimales ORDER BY id ASC";

    //generamos la consulta
    mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

	$res = mysqli_query($conexion,$sql);
	$id = 0;
	while($row = mysqli_fetch_array($res)){
		$id = $row['id'];
	}
	$id = $id + 1;

	$sqlInsert = "INSERT INTO animales (nombre, tipo, raza, descripcion, imganimal, edad, iduser, genero, chip, vacunado) VALUES ('$nombre', '$tipo', '$raza', '$descripcion', '$id.png', '$edad', '$iduser', '$genero', '$chip', '$vacuna');";

	$registro = array(); //creamos un array
	if(!$result = mysqli_query($conexion, $sqlInsert)) die();
	$registro[] = array('resu'=> "hecho");
    //desconectamos la base de datos
    $close = mysqli_close($conexion) 
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");

	//Creamos el JSON
    $json_string = json_encode($registro);
    echo '{';
    echo 'subir:';
    echo $json_string;
    echo '}';	
?>