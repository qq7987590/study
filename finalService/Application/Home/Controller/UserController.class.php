<?php
namespace Home\Controller;
use Think\Controller;
class UserController extends Controller {
    public function login($email="",$password=""){
        $return['loginStatus'] = false;
        $return['userName'] = "";
        $return['userType'] = -1;
    	if($email != "" && $password != ""){
    		$user = D('User');
    		$map['email'] = $email;
    		$map['password'] = $password;
    		$result = $user -> where($map) -> select();
    		if(sizeof($result) != 0){
                $return=$result[0];
                $return['loginStatus'] = true;
                $this->jsonReturn($return,'jsonp');
            }
            else{
                $this->jsonReturn($return,'jsonp');
            }
    	}
    	else{
    		$this->jsonReturn($return,'jsonp');
    	}
    }
    private function jsonReturn($return){
        echo json_encode($return);
    }
}