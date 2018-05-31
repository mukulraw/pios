<?php
include('../api/config.php');

$user_id = $_POST['user_id'];
$fid = $_POST['fid'];



$query = "UPDATE users SET fcm_reg_id = '$fid' WHERE id = '$user_id'";

$data = mysqli_query($con , $query);

if($data)
{
	echo 1;
}
else
{
	echo 0;
}



?>