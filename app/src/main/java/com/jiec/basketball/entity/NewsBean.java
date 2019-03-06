package com.jiec.basketball.entity;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.wangcj.common.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NewsBean implements Serializable {

    /**
     * id
     */
    private String id;

    private String url;
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
    /**
     * 时间
     */
    private String date;

    private Author author;

    private List<Category> categories;

    private List<Tag> tags;

    private String imageUrl;

    private String videoId;

    private int total_comment;

    private int total_like;

    private int total_save;
    private int my_save;

    /**评论相关信息内容*/
    private String comment_id;
    private String comment_content;
    private String comment_date;

    public int getMy_save() {
        return my_save;
    }

    public void setMy_save(int my_save) {
        this.my_save = my_save;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public CustomFields getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(CustomFields custom_fields) {
        this.custom_fields = custom_fields;
    }

    private CustomFields custom_fields;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getAuthorName() {
        if (this.author != null) {
            return this.author.getName();
        }

        return "";
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImgsrc() {
        if (!TextUtils.isEmpty(imageUrl)) {
            return imageUrl;
        }

        return getImagesrc();
    }

    public String getImagesrc() {
        if (TextUtils.isEmpty(getContent())) {
            return "";
        }

        try {
            int startIndex = getContent().indexOf("<img");

            if (startIndex == -1) {
                return "";
            }

            String imgContent = getContent().substring(startIndex);

            startIndex = imgContent.indexOf("src=\"") + "src=\"".length();
            int endIndex = startIndex + imgContent.substring(startIndex).indexOf("\"");

            String imgStr = imgContent.substring(startIndex, endIndex);
            LogUtil.d("test", "img url = " + Html.fromHtml(imgStr).toString());

            return Html.fromHtml(imgStr).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public int getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(int total_comment) {
        this.total_comment = total_comment;
    }

    public int getTotal_like() {
        return total_like;
    }

    public void setTotal_like(int total_like) {
        this.total_like = total_like;
    }

    public int getTotal_save() {
        return total_save;
    }

    public void setTotal_save(int total_save) {
        this.total_save = total_save;
    }

    public List<String> getVideoIds() {
        if (TextUtils.isEmpty(getContent())) {
            return null;
        }

        List<String> ids = new ArrayList<>();
        String content = getContent();

        while (true) {
            try {
                int startIndex = content.indexOf("<iframe");

                if (startIndex == -1) {
                    return ids;
                }

                String imgContent = getContent().substring(startIndex);

                startIndex = imgContent.indexOf("src=\"") + "src=\"".length();
                int endIndex = startIndex + imgContent.substring(startIndex).indexOf("\"");

                String imgStr = imgContent.substring(startIndex, endIndex);
                Log.e("test", "img url = " + Html.fromHtml(imgStr).toString());

                ids.add(imgStr);

                content = imgContent.substring(endIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return ids;
        }
    }

    public String getKind() {
        if (categories != null && categories.size() > 0) {
            return categories.get(0).getTitle();
        }
        return "";
    }

    public List<String> getTagStrs() {
        if (getTags() != null && getTags().size() > 0) {
            List<String> strs = new ArrayList<>();
            for (Tag tag : getTags()) {
                strs.add(tag.getTitle());
            }

            return strs;
        }

        return null;
    }

    public int getSumViews() {
        int sum = 1000;
        if (getCustom_fields() != null) {

            if (getCustom_fields().getViews() != null && getCustom_fields().getViews().size() > 0) {
                for (int view : getCustom_fields().getViews()) {
                    sum += view;
                }
            }
        }
        return sum;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class Author implements Serializable {
        public int id;
        public String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

