<?php 
	$id=$_GET['id'];
	$idimg=$_GET['idimg'];

	$sql = "DELETE FROM animales where id=$id;";

	$server = "";
	$user = "";
	$pass = "";
	$bd = "adoptales";

	// Create connection
	$conn = mysqli_connect($server, $user, $pass, $bd);
	// Check connection
	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}

	$respuesta = array(); //creamos un array

	unlink("ftp://losnaranjosdam.online/adoptales/fotos/"+$idimg)
	if (mysqli_query($conn, $sql)) {
		$respuesta[] = array('resp' => "hecho");
	} else {
		$respuesta[] = array('resp' => "hecho");
	}

	mysqli_close($conn);

	//Creamos el JSON
    $json_string = json_encode($respuesta);
    echo '{';
    echo 'respuesta:';
    echo $json_string;
    echo '}';	
?>