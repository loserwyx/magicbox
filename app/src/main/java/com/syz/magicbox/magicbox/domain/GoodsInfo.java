package com.syz.magicbox.magicbox.domain;

import java.util.List;

import android.graphics.Bitmap;

public class GoodsInfo {
	
	public List<Bitmap> goodsBitmaps;
	public String goodsDes;
	public String goodsTitle;
	public String goodsMaster;
	public String goodsTime;
	public String goodsPice;
	
	public List<Bitmap> getGoodsBitmaps() {
		return goodsBitmaps;
	}
	public void setGoodsBitmaps(List<Bitmap> goodsBitmaps) {
		this.goodsBitmaps = goodsBitmaps;
	}
	public String getGoodsDes() {
		return goodsDes;
	}
	public void setGoodsDes(String goodsDes) {
		this.goodsDes = goodsDes;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public String getGoodsMaster() {
		return goodsMaster;
	}
	public void setGoodsMaster(String goodsMaster) {
		this.goodsMaster = goodsMaster;
	}
	public String getGoodsTime() {
		return goodsTime;
	}
	public void setGoodsTime(String goodsTime) {
		this.goodsTime = goodsTime;
	}
	public String getGoodsPice() {
		return goodsPice;
	}
	public void setGoodsPice(String goodsPice) {
		this.goodsPice = goodsPice;
	}

}
