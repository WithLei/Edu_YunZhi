package com.android.renly.edu_yunzhi.Bean;

import com.android.renly.edu_yunzhi.Common.AppNetConfig;

public class GalleryData {
    private String imgurl;
    private String title;
    private String titleUrl;


    public GalleryData(String imgurl, String title, String titleUrl) {
        if (imgurl.startsWith("./")) {
            imgurl = AppNetConfig.BASE_URL_RS + imgurl.substring(2);
        }
        this.imgurl = imgurl;
        this.title = title;
        this.titleUrl = titleUrl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }
}
