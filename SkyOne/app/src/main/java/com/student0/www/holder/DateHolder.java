package com.student0.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.student0.www.adapter.DataForm;
import com.student0.www.skyone.R;

/**
 * Created by willj on 2017/2/24.
 */

public class DateHolder extends RecyclerView.ViewHolder{
    public TextView date;

    public DateHolder(View itemView) {
        super(itemView);

        date = (TextView)itemView.findViewById(R.id.id_date);
    }

    public void bindHolder(DataForm dataForm){
        date.setText(dataForm.getData());
    }
}
