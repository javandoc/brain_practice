package com.yzk.practice_brain.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.yzk.practice_brain.R;
import com.yzk.practice_brain.adapter.SutableAdapter;
import com.yzk.practice_brain.application.GlobalApplication;
import com.yzk.practice_brain.base.BaseFragmentActivity;
import com.yzk.practice_brain.bean.SuTableResult;
import com.yzk.practice_brain.config.Config;
import com.yzk.practice_brain.log.LogUtil;
import com.yzk.practice_brain.network.HttpRequestUtil;
import com.yzk.practice_brain.ui.CircularProgressView;
import com.yzk.practice_brain.ui.Controller;
import com.yzk.practice_brain.utils.NetworkUtils;
import com.yzk.practice_brain.utils.ParseJson;
import com.yzk.practice_brain.utils.SoundEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

/**
 * Created by android on 11/24/16.
 */

public class SuTablePracticeActivity extends BaseFragmentActivity implements Controller.ControllerCallBack, ResponseStringDataListener {

    private static final int REQUEST_DATA_TASK = 0x1;

    @Bind(R.id.controlPanel)
    Controller controllPanel;

    @Bind(R.id.loading)
    CircularProgressView loading;
    private SutableAdapter sutableAdapter;

    @Bind(R.id.grid)
    GridView gridView;

    private List<SuTableResult.Table> sortTempList = new ArrayList<>();
    private ArrayList<SuTableResult.Table> randomList;
    private int index = 0;
    private int score=10;

    private Handler mHanlder = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sutable_layout);
    }

    @Override
    protected void uIViewInit() {

        controllPanel.setClickCallBack(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                assertResult(view, i);


            }

            private void assertResult(View view, int i) {
                if (index > sortTempList.size() - 1) {
                    index = sortTempList.size() - 1;
                }


                final RelativeLayout backGround = (RelativeLayout) view.findViewById(R.id.backgroud);
                TextView tvText = (TextView) view.findViewById(R.id.tvText);

                SuTableResult.Table o = randomList.get(i);
                SuTableResult.Table table = sortTempList.get(index);
                if (o.value.equals(table.value)) {
                    backGround.setBackgroundResource(R.drawable.home_bgview_blue);
                    if (index == sortTempList.size() - 1) {
                        Toast.makeText(SuTablePracticeActivity.this, "闯关成功", Toast.LENGTH_SHORT).show();
                        SoundEffect.getInstance().play(SoundEffect.SUCCESS);

                        forEachList();

                        return;
                    }
                    ++index;


                    Toast.makeText(SuTablePracticeActivity.this, "正确", Toast.LENGTH_SHORT).show();
                    SoundEffect.getInstance().play(SoundEffect.CORRECT);
                } else {
                    Toast.makeText(SuTablePracticeActivity.this, "错误", Toast.LENGTH_SHORT).show();
                    --score;
                    backGround.setBackgroundResource(R.drawable.home_bgview_green);
                    SoundEffect.getInstance().play(SoundEffect.FAIL);
                    if (score<0){
                        score=0;
                        forEachList();
                        return;
                    }
                    mHanlder.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backGround.setBackgroundResource(R.drawable.home_bgview_white);
                        }
                    }, 100);


                }


                LogUtil.e(o.key + ":" + backGround + ":" + tvText);
            }
        });


    }


    private void forEachList() {
        for (SuTableResult.Table table :
                randomList) {
            table.flag = true;
        }
        index=0;
        score=10;
        gridView.setEnabled(false);
        sutableAdapter.notifyDataSetChanged();
    }


    @Override
    protected void uIViewDataApply() {
        getDataFromNet();
    }

    private void getDataFromNet() {
        if (NetworkUtils.isConnected(this)) {
            loading.setVisibility(View.VISIBLE);
            HttpRequestUtil.HttpRequestByGet(Config.SUTABLE_PRACTICE_URL, this, REQUEST_DATA_TASK);
        } else {
            Toast.makeText(this, R.string.net_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void controll(View view) {
        switch (view.getId()) {
            case R.id.retry:
                getDataFromNet();
                gridView.setEnabled(true);
                break;
            case R.id.back:
                finish();
                break;

            case R.id.play:
                try {
                    if (GlobalApplication.instance.getiMediaInterface().isPlaying()) {
                        ((ImageButton) view).setSelected(false);
                        GlobalApplication.instance.getiMediaInterface().pause();
                    } else {
                        ((ImageButton) view).setSelected(true);
                        GlobalApplication.instance.getiMediaInterface().play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    /**
     * 将list顺序打乱
     *
     * @param sourceList
     * @return
     */
    private ArrayList randomList(ArrayList sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }
        ArrayList randomList = new ArrayList(sourceList.size());
        do {
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
        } while (sourceList.size() > 0);
        return randomList;
    }

    private boolean isEmpty(ArrayList sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    @Override
    public void onDataDelivered(int taskId, String data) {
        switch (taskId) {
            case REQUEST_DATA_TASK:
                loading.setVisibility(View.GONE);
                sortTempList.clear();
                SuTableResult suTableResult = ParseJson.parseJson(data, SuTableResult.class);
                if (null != suTableResult && null != suTableResult.data && suTableResult.data.fiveContentView.size() > 0) {
                    sortTempList.addAll(suTableResult.data.fiveContentView);

                    randomList = randomList(suTableResult.data.fiveContentView);
                    LogUtil.e(suTableResult.toString());
                    if (null == sutableAdapter) {

                        sutableAdapter = new SutableAdapter();
                        gridView.setAdapter(sutableAdapter);
                    }
                    sutableAdapter.setData(randomList);
                }

                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {
        Toast.makeText(this, R.string.request_error, Toast.LENGTH_SHORT).show();
        loading.setVisibility(View.GONE);
    }
}
