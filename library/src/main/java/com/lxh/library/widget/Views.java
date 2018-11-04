package com.lxh.library.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author 李校辉 on 2017/3/1.
 */

public interface Views {
    interface onItemClickListener {
        void onItemClick(RecyclerView.Adapter<?> parent, View view, int position, long id);
    }

    interface onClickStringListener {
        void onClickString(String str);
    }

    interface onItemClickPosListener {
        void onItemClickPos(int pos);
    }


    interface onScrollChangeListener {
        void onItemClickPos(float scale);
    }

    interface onItemListener {
        void onItemClickPos(int pos, View view);
    }

    interface onItemLongListener {
        void onItemClickPos(int pos, View view);
    }

    interface onChoicePopWindowListener {
        void onItemClickPos(int pos, String text);
    }
    interface choicePopWindowListener{
        String[] getPopValues();

    }

}
