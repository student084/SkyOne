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
import com.student0.www.adapter.InterPhotosRecycleViewAdapter;
import com.student0.www.bean.PerDatePhotos;
import com.student0.www.skyone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/2/23.
 */

public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private static List<PerDatePhotos> DPList = new ArrayList<PerDatePhotos>();
    private InterPhotosRecycleViewAdapter recycleViewAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);

        //Get the view of the fragment
        View view = inflater.inflate(R.layout.photos, container, false);

        //Get the instance of recycleView
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle);


        //分类设置换行情况
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            //返回值为跨度值，及分裂数的倒数视为一个跨度值的单位
            @Override
            public int getSpanSize(int position) {
                int type = recyclerView.getAdapter().getItemViewType(position);
                if (type == InterPhotosRecycleViewAdapter.TYPE_DATE){
                    //返回分列数*跨度值，即为占据一行
                    return gridLayoutManager.getSpanCount();
                }else{
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);

        //Add data to adapter ,so that the RecycleView haves pics and data to show instead of black
        //Data form is List<PerDataPhotos>
        //Init RecycleView's adapter
        DPList = initData();
        recycleViewAdapter = new InterPhotosRecycleViewAdapter(getActivity());
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
