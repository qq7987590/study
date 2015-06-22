<?php
namespace Home\Controller;
use Think\Controller;
class ReportController extends Controller {
    public function getAllReport(){
        $report = M("Report");
        $result = $report -> select();
        if(sizeof($result) == 0){
            echo "-1";
        }
        else{
            $this -> jsonReturn($result);
        }
    }
    public function createReport(){
    	$report = D("Report");
        if ($report -> create()){
            $report -> add();
            echo "1";
        }
        else{
            echo "0";
        }
    }
    private function jsonReturn($return){
        echo json_encode($return);
    }
}