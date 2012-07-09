package pl.mu.adapter;

import java.util.List;

import pl.mu.R;
import pl.mu.data.ReminderObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReminderArrayAdapter extends ArrayAdapter<ReminderObject> {
	private Context context;
	private List<ReminderObject> data;
	
	public ReminderArrayAdapter(Context context,List<ReminderObject> data) {
		super(context, R.layout.list_item, data);   
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder viewHolder = null;
				
		if(rowView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			
			rowView = inflater.inflate(R.layout.list_item, null);
			
			viewHolder = new ViewHolder();
		    viewHolder.title = (TextView) rowView.findViewById(R.id.textview_item_title);
			viewHolder.description =(TextView) rowView.findViewById(R.id.textview_item_description);
		   
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}
		
		viewHolder.title.setText(data.get(position).title);
		viewHolder.description.setText(data.get(position).description);
		
		return rowView;
	}
	
	static class ViewHolder{
		public TextView title;
		public TextView description;
	}
}
