package cn.account.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.account.bean.ReadilyShoot;


@Repository
public interface ReadilyShootMapper {

	public ReadilyShoot queryByReportSerialNumberAndPassword(@Param("reportSerialNumber")String reportSerialNumber, @Param("password")String password);

	public int saveReadilyShoot(ReadilyShoot readilyShoot);
	
}
