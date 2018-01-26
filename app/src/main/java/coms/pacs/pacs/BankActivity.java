package coms.pacs.pacs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import coms.pacs.pacs.Activity.LoginActivity;
import coms.pacs.pacs.Activity.MainActivity;
import coms.pacs.pacs.Activity.MenuActivity;
import coms.pacs.pacs.Activity.ReportDetailActivity;
import coms.pacs.pacs.Dialog.WriteReportDialog;
import coms.pacs.pacs.Utils.K2JUtils;

public class BankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username = K2JUtils.get("username", "");

        if(TextUtils.isEmpty(username)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
