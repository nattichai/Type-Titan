package Main;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

public class MyController implements Initializable{
	
//	scene
	@FXML static Pane scene;
//	monster area
	@FXML Text timerText;
	@FXML Text hpText;
	@FXML ProgressBar hpBar;
	@FXML Text stageText;
	@FXML ImageView monsterImage;
	@FXML Button fightBossBtn;
//	word area
	@FXML Text wordText;
	@FXML Rectangle bgWord;
	@FXML Text comboText;
//	tab menu
	@FXML TabPane tabMenu;
//	player area
	@FXML Text levelText;
	@FXML Text dpsText;
	@FXML Text moneyText;
	@FXML Text costText;
	@FXML Text dpsPlusText;
	@FXML Button upgradeBtn;
//	skill area
	@FXML ImageView skill01;
	@FXML ImageView skill02;
	@FXML ImageView skill03;
	@FXML Text skill01Cd;
	@FXML Text skill02Cd;
	@FXML Text skill03Cd;
//	save data
	static String[] save;
//	monster area
	static Monster monster;
	static double monsterMaxHp;
	static boolean canFightBoss;
	static boolean isFightBoss;
	static int stage;
	static int maxStage;
	static int timer;
//	word area
	static String word;
	static int currentChar;
	static int comboCount;
	static boolean comboMiss;
	static int wordMode;
//	player area
	static int level;
	static double baseDps;
	static double dps;
	static double money;
	static boolean canUpgrade;
	static double cost;
	static double dpsPlus;
//	skill area
	static Skill01 skill1;
	static Skill02 skill2;
	static Skill03 skill3;
//	timeline
	static Timeline timeline;
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		try {	loadGame();			} catch (Exception e1) {}
//		monster initialization
		monsterMaxHp = Double.parseDouble(save[0]);
		newMonster();
		setCanFightBoss(Boolean.parseBoolean(save[1]));
		setTimer(20, false);
		setStage(Integer.parseInt(save[3]));
//		word initialization
		wordMode = (Integer.parseInt(save[14]));
		newWord();
		setComboCount(Integer.parseInt(save[5]));
//		player initialization
		addLevel(Integer.parseInt(save[6]));
		addCost(Double.parseDouble(save[9]));
		addDpsPlus(Double.parseDouble(save[10]));
		addDps(Double.parseDouble(save[7]));
		addMoney(Double.parseDouble(save[8]));
//		skill initialization
		skill1 = new Skill01(1, Integer.parseInt(save[11]), this);
		skill2 = new Skill02(2, Integer.parseInt(save[12]), this);
		skill3 = new Skill03(3, Integer.parseInt(save[13]), this);
//		timeline initialization
		timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
			setTimer(timer - 1, true);
			if (timer <= 0) {
				timeline.stop();
				setTimer(20, false);
				setStage(maxStage);
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
	}

	public void input(KeyEvent e) throws Exception{
		if (		(e.getCode() == KeyCode.LEFT && word.charAt(currentChar) == '⇦') ||	//left arrow
				(e.getCode() == KeyCode.RIGHT && word.charAt(currentChar) == '⇨') ||	//right arrow
				(e.getCode() == KeyCode.UP && word.charAt(currentChar) == '⇧') ||	//up arrow
				(e.getCode() == KeyCode.DOWN && word.charAt(currentChar) == '⇩') ||	//down arrow
				(e.getText().equals("" + word.charAt(currentChar))) ||				//a - z
				(e.getCode() == KeyCode.ENTER) ||									//enter (hack)
				(word.charAt(currentChar) == '?')		){							//?
			correct();
		}
		else if (	("a".compareTo(e.getText()) < 0 && "z".compareTo(e.getText()) > 0) ||
					("A".compareTo(e.getText()) < 0 && "Z".compareTo(e.getText()) > 0) ||
					e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.LEFT || 
					e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN ) 	{
			wrong();
		} else if (e.getText().equals(" ") && canUpgrade == true) {
			upgrade();
		} else if (e.getText().equals("1")) {
			useSkill1();
		} else if (e.getText().equals("2")) {
			useSkill2();
		} else if (e.getText().equals("3")) {
			useSkill3();
		}
	}

