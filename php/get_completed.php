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

$result = mysqli_query($dbhandle,"SELECT * FROM request where status = 1") or die(mysql_error());

if (mysqli_num_rows($result) > 0) {
$response["request"] = array();

while ($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
        // temp user array

        $req= array();
        $req["pname"] = $row["pname"];
        $req["age"] = $row["age"];
        $req["mobile"] = $row["phone"];
        $req["reason"] = $row["reason"];
        $req["units"] = $row["units"];
        $req["date"] = $row["last_date"];
        $req["type"] = $row["type"];
        $req["group"] = $row["bgroup"];
        $req["hname"] = $row["handler"];
        $req["st"] = $row["status"]; 
        // push single product into final response array
        array_push($response["request"], $req);

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

