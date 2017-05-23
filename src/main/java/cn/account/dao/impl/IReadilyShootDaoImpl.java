package cn.account.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.bean.ReadilyShoot;
import cn.account.dao.IReadilyShootDao;
import cn.account.dao.mapper.ReadilyShootMapper;


@Repository
public class IReadilyShootDaoImpl implements IReadilyShootDao {

	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	
	private ReadilyShootMapper readilyShootMapper;
	@Override
	
	public ReadilyShoot queryByReportSerialNumberAndPassword(String reportSerialNumber, String password) {
		
		return readilyShootMapper.queryByReportSerialNumberAndPassword(reportSerialNumber, password);
	}
	
	@Override
	public int saveReadilyShoot(ReadilyShoot readilyShoot) {
		return readilyShootMapper.saveReadilyShoot(readilyShoot);
	};
	
	

}
