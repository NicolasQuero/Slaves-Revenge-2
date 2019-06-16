package menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import items.Item;
import utilities.AnimationTable;
import utilities.BaseActor;

public class Inventory extends BaseActor {
	
	private Stage stage;
	
	private BitmapFont font = new BitmapFont();
	
	private final static int INVENTORY_WIDTH = 800;
	private final static int INVENTORY_HEIGHT = 600;
	private static Drawable background = new TextureRegionDrawable(new Texture("menus/inventory/background.png"));
	private static Drawable leftBackground = new TextureRegionDrawable(new Texture("menus/inventory/inventoryLeftBg.png"));
	private static Drawable rightBackground = new TextureRegionDrawable(new Texture("menus/inventory/inventoryRightBg.png"));
	private static Drawable inventoryBackground = new TextureRegionDrawable(new Texture("menus/inventory/inventory_items_bg.png"));
	private static Drawable selectedDescriptionBackground = new TextureRegionDrawable(new Texture("menus/inventory/selected_description_bg.png"));
	
	private Label.LabelStyle baseLabelStyle = new Label.LabelStyle(font, Color.WHITE);
	
	private TextureAtlas buttons_atlas;
	private Skin mySkin;
	private TextButton equipment_btn, resources_btn;
	private Table mainTable, rightTable, leftTable;
	private Table inventoryTable, equInventoryTable;
	private Container<Table> rightTableCont;
	
	private int resNbItems;
	private int equNbItems;
	private ArrayList<Item> equInventory;
	private ArrayList<Item> resInventory;
	private ArrayList<AnimationTable> resImages, itemImages;
	private Texture default_icon = new Texture("menus/inventory/default_selected_item.png");
	private AnimationTable selected_item_icon;
	private Label selected_item_description;
	private Table selectedItemDescriptionTable;
	private int money;
	private boolean equipmentFocused;

