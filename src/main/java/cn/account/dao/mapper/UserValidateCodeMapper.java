package cn.account.dao.mapper;

import org.springframework.stereotype.Repository;

import cn.account.bean.UserBind;
import cn.account.bean.po.UserValidateCodePo;

@Repository
public interface UserValidateCodeMapper {
	/**
	 * 添加验证码
	 * @param userValidateCodePo
	 * @return
	 */
    public int addUserValidateCode(UserValidateCodePo userValidateCodePo);
    /**
     * 查询验证码
     * @param mobilephone
     * @return
     */
	public UserBind getUserValidateCodeByMobilephone(String mobilephone);
}
