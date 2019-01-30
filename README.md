# LDBus
> 利用LiveData的生命周期属性，封装的消息总线组件

## 饮用
```groovy
compile 'cn.zgy.bus:ldbus:0.0.1'
```

## 使用

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