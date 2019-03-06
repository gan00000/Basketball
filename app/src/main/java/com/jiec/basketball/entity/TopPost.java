package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * 推荐新闻
 * Created by wangchuangjie on 2018/5/24.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class TopPost {

    /**
     * status : ok
     * count : 6
     * count_total : 2415
     * pages : 403
     * posts : [{"id":60526,"type":"post","slug":"%e4%b8%80%e5%88%86%e9%90%98%e9%80%a3%e9%80%81kobe%e5%9b%9b%e6%ac%a1%e7%81%ab%e9%8d%8b%ef%bc%81%e4%bb%96%e8%8b%a5%e5%9c%a8%e7%8f%be%e5%9c%a8%e6%89%93%e7%90%83%e8%81%af%e7%9b%9f%e5%bf%85%e7%bf%bb","url":"http://www.ballgametime.com/news/60526.html","status":"publish","title":"一分鐘連送Kobe四次火鍋！他若在現在打球聯盟必翻天，現已是一方教父！","title_plain":"。<\/p>\n","excerpt":"<p>現在NBA已經變成了什麼樣子了？各個位置模糊無比，傳統中鋒原來越少，每隻球隊都在強調快節奏，無論是前鋒和中鋒都 [","date":"2018-05-24 18:09:51","modified":"2018-05-24 18:09:51","categories":[{"id":17,"slug":"news","title":"新聞","description":"","parent":0,"post_count":16541}],"tags":[{"id":673,"slug":"andrei-kirilenko","title":"Andrei Kirilenko","description":"","post_count":6}],"author":{"id":8,"slug":"lanweiheai","name":"你家大门常打开","first_name":"你家大门常打开","last_name":"","nickname":"你家大门常打开","url":"http://你家大门常打开","description":""},"comments":[],"attachments":[],"comment_count":0,"comment_status":"open","custom_fields":{"views":["731"]}}]
     */

    private String status;
    private int count;
    private int count_total;
    private int pages;
    private List<NewsBean> posts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<NewsBean> getPosts() {
        return posts;
    }

    public void setPosts(List<NewsBean> posts) {
        this.posts = posts;
    }
}
