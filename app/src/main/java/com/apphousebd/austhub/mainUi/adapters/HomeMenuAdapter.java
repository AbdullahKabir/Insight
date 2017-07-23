package com.apphousebd.austhub.mainUi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.homeDataModel.HomeMenuListItem;
import com.apphousebd.austhub.mainUi.fragments.HomeFragment;

import java.util.List;

/**
 * Created by SayefReyadh on 12/26/2016.
 */

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.HomeItemHolder> {

    private List<HomeMenuListItem> listData;
    private LayoutInflater inflater;
    private HomeFragment.HomeFragmentListener mHomeFragmentListener;
    private Context mContext;
    private int lastPosition = -1;


    public HomeMenuAdapter(Context context, HomeFragment.HomeFragmentListener listener, List<HomeMenuListItem> listData) {
        mHomeFragmentListener = listener;
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.homemenu_list_item, parent, false);
        return new HomeItemHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        HomeMenuListItem item = listData.get(position);
        holder.mImageView.setImageResource(item.getImageResId());
        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class HomeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private LinearLayout container;
        private ImageView mImageView;

        HomeItemHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.cont_item_root);
            mImageView = (ImageView) itemView.findViewById(R.id.im_item_icon);
            title = (TextView) itemView.findViewById(R.id.home_item_title_text_view);

            Typeface typeface = Typeface.createFromAsset(
                    mContext.getAssets(), "fonts/SofadiOne-Regular.ttf");

            title.setTypeface(typeface);

            container.setOnClickListener(this);
            title.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();

            mHomeFragmentListener.onButtonClickListener(index + 1);
        }
    }
}
