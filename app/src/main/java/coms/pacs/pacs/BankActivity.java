package coms.pacs.pacs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import coms.pacs.pacs.Activity.LoginActivity;
import coms.pacs.pacs.Activity.MainActivity;
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
