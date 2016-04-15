<?php 
include_once "inc/db_connect.php";

$db = new Db_Connect();
$link = $db->connect();


$query = "SELECT * FROM users";
$result = mysqli_query($link, $query);

while($row = mysqli_fetch_object($result))
{
    echo $row->pid." - ".$row->username."</br>";
}
?>