	public Inventory(Stage stage) {
		super(Gdx.graphics.getWidth()/2 - INVENTORY_WIDTH/2, Gdx.graphics.getHeight()/2 - INVENTORY_HEIGHT/2 + 20, stage);
		this.stage = stage;
		this.remove();
		super.setWidth(INVENTORY_WIDTH * Gdx.graphics.getWidth() / 1280);
		super.setHeight(INVENTORY_HEIGHT * Gdx.graphics.getHeight() / 800);
		this.resInventory = new ArrayList<Item>();
		this.equInventory = new ArrayList<Item>();
		this.money = 0;
		this.equipmentFocused = false;
		this.resNbItems = 0;
		this.equNbItems = 0;
		this.itemImages = new ArrayList<AnimationTable>();
		this.resImages = new ArrayList<AnimationTable>();
		this.selected_item_icon = new AnimationTable("menus/inventory/selected_item_bg.png", default_icon, 1, 3, 3, 1);
		this.selected_item_description = new Label("Aucun item n'est s�lectionn� \n Ta grosse daronne pue des pieds aaaaaaaaaaaa \n Ton dar le pd \n Fume des opiac�es", baseLabelStyle);
		
		buttons_atlas = new TextureAtlas(Gdx.files.internal("menus/inventory/inventory_buttons.pack"));
		mySkin = new Skin(buttons_atlas);
		mainTable = new Table(mySkin);
		leftTable = new Table(mySkin);
		rightTable = new Table(mySkin);
		selectedItemDescriptionTable = new Table(mySkin);
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
				if (!equipmentFocused) {
					equipmentFocused = true;
					Cell<Table> cell = rightTable.getCell(inventoryTable);
					inventoryTable.remove();
					rightTable.getCells().removeValue(cell, true);
				    rightTable.invalidate();
					rightTable.setWidth(Gdx.graphics.getWidth() / 1280 * 371);
					rightTable.setHeight(Gdx.graphics.getHeight() / 800 * 470);
					rightTable.setPosition(Gdx.graphics.getWidth()/1280 * 401, Gdx.graphics.getHeight()/800 * 40);
					rightTable.row();
					rightTable.add(equInventoryTable).padBottom(54).padTop(25).colspan(4);
				}
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
				if (equipmentFocused) {
					equipmentFocused = false;
					Cell cell = rightTable.getCell(equInventoryTable);
					equInventoryTable.remove();
					rightTable.getCells().removeValue(cell, true);
				    rightTable.invalidate();
				    rightTable.row();
					rightTable.add(inventoryTable).padBottom(54).padTop(25).colspan(4);
				}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                resources_btn.setChecked(false);
                return true;
		}});

		rightTableCont = new Container<Table>();
		rightTableCont.setSize(371, 470);
		rightTableCont.setPosition(Gdx.graphics.getWidth()/1280 * 401, Gdx.graphics.getHeight()/800 * 40);
		
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
		leftTable.bottom().left();
		leftTable.row();
		leftTable.add(selected_item_icon).padBottom(104 + 28).padLeft(15 + 29).prefWidth(68).prefHeight(68);
		selectedItemDescriptionTable.setBackground(selectedDescriptionBackground);
		selectedItemDescriptionTable.setFillParent(false);
		selectedItemDescriptionTable.setWidth(Gdx.graphics.getWidth() / 1280 * 155);
		selectedItemDescriptionTable.setHeight(Gdx.graphics.getHeight() / 800 * 156);
		selected_item_description.setWrap(true);
		selectedItemDescriptionTable.add(selected_item_description).pad(10)
		.prefWidth(selectedItemDescriptionTable.getWidth() - 20).prefHeight(selectedItemDescriptionTable.getHeight() - 20);
		leftTable.add(selectedItemDescriptionTable).padLeft(14).padBottom(50)
		.prefWidth(Gdx.graphics.getWidth() / 1280 * 155).prefHeight(Gdx.graphics.getHeight() / 800 * 156);
		
		rightTable.setBackground(rightBackground);
		rightTable.setFillParent(false);
		rightTable.setWidth(Gdx.graphics.getWidth() / 1280 * 371);
		rightTable.setHeight(Gdx.graphics.getHeight() / 800 * 470);
		rightTable.setPosition(Gdx.graphics.getWidth()/1280 * 401, Gdx.graphics.getHeight()/800 * 40);
		rightTable.add(equipment_btn).top().padLeft(27 - 11).padTop(22).prefWidth(169).prefHeight(52).colspan(2);
		rightTable.add(resources_btn).top().padRight(27 - 11).padTop(22).prefWidth(169).prefHeight(52).colspan(2).row();
		inventoryTable = new Table(mySkin);
		inventoryTable.setBackground(inventoryBackground);
		inventoryTable.setFillParent(false);
		inventoryTable.setWidth(Gdx.graphics.getWidth() / 1280 * 309);
		inventoryTable.setHeight(Gdx.graphics.getHeight() / 800 * 315);
		inventoryTable.setPosition(Gdx.graphics.getWidth()/1280 * 19, Gdx.graphics.getHeight()/800 * 54);
		inventoryTable.top().left();
		equInventoryTable = new Table(mySkin);
		equInventoryTable.setBackground(inventoryBackground);
		equInventoryTable.setFillParent(false);
		equInventoryTable.setWidth(Gdx.graphics.getWidth() / 1280 * 309);
		equInventoryTable.setHeight(Gdx.graphics.getHeight() / 800 * 315);
		equInventoryTable.setPosition(Gdx.graphics.getWidth()/1280 * 19, Gdx.graphics.getHeight()/800 * 54);
		equInventoryTable.top().left();
		
		rightTableCont.setActor(rightTable);
		rightTable.add(inventoryTable).expandY().padBottom(54).padTop(25).colspan(4);
		mainTable.addActor(leftTable);
		mainTable.addActor(rightTable);
		//mainTable.debug();
		//rightTable.debug();
		//leftTable.debug();
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
		if (!item.isEquipment()) {
			if (resNbItems >= 16)
				System.out.println("Inventaire plein � qu�cra");
			else {
				resInventory.add(item);
				resImages.add(new AnimationTable(item.getIcon()));
				resImages.get(resNbItems).addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				selected_item_icon.setIcon(item.getIcon());
				selected_item_description.setText(item.getDescription());
			}
			@Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
				
			});
				if (resNbItems / 4 == 0) { // first row
					if (resNbItems % 4 == 0) // first item
						inventoryTable.add(resImages.get(resNbItems)).padLeft(12).padTop(16).prefWidth(64).prefHeight(64);
					//else if (nbItems % 4 == 3) {
						//inventoryTable.add(itemImages.get(nbItems)).padRight(10).padTop(16).prefWidth(64).prefHeight(64);
					//}
					else
						inventoryTable.add(resImages.get(resNbItems)).padLeft(10).padTop(16).prefWidth(64).prefHeight(64);
				}
				else { // other rows
					if (resNbItems % 4 == 0) { // first item
						inventoryTable.row();
						inventoryTable.add(resImages.get(resNbItems)).padLeft(12).padTop(10).prefWidth(64).prefHeight(64);
					}
					/* if (nbItems % 4 == 3) {
						inventoryTable.add(itemImages.get(nbItems)).padRight(10).padTop(10).prefWidth(64).prefHeight(64);
					}*/
					else
						inventoryTable.add(resImages.get(resNbItems)).padLeft(10).padTop(10).prefWidth(64).prefHeight(64);
				}
				resNbItems += 1;
			}
		}
		else {
			if (equNbItems >= 16)
				System.out.println("Inventaire plein � qu�cra");
			else {
				equInventory.add(item);
				itemImages.add(new AnimationTable(item.getIcon()));
				if (equNbItems / 4 == 0) { // first row
					if (equNbItems % 4 == 0) // first item
						equInventoryTable.add(itemImages.get(equNbItems)).padLeft(12).padTop(16).prefWidth(64).prefHeight(64);
					//else if (nbItems % 4 == 3) {
						//inventoryTable.add(itemImages.get(nbItems)).padRight(10).padTop(16).prefWidth(64).prefHeight(64);
					//}
					else
						equInventoryTable.add(itemImages.get(equNbItems)).padLeft(10).padTop(16).prefWidth(64).prefHeight(64);
				}
				else { // other rows
					if (equNbItems % 4 == 0) { // first item
						equInventoryTable.row();
						equInventoryTable.add(itemImages.get(equNbItems)).padLeft(12).padTop(10).prefWidth(64).prefHeight(64);
					}
					/* if (nbItems % 4 == 3) {
						inventoryTable.add(itemImages.get(nbItems)).padRight(10).padTop(10).prefWidth(64).prefHeight(64);
					}*/
					else
						equInventoryTable.add(itemImages.get(equNbItems)).padLeft(10).padTop(10).prefWidth(64).prefHeight(64);
				}
				equNbItems += 1;
			}
		}
	}
	
	public void removeItem(Item item) {
		if (!item.isEquipment() && resInventory.contains(item)) {
			resInventory.remove(item);
		}
		else if (item.isEquipment() && equInventory.contains(item)) {
			equInventory.remove(item);
		}
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
