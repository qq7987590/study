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
    public function updateReport(){
        $report = D("Report");
        $reportNumber = $_POST['report_number'];
        $report -> where("report_number = '$reportNumber'") -> save($_POST);
    }
    public function getReportById(){
        $report = M("Report");
        $id = $_POST['id'];
        $result = $report -> where("report_number = '$id'") -> select();
        if(sizeof($result) != 0){
            $this -> jsonReturn($result[0]);
        }
        else{
            echo "-1";
        }
    }
    private function jsonReturn($return){
        echo json_encode($return);
    }
}