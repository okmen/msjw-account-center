package cn.account.service;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings(value="all")
public class Test {
	
	
	/**
	 * 过滤xml中的base64图片字符串
	 * @param base64Str base64字符串
	 * @param replaceNodes 节点list
	 * @return
	 */
	public static String filterBase64(String base64Str,List<String> replaceNodes){
		String returnStr = "";
		for(int i=0;i<replaceNodes.size();i++){
			String replaceNode = replaceNodes.get(i);
			int startIndex = base64Str.indexOf(replaceNode);
			startIndex = startIndex + 1 + replaceNode.length();
			int endIndex = base64Str.lastIndexOf(replaceNode);
			endIndex = endIndex - 2;
			String replaceStr = base64Str.substring(startIndex, endIndex);
			base64Str = base64Str.replace(replaceStr, "照片base64不打印");
			if(i == replaceNodes.size() - 1){
				returnStr = base64Str;
			}
		}
		return returnStr;
	}
	
	
	public static void main(String[] args) {
		String base64Str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><body><hphm>粤B12345</hphm><hpzl>02</hpzl><wfxw1>测试，请忽略</wfxw1><wfxw2></wfxw2><wfxw3></wfxw3><wfdd>62000-4044---深南路-深南大道岗厦路段</wfdd><wfsj>2017-08-31 09:27</wfsj><lrr>测试</lrr><lrrxm>测试</lrrxm><lrrlxdh>17779607572</lrrlxdh><lrly>zfb</lrly><jbtp1>1111111111111111111111111111111111111111111111111111111111111111111111111111111111111</jbtp1><jbtp2>222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222</jbtp2><jbtp3>33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333</jbtp3><sfzmhm>362426199303075211</sfzmhm><alipay_user_id>2088112111655412</alipay_user_id><shsm>1</shsm></body></request>";
		List<String> replaceNodes = new ArrayList<String>();
		replaceNodes.add("jbtp1");
		replaceNodes.add("jbtp2");
		replaceNodes.add("jbtp3");
		
		String returnStr = filterBase64(base64Str, replaceNodes);
		System.out.println(returnStr);
		
		/*String returnStr = "";
		for(int i=0;i<replaceNodes.size();i++){
			String replaceNode = replaceNodes.get(i);
			int startIndex = string.indexOf(replaceNode);
			startIndex = startIndex + 1 + replaceNode.length();
			int endIndex = string.lastIndexOf(replaceNode);
			endIndex = endIndex - 2;
			String replaceStr = string.substring(startIndex, endIndex);
			string = string.replace(replaceStr, "照片base64不打印");
			if(i == replaceNodes.size() - 1){
				returnStr = string;
			}
		}
		System.out.println(returnStr);*/
	}
}
