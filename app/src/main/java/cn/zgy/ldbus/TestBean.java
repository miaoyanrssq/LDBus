package cn.zgy.ldbus;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import cn.zgy.livedatabus.LiveDataBus;

public class TestBean {
    int id;
    String name;

    public TestBean(int id, String name) {
        this.id = id;
        this.name = name;
        LiveDataBus.get().with("hello1", String.class)
                .observeStickyForever(observer);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Observer<String> observer =new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            name = s;
        }
    };
}
