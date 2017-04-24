package cn.account.orm;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 须知文档
 * @author Mbenben
 *
 */
public class DocumentationORM {
	private Long id;
	/**
	 * 须知key，注意命名规则
	 */
	private String noticeKey;
	/**
	 * 内容(html代码块)
	 */
	private String content;
	/**
	 * 创建日期
	 */
	private Date addDate;
	/**
	 * 文档标题
	 */
	private String title;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNoticeKey() {
		return noticeKey;
	}
	public void setNoticeKey(String noticeKey) {
		this.noticeKey = noticeKey;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
}
