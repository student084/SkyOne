package com.student0.www.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.student0.www.Config;
import com.student0.www.adapter.TempPhotosAdapter;
import com.student0.www.skyone.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class TempFragment extends Fragment {

    private GridView gridView;
    private File currentDir;
    private List<String> photosName;
    private TempPhotosAdapter photosAdapter;
    private View view;
    //ProgressDialog mProgressDialog;
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.temps, container, false);
        gridView = (GridView) view.findViewById(R.id.id_gridView);

        initDatas();

        photosAdapter = new TempPhotosAdapter(getContext());
        //mProgressDialog = mProgressDialog.show(view.getContext(),null, "Loading ...");
        //mProgressDialog.dismiss();
        gridView.setAdapter(photosAdapter);
        return view;
    }

    public void initDatas(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getContext(), "当前存储卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        currentDir = new File(Config.PHOTOS_DIR);
        photosName = Arrays.asList(currentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".jpg")
                        || name.endsWith(".jpeg")
                        ||name.endsWith(".png"))
                    return true;
                return false;
            }
        }));
    }


    public void notifyView(){
        photosAdapter.notifyDataSetChanged();
    }
}
