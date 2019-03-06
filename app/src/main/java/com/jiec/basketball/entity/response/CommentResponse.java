package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.entity.Category;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/8.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class CommentResponse extends CommResponse {

    SaveBean savedposts;

    public SaveBean getSavedposts() {
        return savedposts;
    }

    public void setSavedposts(SaveBean savedposts) {
        this.savedposts = savedposts;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class SaveBean {
        private int count;

        private int count_total;

        private int pages;

        private Category category;

        private List<NewsBean> comments;

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

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public List<NewsBean> getComments() {
            return comments;
        }

        public void setComments(List<NewsBean> comments) {
            this.comments = comments;
        }
    }
}

