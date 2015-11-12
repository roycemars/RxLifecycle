package c.mars.rxlifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observables.ConnectableObservable;
import timber.log.Timber;

/**
 * pressing on FAB displays activity and makes current RxActivity to unsubscribe on PAUSE event
 * to keep data stream from Source running even when we unsubscribe - we made it Singleton
 * and use ConnectableObservable.autoConnect() to keep source hot and automatically connect
 * only on the first subscription
 */

public class LauncherActivity extends RxAppCompatActivity {

    private
    Observable<Long> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Timber.plant(new Timber.DebugTree());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        observable = SourceSingleton.getInstance().getObservable()
                .compose(RxLifecycle.bindUntilActivityEvent(lifecycle(), ActivityEvent.PAUSE))
                .doOnUnsubscribe(() -> Timber.d("unsubscribed"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        observable.forEach(aLong -> Timber.d(">>" + aLong));
    }
}
