<?php
include('../api/config.php');

$user_id = $_POST['user_id'];
$rid = $_POST['rid'];
$post_type = $_POST['post_type'];

date_default_timezone_set('Asia/Calcutta');
$create_date = date("Y-m-d H:i:s");


$profile_pic = $_FILES['image']['name'];
$tmp_path = $_FILES['image']['tmp_name'];
$path = "../uploads/posts/".$profile_pic ;
move_uploaded_file($tmp_path,$path);

$query = "INSERT INTO wall_posts (rid , type , post , user_id , total_likes , total_comments , created_date , post_type) VALUES ('$rid' , 'image' , '$profile_pic' , '$user_id' , '0' , '0' , '$create_date' , '$post_type')";

$data = mysqli_query($con , $query);
$lid = mysqli_insert_id($con);

if($data)
{
	
	echo "success";
	
	sendNotifications($lid , $rid , $con);
		
}
else
{
	echo "failure";
}

function sendNotifications($lastId , $rid , $con)
		{
			
			
			
			
			$result = mysqli_query($con , "SELECT * FROM wall_posts WHERE id = '$lastId' ORDER BY created_date DESC");
  
  
  //echo "SELECT * FROM chat WHERE id = '$lastId'";
  
  
  //print_r($result);
  
  
  //$asd = mysqli_num_rows($result);
  
  //echo $asd;
  
  //if($asd > 0)
  //{
	  while($row = mysqli_fetch_assoc($result)){
	 
	
	$iidd = $row['user_id'];
		
		$user_query = "SELECT * FROM users WHERE id = '$iidd'";
		
		//echo $user_query;
		
		$ud = mysqli_query($con , $user_query);
		
		//print_r($ud);
		
		$udata = mysqli_fetch_assoc($ud);
		
		//print_r($udata);
		
        $tresult = array(
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
   
   
   
   //print_r($Eresult);
   //echo $userId;
   
   
   //echo "SELECT * FROM go_live_view WHERE liveId = '$videoId' AND viewId = '$userId'";
   
  /*  $key_data = mysql_fetch_array(mysql_query("SELECT * FROM go_live_view WHERE liveId = '$videoId' AND viewId = '$userId'")))
   {
	   
	   $target[] = array($key_data['keey']);
	   echo $target;
	   
   } */
   $data11 = mysqli_query($con , "SELECT * FROM ractive WHERE rid = '$rid'");
   while($key_data = mysqli_fetch_assoc($data11)){
	   
	   $iiddd = $key_data['user_id'];
	   
	   $user_query1 = "SELECT * FROM users WHERE id = '$iiddd'";
		
		//echo $user_query;
		
		$ud1 = mysqli_query($con , $user_query1);
		
		//print_r($ud);
		
		$udata1 = mysqli_fetch_assoc($ud1);
	   
	   $target[] = $udata1['fcm_reg_id'];
   }
	   
   
   
   
   
   
   
   
   
   //$target = //'ex_Vbjfca08:APA91bFcu_MToyo-QchT3ngetASkFGkI0OaDwZ0FRbKjVK5wwx3sfrvSUp_RsfKArztHZlGg6RP9IT0dN1GTup5Kwfs8br7_m8D3dV7P33WRv6e-NgQbRHjqAgT6z5PjNMWHjw5rU0M7'//;
	//or
	//$target = array('token1','token2','asdklj');
   
   
   
   //print_r($target);
   
   
   
   $url = 'https://fcm.googleapis.com/fcm/send';
//api_key available in Firebase Console -> Project Settings -> CLOUD MESSAGING -> Server key
//$server_key = 'AAAAwMRIlGQ:APA91bHNhhaI2KVrR1tF3g04pBd2TE4WYiD-0wdDsZCMk712gkz04bMAfwzmouib748bq2SamMYHSj2M2PFGjNvM1DmSTb8nCvN6C8LllAXZhLSpGzuwXyAFALxdjCnIivaJAGkXPCkC';


//echo $Eresult;


$jsdata = array();
$jsdata['type'] = "post";
$jsdata['data'] = $tresult;

			
$fields = array();
$fields['data'] = $jsdata;





//$fields['data'] = '{"asd": "asdasd"}';
if(is_array($target)){
	$fields['registration_ids'] = $target;
	//$fields['registration_ids'] = "ex_Vbjfca08:APA91bFcu_MToyo-QchT3ngetASkFGkI0OaDwZ0FRbKjVK5wwx3sfrvSUp_RsfKArztHZlGg6RP9IT0dN1GTup5K";
	$fields['priority'] = 'high';
}else{
	$fields['to'] = $target;
	//$fields['to'] = "ex_Vbjfca08:APA91bFcu_MToyo//-QchT3ngetASkFGkI0OaDwZ0FRbKjVK5wwx3sfrvSUp_RsfKArztHZlGg6RP9IT0dN1GTu//p5K";
	$fields['priority'] = 'high';
}

define("GOOGLE_API_KEY", "AAAABeeSJXI:APA91bHUpZRIDwz0Xy35A5q3-V1EoFLdesVqW7uPiLUjSIH8HRwFCnNnbnhfdoklhZHXgDBR6Q8p8d4lDJB9rRCPkCWtRIW97uZqZGhXfmAv-ATjBamaADv6M2xmQ2P4cPmM-g1gwpmo");

//header with content_type api key
$headers = array(
	'Content-Type:application/json',
  'Authorization: key=' . GOOGLE_API_KEY
);
		//print_r($fields);	
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);

//echo $result;

if ($result === FALSE) {
	die('FCM Send Error: ' . curl_error($ch));
}
curl_close($ch);
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   return $Eresult;
   
 
  	
			
			
		}


?>