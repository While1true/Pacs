package coms.pacs.pacs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

import coms.pacs.pacs.Activity.LoginActivity;
import coms.pacs.pacs.Activity.MainActivity;
import coms.pacs.pacs.Model.Base;
import coms.pacs.pacs.Rx.DataObserver;
import coms.pacs.pacs.Rx.MyObserver;
import coms.pacs.pacs.Rx.Utils.RxBus;
import coms.pacs.pacs.Utils.K2JUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

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
