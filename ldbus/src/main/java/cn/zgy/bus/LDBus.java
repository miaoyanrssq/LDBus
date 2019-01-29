package cn.zgy.bus;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LocalLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
*
* @author zhengy
* create at 2019/1/29 下午2:18
**/
public class LDBus {
    private final Map<String, BusLiveData<Object>> bus;

    private LDBus(){
        bus = new HashMap<>();
    }

    private static class SingletonHolder{
        private static final LDBus LD_BUS = new LDBus();
    }

    public static LDBus get(){
        return SingletonHolder.LD_BUS;
    }

    public synchronized <T> Observable<T> with(String key, Class<T> type){
        if(!bus.containsKey(key)){
            bus.put(key, new BusLiveData<>(key));
        }
        return (Observable<T>) bus.get(key);
    }

    public Observable<Object> with(String key) {
        return with(key, Object.class);
    }



    private class BusLiveData<T> extends LocalLiveData<T> implements Observable<T>{

        @NonNull
        private final String key;
        private Map<Observer, Observer> observerMap = new HashMap<>();

        private BusLiveData(String key) {
            this.key = key;
        }


        @Override
        public void setValue(T value) {
            super.setValue(value);
        }

        @Override
        public void postValue(T value) {
            super.postValue(value);
        }



        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull android.arch.lifecycle.Observer<T> observer) {
            super.observe(owner, observer);
        }
        @Override
        public void observeForever(@NonNull Observer<T> observer) {
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, new ObserverWrapper(observer));
            }
            super.observeForever(observerMap.get(observer));
        }

        @Override
        public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observeSticky(owner, observer);
        }

        @Override
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

    private static class ObserverWrapper<T> implements Observer<T>{
        private Observer<T> observer;
        public ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if(observer != null){
                if(isCallOnObserve()){
                    return;
                }
                observer.onChanged(t);
            }
        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement element : stackTrace) {
                    if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) &&
                            "observeForever".equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
