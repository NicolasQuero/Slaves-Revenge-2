package menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Spells.Spell;
import entities.Entity;
import utilities.AnimationTable;
import utilities.SpellActor;

public class UpgradeEnts {
	
	ArrayList<Entity> entities;
	Rectangle[] entity_image_hitboxes; // images' hitboxes on the right
	Rectangle[] entity_squad_hitboxes; // images' hitboxes in the squad
	Entity[] selected_entity; //0 is the dragged entity, 1 is the target entity
	Vector3 touchPos = new Vector3();
	BitmapFont font = new BitmapFont();
	BitmapFont fontBig = new BitmapFont();
	private static Drawable upgrade_bg = new TextureRegionDrawable(new Texture("menus/upgrade_charac/upgrade_bg_right.png"));
	private static Drawable upgrade_bg_left = new TextureRegionDrawable(new Texture("menus/upgrade_charac/upgrade_bg_lefthell.png"));

	private static Image icon_under_d = new Image(new Texture("menus/upgrade_charac/icon_under_d.png"));
	private static Image icon_under_g = new Image(new Texture("menus/upgrade_charac/icon_under_g.png"));
	private static Image icon_player = new Image(new Texture("menus/upgrade_charac/icon_player.png"));
	private NinePatch text_bg = new NinePatch(new Texture("menus/upgrade_charac/text_bg.png"));
	
	private String SPELL_BG_SRC = "menus/upgrade_charac/upgrade_spell_bg.png";
	private String ULT_BG_SRC = "menus/upgrade_charac/upgrade_ult_bg.png";
	private static Image spell_bg = new Image(new Texture("menus/upgrade_charac/upgrade_spell_bg.png"));
	private static Image ult_bg = new Image(new Texture("menus/upgrade_charac/upgrade_ult_bg.png"));
	
	private SpellActor[] spells = new SpellActor[4];
	
	private float ATK_ICON_SPEED = 0.2f;
	private static TextureRegion[][] atk_icon_spritesheet = TextureRegion.split(new Texture("icons/atk_icon.png"), 16, 16);
	private Animation<?> atk_icon = new Animation<TextureRegion>(ATK_ICON_SPEED, atk_icon_spritesheet[0]);
	private float DEF_ICON_SPEED = 0.125f;
	private static TextureRegion[][] def_icon_spritesheet = TextureRegion.split(new Texture("icons/def_icon.png"), 32, 32);
	private Animation<?> def_icon = new Animation<TextureRegion>(DEF_ICON_SPEED, def_icon_spritesheet[0]);
	private AnimationTable atk_icon_t = new AnimationTable(atk_icon);
	private AnimationTable def_icon_t = new AnimationTable(def_icon);
	
	private AnimationTable icon_player_t, icon_player_t2, icon_player_t3;
	
	float xDraw, yDraw; // bottom left of the upgrade bg
	
	float stateTime;
	
	private Container<Table> tableCont;
	private Table table;
	private Table tableG, tableD;
	private Stage stage;
	
	private TextureAtlas text_atlas;
	private TextButton buttonAtkPlus, buttonDefPlus, buttonMinus;
	private TextButton[] buttonSpellPlus = new TextButton[4];
	
	private Entity[] played_entities = new Entity[3];
	
	private Label.LabelStyle baseStyle = new Label.LabelStyle(font, Color.WHITE);

	private Label tableDTop = new Label("Aptitudes", new Label.LabelStyle(fontBig, Color.WHITE));
	private Label entSpellPts = new Label("Points d'aptitude", baseStyle);
	private Label entName = new Label("", baseStyle);
	private Label entName2 = new Label("", baseStyle);
	private Label entName3 = new Label("", baseStyle);
	private Label entAtkLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
	private Label entDefLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
	private Label entUpPtsLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
	
