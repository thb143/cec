package cn.mopon.cec.core.fantasy;

public class FanEntity {
	@FanAnnotations
	private String name;

	public String getName() {
		return name;
	}
	
	@FanAnnotations(name = "fan")
	public void setName(String name) {
		this.name = name;
	}
}
