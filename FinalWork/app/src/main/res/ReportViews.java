import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by libingkun on 2015/6/22.
 */
public class ReportViews {
    //view控件群
    public View viewRoot;
    public EditText distributor;
    public Spinner saleman;
    public EditText firstAccessNumber;
    public EditText reportMonth;
    public EditText contacts;
    public EditText contactsPhone;
    public EditText firstDistributor;
    public EditText secondDistributor;
    public EditText thirdDistributor;
    public EditText assessment;
    public EditText street;
    public EditText location;
    public EditText assessRemark;
    public EditText villageName;
    public EditText assessDate;
    public EditText outsideTime;
    public EditText assessPrice;
    public Spinner  firstAppraiser;
    public EditText firstAppraiserRemark;
    public Spinner  secondAppraiser;
    public EditText secondAppraiserRemark;
    public EditText reportNumber;
    public EditText reportDate;
    public EditText clerkRemark;
    public EditText fee;
    public EditText treasuterRemark;
    ReportViews(View viewRoot){
        this.viewRoot = viewRoot;
        distributor = viewRoot.findViewById(R.id.distributor)
    }
}
