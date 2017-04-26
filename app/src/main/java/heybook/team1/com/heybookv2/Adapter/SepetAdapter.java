package heybook.team1.com.heybookv2.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;

/**
 * Created by senaaltun on 24/04/2017.
 */

public class SepetAdapter extends RecyclerView.Adapter<SepetAdapter.ItemViewHolder> {
    private ArrayList<Data> sepetArrayList;
    private Context context;
    private LayoutInflater inflater;
    private double totalPrice=0;

    public SepetAdapter(Context context, ArrayList<Data> sepetArrayList) {
        this.context = context;
        this.sepetArrayList = sepetArrayList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View root = inflater.inflate(R.layout.item_sepet_row, parent, false);
        ItemViewHolder holder = new ItemViewHolder(root);
        Data sepetBookData = sepetArrayList.get(viewType);
        holder.sepetBookTitle.setText(sepetBookData.getBook_title());
        holder.sepetBookPrice.setText(sepetBookData.getPrice() + " TL");
        Glide.with(context)
                .load(sepetBookData.getPhoto())
                .into(holder.sepetBookImage);
        holder.deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sepetArrayList.remove(viewType);
                notifyItemRemoved(viewType);
                notifyItemRangeChanged(viewType,sepetArrayList.size());
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        Data sepetBookData = sepetArrayList.get(position);

        holder.sepetBookTitle.setText(sepetBookData.getBook_title());
        holder.sepetBookPrice.setText(sepetBookData.getPrice() + " TL");
        Glide.with(holder.sepetBookImage.getContext())
                .load(sepetBookData.getPhoto())
                .into(holder.sepetBookImage);
        holder.deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sepetArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,sepetArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sepetArrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sepetBookTitle;
        TextView sepetBookPrice;

        ImageView sepetBookImage;

        ImageView deleteBook;


        public ItemViewHolder(View view) {
            super(view);

            sepetBookTitle = (TextView) view.findViewById(R.id.sepetBookTitle);
            sepetBookPrice = (TextView) view.findViewById(R.id.sepetBookPrice);
            sepetBookImage = (ImageView) view.findViewById(R.id.sepetBookImage);
            deleteBook = (ImageView)view.findViewById(R.id.deleteBook);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
