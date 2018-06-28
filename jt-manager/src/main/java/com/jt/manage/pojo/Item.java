package com.jt.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;

@JsonIgnoreProperties(ignoreUnknown=true)
@Table(name="tb_item")
public class Item extends BasePojo {
	private static final long serialVersionUID = 2918636701220453178L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;		// 商品ID号
	private String title;	// 商品标题
	private String sellPoint;// 商品卖点信息
	private Long price;		// 价格，计算速度快
	private Integer num;	// 商品的数量
	private String barcode;	// 二维码
	private String image;	// 商品图片信息
	private Long cid;		// 商品的分类ID
	private Integer status;	// 商品的状态: 1-默认；2-
	// 为了满足页面图片展现添加该方法
	public String[] getImages() {
		return image.split(",");
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
