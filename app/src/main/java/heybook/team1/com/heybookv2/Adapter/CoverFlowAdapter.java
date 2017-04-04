package heybook.team1.com.heybookv2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import heybook.team1.com.heybookv2.Activity.SingleBook;
import heybook.team1.com.heybookv2.Model.VitrinBookEntity;
import heybook.team1.com.heybookv2.R;

/**
 * Created by senaaltun on 02/04/2017.
 */

public class CoverFlowAdapter extends BaseAdapter {
    private ArrayList<VitrinBookEntity> vitrinData = new ArrayList<>();
    private Context context;


    public CoverFlowAdapter(Context context,ArrayList<VitrinBookEntity> data){
        this.context=context;
        vitrinData = data;
    }

    @Override
    public int getCount() {
        return vitrinData.size();
    }

    @Override
    public Object getItem(int position) {
        return vitrinData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_coverflow, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView)rowView.findViewById(R.id.label);
            viewHolder.image = (ImageView)rowView.findViewById(R.id.vitrinImage);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder)rowView.getTag();

        holder.text.setText(vitrinData.get(position).getTitle());
        Glide.with(context)
                .load(vitrinData.get(position).getImageUrl())
                .into(holder.image);

        return rowView;
    }
    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }
}
