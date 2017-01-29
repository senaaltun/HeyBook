package heybook.team1.com.heybookv2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import heybook.team1.com.heybookv2.Activity.SingleBook;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;

/**
 * Created by Sena Altun on 29.01.2017.
 */

public class ResultBookAdapter extends RecyclerView.Adapter<ResultBookAdapter.ViewHolder> {
    private ArrayList<Data> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public ResultBookAdapter(Context context, ArrayList<Data> data){
        this.context=context;
        this.data=data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ResultBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View root = layoutInflater.inflate(R.layout.search_result_single_book, parent, false);

        ResultBookAdapter.ViewHolder holder = new ResultBookAdapter.ViewHolder(root);
        Data bookData = data.get(position);
        holder.resultBookName.setText(bookData.getBook_title());
        holder.resultBookAuthor.setText(bookData.getAuthor_title());
        Glide.with(context)
                .load(bookData.getPhoto())
                .into(holder.resultBookImage);


        return holder;
    }

    @Override
    public void onBindViewHolder(ResultBookAdapter.ViewHolder holder, int position) {
        final int pos = position;
        Data bookData = data.get(position);
        Log.d("Book Author",bookData.getAuthor_title());

        holder.resultBookName.setText(bookData.getBook_title());
        holder.resultBookAuthor.setText("Yazar: " + bookData.getAuthor_title());
        holder.resultBookPrice.setText("Fiyat: " + bookData.getPrice()+" TL");
        Glide.with(holder.resultBookImage.getContext())
                .load(bookData.getPhoto())
                .into(holder.resultBookImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView resultBookName;
        TextView resultBookAuthor;
        TextView resultBookPrice;
        ImageView resultBookImage;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            resultBookName = (TextView)view.findViewById(R.id.resultBookName);
            resultBookAuthor = (TextView)view.findViewById(R.id.resultBookAuthor);
            resultBookImage = (ImageView)view.findViewById(R.id.resultBookImage);
            resultBookPrice = (TextView)view.findViewById(R.id.resultBookPrice);
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
