<?php
  include 'config.php';
 
$hostname = "localhost";
//connection to the database
$dbhandle = mysqli_connect($hostname, $username, $password)
or die("Unable to connect to MySQL");
//select a database to work with
$selected = mysqli_select_db($dbhandle,$dbname)
or die("Could not select vit");

$response = array();
$phno= $_POST['mobile'];
    $date= $_POST['date'];
$success=0;

        
       
  $result=mysqli_query($dbhandle,"UPDATE bd SET entry = '$date' WHERE phone = '$phno'");
         
      
if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Donor successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    }
 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}      
?>
