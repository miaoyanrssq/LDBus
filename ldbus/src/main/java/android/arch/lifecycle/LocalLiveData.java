package android.arch.lifecycle;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LocalLiveData<T> extends MutableLiveData<T> {

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        if(owner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED){
            return;
        }
        try {
            LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
            wrapper.mLastVersion = getVersion();
            Object existing = invokePutIfAbsent(observer, wrapper);
            if(existing != null && !invokeIsAttachedTo(existing, owner)){
                throw new IllegalArgumentException("Cannot add the same observer"
                        + " with different lifecycles");
            }
            if(existing != null){
                return;
            }
            owner.getLifecycle().addObserver(wrapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer){
        super.observe(owner, observer);
    }





    private Object invokePutIfAbsent(Object observer, Object wrapper) throws Exception{

        Object mObservers = getObservers();
        Class<?> safeIterableMap = mObservers.getClass();
        Method putIfAbsent = safeIterableMap.getDeclaredMethod("putIfAbsent", Object.class, Object.class);
        putIfAbsent.setAccessible(true);
        return putIfAbsent.invoke(mObservers, observer, wrapper);
    }

    private boolean invokeIsAttachedTo(Object caller, Object owner) throws Exception{
        Class<?> type = caller.getClass().getSuperclass();
        Method isAttachedTo = type.getDeclaredMethod("isAttachedTo", LifecycleOwner.class);
        isAttachedTo.setAccessible(true);
        return (boolean) isAttachedTo.invoke(caller, owner);
    }


    private Object getObservers() throws Exception{
        Field fields = LiveData.class.getDeclaredField("mObservers");
        fields.setAccessible(true);
        Object observers = fields.get(this);
        return observers;
    }
}
