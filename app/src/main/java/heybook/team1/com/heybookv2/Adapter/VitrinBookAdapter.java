package heybook.team1.com.heybookv2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import heybook.team1.com.heybookv2.Activity.SingleBook;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;

/**
 * Created by Nokta on 7.01.2017.
 */

public class VitrinBookAdapter extends RecyclerView.Adapter<VitrinBookAdapter.ViewHolder>  {

    private ArrayList<Data> data;
    private Context context;
    private LayoutInflater inflater;


    public VitrinBookAdapter(Context context,ArrayList<Data> data){
        this.context = context;
        this.data=data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View root = inflater.inflate(R.layout.vitrin_book_list_row, viewGroup, false);

        ViewHolder holder = new ViewHolder(root);
        Data bookData = data.get(position);
        holder.bookName.setText(bookData.getBook_title());
        holder.bookAuthor.setText(bookData.getAuthor_title());
        Glide.with(context)
                .load(bookData.getPhoto())
                .into(holder.bookImage);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        Data bookData = data.get(position);
        Log.d("Book Author",bookData.getAuthor_title());

        holder.bookName.setText(bookData.getBook_title());
        holder.bookAuthor.setText("Yazar: " + bookData.getAuthor_title());
        holder.price.setText("Fiyat: " + bookData.getPrice()+" TL");
        Glide.with(holder.bookImage.getContext())
                .load(bookData.getPhoto())
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bookName;
        TextView bookAuthor;
        TextView price;
        ImageView bookImage;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            bookName = (TextView)view.findViewById(R.id.bookName);
            bookAuthor = (TextView)view.findViewById(R.id.bookAuthor);
            bookImage = (ImageView)view.findViewById(R.id.bookImage);
            price = (TextView)view.findViewById(R.id.price);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,SingleBook.class);
            int pos = this.getAdapterPosition();
            intent.putExtra("Position",pos);
            context.startActivity(intent);
            Log.d("Position",String.valueOf(pos));
        }
    }





}
