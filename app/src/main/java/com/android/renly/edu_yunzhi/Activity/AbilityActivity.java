package com.android.renly.edu_yunzhi.Activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.Utils.UIUtils;

import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getScreenWidth;

public class AbilityActivity extends BaseActivity {
    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData() {
        String imageUrl1 = "http://r.photo.store.qq.com/psb?/V13Hh3Xy2gxYy4/2F9CwiIOTHpPl2fw4x2NNewRNoeIJ4fTjir4rFh4D60!/r/dIMAAAAAAAAA";
        String imageUrl2 = "http://r.photo.store.qq.com/psb?/V13Hh3Xy2gxYy4/gq7acrK9O8VHKSHER2*RhOinchlUQy*M0Wu2aqf.xkY!/r/dDMBAAAAAAAA";
        int screenWidthDp = UIUtils.px2dp(getScreenWidth(this));

        String html = "<html>" +
                "<body>" +
                "   <style type=\"text/css\">  " +
                "  body{  " +
                "   margin:0px;  " +
                "  }   " +
                " </style>  " +
                "<img id=\"img\" src=\""+imageUrl1+"\" width=\""+screenWidthDp+"\"/>" +
                "<script>" +
                " function getsize(){" +
                "  var img = document.getElementById(\"img\");" +
                "  javascript:stub.getNaturalSize(img.naturalWidth,img.naturalHeight);" +
                " }" +
                "</script>" +
                "</body>" +
                "</html>";
        WebView webView = findViewById(R.id.web_view);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:getsize()");
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void getNaturalSize(int imgWidth, int imgHeight) {
                Log.i("image size","width: "+imgWidth+" height: "+imgHeight);
            }
        },"stub");

        webView.loadData(html, "text/html", "utf-8");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ability;
    }
}
