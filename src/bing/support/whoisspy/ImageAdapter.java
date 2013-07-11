package bing.support.whoisspy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View view = View.inflate(mContext, R.layout.player_card_layout, null);
		RelativeLayout playerCard = (RelativeLayout) view.findViewById(R.id.player_card);

		ImageView image = (ImageView) playerCard.findViewById(R.id.portrait);
		TextView text = (TextView) playerCard.findViewById(R.id.player_name);

		image.setImageResource(mThumbIds[position]);
		text.setText(mTextValues[position]);

		return playerCard;
	}

	// references to our images
	private Integer[] mThumbIds = { 
			R.drawable.ic_launcher, 
			R.drawable.ic_launcher, 
			R.drawable.ic_launcher};

	private String[] mTextValues = {
			"Button", 
			"Button", 
			"TextView" };

	@Override
	public Object getItem(int position) {
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
