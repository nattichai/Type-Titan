package main;

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

import monster.Monster;
import skill.*;
import word.Word;

public class MyController implements Initializable{
	
//	scene
	@FXML private static Pane scene;
	@FXML private ImageView settingImage;
//	monster area
	@FXML private Text timerText;
	@FXML private Text hpText;
	@FXML private ProgressBar hpBar;
	@FXML private Text stageText;
	@FXML private ImageView monsterImage;
	@FXML private Button fightBossBtn;
//	word area
	@FXML private Text wordText;
	@FXML private Rectangle bgWord;
	@FXML private Text comboText;
//	tab menu
	@FXML private TabPane tabMenu;
//	player area
	@FXML private Text levelText;
	@FXML private Text dpsText;
	@FXML private Text moneyText;
	@FXML private Text costText;
	@FXML private Text dpsPlusText;
	@FXML private Button upgradeBtn;
//	skill area
	@FXML private ImageView skillHelloImage;
	@FXML private ImageView skillLolImage;
	@FXML private ImageView skillQuestionImage;
	@FXML private Text skillHelloText;
	@FXML private Text skillLolText;
	@FXML private Text skillQuestionText;
	
//	save data
	private static String[] save;
	private static int cnt;
//	monster area
	private static Monster monster;
	private static double monsterMaxHp;
	private static boolean canFightBoss;
	private static boolean isFightBoss;
	private static int stage;
	private static int maxStage;
	private static int timer;
//	word area
	private static Word words;
	private static String word;
	private static int currentChar;
	private static int comboCount;
	private static boolean comboMiss;
	private static int wordMode;
//	player area
	private static int level;
	private static double baseDps;
	private static double dps;
	private static double money;
	private static boolean canUpgrade;
	private static double cost;
	private static double dpsPlus;
//	skill area
	private static SkillHello skillHello;
	private static SkillLol skillLol;
	private static SkillQuestion skillQuestion;
//	timeline
	private static Timeline timeline;
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		try {	loadGame();			} catch (Exception e1) {}
		if (cnt < 15) {	//save not found
			newGame();
			return;
		}
//		setting initialization
		settingImage.setImage(new Image(ClassLoader.getSystemResource("setting.png").toString()));
//		monster initialization
		monsterMaxHp = Double.parseDouble(save[0]);
		newMonster();
		setCanFightBoss(Boolean.parseBoolean(save[1]));
		setTimer(20, false);
		setStage(Integer.parseInt(save[3]));
//		word initialization
		words = new Word();
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
		skillHello = new SkillHello(1, Integer.parseInt(save[11]), this);
		skillLol = new SkillLol(2, Integer.parseInt(save[12]), this);
		skillQuestion = new SkillQuestion(3, Integer.parseInt(save[13]), this);
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
		skillHello.decreaseCooldown(1);
		skillLol.decreaseCooldown(1);
		skillQuestion.decreaseCooldown(1);
		monster.decreaseHp(dps);
		if (monster.getHp() <= 0) {
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
		addMoney(monster.getMaxHp());
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
		monsterImage.setImage(monster.getImage());
		setHpBar();
		if (isFightBoss == false)
			stageText.setText(stage + " / " + maxStage);
		else
			stageText.setText("BOSS");
	}
	
	public void setHpBar() {
		hpBar.setProgress(monster.getHp() / monster.getMaxHp());
		hpText.setText(doubleToText(monster.getHp()) + " / " + doubleToText(monster.getMaxHp()));
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
		word = words.randomWord(this);
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
		if (skillHello.getCooldown() == 0)
			skillHello.useSkill();
	}
	
	public void useSkill2() {
		if (skillLol.getCooldown() == 0)
			skillLol.useSkill();
	}
	
	public void useSkill3() {
		if (skillQuestion.getCooldown() == 0)
			skillQuestion.useSkill();
	}
	
