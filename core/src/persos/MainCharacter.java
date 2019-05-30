package persos;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

public class MainCharacter {
	protected int HP;
    protected ProgressBar HP_bar;
    protected int MP;
    protected ProgressBar MP_bar;
    protected boolean alive;
    protected int[] vars_combat = new int[2]; // [HP(0),MP(1)]
    protected int lvl_ball; //level of the ball
    protected static int[] lvl_balls = new int[3];
    protected int[] skills_lvl;
    protected int[] caracs;
    protected int[] pos = new int[2];
    
    public class RedBall extends MainCharacter {
        RedBall() {
            HP = 100;
            
            MP = 100;
            alive = true;
            vars_combat[0]=HP; vars_combat[1]=MP;
			lvl_ball = lvl_balls[0];
			skills_lvl = new int[5];
            caracs = new int[5]; caracs[0]=10; caracs[1]=25; // [0=atk, 1=spell_dmg]
            
        }

    }
    
    public class Boss {
        protected int HP;
        protected boolean alive;
        protected int[] boss_dmg = new int[3];
        
        Boss() {
            boss_dmg[0]=10;boss_dmg[1]=20;boss_dmg[2]=35;
            HP = 300;
            alive = true;
        }
        //@SuppressLint("SetTextI18n")
        public boolean set_HP(int val) {
            if (val>0) { // we check if we have to add or substract HP
                if ((HP + val) < 300) { // we check that it doesn't overpass HP
                    HP += val;
                    HP_bar.setValue(HP);

                    /*TextView dmg_txtv = new TextView(MainActivity.this);
                    dmg_txtv.setText("-"+val);
                    dmg_txtv.setId(0);
                    constraint_layout.addView(dmg_txtv);
                    c_set.clone(constraint_layout);
                    c_set.connect(dmg_txtv.getId(), ConstraintSet.TOP, constraint_layout.getId(), ConstraintSet.TOP, 60);
                    c_set.applyTo(constraint_layout);*/


                    return true;
                }
                else {
                    HP=300;
                    HP_bar.setValue(HP);
                    return true;
                }
            }
            if (val<0) {
                if ((HP + val) > 0){
                    HP += val;
                    HP_bar.setValue(HP);
                    return true;
                }
                else {
                    HP = 0;
                    HP_bar.setValue(HP);
                    return false; //ball is dead
                }
            }
        return false;
        }
        public int get_hp(){
            return HP;
        }
    }
}
