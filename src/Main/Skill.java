package Main;

import javafx.scene.image.Image;

public class Skill{
	
	Image image;
	int cooldown;

	public Skill(int cd) {
		cooldown = cd;
	}
	
	public Image getSkillImage(int idSkill){
		image = new Image(ClassLoader.getSystemResource("skill0"+ idSkill + ".png").toString());
		return image;
	}
	
	public void decreaseCooldown(int c) {
		cooldown -= c;
		if (cooldown < 0)
			cooldown = 0;
	}

}
