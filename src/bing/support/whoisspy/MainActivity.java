package bing.support.whoisspy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemSelectedListener{
	
	//only for debug
	private TextView debug_text = null;
	
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

    
}
