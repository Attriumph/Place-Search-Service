package lingquan.firstApp.placesearch.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import lingquan.firstApp.placesearch.R;


/**
 * Created by yinjinbiao on 2016/6/17.
 */
public class WebViewActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView img;
    private TextView txt;
    private WebView mWebView;
    private String url;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.back);
        mWebView = (WebView) findViewById(R.id.webview);
        txt = (TextView) findViewById(R.id.name);

        img.setOnClickListener(this);
        txt.setText(name);

        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setSupportZoom(true); // 设置允许缩放
        mWebView.getSettings().setBuiltInZoomControls(true); // 设置允许缩放控件
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true); // 设置此属性，可任意比例缩放。
        mWebView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {

        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
        if (!TextUtils.isEmpty(url))
            mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
