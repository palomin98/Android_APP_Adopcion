<?php 

	$nombre = $_GET['nombre'];
	$nombreusuario = $_GET['nombreusuario'];
	$email = $_GET['email'];
	$password = $_GET['password'];
	$nacimiento = $_GET['nacimiento'];
	$telefono = $_GET['telefono'];
	$imgperfil = $_GET['imgperfil'];
	$direccion = $_GET['direccion'];
	
	$sqlSelect = "SELECT count(nombre) as 'numero'  FROM usuarios where email='$email' or nombreuser='$nombreusuario';";

	$server = "";
	$user = "";
	$pass = "";
	$bd = "adoptales";

    //Creamos la conexión
    $conexion = mysqli_connect($server, $user, $pass,$bd) 
    or die("Ha sucedido un error inexperado en la conexion de la base de datos");

    //generamos la consulta
    mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

    if(!$result = mysqli_query($conexion, $sqlSelect)) die();

	$sql ="SELECT id FROM imagenesUsuarios ORDER BY id ASC";

	$res = mysqli_query($conexion,$sql);
	$id = 0;
	while($row = mysqli_fetch_array($res)){
		$id = $row['id'];
	}
	$id = $id + 1;

	$sqlInsert = "INSERT INTO usuarios (nombre, nombreuser, email, password, nacimiento, telefono, imgperfil, direccion) VALUES ('$nombre', '$nombreusuario', '$email', '$password', '$nacimiento', '$telefono', '$id.png', '$direccion');";

	$registro = array(); //creamos un array

    while($row = mysqli_fetch_array($result)) 
    { 
		$contar = $row['numero'];

        if($contar < 1){
			mysqli_query($conexion, $sqlInsert);
			$contar = "hecho";
		}else{
			$contar = "no hecho";
		}
		
		$registro[] = array('contar'=> $contar);
    }

    //desconectamos la base de datos
    $close = mysqli_close($conexion) 
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");

	//Creamos el JSON
    $json_string = json_encode($registro);
    echo '{';
    echo 'registro:';
    echo $json_string;
    echo '}';	
?>