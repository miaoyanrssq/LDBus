package cn.zgy.ldbus;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.zgy.bus.LDBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LDBus.get().with("hello1", String.class)
                .observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                });

        findViewById(R.id.hello1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("type", "observer");
                startActivity(intent);

            }
        });
        findViewById(R.id.hello2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("type", "sticky");
                startActivity(intent);

            }
        });
        findViewById(R.id.hello3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("type", "forever");
                startActivity(intent);
            }
        });
        findViewById(R.id.hello4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("type", "stickyForever");
                startActivity(intent);

            }
        });


        findViewById(R.id.hello5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LDBus.get().with("hello1").setValue("消息");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LDBus.get().with("hello1").postValue("异步消息");
                    }
                }).start();

            }
        });

    }
}
