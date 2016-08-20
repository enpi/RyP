package com.codamasters.ryp.UI.professor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.codamasters.ryp.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

/**
 * Created by Juan on 06/04/2016.
 */

public class ProfessorWebViewActivity extends AppCompatActivity {

    private SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_web_view_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        spinKitView = (SpinKitView) findViewById(R.id.spin_kit_web);
        Sprite drawable = new ThreeBounce();
        spinKitView.setIndeterminateDrawable(drawable);

        final WebView webView = (WebView) findViewById(R.id.wv_teacher);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && spinKitView.getVisibility() == spinKitView.GONE) {
                    spinKitView.setVisibility(spinKitView.VISIBLE);
                }
                if (progress == 100) {
                    spinKitView.setVisibility(spinKitView.GONE);
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getExtras().getString("web_url"));


    }
}
