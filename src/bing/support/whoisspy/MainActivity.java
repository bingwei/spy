package bing.support.whoisspy;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import bing.support.whoisspy.model.Couple;

public class MainActivity extends Activity implements OnItemSelectedListener, OnClickListener{
	
	//only for debug
	private TextView debug_text = null;
	private EditText major_name = null;
	private EditText spy_name = null;
	private EditText couple_id = null;
	private Button insert = null;
	private Button show = null;
	private Button delete = null;
	
	private Couple couple = null;
	
	private Spinner sp_spy_num = null;
	private Spinner sp_mf_num = null;

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
    	
    	
    	sp_spy_num = (Spinner)findViewById(R.id.sp_spy_num);
    	sp_mf_num = (Spinner)findViewById(R.id.sp_mf_num);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
    	        R.array.spies_array, android.R.layout.simple_spinner_dropdown_item);
    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	sp_spy_num.setAdapter(adapter);
    	sp_mf_num.setAdapter(adapter);
    	
    	sp_spy_num.setOnItemSelectedListener(this);
    	sp_mf_num.setOnItemSelectedListener(this);
    	
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
    	debug_text.setText(String.format("%s: %s; view id: %d, id: %d; sp_spy_num: %d, sp_mf_num: %d",
    			prefix, (String)parent.getItemAtPosition(pos), view.getId(), id, R.id.sp_spy_num, R.id.sp_mf_num));
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		String majorName = major_name.getText().toString();
		String spyName = spy_name.getText().toString();
		String id = couple_id.getText().toString();
		couple = new Couple(majorName, spyName);
		StringBuffer content = new StringBuffer();
    	switch(arg0.getId()){
    	case R.id.add_couple:
    		Couple.create(majorName, spyName, this);
    		content.append("add");
    		break;
    	case R.id.show_couple:
    		if(couple_id.getText().length() != 0){
    			couple = Couple.findById(Long.valueOf(id), this);
    			if(couple != null){
    				content.append(String.format("Found '%s: %s'", couple.getMajorName(), couple.getSpyName()));
    			}else{
    				content.append("Not found");
    			}
    		}else{
    			List<String> majorNames = Couple.getAllMajorNames(this);
    			for(String c: majorNames){
    				content.append(c + "\n");
    			}
    		}
    		break;
    	case R.id.remove_couple:
    		if(couple_id.getText().length() != 0){
	    		couple = Couple.findById(Long.valueOf(id), this);
	    		couple.delete(this);
	    		content.append("removed");
    		}else{
    			Couple.deleteAll(this);
    			content.append("Remove all");
    		}
    		break;
    	default:
    		break;
    	}
    	
    	debug_text.setText(content);
		
	}

    
}
