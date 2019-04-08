package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by Jiec on 2019/2/16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NotifyResponse extends CommResponse {

    Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class Result {

        /**
         * count : 1
         * count_total : 1
         * pages : 1
         * notification : [{"post_id":"102246","post_title":"","post_guid":"http://www.ballgametime.com/?p=102246","author":"18","comment_id":"128","comment_content":"test123","reply_msg":"","type":"like","read":"1","created_on":"2019-02-02 15:11:58","user_id":"18","display_name":"JC","user_img":""}]
         */

        private int count;
        private String count_total;
        private int pages;
        private List<NotificationBean> notification;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCount_total() {
            return count_total;
        }

        public void setCount_total(String count_total) {
            this.count_total = count_total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<NotificationBean> getNotification() {
            return notification;
        }

        public void setNotification(List<NotificationBean> notification) {
            this.notification = notification;
        }

        @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
        public static class NotificationBean {
            /**
             * post_id : 102246
             * post_title :
             * post_guid : http://www.ballgametime.com/?p=102246
             * author : 18
             * comment_id : 128
             * comment_content : test123
             * reply_msg :
             * type : like
             * read : 1
             * created_on : 2019-02-02 15:11:58
             * user_id : 18
             * display_name : JC
             * user_img :
             */

            private String post_id;
            private String post_title;
            private String post_guid;
            private String author;
            private String comment_id;
            private String comment_content;
            private String reply_msg;
            private String type;
            private String read;
            private String created_on;
            private String user_id;
            private String display_name;
            private String user_img;

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
            }

            public String getPost_title() {
                return post_title;
            }

            public void setPost_title(String post_title) {
                this.post_title = post_title;
            }

            public String getPost_guid() {
                return post_guid;
            }

            public void setPost_guid(String post_guid) {
                this.post_guid = post_guid;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
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

            public String getReply_msg() {
                return reply_msg;
            }

            public void setReply_msg(String reply_msg) {
                this.reply_msg = reply_msg;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRead() {
                return read;
            }

            public void setRead(String read) {
                this.read = read;
            }

            public String getCreated_on() {
                return created_on;
            }

            public void setCreated_on(String created_on) {
                this.created_on = created_on;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            public String getUser_img() {
                return user_img;
            }

            public void setUser_img(String user_img) {
                this.user_img = user_img;
            }
        }
    }
}
