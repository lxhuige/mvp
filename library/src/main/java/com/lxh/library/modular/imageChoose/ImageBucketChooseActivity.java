package com.lxh.library.modular.imageChoose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lxh.library.R;
import com.lxh.library.base.AppActivity;

import com.lxh.library.base.NullPresenter;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择相册
 */
public class ImageBucketChooseActivity extends AppActivity {
    private ImageFetcher mHelper;
    private List<ImageBucket> mDataList = new ArrayList<>();
    private ImageBucketAdapter mAdapter;
    private int availableSize;
    private Intent intent;

    private void initData() {
        mDataList = mHelper.getImagesBucketList(false);
        intent = getIntent();
        availableSize = intent.getIntExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, CustomConstants.MAX_IMAGE_SIZE);


    }

    private void initView() {
        ListView mListView =findViewById(R.id.listview);
        mAdapter = new ImageBucketAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            //---------------------------------------------------------跳转到选择图片----------------------------------------------
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectOne(position);
                Intent intent = new Intent(ImageBucketChooseActivity.this, ImageChooseActivity.class);
                intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                        (Serializable) mDataList.get(position).imageList);
                intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME,
                        mDataList.get(position).bucketName);
                intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, availableSize);
                startActivityForResult(intent, 456);
            }
        });
    }

    //-------------------------------------------接收返回的集合---------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 456 && resultCode == RESULT_OK) {
            @SuppressWarnings("unchecked")
            List<ImageItem> incomingDataList = (List<ImageItem>) data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
            intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, (Serializable) incomingDataList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void selectOne(int position) {
        int size = mDataList.size();
        for (int i = 0; i != size; i++) {
            mDataList.get(i).selected = i == position;
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public int getContentView() {
        return R.layout.act_image_bucket_choose;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        initTitle("相册");
        mHelper = ImageFetcher.getInstance(getApplicationContext());
        initData();
        initView();
    }

    @Nullable
    @Override
    public NullPresenter createPresenter() {
        return null;
    }
}
