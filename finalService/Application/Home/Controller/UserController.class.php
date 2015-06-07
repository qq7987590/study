<?php
namespace Home\Controller;
use Think\Controller;
class UserController extends Controller {
    public function login($email="",$password=""){
    	if($email != "" && $password != ""){

    	}
    	else{
    		echo "请输入用户名或密码";
    	}
    }
}