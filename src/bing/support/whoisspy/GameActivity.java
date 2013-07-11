package bing.support.whoisspy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GameActivity extends Activity implements OnItemClickListener{
	
	private GridView gameCard = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        
        initLayout();
    }
    
	private void initLayout() {
		gameCard = (GridView)findViewById(R.id.game_card); 
		
		gameCard.setAdapter(new ImageAdapter(this));

		gameCard.setOnItemClickListener(this);
    }
    

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}


}
