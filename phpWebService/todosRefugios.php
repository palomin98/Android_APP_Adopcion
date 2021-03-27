<?php 

	$ano=$_GET['ano'];
	$sql = "SELECT * FROM refugios;";

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

    $refugios = array(); //creamos un array

    while($row = mysqli_fetch_array($result)) 
    { 
		$id = $row['id'];
        $nombrerefugio = $row['nombrerefugio'];
        $nombreadmin = $row['nombreadmin'];
		$email = $row['email'];
        $telefono = $row['telefono'];
        $imgperfil = $row['imgperfil'];
		$direccion = $row['direccion'];

        $refugios[] = array('id' => $id, 'nombrerefugio'=> $nombrerefugio, 'nombreadmin'=> $nombreadmin, 'email' => $email, 'telefono'=> $telefono, 'imgperfil'=> $imgperfil, 'direccion'=> $direccion);
    }

    //desconectamos la base de datos
    $close = mysqli_close($conexion) 
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


    //Creamos el JSON
    $json_string = json_encode($refugios);
    echo '{';
    echo 'refugios:';
    echo $json_string;
    echo '}';	
?>