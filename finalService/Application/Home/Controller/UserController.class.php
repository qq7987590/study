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
    public function changeInfo(){
        $result = $this -> checkUser($_POST['email'],$_POST['password']);
        if(sizeof($result)==1){
            $uid = $result[0]['uid'];
            $user = M("User");
            $data = array(
                'email' => $_POST['newEmail'],
                'name' => $_POST['name'],
                'password' => $_POST['newPassword'],
                'phone' => $_POST['phone'],
                'sex' => $_POST['sex'],
                'birthday' => $_POST['birthday'],
                'idcard' => $_POST['IDCard']
                );

            $user -> where("uid = $uid") ->setField($data);
            echo "1";
        }
        else{
            echo "0";
        }
        
    }
    public function getAllUser(){
        $user = M("User");
        $result = $user -> select();
        if(sizeof($result) == 0){
            echo "-1";
        }
        else{
            $this -> jsonReturn($result);
        }
    }
    private function jsonReturn($return){
        echo json_encode($return);
    }
    private function checkUser($email,$password){
        $user = M('User');
        $map['email'] = $email;
        $map['password'] = $password;
        $result = $user -> where($map) ->select();
        return $result;
    }
}