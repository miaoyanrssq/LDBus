package cn.zgy.livedatabus;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {

    private final Map<String, BusLiveData<Object>> bus;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataBus LD_BUS = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return SingletonHolder.LD_BUS;
    }

    public synchronized <T> BusLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusLiveData<>(key));
        }
        return (BusLiveData<T>) bus.get(key);
    }

    public BusLiveData<Object> with(String key) {
        return with(key, Object.class);
    }


    public class BusLiveData<T> extends MutableLiveData<T> {
        @NonNull
        private final String key;
        private Map<Observer, Observer> observerMap = new HashMap<>();

        private boolean changeData;

        public BusLiveData(@NonNull String key) {
            this.key = key;
        }

        @Override
        public void setValue(T value) {
            changeData = true;
            super.setValue(value);
        }

        @Override
        public void postValue(T value) {
            changeData = true;
            super.postValue(value);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            changeData = false;
            super.observe(owner, new ObserverWrapper<>(observer, this));
        }

        @Override
        public void observeForever(@NonNull Observer<T> observer) {
            changeData = false;
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, new ObserverWrapper(observer, this));
            }
            super.observeForever(observerMap.get(observer));
        }

        public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observe(owner, observer);
        }

        public void observeStickyForever(@NonNull Observer<T> observer) {
            super.observeForever(observer);
        }

        @Override
        public void removeObserver(@NonNull Observer<T> observer) {
            Observer rObserver;
            if(observerMap.containsKey(observer)){
                rObserver = observerMap.remove(observer);
            }else {
                rObserver = observer;
            }
            super.removeObserver(rObserver);
            if(!hasObservers()){
                get().bus.remove(key);
            }
        }
    }


    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;
        private BusLiveData<T> liveData;

        public ObserverWrapper(Observer<T> observer, BusLiveData<T> liveData) {
            this.observer = observer;
            this.liveData = liveData;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (observer != null) {
                if (!liveData.changeData) {
                    return;
                }
                observer.onChanged(t);
            }

        }
    }
}
