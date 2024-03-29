package bing.support.whoisspy.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import bing.support.whoisspy.R;
import bing.support.whoisspy.constant.Key;
import bing.support.whoisspy.utils.Logger;


//TODO 复杂的判断
/**
 * 0. 把头像背景改成半透明，附加到底牌之上
 * 1. 找到spy, 全部翻牌，已被杀的状态不变
 * 2. 找到平民，冤死
 * 3. 如果平民死亡数量超过3/4（包括当前用户)，则失败
 * 4. 如果已经按过该牌则跳过
 * 5. 从数据库中获取随机的身份，赋值给major和spy
 */

public class GameActivity extends Activity implements OnItemClickListener{
	
	private enum STATUS {PICK_CHARACTER, GAMING, GAME_OVER;}
	private enum WINNER {MAJOR, SPY;}//白板和卧底视为同伴
	
	private Map<Integer, String> ids = new HashMap<Integer, String>();//玩家-对应列表
	private List<Integer> clickedItems = new ArrayList<Integer>();//用于记录玩家身份以及状态
	private GridView gameView = null;
	private ImageAdapter imageAdapter = null;
	private List<ViewHolder> pcds = null;//用于更新卡面
	private List<String> players = null;//玩家
	private List<String> characters = null;//角色
	private Set<Integer> spyPositions = new HashSet<Integer>();//Used to store spy random index
	private Set<Integer> mfPositions = new HashSet<Integer>();//Used to store multi_face random index
	private STATUS status = null;
	private WINNER winner = null;
	private int AMOUNT_ALL = 20;//总人数
	private int AMOUNT_SPY= 4;
	private int AMOUNT_MULTIFACE= 1;
	private String MAJOR = null;//平民角色
	private String SPY = null;//间谍角色
	private String MULTI_FACE = "";//白板角色
	private Bundle bundle = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        
        status = STATUS.PICK_CHARACTER;
        bundle = getIntent().getExtras();
        initLayout(bundle);
    }
    
	private void initLayout(Bundle mBundle) {
		//reset status and clear data
		status = STATUS.PICK_CHARACTER;
		ids.clear();
		clickedItems.clear();
		spyPositions.clear();
		mfPositions.clear();
		//==========test data=============
		if(null == mBundle.getString(Key.MAJOR_NAME)
				|| "".equals(mBundle.getString(Key.MAJOR_NAME))){
			MAJOR = "伯爵";
			SPY = "男爵";
		}else{
			MAJOR = mBundle.getString(Key.MAJOR_NAME);
			SPY = mBundle.getString(Key.SPY_NAME);
		}
		
		AMOUNT_ALL = bundle.getInt(Key.AMOUNT);
		AMOUNT_SPY = bundle.getInt(Key.SPY_AMOUNT);
		AMOUNT_MULTIFACE = bundle.getInt(Key.MULTIFACE_AMOUNT);
		
		characters = new ArrayList<String>();
		for(int i= 0; i < AMOUNT_ALL; i++){
			characters.add(MAJOR);
		}
		while(spyPositions.size() < AMOUNT_SPY){//set用来过滤重复的随机值
			spyPositions.add(new Random().nextInt(AMOUNT_ALL));
		}
		while(mfPositions.size() < AMOUNT_MULTIFACE){//set用来过滤重复的随机值
			int r = new Random().nextInt(AMOUNT_ALL);
			if(!spyPositions.contains(r)){
				mfPositions.add(r);
			}
		}
		Iterator<Integer> iter = spyPositions.iterator();
		while(iter.hasNext()){
			int index = iter.next();
			Logger.d(String.format("Pick %d as spy", index+1));
			characters.set(index, SPY);
		}
		iter = mfPositions.iterator();
		while(iter.hasNext()){
			int index = iter.next();
			Logger.d(String.format("Pick %d as multi-face", index+1));
			characters.set(index, MULTI_FACE);
		}
		
		players = new ArrayList<String>();
		for(int i= 0; i < AMOUNT_ALL; i++){
			players.add(String.format("player_%d", i+1));
		}
		//==================================
		
		pcds = new ArrayList<ViewHolder>();
		for(int i = 0; i < AMOUNT_ALL; i++){
			ViewHolder p = new ViewHolder(
					String.format("Button %d", (i+1)),
					R.drawable.basic,
					R.drawable.basic
					); 
			pcds.add(p);
		}
		
		imageAdapter = new ImageAdapter(this, pcds);
		gameView = (GridView)findViewById(R.id.game); 
		gameView.setAdapter(imageAdapter);
		gameView.setOnItemClickListener(this);
		
		new AlertDialog.Builder(GameActivity.this)
        .setIconAttribute(android.R.attr.alertDialogIcon)
        .setTitle(R.string.alert_dialog_game_start)
        .setMessage(getString(R.string.deliver_to_first_player) + " " + players.get(0))
        .setPositiveButton(getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
		
    }
    

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		
		switch(status){
		case PICK_CHARACTER:
			Logger.d("Current postion: " + position);
			if(clickedItems.contains(position)){
				break;
			}else{
				clickedItems.add(position);
			}
			Logger.d("Current size of clicked: " + clickedItems.size());
			for(int i = 0; i < clickedItems.size(); i++){
				Logger.e("clicked contains: " + clickedItems.get(i));
			}
			ids.put(position, players.get(clickedItems.size()-1));
			// Need to set user's image to replace R.drawable.logo
			ViewHolder viewHolder = new ViewHolder(players.get(clickedItems.size()-1), R.drawable.logo, R.drawable.basic);
			pcds.set(position, viewHolder);
			imageAdapter.notifyDataSetChanged();
			
			String message = null;
			if(clickedItems.size() < AMOUNT_ALL){
				message = getString(R.string.pick_character_msg) + players.get(clickedItems.size());
			}else{
				message = "Please deliver the phone to owner";
				status = STATUS.GAMING;
				clickedItems.clear();
			}
			new AlertDialog.Builder(GameActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle(getString(R.string.pick_character_title) + characters.get(position))
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
			break;
		case GAMING:
			Logger.d("postion is in ids: " + ids.containsKey(position));
			if(!ids.containsKey(position)) break;//skip clicked card
			new AlertDialog.Builder(GameActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle(getString(R.string.gaming_title))
            .setMessage(getString(R.string.gaming_msg))
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	updateGamingInfo(position);
                }
            }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
				}
            }).show();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * Judge the status while gaming
	 * @param position
	 */
	public void updateGamingInfo(int position){
		if(spyPositions.contains(position) || mfPositions.contains(position)){//found spy or mf
			int character;
			if(spyPositions.remove(position)){
				character = R.drawable.spy;
			}else{
				mfPositions.remove(position);
				character = R.drawable.multi_face;
			}

    		if(0 == spyPositions.size() + mfPositions.size()){
    			status = STATUS.GAME_OVER;
    			winner = WINNER.MAJOR;
    		}
			// Need to set user's character image to replace R.drawable.logo
			ViewHolder viewHolder = new ViewHolder(ids.get(position), R.drawable.logo, character);
			pcds.set(position, viewHolder);
			imageAdapter.notifyDataSetChanged();
    	}
    	else{
    		ids.remove(position);//remove from tracking list
    		int leftGoodPeople = ids.size() -spyPositions.size()-mfPositions.size();
    		if(leftGoodPeople <= 1 || leftGoodPeople < Math.floor(AMOUNT_ALL/4.0)){
    			status = STATUS.GAME_OVER;
    			winner = WINNER.SPY;
    		}
    		ViewHolder viewHolder = new ViewHolder(ids.get(position), R.drawable.logo, R.drawable.wronged);
			pcds.set(position, viewHolder);
			imageAdapter.notifyDataSetChanged();
    	}
		if(status == STATUS.GAME_OVER){
			ViewHolder viewHolder = null;
			Iterator<Integer> it = ids.keySet().iterator();
			while(it.hasNext()){
				int index = it.next();
				if(characters.get(index) == MAJOR){
					if(winner == WINNER.MAJOR){
						viewHolder = new ViewHolder(ids.get(index), R.drawable.logo, R.drawable.success);
					}else{
						viewHolder = new ViewHolder(ids.get(index), R.drawable.logo, R.drawable.failure);
					}
				}
				else if(characters.get(index) == SPY){
					viewHolder = new ViewHolder(ids.get(index), R.drawable.logo, R.drawable.spy);
				}else{
					viewHolder = new ViewHolder(ids.get(index), R.drawable.logo, R.drawable.multi_face);
				}
				pcds.set(index, viewHolder);
			}
			imageAdapter.notifyDataSetChanged();
			generateGameOverDialog();
		}
	}
	
	
	/**
	 * Show winner and provide other options(replay or return settings)
	 */
	public void generateGameOverDialog(){
		String w = null;
		if(winner == WINNER.MAJOR){
			w = getString(R.string.major);
		}else{
			w = getString(R.string.spy);
		}
		new AlertDialog.Builder(GameActivity.this)
        .setIconAttribute(android.R.attr.dialogIcon)
        .setTitle("Congratulations!")
        .setMessage(String.format("Winner is %s !\n %s VS %s(spy)", w, MAJOR, SPY))
        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setNeutralButton(R.string.return_to_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setNeutralButton(R.string.play_another_around, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	Bundle b = new Bundle();
            	b.putString(Key.MAJOR_NAME, "猴子");
            	b.putString(Key.SPY_NAME, "猩猩");
            	initLayout(b);
            }
        }).show();
	}


}
