package coms.pacs.pacs.Rx;


import coms.pacs.pacs.Room.DownStatu;
import coms.pacs.pacs.Rx.Utils.RxLifeUtils;
import coms.pacs.pacs.Utils.K2JUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class MyObserver<T> implements Observer<T> {
    private static final String TAG = "MyObserver";
    private Object tag = null;
    Disposable d;

    protected MyObserver(Object tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        RxLifeUtils.getInstance().add(tag, d);
    }


    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        K2JUtils.log("web",e.getMessage());
    }

    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed())
            d.isDisposed();
    }

    public void onProgress(DownStatu downStatu) {
    }

}

