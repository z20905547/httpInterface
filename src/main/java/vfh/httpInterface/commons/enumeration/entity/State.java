package vfh.httpInterface.commons.enumeration.entity;


import vfh.httpInterface.commons.enumeration.ValueEnum;

/**
 * 状态枚举
 * 
 * @author maurice
 *
 */
public enum State implements ValueEnum<Integer> {
	
	/**
	 * 启用
	 */
	ENABLE(1,"启用"),
	/**
	 * 禁用
	 */
	DISABLE(2,"禁用"),
	/**
	 * 删除
	 */
	DELETE(3,"删除");
	
	//值
	private Integer value;
	//名称
	private String name;
	
	private State(Integer value,String name) {
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
