<?php 
	$sql = $_GET['sqlFiltros'];

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
		$id = $row['id'];
        $nombre = $row['nombre'];
		$tipo = $row['tipo'];
        $raza = $row['raza'];
        $descripcion = $row['descripcion'];
        $imganimal = $row['imganimal'];
		$edad = $row['edad'];
        $iduser = $row['iduser'];
		$vacuna = $row['vacunado'];
		$chip = $row['chip'];
		$genero = $row['genero'];

        $animales[] = array('id' => $id, 'nombre'=> $nombre, 'tipo'=> $tipo, 'raza' => $raza, 'descripcion'=> $descripcion, 'imganimal'=> $imganimal, 'edad' => $edad, 'iduser'=> $iduser, 'vacuna'=>$vacuna , 'chip'=>$chip, 'genero'=>$genero);
    }

    //desconectamos la base de datos
    $close = mysqli_close($conexion) 
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


    //Creamos el JSON
    $json_string = json_encode($animales);
    echo '{';
    echo 'animales:';
    echo $json_string;
    echo '}';	
?>