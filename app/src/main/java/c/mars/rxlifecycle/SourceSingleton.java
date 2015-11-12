package c.mars.rxlifecycle;

import java.util.concurrent.TimeUnit;

import lombok.Data;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by Constantine Mars on 11/12/15.
 */
@Data
public class SourceSingleton {
    private Observable<Long> observable;

    private SourceSingleton(){
        observable = rx.Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .autoConnect();
    }

    private static class Holder{
        private static final SourceSingleton INSTANCE = new SourceSingleton();
    }

    public static SourceSingleton getInstance() {return Holder.INSTANCE;}
}
