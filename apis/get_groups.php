<?php
include('../api/config.php');

$admin_id = $_POST['user_id'];
$rid = $_POST['rid'];


$query = "SELECT * FROM user_group WHERE rid = '$rid' AND admin_id = '$admin_id' ORDER BY last_message_time DESC";

$data = mysqli_query($con , $query);

$post = array();

while($row = mysqli_fetch_assoc($data))
    {
        $post[] = $row;
    }

	
	echo json_encode($post);
	

?>