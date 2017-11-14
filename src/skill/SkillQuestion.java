package skill;

import javafx.scene.text.Font;
import main.MyController;

public class SkillQuestion extends Skill{

	MyController myController;

	public SkillQuestion(int idSkill, int cd, MyController mc) {
		super(idSkill, cd);
		myController = mc;
		myController.getSkillQuestionImage().setImage(image);
		decreaseCooldown(0);
	}
	
	public void decreaseCooldown(int c) {
		super.decreaseCooldown(c);
		if (getCooldown() != 0) {
			myController.getSkillQuestionText().setText("???? " + getCooldown());
			myController.getSkillQuestionImage().setOpacity(0.5);
		}
		else {
			myController.getSkillQuestionText().setText("???? ready");
			myController.getSkillQuestionImage().setOpacity(1);
		}
	}
	
	public void useSkill() {
		decreaseCooldown(-300);
		if (getCooldown() != 0) {
			myController.getSkillQuestionImage().setOpacity(0.5);
		}
		else {
			myController.getSkillQuestionImage().setOpacity(1);
		}
		myController.setCurrentChar(0);
		myController.setWord("??????????????????????");
		myController.getWordText().setFont(new Font(25));
		myController.getWordText().setText(myController.getWord().substring(myController.getCurrentChar()));
	}
	
}