	public void pressTab() throws AWTException {
		Robot rb = new Robot();
		rb.keyPress(java.awt.event.KeyEvent.VK_TAB);
		rb.keyRelease(java.awt.event.KeyEvent.VK_TAB);
	}

	private void correct() {
		bgWord.setFill(Color.LIGHTGREEN);
		currentChar ++;
		wordText.setText(word.substring(currentChar));
		skill1.decreaseCooldown(1);
		skill2.decreaseCooldown(1);
		skill3.decreaseCooldown(1);
		monster.decreaseHp(dps);
		if (monster.hp <= 0) {
			win();
		}
		if (currentChar >= word.length()) {	//complete word
			if (comboMiss == false) {	//not miss combo
				setComboCount(comboCount + 1);
			}
			newWord();
		}
	}
	
	private void win() {
		addMoney(monster.maxHp);
		if (stage != maxStage) {		//not be last stage
			monsterMaxHp *= 1.5;
		}
		if (isFightBoss == true) {	//win boss
			timeline.stop();
			setCanFightBoss(false);
			setTimer(20, false);
			setStage(1);
		} else {						//win monster
			setStage(stage + 1);
		}
	}

	private void wrong() {
		bgWord.setFill(Color.INDIANRED);
		monster.decreaseHp(-dps / 2);
		dps = baseDps;
		addDps(0);
		setComboCount(0);
		comboMiss = true;
	}
	
	private void upgrade() {
		double c = cost;
		addLevel(1);
		addCost(c * 0.3);
		addMoney(-c);
		addDps(dpsPlus);
		addDpsPlus(dpsPlus * 0.29);
	}
	
	public void newMonster(){
		monster = new Monster(monsterMaxHp, this);
		monsterImage.setFitHeight(100);
		monsterImage.setImage(monster.image);
		setHpBar();
		if (isFightBoss == false)
			stageText.setText(stage + " / " + maxStage);
		else
			stageText.setText("BOSS");
	}
	
	public void setHpBar() {
		hpBar.setProgress(monster.hp / monster.maxHp);
		hpText.setText(doubleToText(monster.hp) + " / " + doubleToText(monster.maxHp));
	}
	
	public void newBoss() {
		if (canFightBoss == false)
			return;
		monsterImage.setFitHeight(150);
		isFightBoss = true;
		monsterMaxHp *= 5;
		newMonster();
		monsterMaxHp /= 5;
		timeline.play();
		setTimer(20, true);
	}

	private void setCanFightBoss(boolean b) {
		canFightBoss = b;
		fightBossBtn.setVisible(b);
		fightBossBtn.setBackground(new Background(new BackgroundFill(Color.DARKSALMON, null, null)));
	}
	
	private void setTimer(int i, boolean b) {
		timer = i;
		timerText.setText("" + timer);
		timerText.setVisible(b);
		isFightBoss = b;
	}

	private void setStage(int s){
		stage = s;
		maxStage = 5;
		if (stage == maxStage + 2) {
			stage = maxStage;
			newBoss();
		} else if (stage > maxStage) {		//last stage
			stage = maxStage;
			if (canFightBoss == false) {		//never fight boss before
				setCanFightBoss(true);
				newBoss();
			} else {
				newMonster();
			}
		} else {		//normal stage
			newMonster();
		}
		if (isFightBoss == false) {
			stageText.setText(stage + " / " + maxStage);
			fightBossBtn.setText("FIGHT BOSS");
			fightBossBtn.setOnMouseClicked(e -> {setStage(maxStage + 2);});
		}
		else {
			stageText.setText("BOSS");
			fightBossBtn.setText("LEAVE BOSS");
			fightBossBtn.setOnMouseClicked(e -> {timer = 0;});
		}
	}

	public void newWord(){
		bgWord.setFill(Color.LIGHTBLUE);
		word = Word.randomWord(this);
		wordText.setText(word);
		currentChar = 0;
		comboMiss = false;
	}
	
