<?php
include('../api/config.php');

$rid = $_POST['rid'];


$query = "SELECT * FROM wall_posts WHERE rid = '$rid' ORDER BY created_date DESC";



$data = mysqli_query($con , $query);


while($row = mysqli_fetch_assoc($data))
    {
        $iidd = $row['user_id'];
		
		$user_query = "SELECT * FROM users WHERE id = '$iidd'";
		
		//echo $user_query;
		
		$ud = mysqli_query($con , $user_query);
		
		//print_r($ud);
		
		$udata = mysqli_fetch_assoc($ud);
		
		//print_r($udata);
		
        $tresult[] = array(
    "id" => $row['id'],
	"rid" => $row['rid'],
	"type" => $row['type'],
	"post" => $row['post'],
	"total_likes" => $row['total_likes'],
	"total_comments" => $row['total_comments'],
	"post_type" => $row['post_type'],
	"created_date" => $row['created_date'],
	"sender_image" => $udata['profile_pic'],
	"sender_name" => $udata['name'],
	"user_id" => $row['user_id']
    );
    }

	
	echo json_encode($tresult);
	

?>