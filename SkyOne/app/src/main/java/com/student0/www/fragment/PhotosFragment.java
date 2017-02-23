package com.student0.www.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.student0.www.SimulateData;
import com.student0.www.adapter.MyRecycleViewAdapter;
import com.student0.www.bean.PerDatePhotos;
import com.student0.www.bean.Photo;
import com.student0.www.skyone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private static List<PerDatePhotos> DPList = new ArrayList<PerDatePhotos>();
    private MyRecycleViewAdapter recycleViewAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        //Get the view of the fragment
        View view = inflater.inflate(R.layout.photos, container, false);
        //Get the instance of recycleView
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(gridLayoutManager);
        //Add data to adapter ,so that the RecycleView haves pics and data to show instead of black
        //Data form is List<PerDataPhotos>
        //Init RecycleView's adapter
        DPList = initData();
        recycleViewAdapter = new MyRecycleViewAdapter(getActivity());
        recyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.AddList(DPList);
        recycleViewAdapter.notifyDataSetChanged();
        //Add adapter to RecycleView, so the RecycleView can get and show the data
        return view;
    }

    private List<PerDatePhotos> initData() {
        List<PerDatePhotos> perDatePhotosList = new ArrayList<>();
        //PerDatePhotos(int batch, String data, List<Photo> photos)
        //Photo(int photoId, String resourceURL)
        perDatePhotosList = new SimulateData().getPerDatePhotosList();

        return perDatePhotosList;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