	public void wordOnly() {
		wordMode = 1;
		newWord();
	}
	
	public void arrowOnly() {
		wordMode = 2;
		newWord();
	}
	
	public void bothWordAndArrow() {
		wordMode = 3;
		newWord();
	}
	
	public void setComboCount(int c) {
		comboCount = c;
		comboText.setText(c + "\ncombo");
		if (comboCount > 0)
			comboText.setVisible(true);
		else
			comboText.setVisible(false);
		if (comboCount == 1)
			baseDps = dps;
		addDps(0);
	}
	
	public void addLevel(int l) {
		level += l;
		levelText.setText("Level : " + level);
	}
	
	public void addDps(double d) {
		baseDps += d;
		dps = baseDps * (1 + 0.1 * comboCount);
		dpsText.setText(doubleToText(dps));
	}
	
	public void addMoney(double m) {
		money += m;
		moneyText.setText(doubleToText(money));
		if (money >= cost) {
			upgradeBtn.setBackground(new Background(new BackgroundFill(Color.GOLD, null, null)));
			canUpgrade = true;
		} else {
			upgradeBtn.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGRAY, null, null)));
			canUpgrade = false;
		}
	}
	
	public void addCost(double c) {
		cost += c;
		costText.setText(doubleToText(cost));
	}
	
	public void addDpsPlus(double d) {
		dpsPlus += d;
		dpsPlusText.setText(doubleToText(dpsPlus));
	}
	
	public String doubleToText(double d) {
		int count = 0;
		String number;
		String unit = "";
		while (d >= 1000) {
			d /= 1000;
			count ++;
		}
		number = String.format("%.2f ", d);
		if (count == 0) unit = "";
		else if (count == 1) unit = "K";
		else if (count == 2) unit = "M";
		else if (count == 3) unit = "B";
		else if (count == 4) unit = "T";
		else unit = ("" + (char)(97 + (count-5) / 26)) + ("" + (char)(97 + (count-5)));
		return number + unit;
	}
	
	public void useSkill1() {
		if (skill1.cooldown == 0)
			skill1.useSkill();
	}
	
	public void useSkill2() {
		if (skill2.cooldown == 0)
			skill2.useSkill();
	}
	
	public void useSkill3() {
		if (skill3.cooldown == 0)
			skill3.useSkill();
	}
	
	public void newGame() {
//		monster reset
		monsterMaxHp = 50;
		newMonster();
		setCanFightBoss(false);
		setTimer(20, false);
		setStage(1);
//		word reset
		wordMode = 3;
		newWord();
		setComboCount(0);
//		player reset
		addLevel(-level + 1);
		addCost(-cost + 100);
		addDpsPlus(-dpsPlus + 1);
		addDps(-dps + 10);
		addMoney(-money);
//		skill reset
		skill1 = new Skill01(1, 100, this);
		skill2 = new Skill02(2, 200, this);
		skill3 = new Skill03(3, 300, this);
//		timeline reset
		timeline.stop();
	}
	
	@SuppressWarnings("resource")
	public static void loadGame() throws Exception {
		File file = new File("save.txt");
		FileReader reader = null;
		char[] r = new char[1000];
		int cnt = 0;
		file.createNewFile();
		reader = new FileReader(file);
		reader.read(r);
		
		save = new String[50];
		for (int i = 0; i < save.length; i++) {
			save[i] = new String();
		}
		for (char i : r) {
			if (i == '\n') {
				cnt ++;
			} else {
				save[cnt] += i;
			}
		}
	}
	
	public static void saveGame() throws Exception {
		File file = new File("save.txt");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write(	monsterMaxHp + "\n" +
						canFightBoss + "\n" +
						isFightBoss + "\n" +
						stage + "\n" +
						timer + "\n" +
						comboCount + "\n" +
						level + "\n" +
						baseDps + "\n" +
						money + "\n" +
						cost + "\n" +
						dpsPlus + "\n" +
						skill1.cooldown + "\n" +
						skill2.cooldown + "\n" +
						skill3.cooldown + "\n" +
						wordMode + "\n"		);
		writer.flush();
		writer.close();
	}

}