	public UpgradeEnts(ArrayList<Entity> entities, Entity[] played_entities) {
		text_bg.setPadding(3, 3, 3, 3);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fontBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fontBig.getData().setScale(2);
		this.entities = entities;
		stateTime = 0f;
		this.stage = new Stage();
		
		int i = 0;
		for (Entity ent : played_entities) {
			if (ent != null) {
				this.played_entities[i] = ent;
				if (i == 0) {
					entAtkLabel.setText("ATK : " + (ent.getATK()-ent.getAtkBonus()) + " (+" + ent.getAtkBonus() + ")");
					entDefLabel.setText("DEF : " + (ent.getDEF()-ent.getDefBonus()) + " (+" + ent.getDefBonus() + ")");
					entUpPtsLabel.setText("Points restants : " + ent.getUpPts());
					entName.setText(ent.getType().getId() + " : LVL " + ent.getLvl_ent());
					icon_player_t = new AnimationTable("menus/upgrade_charac/icon_player.png", ent.getIcon(), 3);
					tableDTop.setText("Aptitudes de " + ent.getType().getId());
					entSpellPts.setText("Points d'aptitude restants : " + ent.getUpSpellPts());
					int k = 0;
					for (Spell spell : ent.getSpells()) {
						if (spell != null && k != 3) {
							this.spells[k] = new SpellActor(SPELL_BG_SRC, spell, 64, 20, 8);
						}
						if (k == 3 && spell != null) {
							this.spells[k] = new SpellActor(ULT_BG_SRC, spell, 64, 20, 8);
						}
						k++;
						
					}
				}
				else if (i==1) {
					entName2.setText(ent.getType().getId() + " : LVL " + ent.getLvl_ent());
					icon_player_t2 = new AnimationTable("menus/upgrade_charac/icon_under_g.png", ent.getIcon(), 2);
				}
				else if (i==2) {
					entName3.setText(ent.getType().getId() + " : LVL " + ent.getLvl_ent());
					icon_player_t3 = new AnimationTable("menus/upgrade_charac/icon_under_d.png", ent.getIcon(), 2);
				}
				i++;
			}
		}
		
		Gdx.input.setInputProcessor(stage);
		
		tableCont = new Container<Table>();
		tableCont.background(upgrade_bg); // the main table is table, the left part of the upgrade is tableG, right part with spells is tableD
		tableCont.setSize(800, 600);
		tableCont.setPosition(0, 100);
		
		text_atlas = new TextureAtlas(Gdx.files.internal("buttons/button_square/button_square.pack"));
		Skin mySkin = new Skin(text_atlas);
		table = new Table(mySkin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		tableG = new Table(mySkin);
		tableG.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		tableD = new Table(mySkin);
		tableD.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = mySkin.getDrawable("button_square_up");
		textButtonStyle.down = mySkin.getDrawable("button_square_down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = font;
		
		buttonAtkPlus = new TextButton("+", textButtonStyle);
		buttonAtkPlus.pad(10);
		buttonAtkPlus.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				buttonAtkPlus.setChecked(true);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                buttonAtkPlus.setChecked(false);
                if (played_entities[0].getUpPts() > 0) {
                	played_entities[0].addUpPts(-1);
                	played_entities[0].addAtk(1);
					entAtkLabel.setText("ATK : " + (played_entities[0].getATK()-played_entities[0].getAtkBonus()) + " (+" + played_entities[0].getAtkBonus() + ")");
					entUpPtsLabel.setText("Points restants : " + played_entities[0].getUpPts());
                }
                return true;
		}});
		
