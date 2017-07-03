package com.example.android.wassupworld;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.util.List;


public class WebViewActivity extends AppCompatActivity {

    private static final String SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService";
    private static final String CHROME_PACKAGE = "com.android.chrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        Intent i = getIntent();
        if (i != null) {
            String url = i.getStringExtra(Intent.EXTRA_TEXT);
            if (url != null) {
                if (isChromeCustomTabsSupported(this)) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    String shareLabel = "Share";
                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            android.R.drawable.ic_menu_share);
                    builder.addDefaultShareMenuItem();

                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(this, Uri.parse(url));
                    finish();
                } else {
                    WebView webView = (WebView) findViewById(R.id.web_view);
                    webView.loadUrl(url);
                }
            }
        }

    }

    private static boolean isChromeCustomTabsSupported(@NonNull final Context context) {
        Intent serviceIntent = new Intent(SERVICE_ACTION);
        serviceIntent.setPackage(CHROME_PACKAGE);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        return !(resolveInfos == null || resolveInfos.isEmpty());
    }
}
