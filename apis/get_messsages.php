<?php
include('../api/config.php');

$group_id = $_POST['group_id'];



$query = "SELECT * FROM chat WHERE group_id = '$group_id' ORDER BY created_date DESC";

$data = mysqli_query($con , $query);



while($row = mysqli_fetch_assoc($data))
    {
		$iidd = $row['sender_id'];
		
		$user_query = "SELECT * FROM users WHERE id = '$iidd'";
		
		//echo $user_query;
		
		$ud = mysqli_query($con , $user_query);
		
		//print_r($ud);
		
		$udata = mysqli_fetch_assoc($ud);
		
		//print_r($udata);
		
        $tresult[] = array(
    "id" => $row['id'],
	"group_id" => $row['group_id'],
	"sender_id" => $row['sender_id'],
	"message" => $row['message'],
	"created_date" => $row['created_date'],
	"sender_image" => $udata['profile_pic'],
	"sender_name" => $udata['name']
    );
	
	
    }

	
	echo json_encode($tresult);
	

?>