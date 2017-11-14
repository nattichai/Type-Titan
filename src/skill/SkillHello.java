package skill;

import javafx.scene.text.Font;
import main.MyController;

public class SkillHello extends Skill{
	
	MyController myController;

	public SkillHello(int idSkill, int cd, MyController mc) {
		super(idSkill, cd);
		myController = mc;
		myController.getSkillHelloImage().setImage(image);
		decreaseCooldown(0);
	}

	public void decreaseCooldown(int c) {
		super.decreaseCooldown(c);
		if (getCooldown() != 0) {
			myController.getSkillHelloText().setText("hello " + getCooldown());
			myController.getSkillHelloImage().setOpacity(0.5);
		}
		else {
			myController.getSkillHelloText().setText("hello ready");
			myController.getSkillHelloImage().setOpacity(1);
		}
	}
	
	public void useSkill() {
		decreaseCooldown(-100);
		if (getCooldown() != 0) {
			myController.getSkillHelloImage().setOpacity(0.5);
		}
		else {
			myController.getSkillHelloImage().setOpacity(1);
		}
		myController.setCurrentChar(0);
		myController.setWord("hellohellohello");
		myController.getWordText().setFont(new Font(25));
		myController.getWordText().setText(myController.getWord().substring(myController.getCurrentChar()));
	}
	
}
