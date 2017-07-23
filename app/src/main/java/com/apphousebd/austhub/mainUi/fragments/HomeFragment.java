package com.apphousebd.austhub.mainUi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.homeDataModel.HomeMenuData;
import com.apphousebd.austhub.mainUi.adapters.HomeMenuAdapter;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recView;
    private HomeMenuAdapter adapter;

    private HomeFragmentListener mHomeFragmentListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof HomeFragmentListener)) throw new AssertionError();
        mHomeFragmentListener = (HomeFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recView = (RecyclerView) view.findViewById(R.id.rec_list);

        adapter = new HomeMenuAdapter(getContext(), mHomeFragmentListener, HomeMenuData.getListData(getContext()));

        recView.setAdapter(adapter);

        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            recView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }

        recView.setHasFixedSize(true);

        return view;
    }


    public interface HomeFragmentListener {
        void onButtonClickListener(int index);
    }

}
