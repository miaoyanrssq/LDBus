package cn.zgy.ldbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.zgy.bus.LDBus;
import cn.zgy.livedatabus.LiveDataBus;


public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.get().with("hello1").setValue("消息2");
            }
        });

        findViewById(R.id.hello2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.get().with("hello2").setValue("消息6");
            }
        });

    }
}
