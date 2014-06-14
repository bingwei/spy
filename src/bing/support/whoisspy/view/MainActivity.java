package bing.support.whoisspy.view;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bing.support.whoisspy.R;
import bing.support.whoisspy.constant.Key;
import bing.support.whoisspy.constant.Limit;
import bing.support.whoisspy.model.Character;
import bing.support.whoisspy.utils.Logger;

public class MainActivity extends Activity implements OnItemSelectedListener,
		OnClickListener, OnSeekBarChangeListener{
	
	//only for debug
	private TextView debug_text = null;
	private EditText major_name = null;
	private EditText spy_name = null;
	private EditText couple_id = null;
	private Button insert = null;
	private Button show = null;
	private Button delete = null;
	
	private Character couple = null;
	
	private Button start_game = null;
	private EditText et_major_name = null;
	private EditText et_spy_name = null;
	private Spinner sp_spy_num = null;
	private Spinner sp_mf_num = null;
	private EditText all_player_num = null;
	private SeekBar all_player_seeker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init_layout();
    }
    
    private void init_layout(){
    	
    	debug_text = (TextView)findViewById(R.id.debug_text);
    	
    	major_name = (EditText)findViewById(R.id.major_name);
    	spy_name = (EditText)findViewById(R.id.spy_name);
    	couple_id = (EditText)findViewById(R.id.couple_id);
    	
    	insert = (Button)findViewById(R.id.add_couple);
    	show = (Button)findViewById(R.id.show_couple);
    	delete = (Button)findViewById(R.id.remove_couple);
    	
    	insert.setOnClickListener(this);
    	show.setOnClickListener(this);
    	delete.setOnClickListener(this);
    	
    	//----------------------------------------------
    	
    	et_spy_name = (EditText)findViewById(R.id.et_spy_name);
    	et_major_name = (EditText)findViewById(R.id.et_major_name);
    	start_game = (Button)findViewById(R.id.btn_start_game);
    	start_game.setOnClickListener(this);
    	
    	sp_spy_num = (Spinner)findViewById(R.id.sp_spy_num);
    	sp_mf_num = (Spinner)findViewById(R.id.sp_mf_num);
    	all_player_num = (EditText)findViewById(R.id.all_player_num);
    	all_player_seeker = (SeekBar)findViewById(R.id.all_player_seeker);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
    	        R.array.spies_array, android.R.layout.simple_spinner_dropdown_item);
    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	sp_spy_num.setAdapter(adapter);
    	sp_spy_num.setSelection(1);
    	sp_mf_num.setAdapter(adapter);
    	
    	sp_spy_num.setOnItemSelectedListener(this);
    	sp_mf_num.setOnItemSelectedListener(this);
    	all_player_seeker.setOnSeekBarChangeListener(this);
    	all_player_num.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            	if(!s.toString().isEmpty()){
            		all_player_seeker.setProgress(Integer.valueOf(s.toString()) - Limit.MIN_AMOUNT);
            		all_player_num.setSelection(s.toString().length());//输入过程中获取长度
            	}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
         });
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
    	String prefix = "not found";
    	switch(parent.getId()){
    	case R.id.sp_spy_num:
    		prefix = "spy";
    		break;
    	case R.id.sp_mf_num:
    		prefix = "multi-facial";
    		break;
    	default:
    		break;
    	}
    	if(parent !=null){
    		debug_text.setText(String.format("%s: %s; view id: %d, id: %d; sp_spy_num: %d, sp_mf_num: %d",
    				prefix, (String)parent.getItemAtPosition(pos), view.getId(), id, R.id.sp_spy_num, R.id.sp_mf_num));
    	}
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(View arg0) {
		String majorName = major_name.getText().toString();
		String spyName = spy_name.getText().toString();
		String id = couple_id.getText().toString();
		couple = new Character(majorName, spyName);
		StringBuffer content = new StringBuffer();
    	switch(arg0.getId()){
    	case R.id.add_couple:
    		Character.create(majorName, spyName, this);
    		content.append("add");
    		break;
    	case R.id.show_couple:
    		if(couple_id.getText().length() != 0){
    			couple = Character.findById(Long.valueOf(id), this);
    			if(couple != null){
    				content.append(String.format("Found '%s: %s'", couple.getMajorName(), couple.getSpyName()));
    			}else{
    				content.append("Not found");
    			}
    		}else{
    			List<String> majorNames = Character.getAllMajorNames(this);
    			for(String c: majorNames){
    				content.append(c + "\n");
    			}
    		}
    		break;
    	case R.id.remove_couple:
    		if(couple_id.getText().length() != 0){
	    		couple = Character.findById(Long.valueOf(id), this);
	    		couple.delete(this);
	    		content.append("removed");
    		}else{
    			Character.deleteAll(this);
    			content.append("Remove all");
    		}
    		break;
    	//======================================
    	case R.id.btn_start_game:
    		String sMajorName = et_major_name.getText().toString();
    		String sSpyName = et_spy_name.getText().toString();
    		int sAmount = Integer.valueOf(all_player_num.getText().toString());
    		int sSpyNum = sp_spy_num.getSelectedItemPosition();
    		int sMfNum = sp_mf_num.getSelectedItemPosition();
    		Intent intent = new Intent(this, GameActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		Logger.d("major name: " + sMajorName);
    		Logger.d("spy name: " + sSpyName);
    		Logger.d("amount: " + sAmount);
    		Logger.d("amount/4.0: " + Math.ceil(sAmount/4.0));
    		Logger.d("spy number: " + sSpyNum);
    		Logger.d("mf number: " + sMfNum);
    		intent.putExtra(Key.MAJOR_NAME, sMajorName);
    		intent.putExtra(Key.SPY_NAME, sSpyName);
    		intent.putExtra(Key.AMOUNT, sAmount);
    		intent.putExtra(Key.SPY_AMOUNT, sSpyNum);
    		intent.putExtra(Key.MULTIFACE_AMOUNT, sMfNum);
    		//TODO 判断数量是否符合规则
    		String warningInfo = null;
    		if(sSpyNum == 0 && sMfNum == 0){
    			warningInfo = getString(R.string.warning_must_pick_one_special_character);
    		}else if(sSpyNum + sMfNum > Math.ceil(sAmount/4.0)) {
    			warningInfo = getString(R.string.warning_pick_too_many_special_characters);
    		}else if(sMajorName.length()==0 || sSpyName.length()==0){
    			warningInfo = getString(R.string.warning_character_is_empty);
    		}else if(sMajorName.equals(sSpyName)){
    			warningInfo = getString(R.string.warning_special_character_is_same_with_normal);
    		}
    		if(warningInfo == null){
    			this.startActivity(intent);
    		}else{
    			Toast.makeText(getApplicationContext(), warningInfo, Toast.LENGTH_SHORT).show();
    		}
    		break;
    	default:
    		break;
    	}
    	
    	debug_text.setText(content);
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		all_player_num.setText(String.valueOf(progress + Limit.MIN_AMOUNT));
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}


    
}
