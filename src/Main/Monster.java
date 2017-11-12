package Main;

import java.util.Random;

import javafx.animation.*;
import javafx.scene.image.*;
import javafx.util.Duration;

public class Monster {
	
	Image image;
	double maxHp;
	double hp;
	MyController myController;

	public Monster(double x, MyController mc){
		image = randomMonster();
		maxHp = x;
		hp = maxHp;
		myController = mc;
	}

	public Image randomMonster(){
		int rnd = new Random().nextInt(4) + 1;
		image = new Image(ClassLoader.getSystemResource("monster0" + rnd + ".png").toString());
		return image;
	}

	public void decreaseHp(double d) {
		hp -= d;
		if (hp >= maxHp)
			hp = maxHp;
		myController.monsterImage.setOpacity(0.5);
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
			myController.monsterImage.setOpacity(1);
		}));
		timeline.setCycleCount(1);
		timeline.play();
		myController.setHpBar();
	}
	
}
