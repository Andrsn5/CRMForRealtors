package com.company.crm.model;

public class Photo {
    private Integer id;
    private String photoUrl;
    private Integer displayOrder;
    private String caption;
    private Integer objectId;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }@Override
    public String toString() {
        return String.format("Photo{id=%d, url='%s', order=%d, caption='%s', objectId=%d}",
                id, photoUrl, displayOrder, caption, objectId);
    }
}