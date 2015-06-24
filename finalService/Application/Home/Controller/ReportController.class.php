<?php
namespace Home\Controller;
use Think\Controller;
class ReportController extends Controller {
    public function getAllReport(){
        $userType = $_POST["user_type"];
        $userName = $_POST["user_name"];
        $report = M("Report");
        switch ($userType) {
            case '0':
                #派单员
                # code...
                break;
            case '1':
                #业务员
                # code...
                $report -> where("saleman = '$userName'");
                break;
            case '2':
                #评估员
                # code...
                $report -> where("assessment = '$userName'");
                break;
            case '3':
                #财务员
                # code...
                break;
            case '4':
                #文员
                # code...
                break;
            case '5':
                #一级评估师
                # code...
                $report -> where("fist_appraiser = '$userName'");
                break;
            case '6':
                #二级评估师
                # code...
                $report -> where("second_appraiser = '$userName'");
                break;
            case '7':
                #管理员
                # code...
                break;
        }
        $result = $report -> order("first_assess_number") -> select();
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
        $stat["stat"] = 0;
        if($_POST["assess_date"] != "" && $_POST["outside_time"] != ""){
            $stat["stat"] = 1;
        }
        if($_POST["report_number"] != "" && $_POST["report_type"] != "" && $_POST["report_date"] != ""){
            $stat["stat"] = 2;
        }
        $report -> where("report_number = '$reportNumber'") -> save($stat);
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