package com.ynyes.lyz.entity.user;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.ynyes.lyz.annotation.AppManyToOne;
import com.ynyes.lyz.entity.TdCity;
import com.ynyes.lyz.entity.TdDiySite;
import com.ynyes.lyz.entity.TdShippingAddress;

@Entity
@Table(name = "td_user")
public class TdUser {
	
	public TdUser() {
		super();
	}
	
	public TdUser(Long id, List<TdShippingAddress> shippingAddressList, Double balance, String cityName, String diyName,
			String headImageUri, Boolean isEnable, Date lastLoginTime, String password, String realName,
			String referPhone, Date registerTime, String sex, Long upperDiySiteId, Long userType, String username,
			Double cashBalance, Double unCashBalance, Long cityId, Double sortId, Date birthday, Long customerId,
			String opUser, Boolean isLogin, Date lastVisitTime, String loginSession, Long sellerId, String sellerName,
			String diyCode, Boolean isCashOnDelivery, Integer identityType) {
		super();
		this.id = id;
		this.shippingAddressList = shippingAddressList;
		this.balance = balance;
		this.cityName = cityName;
		this.diyName = diyName;
		this.headImageUri = headImageUri;
		this.isEnable = isEnable;
		this.lastLoginTime = lastLoginTime;
		this.password = password;
		this.realName = realName;
		this.referPhone = referPhone;
		this.registerTime = registerTime;
		this.sex = sex;
		this.upperDiySiteId = upperDiySiteId;
		this.userType = userType;
		this.username = username;
		this.cashBalance = cashBalance;
		this.unCashBalance = unCashBalance;
		this.cityId = cityId;
		this.sortId = sortId;
		this.birthday = birthday;
		this.customerId = customerId;
		this.opUser = opUser;
		this.isLogin = isLogin;
		this.lastVisitTime = lastVisitTime;
		this.loginSession = loginSession;
		this.sellerId = sellerId;
		this.sellerName = sellerName;
		this.diyCode = diyCode;
		this.isCashOnDelivery = isCashOnDelivery;
		this.identityType = identityType;
	}

	// 自增主键
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// 收货地址
	@OneToMany
	@JoinColumn(name = "userId")
	private List<TdShippingAddress> shippingAddressList;
	
	// 用户总余额
	@Column(length = 20, scale = 2, nullable = false)
	private Double balance;
	
	// 所属城市名
	@AppManyToOne(target = TdCity.class, field = "cityName")
	@Column(length = 10, nullable = false)
	private String cityName;
	
	// 所属门店名
	@AppManyToOne(target = TdDiySite.class, field = "title")
	@Column(length = 15, nullable = false)
	private String diyName;
	
	// 用户头像路径
	@Column(length = 50, nullable = false)
	private String headImageUri;
	
	// 是否启用
	@Column(length = 1, nullable = false)
	private Boolean isEnable;
	
	// 上一次登录时间
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;
	
	// 密码
	@Column(length = 32, nullable = false)
	private String password;
	
	// 用户真实姓名
	@Column(length = 10, nullable = false)
	private String realName;
	
	// 用户推荐人电话号码
	@Column(length = 11)
	private String referPhone;
	
	// 注册时间
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;
	
	// 性别，默认为保密
	@Column(length = 2, nullable = false)
	private String sex = "保密";
	
	// 用户所属门店id
	@AppManyToOne(target = TdDiySite.class, field = "id")
	@Column(length = 20, nullable = false)
	private Long upperDiySiteId;
	
	// 用户类型：0代表会员，1代表销售顾问，2代表店长，3代表店主，4代表区域经理,5代表配送员，默认为0， 会员
	@Column(length = 1, nullable = false)
	private Long userType = 0l;
	
	// 登录用户名
	@Column(length = 11, nullable = false, unique = true)
	private String username;
	
	// 用户可提现余额
	@Column(length = 20, scale = 2, nullable = false)
	private Double cashBalance;
	
	// 用户不可提现余额
	@Column(length = 20, scale = 2, nullable =false)
	private Double unCashBalance;
	
	// 用户城市id，实际上是引用EBS提供的SOB_ID
	@AppManyToOne(target = TdCity.class, field = "sobIdCity")
	@Column(length = 10, nullable = false)
	private Long cityId;
	
	// 排序号，默认为99.9
	@Column(length = 20, scale = 2, nullable = false)
	private Double sortId = 99.9d;
	
	// 用户生日
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;
	
