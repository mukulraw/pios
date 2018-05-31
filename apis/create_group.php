<?php
include('../api/config.php');

// admin id
// rid
// group name
// group users id
// last message
// last message time


$admin_id = $_POST['user_id'];
$rid = $_POST['rid'];
$group_name = $_POST['group_name'];
$group_users = $_POST['group_users'];

date_default_timezone_set('Asia/Calcutta');
$create_date = date("Y-m-d H:i:s");

$query = "INSERT INTO user_group(rid, admin_id , group_name , created_date , last_message_time) values('$rid','$admin_id' , '$group_name' , '$create_date' , '$create_date')";



$data = mysqli_query($con , $query);
$lid = mysqli_insert_id($con);



$users_array = explode(',', $group_users);


$query3 = "INSERT INTO group_users(group_id, user_id , status) values('$lid','$admin_id' , '1')";


	
	$data3 = mysqli_query($con , $query3);


foreach($users_array as $user){
    
	$query2 = "INSERT INTO group_users(group_id, user_id , status) values('$lid','$user' , '1')";
	
	
	
	$data2 = mysqli_query($con , $query2);
	
}


$details= array(
   'message' => "Group created successfully",
        'status' => "1"
    );
 echo json_encode($details);


?>