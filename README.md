# LDBus
> 利用LiveData的生命周期属性，封装的消息总线组件

## 引用
```groovy
compile 'cn.zgy.bus:ldbus:0.0.2'（采用hook源码的方式，更改mLastVersion）
或者：
compile 'cn.zgy.livedatabus:LiveDataBus:0.0.2'（本地逻辑实现，不修改源码）
```

## 使用

### 接收消息

1. 跟随生命周期（observe）

```java
 LDBus.get().with("key", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {

                    }
                });
```

2. 跟随生命周期的粘性消息（observeSticky）

```java
LDBus.get().with("key", String.class)
                    .observeSticky(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String s) {

                        }
                    });
```

3. 无视生命周期（observeForever，需要手动销毁）
```java

Observer<String> observer =new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {

        }
    };
注册：
LDBus.get().with("key", String.class)
                    .observeForever(observer);

注销：
LDBus.get().with("key", String.class)
                .removeObserver(observer);
```

4. 无视生命周期粘性消息（observeStickyForever，需要手动销毁）
```java

Observer<String> observer =new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {

        }
    };
注册：
LDBus.get().with("key", String.class)
                    .observeStickyForever(observer);

注销：
LDBus.get().with("key", String.class)
                .removeObserver(observer);
```

### 发送消息
主线程：
```java
LDBus.get().with("key").setValue("消息");
```

子线程：
```java
LDBus.get().with("key").postValue("异步消息");
```