	public void newGame() {
//		setting reset
		settingImage.setImage(new Image(ClassLoader.getSystemResource("setting.png").toString()));
//		monster reset
		monsterMaxHp = 50;
		newMonster();
		setCanFightBoss(false);
		setTimer(20, false);
		setStage(1);
//		word reset
		words = new Word();
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
		skillHello = new SkillHello(1, 100, this);
		skillLol = new SkillLol(2, 200, this);
		skillQuestion = new SkillQuestion(3, 300, this);
//		timeline reset
		timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
			setTimer(timer - 1, true);
			if (timer <= 0) {
				timeline.stop();
				setTimer(20, false);
				setStage(maxStage);
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.stop();
	}
	
	@SuppressWarnings("resource")
	public void loadGame() throws Exception {
		File file = new File("save.txt");
		FileReader reader = null;
		char[] r = new char[1000];
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
						skillHello.getCooldown() + "\n" +
						skillLol.getCooldown() + "\n" +
						skillQuestion.getCooldown() + "\n" +
						wordMode + "\n"		);
		writer.flush();
		writer.close();
	}
	
	public static Pane getScene() {
		return scene;
	}

	public ImageView getSettingImage() {
		return settingImage;
	}

	public Text getTimerText() {
		return timerText;
	}

	public Text getHpText() {
		return hpText;
	}

	public ProgressBar getHpBar() {
		return hpBar;
	}

	public Text getStageText() {
		return stageText;
	}

	public ImageView getMonsterImage() {
		return monsterImage;
	}

	public Button getFightBossBtn() {
		return fightBossBtn;
	}

	public Text getWordText() {
		return wordText;
	}

	public Rectangle getBgWord() {
		return bgWord;
	}

	public Text getComboText() {
		return comboText;
	}

	public TabPane getTabMenu() {
		return tabMenu;
	}

	public Text getLevelText() {
		return levelText;
	}

	public Text getDpsText() {
		return dpsText;
	}

	public Text getMoneyText() {
		return moneyText;
	}

	public Text getCostText() {
		return costText;
	}

	public Text getDpsPlusText() {
		return dpsPlusText;
	}

	public Button getUpgradeBtn() {
		return upgradeBtn;
	}

	public ImageView getSkillHelloImage() {
		return skillHelloImage;
	}

	public ImageView getSkillLolImage() {
		return skillLolImage;
	}

	public ImageView getSkillQuestionImage() {
		return skillQuestionImage;
	}

	public Text getSkillHelloText() {
		return skillHelloText;
	}

	public Text getSkillLolText() {
		return skillLolText;
	}

	public Text getSkillQuestionText() {
		return skillQuestionText;
	}

	public static String[] getSave() {
		return save;
	}

	public static int getCnt() {
		return cnt;
	}

	public static Monster getMonster() {
		return monster;
	}

	public static double getMonsterMaxHp() {
		return monsterMaxHp;
	}

	public static boolean isCanFightBoss() {
		return canFightBoss;
	}

	public static boolean isFightBoss() {
		return isFightBoss;
	}

	public static int getStage() {
		return stage;
	}

	public static int getMaxStage() {
		return maxStage;
	}

	public static int getTimer() {
		return timer;
	}

	public static Word getWords() {
		return words;
	}

	public String getWord() {
		return word;
	}

	public int getCurrentChar() {
		return currentChar;
	}

	public static int getComboCount() {
		return comboCount;
	}

	public static boolean isComboMiss() {
		return comboMiss;
	}

	public static int getWordMode() {
		return wordMode;
	}

	public static int getLevel() {
		return level;
	}

	public static double getBaseDps() {
		return baseDps;
	}

	public static double getDps() {
		return dps;
	}

	public static double getMoney() {
		return money;
	}

	public static boolean isCanUpgrade() {
		return canUpgrade;
	}

	public static double getCost() {
		return cost;
	}

	public static double getDpsPlus() {
		return dpsPlus;
	}

	public static SkillHello getSkillHello() {
		return skillHello;
	}

	public static SkillLol getSkillLol() {
		return skillLol;
	}

	public static SkillQuestion getSkillQuestion() {
		return skillQuestion;
	}

	public static Timeline getTimeline() {
		return timeline;
	}

	public void setCurrentChar(int c) {
		currentChar = c;
	}

	public void setWord(String w) {
		word = w;
	}

}