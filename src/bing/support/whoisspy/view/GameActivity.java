package bing.support.whoisspy.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import bing.support.whoisspy.R;
import bing.support.whoisspy.utils.Logger;

public class GameActivity extends Activity implements OnItemClickListener{
	
	private Map<String, String> ids = new HashMap<String, String>();//玩家-对应列表
	private List<Integer> clickedItems = new ArrayList<Integer>();
	private GridView gameView = null;
	private ImageAdapter imageAdapter = null;
	private List<ViewHolder> pcds = null;
	private List<String> players = null;//玩家
	private List<String> characters = null;//角色
	private enum STATUS {PICK_CHARACTER, GAMING, GAME_OVER;}
	private STATUS status = null;
	private int AMOUNT = 9;
	private int spyPosition;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        
        initLayout();
        status = STATUS.PICK_CHARACTER;
    }
    
	private void initLayout() {
		//==========test data=============
		characters = new ArrayList<String>();
		for(int i= 0; i < AMOUNT; i++){
			characters.add("伯爵");
		}
		spyPosition = new Random().nextInt(AMOUNT);
		Logger.d(String.format("Pick %d as spy", spyPosition+1));
		characters.set(spyPosition, "男爵");
		
		players = new ArrayList<String>();
		for(int i= 0; i < AMOUNT; i++){
			players.add(String.format("player_%d", i+1));
		}
		//==================================
		
		pcds = new ArrayList<ViewHolder>();
		for(int i = 0; i < AMOUNT; i++){
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
			ids.put(players.get(clickedItems.size()-1), characters.get(position));
			// Need to set user's image to replace R.drawable.logo
			ViewHolder viewHolder = new ViewHolder(players.get(clickedItems.size()-1), R.drawable.logo, R.drawable.basic);
			pcds.set(position, viewHolder);
			imageAdapter.notifyDataSetChanged();
			
			String message = null;
			if(clickedItems.size() < AMOUNT){
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
			new AlertDialog.Builder(GameActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle(getString(R.string.gaming_title) + characters.get(position))
            .setMessage(getString(R.string.gaming_msg))
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	//TODO 复杂的判断
                	/**
                	 * 0. 把头像背景改成半透明，附加到底牌之上
                	 * 1. 找到spy, 全部翻牌，已被杀的状态不变
                	 * 2. 找到平民，冤死
                	 * 3. 如果平民死亡数量超过2/3（包括当前用户)，则失败
                	 * 4. 如果已经按过该牌则跳过
                	 */
//                	if(position + 1 >= AMOUNT){
//        				message = "Please deliver the phone to owner";
//        				status = STATUS.GAMING;
//        				clicked.clear();
//        			}else{
//        				message = getString(R.string.pick_character_msg) + players.get(position+1);
//        			}
                	
                	if(spyPosition == position){
                		status = STATUS.GAME_OVER;
                		
                	}
                }
            }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
				}
            }).show();
			break;
		case GAME_OVER:
			break;
		}
		
	}


}