	// 客户id，由EBS提供
	@AppManyToOne(target = TdDiySite.class, field = "customerId")
	@Column(length = 10, nullable = false)
	private Long customerId;
	
	// 配送员编码
	@Column(length = 10)
	private String opUser;
	
	// 当前是否登录，默认未登录
	@Column(length = 1, nullable = false)
	private Boolean isLogin;
	
	// 最后一次访问时间
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastVisitTime;
	
	// 登录sessionId
	@Column(length = 50)
	private String loginSession;
	
	// 所属销顾id
	@AppManyToOne(target = TdUser.class, field = "id")
	@Column(length = 20)
	private Long sellerId;
	
	// 所属销顾姓名
	@AppManyToOne(target = TdUser.class, field = "realName")
	@Column(length = 10)
	private String sellerName;
	
	// 所属门店编码
	@AppManyToOne(target = TdDiySite.class, field = "storeCode")
	@Column(length = 10, nullable = false)
	private String diyCode;
	
	// 是否开启货到付款
	@Column(length = 1, nullable = false)
	private Boolean isCashOnDelivery;
	
	// 身份类型：0、会员；1、非会员；默认是会员
	@Column(length = 1, nullable = false)
	private Integer identityType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TdShippingAddress> getShippingAddressList() {
		return shippingAddressList;
	}

	public void setShippingAddressList(List<TdShippingAddress> shippingAddressList) {
		this.shippingAddressList = shippingAddressList;
	}

	// 获取总额时计算可提现余额和不可提现余额的总和
	public Double getBalance() {
		this.setBalance(this.cashBalance + this.unCashBalance);
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDiyName() {
		return diyName;
	}

	public void setDiyName(String diyName) {
		this.diyName = diyName;
	}

	public String getHeadImageUri() {
		return headImageUri;
	}

	public void setHeadImageUri(String headImageUri) {
		this.headImageUri = headImageUri;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getReferPhone() {
		return referPhone;
	}

	public void setReferPhone(String referPhone) {
		this.referPhone = referPhone;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Long getUpperDiySiteId() {
		return upperDiySiteId;
	}

	public void setUpperDiySiteId(Long upperDiySiteId) {
		this.upperDiySiteId = upperDiySiteId;
	}

	public Long getUserType() {
		return userType;
	}

	public void setUserType(Long userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(Double cashBalance) {
		this.cashBalance = cashBalance;
	}

	public Double getUnCashBalance() {
		return unCashBalance;
	}

	public void setUnCashBalance(Double unCashBalance) {
		this.unCashBalance = unCashBalance;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Double getSortId() {
		return sortId;
	}

	public void setSortId(Double sortId) {
		this.sortId = sortId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getOpUser() {
		return opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	public Date getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public String getLoginSession() {
		return loginSession;
	}

	public void setLoginSession(String loginSession) {
		this.loginSession = loginSession;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getDiyCode() {
		return diyCode;
	}

	public void setDiyCode(String diyCode) {
		this.diyCode = diyCode;
	}

	public Boolean getIsCashOnDelivery() {
		return isCashOnDelivery;
	}

	public void setIsCashOnDelivery(Boolean isCashOnDelivery) {
		this.isCashOnDelivery = isCashOnDelivery;
	}

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	@Override
	public String toString() {
		return "TdUser [id=" + id + ", shippingAddressList=" + shippingAddressList + ", balance=" + balance
				+ ", cityName=" + cityName + ", diyName=" + diyName + ", headImageUri=" + headImageUri + ", isEnable="
				+ isEnable + ", lastLoginTime=" + lastLoginTime + ", password=" + password + ", realName=" + realName
				+ ", referPhone=" + referPhone + ", registerTime=" + registerTime + ", sex=" + sex + ", upperDiySiteId="
				+ upperDiySiteId + ", userType=" + userType + ", username=" + username + ", cashBalance=" + cashBalance
				+ ", unCashBalance=" + unCashBalance + ", cityId=" + cityId + ", sortId=" + sortId + ", birthday="
				+ birthday + ", customerId=" + customerId + ", opUser=" + opUser + ", isLogin=" + isLogin
				+ ", lastVisitTime=" + lastVisitTime + ", loginSession=" + loginSession + ", sellerId=" + sellerId
				+ ", sellerName=" + sellerName + ", diyCode=" + diyCode + ", isCashOnDelivery=" + isCashOnDelivery
				+ ", identityType=" + identityType + "]";
	}
}
