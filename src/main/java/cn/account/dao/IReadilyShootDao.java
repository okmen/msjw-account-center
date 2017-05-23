package cn.account.dao;

import cn.account.bean.ReadilyShoot;

public interface IReadilyShootDao {

	/**
	 * 违法信息查询
	 * @param recordNumber 记录号码
	 * @param queryPassword 查询密码
	 * @return
	 * @throws Exception
	 */
	public ReadilyShoot queryByReportSerialNumberAndPassword(String reportSerialNumber, String password);
	
	/**
	 * 保存违法举报信息
	 * @param readilyShoot
	 */
	public int saveReadilyShoot(ReadilyShoot readilyShoot);
	
	
}
