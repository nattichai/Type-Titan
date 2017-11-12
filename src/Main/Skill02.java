package Main;

import javafx.scene.text.Font;

public class Skill02 extends Skill{

	MyController myController;

	public Skill02(int idSkill, int cd, MyController mc) {
		super(cd);
		myController = mc;
		decreaseCooldown(0);
	}
	
	public void decreaseCooldown(int c) {
		super.decreaseCooldown(c);
		if (cooldown != 0) {
			myController.skill02Cd.setText("lolol " + cooldown);
			myController.skill02.setOpacity(0.5);
		}
		else {
			myController.skill02Cd.setText("lolol ready");
			myController.skill02.setOpacity(1);
		}
	}
	
	public void useSkill() {
		decreaseCooldown(-200);
		if (cooldown != 0) {
			myController.skill02.setOpacity(0.5);
		}
		else {
			myController.skill02.setOpacity(1);
		}
		MyController.currentChar = 0;
		MyController.word = "lololololololololol";
		myController.wordText.setFont(new Font(25));
		myController.wordText.setText(MyController.word.substring(MyController.currentChar));
	}
	
}
