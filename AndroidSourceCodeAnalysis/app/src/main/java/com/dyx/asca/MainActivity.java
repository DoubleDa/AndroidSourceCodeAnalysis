package com.dyx.asca;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dyx.asca.ui.GlideActivity;
import com.dyx.asca.ui.OkHttpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_okhttp)
    Button btnOkhttp;
    @BindView(R.id.btn_retrofit)
    Button btnRetrofit;
    @BindView(R.id.btn_glide)
    Button btnGlide;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_okhttp, R.id.btn_retrofit, R.id.btn_glide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_okhttp:
                intentTo(OkHttpActivity.class);
                break;
            case R.id.btn_glide:
                intentTo(GlideActivity.class);
                break;
            case R.id.btn_retrofit:
                break;
        }
    }

    private void intentTo(Class<?> cla) {
        startActivity(new Intent(this, cla));
    }

}
