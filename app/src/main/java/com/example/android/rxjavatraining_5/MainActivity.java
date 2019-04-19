package com.example.android.rxjavatraining_5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Disposable disposable;

    /**
     * Completable won't emit any item, instead it returns
     * Success or failure state
     * Consider an example of making a PUT request to server to update
     * something where you are not expecting any response but the
     * success status
     * -
     * Completable : CompletableObserver
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Flowable<Integer>flowableObserval=getFlowableObservable();
        SingleObserver<Integer>singleObserver=getFlowableObserver();
        flowableObserval
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer number) throws Exception {
                        return result+number ;
                    }
                })
                .subscribe(singleObserver);
    }

private SingleObserver<Integer>getFlowableObserver(){
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "onSuccess: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };

}
private Flowable<Integer>getFlowableObservable(){
        return Flowable.range(1,100);
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
