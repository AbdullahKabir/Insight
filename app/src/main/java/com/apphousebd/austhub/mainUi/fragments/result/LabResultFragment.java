/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:41 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.fragments.result;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.utilities.OnlineResultFetch;
import com.apphousebd.austhub.utilities.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabResultFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {


    private WebView mLabWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private UserModel mUserModel;

    public LabResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab_result, container, false);

        mLabWebView = (WebView) view.findViewById(R.id.lab_result_web_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.lab_result_swipe);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadWebView();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserModel = Utils.getUserModel(getActivity().getApplicationContext());
        if (mUserModel == null)
            getActivity().finish();
    }

    @Override
    public void onRefresh() {
        loadWebView();
    }

    private void loadWebView() {
        OnlineResultFetch.loadUrl(
                getContext(),
                OnlineResultFetch.LAB_RESULT_BASE_URL +
                        mUserModel.getDept() + "_s_" +
                        mUserModel.getYear() + "_" + mUserModel.getSemester() +
                        ".php",
//                "https://www.google.com",
                mLabWebView,
                mSwipeRefreshLayout
        );
    }

}
