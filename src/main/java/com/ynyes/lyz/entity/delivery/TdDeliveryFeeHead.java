package com.ynyes.lyz.entity.delivery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "td_delivery_fee_head")
public class TdDeliveryFeeHead {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 10, nullable = false)
	private Long sobId;

	@Column(length = 20, nullable = false)
	private Long goodsId;

	@Column(length = 40, nullable = false)
	private String goodsTitle;
	
	@Column(length = 20, nullable = false)
	private String goodsSku;

	// 商品大分类名称
	@Column(length = 100, nullable = false)
	private String goodsType;

	// 商品大分类ID(1：大桶内外墙漆 2：硝基漆 3：小桶/木器漆 4：4kg以下漆类)
	@Column(length = 10, nullable = false)
	private Integer goodsTypeId;

	// 运费承担对象
	@Column(length = 100, nullable = false)
	private String assumedObject;

	// 运费承担对象ID (1、客户    2、公司)
	@Column(length = 10, nullable = false)
	private Integer assumedObjectId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSobId() {
		return sobId;
	}

	public void setSobId(Long sobId) {
		this.sobId = sobId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(String goodsSku) {
		this.goodsSku = goodsSku;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public Integer getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(Integer goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}
	
	

	public String getAssumedObject() {
		return assumedObject;
	}

	public void setAssumedObject(String assumedObject) {
		this.assumedObject = assumedObject;
	}

	public Integer getAssumedObjectId() {
		return assumedObjectId;
	}

	public void setAssumedObjectId(Integer assumedObjectId) {
		this.assumedObjectId = assumedObjectId;
	}

	@Override
	public String toString() {
		return "TdDeliveryFeeHead [id=" + id + ", sobId=" + sobId + ", goodsId=" + goodsId + ", goodsTitle="
				+ goodsTitle + ", goodsSku=" + goodsSku + ", goodsType=" + goodsType + ", goodsTypeId=" + goodsTypeId
				+ ", assumedObject=" + assumedObject + ", assumedObjectId=" + assumedObjectId + "]";
	}

	
	
}
