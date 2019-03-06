package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

/**
 * Created by Jiec on 2019/1/13.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class LoginResult extends CommResponse {

    /**
     * status : ok
     * desc : login success
     * result : {"user_token":"3e1cf540395b493fbd7ff7a1ce123d41"}
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
         * user_token : 3e1cf540395b493fbd7ff7a1ce123d41
         */

        private String user_token;

        public String getUser_token() {
            return user_token;
        }

        public void setUser_token(String user_token) {
            this.user_token = user_token;
        }
    }
}
