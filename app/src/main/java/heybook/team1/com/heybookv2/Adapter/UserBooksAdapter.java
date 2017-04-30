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

import heybook.team1.com.heybookv2.Activity.Listen;
import heybook.team1.com.heybookv2.R;
import java.util.ArrayList;

import heybook.team1.com.heybookv2.Activity.SingleBook;
import heybook.team1.com.heybookv2.Model.Data;

/**
 * Created by Sena Altun on 7.01.2017.
 */

public class UserBooksAdapter extends RecyclerView.Adapter<UserBooksAdapter.ViewHolder> {

    private ArrayList<Data> data;
    private Context context;
    private LayoutInflater inflater;
    private String bookId;

    public UserBooksAdapter(Context context, ArrayList<Data> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View root = inflater.inflate(R.layout.vitrin_book_list_row, viewGroup, false);

        ViewHolder holder = new ViewHolder(root);
        Data bookData = data.get(position);
        holder.bookName.setText(bookData.getBook_title());
        holder.bookAuthor.setText(bookData.getAuthor_title());
        holder.bookDuration.setText(bookData.getDuration());
        Glide.with(context).load(bookData.getPhoto()).into(holder.bookImage);

        return holder;
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        Data bookData = data.get(position);
        holder.bookName.setText(bookData.getBook_title());
        holder.bookAuthor.setText(bookData.getAuthor_title());
        Glide.with(holder.bookImage.getContext()).load(bookData.getPhoto()).into(holder.bookImage);
    }

    @Override public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookName;
        TextView bookAuthor;
        TextView bookDuration;
        ImageView bookImage;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            bookName = (TextView) view.findViewById(R.id.bookName);
            bookAuthor = (TextView) view.findViewById(R.id.bookAuthor);
            bookImage = (ImageView) view.findViewById(R.id.bookImage);
            bookDuration = (TextView) view.findViewById(R.id.bookDuration);
        }

        @Override public void onClick(View view) {
            Intent intent = new Intent(context, Listen.class);
            int pos = this.getAdapterPosition();
            intent.putExtra("Position", pos);
            intent.putExtra("bookId",data.get(pos).getBook_id());
            intent.putExtra("bookTitle",data.get(pos).getBook_title());
            intent.putExtra("bookAuthor",data.get(pos).getAuthor_title());
            intent.putExtra("bookNarrator",data.get(pos).getNarrator_title());
            intent.putExtra("bookPhoto",data.get(pos).getPhoto());
            intent.putExtra("publisherTitle",data.get(pos).getPublisher_title());
            intent.putExtra("bookAudio",data.get(pos).getAudio());
            intent.putExtra("duration",data.get(pos).getDuration());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
