package cn.zgy.ldbus;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.zgy.livedatabus.LiveDataBus;


public class TestActivity extends AppCompatActivity {

    Observer<String> observer =new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            Toast.makeText(TestActivity.this, s + "forever", Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");




        if(type.equals("observer")){
            LiveDataBus.get().with("hello1", String.class)
                    .observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String s) {
                            Toast.makeText(TestActivity.this, s + "observer", Toast.LENGTH_LONG).show();
                        }
                    });
        }else if(type.equals("sticky")){
            LiveDataBus.get().with("hello1", String.class)
                    .observeSticky(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String s) {
                            Toast.makeText(TestActivity.this, s + "sticky", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else if(type.equals("forever")){
            LiveDataBus.get().with("hello1", String.class)
                    .observeForever(observer);
        }else if(type.equals("stickyForever")){
            LiveDataBus.get().with("hello1", String.class)
                    .observeStickyForever(observer);
        }



        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, TestActivity2.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataBus.get().with("hello1", String.class)
                .removeObserver(observer);
    }
}
