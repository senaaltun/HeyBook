package heybook.team1.com.heybookv2.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Model.Favorite;
import heybook.team1.com.heybookv2.R;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Text;

/**
 * Created by SenaAltun on 04/04/2017.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
  private Context context;
  private List<Favorite> favoritesBookList;
  private LayoutInflater inflater;

  public FavoritesAdapter(Context context, List<Favorite> favoritesBookList) {
    this.context = context;
    this.favoritesBookList = favoritesBookList;
    inflater = LayoutInflater.from(context);
  }

  @Override public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.item_favorites, parent, false);

    ViewHolder holder = new ViewHolder(view);

    holder.favBookAuthor.setText(favoritesBookList.get(viewType).getBookAuthor());
    holder.favBookTitle.setText(favoritesBookList.get(viewType).getBookName());
    holder.favBookDuration.setText(favoritesBookList.get(viewType).getBookDuration());
    Glide.with(context).load(favoritesBookList.get(viewType).getBookImage()).into(holder.favBookImage);

    return holder;
  }

  @Override public void onBindViewHolder(FavoritesAdapter.ViewHolder holder, int position) {

    Favorite favBookData = favoritesBookList.get(position);

    holder.favBookTitle.setText(favBookData.getBookName());
    holder.favBookAuthor.setText(favBookData.getBookAuthor());
    holder.favBookDuration.setText(favBookData.getBookDuration());
    Glide.with(context).load(favBookData.getBookImage()).into(holder.favBookImage);
  }

  @Override public int getItemCount() {
    return favoritesBookList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView favBookTitle;
    TextView favBookAuthor;
    TextView favBookDuration;

    ImageView favBookImage;

    public ViewHolder(View view) {
      super(view);

      favBookTitle = (TextView) view.findViewById(R.id.favBookName);
      favBookAuthor = (TextView) view.findViewById(R.id.favBookAuthor);
      favBookDuration = (TextView)view.findViewById(R.id.favBookDuration);
      favBookImage = (ImageView) view.findViewById(R.id.favBookImage);
    }
  }
}
