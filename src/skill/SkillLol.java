package skill;

import javafx.scene.text.Font;
import main.MyController;

public class SkillLol extends Skill{

	MyController myController;

	public SkillLol(int idSkill, int cd, MyController mc) {
		super(idSkill, cd);
		myController = mc;
		myController.getSkillLolImage().setImage(image);
		decreaseCooldown(0);
	}
	
	public void decreaseCooldown(int c) {
		super.decreaseCooldown(c);
		if (getCooldown() != 0) {
			myController.getSkillLolText().setText("lolol " + getCooldown());
			myController.getSkillLolImage().setOpacity(0.5);
		}
		else {
			myController.getSkillLolText().setText("lolol ready");
			myController.getSkillLolImage().setOpacity(1);
		}
	}
	
	public void useSkill() {
		decreaseCooldown(-200);
		if (getCooldown() != 0) {
			myController.getSkillLolImage().setOpacity(0.5);
		}
		else {
			myController.getSkillLolImage().setOpacity(1);
		}
		myController.setCurrentChar(0);
		myController.setWord("lololololololololol");
		myController.getWordText().setFont(new Font(25));
		myController.getWordText().setText(myController.getWord().substring(myController.getCurrentChar()));
	}
	
}
