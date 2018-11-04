package com.lxh.library.modular.imageChoose;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxh.library.R;
import com.lxh.library.base.AppActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 图片选择
 */
public class ImageChooseActivity extends AppActivity {
    private List<ImageItem> mDataList = new ArrayList<>();
    private int availableSize;
    private GridView mGridView;
    private ImageGridAdapter mAdapter;
    private HashMap<String, ImageItem> selectedImgs = new HashMap<String, ImageItem>();
    private Intent intent;
    private ImageView back;
    private TextView tv_sure;

    private void initView() {
        back = findViewById(R.id.back);
        tv_sure = findViewById(R.id.tv_sure);
        mGridView = findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void initListener() {// ---------------------------------------完成-----------------------------------
        tv_sure.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, new ArrayList<>(selectedImgs.values()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            // ------------------------------------------------选择图片---------------------------------------------
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = mDataList.get(position);
                if (item.isSelected) {
                    item.isSelected = false;
                    selectedImgs.remove(item.imageId);
                } else {
                    if (selectedImgs.size() >= availableSize) {
                        Toast.makeText(ImageChooseActivity.this,
                                "本次最多选择" + availableSize + "张",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    item.isSelected = true;
                    selectedImgs.put(item.imageId, item);
                }
                mAdapter.notifyDataSetChanged();
            }

        });
        // ---------------------------------------------------------------取消-------------------------------------------------
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.act_image_choose;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        intent = getIntent();

        mDataList = (List<ImageItem>) intent.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        if (mDataList == null)
            mDataList = new ArrayList<>();
        String mBucketName = getIntent().getStringExtra(IntentConstants.EXTRA_BUCKET_NAME);
        if (TextUtils.isEmpty(mBucketName)) {
            mBucketName = "请选择";
        }
        initTitle(true, mBucketName, "确定");
        availableSize = getIntent().getIntExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, CustomConstants.MAX_IMAGE_SIZE);
        initView();
    }

    @Override
    public void onClick(@NotNull View v) {
        intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, new ArrayList<>(selectedImgs.values()));
        setResult(RESULT_OK, intent);
        finish();
    }
}