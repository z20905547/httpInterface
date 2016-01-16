package vfh.httpInterface.commons.enumeration.entity;


import vfh.httpInterface.commons.enumeration.ValueEnum;

/**
 * 楼盘状态枚举
 * 
 * @author maurice
 *
 */
public enum BuildingsState implements ValueEnum<Integer> {
	
	EDITING(0,"编辑中"),
	WAITTING(1,"等待审核"),
	PASS(2,"审核通过"),
	FAILD(3,"未通过审核"),
	USEING(4,"已发布"),
	ACTIVIT(4,"活动中"),
	DOWN(4,"下架");
	
	//值
	private Integer value;
	//名称
	private String name;
	
	private BuildingsState(Integer value,String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取值
	 * 
	 * @return Integer
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 获取名称
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
}
