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
$bg= "b+";
    $g= "male";

if($g == "male")
$result = mysqli_query($dbhandle,"SELECT * FROM bd where bg='$bg' AND gender = '$g' ") or die(mysqli_error());

else 
$result = mysqli_query($dbhandle,"SELECT * FROM bd where bg='$bg' ") or die(mysqli_error());

if (mysqli_num_rows($result) > 0) {
$response["match"] = array();

while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
        // temp user array

        $md= array();
        $md["name"] = $row["name"];
        $md["mobile"] = $row["phone"];
        $md["gender"] = $row["gender"];
        $md["bg"] = $row["bg"];
        
        // push single product into final response array
        array_push($response["match"], $md);

    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
 } else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    // echo no users JSON
    echo json_encode($response);
}


mysqli_close($dbhandle);
       
?>

