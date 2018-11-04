package com.lxh.library.modular.imageChoose;

import java.io.Serializable;

/**
 * 图片对象
 */

public class ImageItem implements Serializable {
    private static final long serialVersionUID = -7188270558443739436L;
    public String imageId;
    public String thumbnailPath;//缩略图的位置
    public String sourcePath;//资源位置
    public boolean isSelected = false;
    public String cho;//用于记录图片的选择状态
    public String imagePath;

    public String getCho() {//0是没有选中--1是选中了---2是选择做封面
        return cho;
    }

    public void setCho(String cho) {
        this.cho = cho;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "ImageItem [imageId=" + imageId + ", thumbnailPath="
                + thumbnailPath + ", sourcePath=" + sourcePath
                + ", isSelected=" + isSelected + ", cho=" + cho + "]";
    }


}
