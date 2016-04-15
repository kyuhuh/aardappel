<?php
include_once "inc/db_functions.php";
?>
<form method="post">
    Username: <input type="text" name="user"></br>
    Email: <input type="text" name="mail" /></br>
    Password: <input type="password" name="pass" /></br>
    <input type="submit" value="Opslaan">
</form>
<?php

if(isset($_POST['user'])) {
    $name = $_POST['user'];
    $password = $_POST['pass'];
    $email = $_POST['mail'];
    $db = new DB_Functions();
    if($db->registerUser($name, $email, $password))
    {
        $user = array("status" => "OK",
                      "name" => $name,
                      "email" => $email);
        echo json_encode($user);
    }
     else {
        echo json_encode(array("status" => "FAILED"));
     }
}



