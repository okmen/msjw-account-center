package cn.account.orm;

import java.io.Serializable;
import java.util.Date;
/**
 * 用户短信验证码
 * @author Mbenben
 *
 */
public class UserValidateCodeORM implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String mobilephone;
	
	private String validateCode;
	
	private Date genDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public Date getGenDate() {
		return genDate;
	}
	public void setGenDate(Date genDate) {
		this.genDate = genDate;
	}
	
}
