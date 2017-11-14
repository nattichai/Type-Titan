package skill;

import javafx.scene.image.Image;

public class Skill{
	
	Image image;
	private int cooldown;

	public Skill(int idSkill, int cd) {
		setCooldown(cd);
		image = getSkillImage(idSkill);
	}
	
	public Image getSkillImage(int idSkill){
		image = new Image(ClassLoader.getSystemResource("skill0"+ idSkill + ".png").toString());
		return image;
	}
	
	public void decreaseCooldown(int c) {
		setCooldown(getCooldown() - c);
		if (getCooldown() < 0)
			setCooldown(0);
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

}
