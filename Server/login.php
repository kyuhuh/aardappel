<?php
include_once "inc/db_functions.php";

//var_dump($_POST);
if(isset($_POST['user'])) {
    $password = $_POST['pass'];
    $email = $_POST['user'];
    $db = new DB_Functions();
    
    $user = $db->getUserByEmailAndPassword($email, $password);
    echo json_encode($user);
}
else {
	echo ' { "username" : "Peter", "status" : "Success", "token" : "28hd9d" }';
}



