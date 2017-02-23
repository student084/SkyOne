package com.student0.www.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.student0.www.Config;
import com.student0.www.skyone.R;

/**
 * Created by willj on 2017/2/23.
 */

public class VpSimpleFragment extends Fragment {

    private String title;
    private static final String BUNDLE_TITLE = "title";

    public static VpSimpleFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);

        VpSimpleFragment fragment = new VpSimpleFragment();
        fragment.setArguments(bundle);

        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取bundle
        Bundle bundle = getArguments();

        if (bundle != null){
            title = bundle.getString(BUNDLE_TITLE);

        }
        switch (title){
            case Config.TABLE_PHOTOS:
                return inflater.inflate(R.layout.photos, container, false);
            case Config.TABLE_FRIENDS:
                return inflater.inflate(R.layout.friends, container, false);
            default:
                return inflater.inflate(R.layout.shares, container, false);
        }
    }
}
