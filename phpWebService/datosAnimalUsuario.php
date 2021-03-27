<?php 
	$iduser=$_GET['iduser'];
	$sql = "SELECT * FROM adoptales.usuarios where id='$iduser';";

	$server = "";
	$user = "";
	$pass = "";
	$bd = "adoptales";

    //Creamos la conexión
    $conexion = mysqli_connect($server, $user, $pass,$bd) 
    or die("Ha sucedido un error inexperado en la conexion de la base de datos");

    //generamos la consulta
    mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

    if(!$result = mysqli_query($conexion, $sql)) die();

    $animales = array(); //creamos un array

    while($row = mysqli_fetch_array($result)) 
    { 
		$telefono = $row['telefono'];
        $email = $row['email'];
		$direccion = $row['direccion'];
		
        $animales[] = array('telefono' => $telefono, 'email'=> $email, 'direccion'=> $direccion);
    }

    //desconectamos la base de datos
    $close = mysqli_close($conexion) 
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


    //Creamos el JSON
    $json_string = json_encode($animales);
    echo '{';
    echo 'usuario:';
    echo $json_string;
    echo '}';	
?>