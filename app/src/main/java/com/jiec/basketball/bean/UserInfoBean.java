package com.jiec.basketball.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息实体类
 */
public class UserInfoBean implements Parcelable {
    public String user_token;
    public String user_id;
    public String display_name;
    public String user_email;
    public String user_status;
    public String user_img;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_token);
        dest.writeString(this.user_id);
        dest.writeString(this.display_name);
        dest.writeString(this.user_email);
        dest.writeString(this.user_status);
        dest.writeString(this.user_img);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.user_token = in.readString();
        this.user_id = in.readString();
        this.display_name = in.readString();
        this.user_email = in.readString();
        this.user_status = in.readString();
        this.user_img = in.readString();
    }

    public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
