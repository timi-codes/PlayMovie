package tarrotsystem.com.playmovie.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tarrotsystem.com.playmovie.R;
import tarrotsystem.com.playmovie.model.MovieReviews;
import tarrotsystem.com.playmovie.model.Trailer;
import tarrotsystem.com.playmovie.utilities.Utils;

/**
 * Created by DOTECH on 12/06/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<MovieReviews> mFeedList;
    private Context mContext;

    public ReviewsAdapter(List<MovieReviews> feedList, Context context) {
        this.mFeedList = feedList;
        this.mContext = context;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_video, null);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        final MovieReviews movieReviews = mFeedList.get(position);
        holder.mReviewUser.setText(movieReviews.getAuthor());
        holder.mReviewContent.setText(movieReviews.getContent());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }


    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewUser, mReviewContent;
        private ImageView mAvatar;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mReviewUser = (TextView) itemView.findViewById(R.id.reviewUser);
            mReviewContent = (TextView) itemView.findViewById(R.id.reviewContent);
            mAvatar = (ImageView) itemView.findViewById(R.id.reviewAvatar);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setElevation(mAvatar, Utils.dpToPx(5, mContext));
                ViewCompat.setTranslationZ(mAvatar, Utils.dpToPx(5, mContext));

            } else {
                mAvatar.bringToFront(); // works on both pre-lollipop and Lollipop
            }
        }
    }

}
