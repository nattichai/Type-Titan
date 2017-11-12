package Main;

import javafx.scene.text.Font;

public class Skill01 extends Skill{
	
	MyController myController;

	public Skill01(int idSkill, int cd, MyController mc) {
		super(cd);
		myController = mc;
		decreaseCooldown(0);
	}

	public void decreaseCooldown(int c) {
		super.decreaseCooldown(c);
		if (cooldown != 0) {
			myController.skill01Cd.setText("hello " + cooldown);
			myController.skill01.setOpacity(0.5);
		}
		else {
			myController.skill01Cd.setText("hello ready");
			myController.skill01.setOpacity(1);
		}
	}
	
	public void useSkill() {
		decreaseCooldown(-100);
		if (cooldown != 0) {
			myController.skill01.setOpacity(0.5);
		}
		else {
			myController.skill01.setOpacity(1);
		}
		MyController.currentChar = 0;
		MyController.word = "hellohellohello";
		myController.wordText.setFont(new Font(25));
		myController.wordText.setText(MyController.word.substring(MyController.currentChar));
	}
	
}
