package bing.support.whoisspy.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bing.support.whoisspy.R;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<ViewHolder> pcDatas = new ArrayList<ViewHolder>();
	
	public ImageAdapter(Context c, List<ViewHolder> pcDatas) {
		mContext = c;
		this.pcDatas = pcDatas;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View view = View.inflate(mContext, R.layout.player_card_layout, null);
		RelativeLayout playerCard = (RelativeLayout) view.findViewById(R.id.player_card);

		ImageView image = (ImageView) playerCard.findViewById(R.id.portrait);
		TextView text = (TextView) playerCard.findViewById(R.id.player_name);

		image.setImageResource(pcDatas.get(position).getFrontendImage());
		image.setBackgroundResource(pcDatas.get(position).getBackgroundImage());
		text.setText(pcDatas.get(position).getName());

		return playerCard;
	}
	
	@Override
	public int getCount() {
		return pcDatas.size();
	}
	
	
	@Override
	public Object getItem(int position) {
		return pcDatas.get(position);
	}
	
	public void setItem(int position, ViewHolder p) {
		pcDatas.set(position, p);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
}
