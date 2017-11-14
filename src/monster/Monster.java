package monster;

import java.util.Random;

import javafx.animation.*;
import javafx.scene.image.*;
import javafx.util.Duration;
import main.MyController;

public class Monster {
	
	private Image image;
	private double maxHp;
	private double hp;
	MyController myController;

	public Monster(double x, MyController mc){
		setImage(randomMonster());
		setMaxHp(x);
		setHp(getMaxHp());
		myController = mc;
	}

	public Image randomMonster(){
		int rnd = new Random().nextInt(4) + 1;
		setImage(new Image(ClassLoader.getSystemResource("monster0" + rnd + ".png").toString()));
		return getImage();
	}

	public void decreaseHp(double d) {
		setHp(getHp() - d);
		if (getHp() >= getMaxHp())
			setHp(getMaxHp());
		myController.getMonsterImage().setOpacity(0.5);
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
			myController.getMonsterImage().setOpacity(1);
		}));
		timeline.setCycleCount(1);
		timeline.play();
		myController.setHpBar();
	}

	public double getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(double maxHp) {
		this.maxHp = maxHp;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}
	
}
