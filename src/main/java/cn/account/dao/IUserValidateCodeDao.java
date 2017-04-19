package cn.account.dao;

import cn.account.bean.UserBind;
import cn.account.orm.UserValidateCodeORM;

public interface IUserValidateCodeDao {
	/**
	 * 添加验证码
	 * @param userValidateCodePo
	 * @return
	 */
    public int addUserValidateCode(UserValidateCodeORM userValidateCodePo);
    /**
     * 查询验证码
     * @param mobilephone
     * @return
     */
	public UserBind getUserValidateCodeByMobilephone(String mobilephone);
}
