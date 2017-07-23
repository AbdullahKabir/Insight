package com.apphousebd.austhub.mainUi.fragments.result;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.utilities.OnlineResultFetch;

import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_DEPT;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_SEMESTER;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_YEAR;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabResultFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {


    private WebView mLabWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
    public void onRefresh() {
        loadWebView();
    }

    private void loadWebView() {
        OnlineResultFetch.loadUrl(
                getContext(),
                OnlineResultFetch.LAB_RESULT_BASE_URL + STD_DEPT + "_s_" +
                        String.valueOf(STD_YEAR) + "_" + String.valueOf(STD_SEMESTER) +
                        ".php",
//                "https://www.google.com",
                mLabWebView,
                mSwipeRefreshLayout
        );
    }

}
