package Main;

import javafx.scene.text.Font;

public class Skill03 extends Skill{

	MyController myController;

	public Skill03(int idSkill, int cd, MyController mc) {
		super(idSkill, cd);
		myController = mc;
		myController.skill03.setImage(image);
		decreaseCooldown(0);
	}
	
	public void decreaseCooldown(int c) {
		super.decreaseCooldown(c);
		if (cooldown != 0) {
			myController.skill03Cd.setText("???? " + cooldown);
			myController.skill03.setOpacity(0.5);
		}
		else {
			myController.skill03Cd.setText("???? ready");
			myController.skill03.setOpacity(1);
		}
	}
	
	public void useSkill() {
		decreaseCooldown(-300);
		if (cooldown != 0) {
			myController.skill03.setOpacity(0.5);
		}
		else {
			myController.skill03.setOpacity(1);
		}
		MyController.currentChar = 0;
		MyController.word = "??????????????????????";
		myController.wordText.setFont(new Font(25));
		myController.wordText.setText(MyController.word.substring(MyController.currentChar));
	}
	
}
