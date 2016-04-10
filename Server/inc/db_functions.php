<?php
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'db_connect.php';
        // Establish connection
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
    
    public function registerUser($name, $email, $password) {
        
        $hash = $this->hashSSHA($password);
        $e_password = $hash["encrypted"]; // encrypt the password
        $salt = $hash["salt"]; // salt
        $uuid = uniqid('', true);
        
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $e_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT id, name FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            
            $stmt->store_result();

            /* Bind the result to variables */
            $stmt->bind_result($id, $name);

            $user = $stmt->fetch();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
    
    public function getUserByEmailAndPassword($email, $password) {
        
        $stmt = $this->conn->prepare("SELECT id, name, email, salt, encrypted_password, token FROM users WHERE email = ? LIMIT 1");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $salt, $encrypted_password, $token);
        $stmt->store_result();
        if($stmt->num_rows == 1)  //To check if the row exists
            {
                if($stmt->fetch()) //fetching the contents of the row
                {
                    // verifying user password
                    $hash = $this->checkhashSSHA($salt, $password);
                    // check for password equality
                    if ($encrypted_password == $hash) {
                    // user authentication details are correct
                    $user = array(
                       "status" => "OK",
                       "id" => $id,
                       "username" => $name,
					   "token" => $token,
                       "email" => $email);
                }
                else {
                    $user = array("status" => "Passwords do not match.");
                }
            }

        }
        else {
            $user = array("status" => "User does not exist.");
        }
        $stmt->close();
        return $user;
    }
    
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
    
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
    
    
}

