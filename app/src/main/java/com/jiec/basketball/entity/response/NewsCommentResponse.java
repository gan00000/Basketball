package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by Jiec on 2019/2/17.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NewsCommentResponse extends CommResponse {

    /**
     * status : ok
     * desc : get comments
     * result : {"total_comments":1,"comments":[{"comment_id":"129","post_id":"120132","user_id":"19","comment_author":"王创杰","comment_date":"2019-02-16 23:20:45","comment_content":"裤子吗","user_img":"","total_like":"0","like":[],"my_like":0}],"total_save":"1","my_save":0}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class ResultBean {
        /**
         * total_comments : 1
         * comments : [{"comment_id":"129","post_id":"120132","user_id":"19","comment_author":"王创杰","comment_date":"2019-02-16 23:20:45","comment_content":"裤子吗","user_img":"","total_like":"0","like":[],"my_like":0}]
         * total_save : 1
         * my_save : 0
         */

        private int total_comments;
        private String total_save;
        private int my_save;
        private int pages;
        private List<CommentsBean> comments;

        public int getTotal_comments() {
            return total_comments;
        }

        public void setTotal_comments(int total_comments) {
            this.total_comments = total_comments;
        }

        public String getTotal_save() {
            return total_save;
        }

        public void setTotal_save(String total_save) {
            this.total_save = total_save;
        }

        public int getMy_save() {
            return my_save;
        }

        public void setMy_save(int my_save) {
            this.my_save = my_save;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        /**
         * 評論實體類
         */
        @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
        public static class CommentsBean {
            /**
             * comment_id : 129
             * post_id : 120132
             * user_id : 19
             * comment_author : 王创杰
             * comment_date : 2019-02-16 23:20:45
             * comment_content : 裤子吗
             * user_img :
             * total_like : 0
             * like : []
             * my_like : 0
             */

            private String comment_id;
            private String post_id;
            private String user_id;
            private String comment_author;
            private String comment_date;
            private String comment_content;
            private String user_img;
            private String total_like;
            private int my_like;
            private String total_reply;
            private List<ReplyBean> reply;

            public List<ReplyBean> getReply() {
                return reply;
            }

            public void setReply(List<ReplyBean> reply) {
                this.reply = reply;
            }

            /**
             * 回復留言實體類
             */
            @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
            public static class  ReplyBean{
                private String comment_id;
                private String post_id;
                private String user_id;
                private String comment_author;
                private String comment_date;
                private String comment_content;
                private String user_img;
                private String total_like;
                private String reply_to_user_id;
                private String reply_to_display_name;
                private int my_like;

                public String getComment_id() {
                    return comment_id;
                }

                public void setComment_id(String comment_id) {
                    this.comment_id = comment_id;
                }

                public String getPost_id() {
                    return post_id;
                }

                public void setPost_id(String post_id) {
                    this.post_id = post_id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getComment_author() {
                    return comment_author;
                }

                public void setComment_author(String comment_author) {
                    this.comment_author = comment_author;
                }

                public String getComment_date() {
                    return comment_date;
                }

                public void setComment_date(String comment_date) {
                    this.comment_date = comment_date;
                }

                public String getComment_content() {
                    return comment_content;
                }

                public void setComment_content(String comment_content) {
                    this.comment_content = comment_content;
                }

                public String getUser_img() {
                    return user_img;
                }

                public void setUser_img(String user_img) {
                    this.user_img = user_img;
                }

                public String getTotal_like() {
                    return total_like;
                }

                public void setTotal_like(String total_like) {
                    this.total_like = total_like;
                }

                public String getReply_to_user_id() {
                    return reply_to_user_id;
                }

                public void setReply_to_user_id(String reply_to_user_id) {
                    this.reply_to_user_id = reply_to_user_id;
                }

                public String getReply_to_display_name() {
                    return reply_to_display_name;
                }

                public void setReply_to_display_name(String reply_to_display_name) {
                    this.reply_to_display_name = reply_to_display_name;
                }

                public int getMy_like() {
                    return my_like;
                }

                public void setMy_like(int my_like) {
                    this.my_like = my_like;
                }

            }

            public String getTotal_reply() {
                return total_reply;
            }

            public void setTotal_reply(String total_reply) {
                this.total_reply = total_reply;
            }

            public String getComment_id() {
                return comment_id;
            }

            public void setComment_id(String comment_id) {
                this.comment_id = comment_id;
            }

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getComment_author() {
                return comment_author;
            }

            public void setComment_author(String comment_author) {
                this.comment_author = comment_author;
            }

            public String getComment_date() {
                return comment_date;
            }

            public void setComment_date(String comment_date) {
                this.comment_date = comment_date;
            }

            public String getComment_content() {
                return comment_content;
            }

            public void setComment_content(String comment_content) {
                this.comment_content = comment_content;
            }

            public String getUser_img() {
                return user_img;
            }

            public void setUser_img(String user_img) {
                this.user_img = user_img;
            }

            public String getTotal_like() {
                return total_like;
            }

            public void setTotal_like(String total_like) {
                this.total_like = total_like;
            }

            public int getMy_like() {
                return my_like;
            }

            public void setMy_like(int my_like) {
                this.my_like = my_like;
            }

        }
    }
}
