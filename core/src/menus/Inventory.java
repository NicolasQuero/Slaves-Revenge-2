package menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import items.Item;
import utilities.BaseActor;

public class Inventory extends BaseActor {
	
	private Stage stage;
	
	private BitmapFont font = new BitmapFont();
	
	private final static int INVENTORY_WIDTH = 800;
	private final static int INVENTORY_HEIGHT = 600;
	private static Drawable background = new TextureRegionDrawable(new Texture("menus/inventory/background.png"));
	private static Drawable leftBackground = new TextureRegionDrawable(new Texture("menus/inventory/inventoryLeftBg.png"));
	private static Drawable rightBackground = new TextureRegionDrawable(new Texture("menus/inventory/inventoryRightBg.png"));
	
	private TextureAtlas buttons_atlas;
	private Skin mySkin;
	private TextButton equipment_btn, resources_btn;
	private Table mainTable, rightTable, leftTable;
	
	private ArrayList<Item> inventory;
	private int money;
	private boolean equipmentFocused;

	public Inventory(Stage stage) {
		super(Gdx.graphics.getWidth()/2 - INVENTORY_WIDTH/2, Gdx.graphics.getHeight()/2 - INVENTORY_HEIGHT/2 + 20, stage);
		this.stage = stage;
		this.remove();
		super.setWidth(INVENTORY_WIDTH * Gdx.graphics.getWidth() / 1280);
		super.setHeight(INVENTORY_HEIGHT * Gdx.graphics.getHeight() / 800);
		this.inventory = new ArrayList<Item>();
		this.money = 0;
		this.equipmentFocused = true;
		
		buttons_atlas = new TextureAtlas(Gdx.files.internal("menus/inventory/inventory_buttons.pack"));
		mySkin = new Skin(buttons_atlas);
		mainTable = new Table(mySkin);
		leftTable = new Table(mySkin);
		rightTable = new Table(mySkin);
		TextButtonStyle equipmentButtonStyle = new TextButtonStyle();
		equipmentButtonStyle.up = mySkin.getDrawable("equipements_btn_up");
		equipmentButtonStyle.down = mySkin.getDrawable("equipements_btn_down");
		equipmentButtonStyle.pressedOffsetX = 1;
		equipmentButtonStyle.pressedOffsetY = -1;
		equipmentButtonStyle.font = font;
		TextButtonStyle resourcesButtonStyle = new TextButtonStyle();
		resourcesButtonStyle.up = mySkin.getDrawable("ressources_btn_up");
		resourcesButtonStyle.down = mySkin.getDrawable("ressources_btn_down");
		resourcesButtonStyle.pressedOffsetX = 1;
		resourcesButtonStyle.pressedOffsetY = -1;
		resourcesButtonStyle.font = font;
		equipment_btn = new TextButton("", equipmentButtonStyle);
		equipment_btn.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				equipment_btn.setChecked(true);
				equipmentFocused = true;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                equipment_btn.setChecked(false);
                return true;
		}});
		resources_btn = new TextButton("", resourcesButtonStyle);
		resources_btn.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				resources_btn.setChecked(true);
				equipmentFocused = false;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                resources_btn.setChecked(false);
                return true;
		}});
		
		mainTable.setBackground(background);
		mainTable.setFillParent(false);
		mainTable.setWidth(Gdx.graphics.getWidth() / 1280 * 800);
		mainTable.setHeight(Gdx.graphics.getHeight() / 800 * 600);
		mainTable.setPosition(Gdx.graphics.getWidth()/2 - this.getWidth()/2, Gdx.graphics.getHeight()/2 - this.getHeight()/2 + 20);
		
		leftTable.setBackground(leftBackground);
		leftTable.setFillParent(false);
		leftTable.setWidth(Gdx.graphics.getWidth() / 1280 * 321);
		leftTable.setHeight(Gdx.graphics.getHeight() / 800 * 470);
		leftTable.setPosition(Gdx.graphics.getWidth()/1280 * 50, Gdx.graphics.getHeight()/800 * 40);
		
		rightTable.setBackground(rightBackground);
		rightTable.setFillParent(false);
		rightTable.setWidth(Gdx.graphics.getWidth() / 1280 * 371);
		rightTable.setHeight(Gdx.graphics.getHeight() / 800 * 470);
		rightTable.setPosition(Gdx.graphics.getWidth()/1280 * 401, Gdx.graphics.getHeight()/800 * 40);
		rightTable.add(equipment_btn).padLeft(27 - 11).padTop(54 + 11).prefWidth(169).prefHeight(52).colspan(2);
		rightTable.add(resources_btn).padRight(27 - 11).padTop(54 + 11).prefWidth(169).prefHeight(52).colspan(2);
		
		mainTable.addActor(leftTable);
		mainTable.addActor(rightTable);
	}
	
	public void open(Stage stage) {
		stage.addActor(this);
		stage.addActor(mainTable);
	}
	
	public void close() {
		mainTable.remove();
		super.remove();
	}
	
	public void dispose() {
		
	}
	
	public void addItem(Item item) {
		inventory.add(item);
	}
	
	public void delItem(Item item) {
		if (inventory.contains(item))
			inventory.remove(item);
	}
	
	public void addMoney(int amount) {
		this.money += amount;
	}
	
	public void spendMoney(int amount) {
		this.money -= amount;
	}
	
	public boolean contains(Item item) {
		for (Item i : inventory) {
			if (i.getId().equals(item.getId()))
				return true;
		}
		return false;
	}
	
}
