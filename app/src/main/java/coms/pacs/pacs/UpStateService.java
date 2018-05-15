package coms.pacs.pacs;

import android.content.Intent;

import com.carmelo.library.KeepliveService;

import java.util.concurrent.TimeUnit;

import coms.pacs.pacs.Api.ApiImpl;
import coms.pacs.pacs.Model.Base;
import coms.pacs.pacs.Rx.MyObserver;
import coms.pacs.pacs.Rx.Net.RequestParams;
import coms.pacs.pacs.Rx.RxSchedulers;
import coms.pacs.pacs.Rx.Utils.DeviceUtils;
import coms.pacs.pacs.Rx.Utils.IpUtils;
import coms.pacs.pacs.Rx.Utils.RxLifeUtils;
import coms.pacs.pacs.Utils.K2JUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 不听话的好孩子 on 2018/4/24.
 */

public class UpStateService extends KeepliveService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);
        Observable.interval(0, 5, TimeUnit.MINUTES).subscribe(new MyObserver<Long>(this) {
            @Override
            public void onNext(Long aLong) {
                Observable.just(aLong).flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        return Observable.just(IpUtils.getIPAddressOut());
                    }
                })
                        .flatMap(new Function<String, ObservableSource<Base<Object>>>() {
                            @Override
                            public ObservableSource<Base<Object>> apply(String ip) throws Exception {
                                K2JUtils.log("-----", ip);
                                K2JUtils.log("-----", "|");
                                K2JUtils.log("-----", "|");
                                K2JUtils.log("-----", "|");
                                return ApiImpl.Instance.getApiImpl().addEquipmentInfo(new RequestParams()
                                        .add("username", K2JUtils.get("username", "").toString())
                                        .add("equipment", DeviceUtils.INSTANCE.getUniqueId(getApplicationContext()))
                                        .add("ip", ip)
                                        .add("remark", "内网Ip" + IpUtils.getIPAddressIn()));
                            }
                        }).subscribeOn(Schedulers.io())
                        .subscribe(new MyObserver<Base<Object>>(this) {
                            @Override
                            public void onNext(Base<Object> objectBase) {
                                super.onNext(objectBase);
                                K2JUtils.log("设备运转心跳监测：", objectBase.getCode() == 100 ? objectBase.getMessage() : "设备状态正常!");
                            }
                        });
            }
        });


        return i;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
