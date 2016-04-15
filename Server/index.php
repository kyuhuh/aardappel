<?php
exec("uptime", $system);
echo json_encode(array(
    "status" => "running",
    "uptime" => "forever")
);
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

