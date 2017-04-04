package heybook.team1.com.heybookv2.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;

/**
 * Created by senaaltun on 03/04/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {
    private ArrayList<Data> categoryData;
    private Context context;
    private LayoutInflater inflater;


    public CategoryAdapter(Context context, ArrayList<Data> categoryData) {
        this.context = context;
        this.categoryData = categoryData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CategoryAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.item_category, parent, false);

        ItemViewHolder holder = new ItemViewHolder(root);
        Data categoryBookData = categoryData.get(viewType);
        holder.categoryBookAuthor.setText(categoryBookData.getAuthor_title());
        holder.categoryTitle.setText(categoryBookData.getBook_title());
        Glide.with(context)
                .load(categoryBookData.getPhoto())
                .into(holder.categoryBookImage);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ItemViewHolder holder, int position) {
        final int pos = position;
        Data categoryBookData = categoryData.get(position);

        holder.categoryTitle.setText(categoryBookData.getBook_title());
        holder.categoryBookAuthor.setText(categoryBookData.getAuthor_title());
        Glide.with(holder.categoryBookImage.getContext())
                .load(categoryBookData.getPhoto())
                .into(holder.categoryBookImage);
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryTitle;
        TextView categoryBookAuthor;
        ImageView categoryBookImage;


        public ItemViewHolder(View view) {
            super(view);

            categoryTitle = (TextView) view.findViewById(R.id.categoryBookTitle);
            categoryBookAuthor = (TextView) view.findViewById(R.id.categoryBookAuthor);
            categoryBookImage = (ImageView) view.findViewById(R.id.categoryBookImage);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
