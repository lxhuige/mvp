package com.lxh.library.modular.imageChoose;

import java.util.List;

/**
 * 相册对象
 *封面
 */
public class ImageBucket
{
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;
	public boolean selected = false;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public List<ImageItem> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageItem> imageList) {
		this.imageList = imageList;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
