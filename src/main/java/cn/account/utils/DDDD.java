package cn.account.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DDDD {
	public static void main(String[] args) {
		List<String> filterImgNode = new ArrayList<>();
		filterImgNode.add("CZSFZMHMTPA");
		filterImgNode.add("CZSFZMHMTP");
		filterImgNode.add("qt_tp");
		filterImgNode.add("zp");
		filterImgNode.add("PHOTO6");
		filterImgNode.add("PHOTO9");
		filterImgNode.add("jbtp1");
		filterImgNode.add("jbtp2");
		filterImgNode.add("jbtp3");
		
		
		String imgMsg = "Base64图片太长不打印";
		
		String xx = "<LRIP>192.168.2.219</LRIP><BIND_DEPARTMENT>C</BIND_DEPARTMENT><CZSFZMHMTPA>字符串1</CZSFZMHMTPA><CZSFZMHMTP>字符串2</CZSFZMHMTP></CZSFZMHMTP>";
		
		//xxcj10
		String CZSFZMHMTPA = StringUtils.substringBetween(xx, "<CZSFZMHMTPA>", "</CZSFZMHMTPA>");
		String CZSFZMHMTP = StringUtils.substringBetween(xx, "<CZSFZMHMTP>", "</CZSFZMHMTP>");
		
		String sdfd = xx.replace(CZSFZMHMTPA, imgMsg);
		sdfd = sdfd.replace(CZSFZMHMTP, imgMsg);
		System.out.println(sdfd);
		
		//exam001
		String qt_tp = StringUtils.substringBetween(xx, "<qt_tp>", "</qt_tp>");
		
		//HM1001
		String zp = StringUtils.substringBetween(xx, "<zp>", "</zp>");
		String string = xx.replace(CZSFZMHMTPA, imgMsg);
		
		//xxcj05
		String PHOTO6 = StringUtils.substringBetween(xx, "<PHOTO6>", "</PHOTO6>");
		String PHOTO9 = StringUtils.substringBetween(xx, "<PHOTO9>", "</PHOTO9>");
		
		
		//9.6接口  1003
		String jbtp1 = StringUtils.substringBetween(xx, "<jbtp1>", "</jbtp1>");
		String jbtp2 = StringUtils.substringBetween(xx, "<jbtp2>", "</jbtp2>");
		String jbtp3 = StringUtils.substringBetween(xx, "<jbtp3>", "</jbtp3>");
		
		
		//xxcj15
		String xxcj15_PHOTO6 = StringUtils.substringBetween(xx, "<PHOTO6>", "</PHOTO6>");
		String xxcj15_PHOTO9 = StringUtils.substringBetween(xx, "<PHOTO9>", "</PHOTO9>");
		
		
		//xxcjzrr
		String xxcjzrr_PHOTO6 = StringUtils.substringBetween(xx, "<PHOTO6>", "</PHOTO6>");
		String xxcjzrr_PHOTO9 = StringUtils.substringBetween(xx, "<PHOTO9>", "</PHOTO9>");
		
	}
}
