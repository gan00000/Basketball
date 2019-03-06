package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

/**
 * Created by Jiec on 2019/1/26.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class UserProfile extends CommResponse {

    /**
     * desc : get profile
     * result : {"display_name":"Teh Jun Chuan","user_email":"junchuan_teh@hotmail.com","user_status":"0","user_img":"http://test.ballgametime.com/img/no-img.png"}
     */

    private String desc;
    private ResultBean result;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class ResultBean {
        /**
         * display_name : Teh Jun Chuan
         * user_email : junchuan_teh@hotmail.com
         * user_status : 0
         * user_img : http://test.ballgametime.com/img/no-img.png
         */

        private String user_id;
        private String display_name;
        private String user_email;
        private String user_status;
        private String user_img;

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

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }
    }
}
