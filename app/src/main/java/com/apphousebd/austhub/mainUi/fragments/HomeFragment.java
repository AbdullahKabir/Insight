/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:43 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.homeDataModel.HomeMenuData;
import com.apphousebd.austhub.mainUi.adapters.HomeMenuAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private HomeFragmentListener mHomeFragmentListener;

    private InterstitialAd mInterstitialAd;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof HomeFragmentListener)) throw new AssertionError();
        mHomeFragmentListener = (HomeFragmentListener) context;


        // setting up the ad unit
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(getString(R.string.full_screen_ad));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(
                        new AdRequest.Builder()
                                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                                .build()
                );
            }
        });
        mInterstitialAd.loadAd(
                new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build()
        );

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recView = view.findViewById(R.id.rec_list);

        view.findViewById(R.id.showAdBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }
        });

        HomeMenuAdapter adapter = new HomeMenuAdapter(getContext(), mHomeFragmentListener, HomeMenuData.getListData(getContext()));

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
