package com.hueljk.ibeacon.ui.home.category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.hueljk.ibeacon.R;
import com.hueljk.ibeacon.constants.UrlConstants;
import com.hueljk.ibeacon.mode.Clothes;
import com.hueljk.ibeacon.mode.Result;
import com.hueljk.ibeacon.ui.BaseFragment;
import com.hueljk.ibeacon.ui.adapter.MyAdpter_Clothes;
import com.hueljk.ibeacon.utils.DisplayUtils;
import com.hueljk.ibeacon.utils.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zc on 2017/2/7.
 */
public class TwoCloFragment extends BaseFragment implements View.OnClickListener {
    private GridView mGridView;
    private static OkHttpClient client;
    private MyAdpter_Clothes adapter;
    private ImageView home_img;
    List<Clothes> clothes = new ArrayList<>();
    private String mType;

    static {
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //LayoutInflater他是一个打气筒工具，将布局填充的view里面
        View v = inflater.inflate(R.layout.fragment_two_clo, container, false);
        return v;
    }

    private View menu1, menu2, menu3;
    private View menu_line_1, menu_line_2, menu_line_3;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mGridView = (GridView) view.findViewById(R.id.clothes_gridView);
        home_img = (ImageView) view.findViewById(R.id.two_cloreturn_img);

        menu1 = view.findViewById(R.id.twoclo_menu_1);
        menu2 = view.findViewById(R.id.twoclo_menu_2);
        menu3 = view.findViewById(R.id.twoclo_menu_3);

        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);

        menu_line_1 = view.findViewById(R.id.menu_line_1);
        menu_line_2 = view.findViewById(R.id.menu_line_2);
        menu_line_3 = view.findViewById(R.id.menu_line_3);

        //当前为选中状态的菜单下划线
        mCurLine = menu_line_1;
        mType="2";

        adapter = new MyAdpter_Clothes(mContext,null);
        mGridView.setAdapter(adapter);
    }

    @Override
    protected void setData() {

        super.setData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popSelf();

            }
        });
    }

    private View mCurLine;

    @Override
    public void onClick(View v) {
        //参数view 是被点击的控件
        /**
         *  1.将当前的线隐藏掉
         *  2.被点击的菜单的线要显示出来
         *  3.将当前的线跟新一下
         */

        mCurLine.setVisibility(View.INVISIBLE);

        switch (v.getId()) {
            case R.id.twoclo_menu_1:
                mType="2";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                menu_line_1.setVisibility(View.VISIBLE);
                mCurLine = menu_line_1;
                break;
            case R.id.twoclo_menu_2:
                mType="3";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                menu_line_2.setVisibility(View.VISIBLE);
                mCurLine = menu_line_2;
                break;
            case R.id.twoclo_menu_3:
                mType="4";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                menu_line_3.setVisibility(View.VISIBLE);
                mCurLine = menu_line_3;
                break;
        }
    }
    public void execute() throws Exception {
       String url=UrlConstants.twoCloUrl+"?type="+mType;
        Log.d("clourl-------",url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag", "error");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String ret = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("---------------", "run: "+ret);
                        Type listType = new TypeToken<Result<List<Clothes>>>() {
                        }.getType();
                        Result<List<Clothes>> listResult = JsonUtils.parse(ret, listType);
                        if (listResult.mCode == 200) {
                            List<Clothes> clothes = listResult.mData;
                            int height = clothes.size() / 2 * 220+60;
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
                            DisplayUtils.init(getContext());
                            params.height = DisplayUtils.dip2px(height);
                            mGridView.setLayoutParams(params);
                            adapter.update(clothes);

                        }


                        // 解析json数据得到bean

                    }
                });

            }
        });
    }


}
