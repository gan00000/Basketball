package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.entity.NewList;
import com.jiec.basketball.network.base.CommResponse;

/**
 * Created by Administrator on 2017/2/19.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class HomeResponse extends CommResponse {

    private NewList topslideposts;

    private NewList newsposts;

    private NewList moveposts;

    private NewList picposts;

    private NewList techposts;

    public NewList getTopslideposts() {
        return topslideposts;
    }

    public void setTopslideposts(NewList topslideposts) {
        this.topslideposts = topslideposts;
    }

    public NewList getNewsposts() {
        return newsposts;
    }

    public void setNewsposts(NewList newsposts) {
        this.newsposts = newsposts;
    }

    public NewList getMoveposts() {
        return moveposts;
    }

    public void setMoveposts(NewList moveposts) {
        this.moveposts = moveposts;
    }

    public NewList getPicposts() {
        return picposts;
    }

    public void setPicposts(NewList picposts) {
        this.picposts = picposts;
    }

    public NewList getTechposts() {
        return techposts;
    }

    public void setTechposts(NewList techposts) {
        this.techposts = techposts;
    }

}


