package tarrotsystem.com.playmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tarrotsystem.com.playmovie.R;
import tarrotsystem.com.playmovie.model.Trailer;

/**
 * Created by DOTECH on 12/06/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    public interface Callbacks {
         void onVideoClick(Trailer movieVideos);
    }

    private Callbacks mCallbacks;
    private List<Trailer> mFeedList;
    private Context mContext;

    public VideosAdapter(List<Trailer> feedList, Context context) {
        this.mFeedList = feedList;
        this.mContext = context;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_video, null);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        final Trailer movieVideos = mFeedList.get(position);

        String videoUrl = "http://img.youtube.com/vi/" + movieVideos.getKey() + "/0.jpg";

        Glide.with(mContext)
                .load(videoUrl)
                .centerCrop()
                .into(holder.mVideoContainer);

        holder.mVideoTitle.setText(movieVideos.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCallbacks!=null) {
                    mCallbacks.onVideoClick(movieVideos);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        private ImageView mVideoContainer;
        private TextView mVideoTitle;

        public VideosViewHolder(View itemView) {
            super(itemView);
            mVideoContainer = (ImageView) itemView.findViewById(R.id.videoThumb);
            mVideoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
        }
    }

}
