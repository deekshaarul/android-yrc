<?php
  include 'config.php';
 
$hostname = "localhost";
//connection to the database
$dbhandle = mysqli_connect($hostname, $username, $password)
or die("Unable to connect to MySQL");
//select a database to work with
$selected = mysqli_select_db($dbhandle,$dbname)
or die("Could not select vit");
 $pname=$_POST["pname"];
$mobile=$_POST["mobile"];
$hname=$_POST["hname"];
 $age=$_POST["age"];
$intage= (int)$age;
$units=$_POST["units"];
$intunits= (int)$units;
$reason=$_POST["reason"];
$date=$_POST["date"]; 
$type=$_POST["type"];
$group=$_POST["group"];
$success=0;

        
       
  $row=mysqli_query($dbhandle,"INSERT INTO request VALUES ('$pname','$age','$mobile','$reason','$units','$date','$type','$group','$hname',0)");
         
      

 
if($row)
{
$success=1;
}
$response["success"]=$success;
die(json_encode($response));
mysqli_close($dbhandle);
       
?>
