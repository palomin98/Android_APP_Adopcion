<?php 

	$email=$_GET['email'];
	$password=$_GET['password'];

	$sql = "SELECT count(nombre) as 'numero'  FROM usuarios where email='$email' and password='$password';";

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

    $login = array(); //creamos un array

    while($row = mysqli_fetch_array($result)) 
    { 
        $numero = $row['numero'];

        $login[] = array('numero'=> $numero);
		
		echo $numero;
    }

    //desconectamos la base de datos
    $close = mysqli_close($conexion) 
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");
?>