package com.jiec.basketball.bean;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

/**
 * 未读消息数据模型
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NotifyCounterModel extends CommResponse {

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

        private String total_unread;

        public String getTotal_unread() {
            return total_unread;
        }

        public void setTotal_unread(String total_unread) {
            this.total_unread = total_unread;
        }
    }
}
