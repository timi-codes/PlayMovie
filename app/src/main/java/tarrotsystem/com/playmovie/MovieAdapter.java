package tarrotsystem.com.playmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import tarrotsystem.com.playmovie.utilities.JSONObjectUtil;
import tarrotsystem.com.playmovie.utilities.NetworkUtils;

/**
 * Created by DOTECH on 15/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private MovieAdapterOnClickListener mClickListener;
    private List<JSONObjectUtil.JSONResponse> movieList;
    private Context mContext;

    public MovieAdapter(Context context,MovieAdapterOnClickListener clickListener){
        this.mContext = context;
        this.mClickListener  = clickListener;

    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String imageUrl = NetworkUtils.POSTER_BASE_URL + movieList.get(position).getPoster_path();
        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.movie_placeholder)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.movieThumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList!=null ? movieList.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView movieThumbnail;

        public MovieViewHolder(View itemView){
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_item);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClicked(movieList.get(getLayoutPosition()));
        }
    }

    public void setMovieData(List<JSONObjectUtil.JSONResponse> movielist){
        this.movieList = movielist;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickListener{
        void onItemClicked(JSONObjectUtil.JSONResponse response);
    }
}