		buttonDefPlus = new TextButton("+", textButtonStyle);
		buttonDefPlus.pad(10);
		buttonDefPlus.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				buttonDefPlus.setChecked(true);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                buttonDefPlus.setChecked(false);
                if (played_entities[0].getUpPts() > 0) {
                	played_entities[0].addUpPts(-1);
                	played_entities[0].addDef(1);
					entDefLabel.setText("DEF : " + (played_entities[0].getDEF()-played_entities[0].getDefBonus()) + " (+" + played_entities[0].getDefBonus() + ")");
					entUpPtsLabel.setText("Points restants : " + played_entities[0].getUpPts());
                }
                return true;
		}});
		
		buttonSpellPlus[0] = new TextButton("+", textButtonStyle);
		buttonSpellPlus[0].pad(10);
		buttonSpellPlus[0].addListener(new InputListener() {
				@Override
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					buttonSpellPlus[0].setChecked(true);
	            }
	            @Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	buttonSpellPlus[0].setChecked(false);
	                if (played_entities[0].getUpSpellPts() > 0 && played_entities[0].getSpells()[0] != null) {
	                	played_entities[0].addUpSpellPts(-1);
	                	played_entities[0].getSpells()[0].lvlUp();
						entSpellPts.setText("Points d'aptitude restants : " + played_entities[0].getUpSpellPts());
	                }
	                return true;
			}});
		
		buttonSpellPlus[1] = new TextButton("+", textButtonStyle);
		buttonSpellPlus[1].pad(10);
		buttonSpellPlus[1].addListener(new InputListener() {
				@Override
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					buttonSpellPlus[1].setChecked(true);
	            }
	            @Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	buttonSpellPlus[1].setChecked(false);
	                if (played_entities[0].getUpSpellPts() > 0 && played_entities[0].getSpells()[1] != null) {
	                	played_entities[0].addUpSpellPts(-1);
	                	played_entities[0].getSpells()[1].lvlUp();
						entSpellPts.setText("Points d'aptitude restants : " + played_entities[0].getUpSpellPts());
	                }
	                return true;
			}});
		
		buttonSpellPlus[2] = new TextButton("+", textButtonStyle);
		buttonSpellPlus[2].pad(10);
		buttonSpellPlus[2].addListener(new InputListener() {
				@Override
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					buttonSpellPlus[2].setChecked(true);
	            }
	            @Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	buttonSpellPlus[2].setChecked(false);
	                if (played_entities[0].getUpSpellPts() > 0 && played_entities[0].getSpells()[2] != null) {
	                	played_entities[0].addUpSpellPts(-1);
	                	played_entities[0].getSpells()[2].lvlUp();
						entSpellPts.setText("Points d'aptitude restants : " + played_entities[0].getUpSpellPts());
	                }
	                return true;
			}});

		buttonSpellPlus[3] = new TextButton("+", textButtonStyle);
		buttonSpellPlus[3].pad(10);
		buttonSpellPlus[3].addListener(new InputListener() {
				@Override
	            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					buttonSpellPlus[3].setChecked(true);
	            }
	            @Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            	buttonSpellPlus[3].setChecked(false);
	                if (played_entities[0].getUpSpellPts() > 0 && played_entities[0].getSpells()[3] != null) {
	                	played_entities[0].addUpSpellPts(-1);
	                	played_entities[0].getSpells()[3].lvlUp();
						entSpellPts.setText("Points d'aptitude restants : " + played_entities[0].getUpSpellPts());
	                }
	                return true;
			}});
		
		buttonMinus = new TextButton("-", textButtonStyle);
		buttonMinus.pad(10);
		buttonMinus.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				buttonMinus.setChecked(true);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                buttonMinus.setChecked(false);
                return true;
		}});
		
		table.background(upgrade_bg); // the main table is table, the left part of the upgrade is tableG, right part with spells is tableD
		table.setFillParent(false);
		table.setPosition((Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 800 / 1280)) / 2, (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*600f/800f)/1.5f);
		table.setWidth((Gdx.graphics.getWidth() * 800 / 1280)); 
		table.setHeight(Gdx.graphics.getHeight()*(600f/800f));
		tableD.setWidth((Gdx.graphics.getWidth() * 400 / 1280)); 
		tableD.setHeight(Gdx.graphics.getHeight()*(600f/800f));
		tableD.setPosition(table.getWidth()/2, 0);
		table.setName("Upgrade table");
		tableG.setName("Upgrade table left");
		tableD.setName("Upgrade table right");
		
		tableG.setBackground(upgrade_bg_left); // Left part of the table
		tableG.setFillParent(false);
		tableG.setWidth((Gdx.graphics.getWidth() * 400 / 1280)); 
		tableG.setHeight(Gdx.graphics.getHeight()*(600f/800f));
		tableG.top();
		tableG.row();
		tableG.add(icon_player_t).prefWidth(134).prefHeight(134).padTop(64).colspan(10).expandX();
		tableG.row().expandX().padTop(16);
		tableG.add(entName).colspan(10);
		tableG.row().expandX().padTop(21);
		tableG.add(icon_player_t2).prefWidth(68).prefHeight(68).colspan(2);
		tableG.add(icon_player_t3).prefWidth(68).prefHeight(68).colspan(2).row();
		tableG.add(entName2).colspan(2).padTop(10);
		tableG.add(entName3).colspan(2).padTop(10);
		tableG.row().expandX().padTop(60);
		tableG.add(atk_icon_t).prefWidth(32).prefHeight(32).padLeft(30);
		tableG.add(entAtkLabel);
		tableG.add(buttonAtkPlus).width(16).height(16).left().padLeft(10);
		tableG.add(entUpPtsLabel).padRight(20).row();
		tableG.row().expandX().padTop(30);
		tableG.add(def_icon_t).prefWidth(32).prefHeight(32).padLeft(30);
		tableG.add(entDefLabel);
		tableG.add(buttonDefPlus).width(16).height(16).left().padLeft(10);
		
		tableD.setFillParent(false);
		tableD.top();
		tableD.row();
		tableD.add(tableDTop).padTop(30).expandX().row();
		tableD.add(entSpellPts).padTop(20).expandX().row();
		for (int k = 0; k < 4; k++) {
			if (spells[k] != null) {
				tableD.add(spells[k]).prefWidth(320).prefHeight(80).padTop(30);
				tableD.add(buttonSpellPlus[k]).width(16).height(16).padTop(30).padRight(16);
				tableD.row();
			}
		}
		
		table.addActor(tableG);
		table.addActor(tableD);
		//table.debug();
		//tableG.debug();
		//tableD.debug();
		tableCont.setActor(table);
        stage.addActor(table);
		
		
	}
	
	public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {
		stage.act();
		stage.draw();
		stateTime += delta;
		xDraw = camera.position.x - camera.viewportWidth/2 + 240;
		yDraw = camera.position.y - camera.viewportHeight/2 + 150;
		//batch.draw(upgrade_bg, xDraw, yDraw);
		batch.draw(entities.get(0).getIcon(), xDraw + 130, yDraw + 380, 128, 128);
		font.draw(batch, entities.get(0).getType().getId(), xDraw + 170, yDraw + 370);
		
		batch.draw((TextureRegion) atk_icon.getKeyFrame(stateTime, true), xDraw + 70, yDraw + 200, 32, 32);
		font.draw(batch, "ATK : " + Integer.toString(entities.get(0).getATK()), xDraw + 110, yDraw + 224);
		
		//System.out.println(table.getX() + " " + tableD.getY() + " " + table.getWidth() + " / " + tableD.getX() + " " + tableD.getY() + " " + tableD.getWidth());
		
		
	}
	
	public void dispose() {
		table.remove();
		stage.dispose();
	}
}
