package com.hueljk.ibeacon.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.hueljk.ibeacon.R;
import com.hueljk.ibeacon.constants.UrlConstants;
import com.hueljk.ibeacon.manager.PreferenceManager;
import com.hueljk.ibeacon.mode.BaseEntity;
import com.hueljk.ibeacon.mode.Goods;
import com.hueljk.ibeacon.mode.Home;
import com.hueljk.ibeacon.mode.Result;
import com.hueljk.ibeacon.ui.BaseFragment;
import com.hueljk.ibeacon.ui.adapter.HomeAdapter;
import com.hueljk.ibeacon.ui.adapter.MyAdapter;
import com.hueljk.ibeacon.ui.home.banner.BannerHfpFragment;
import com.hueljk.ibeacon.ui.home.banner.BannerImageViewUtils;
import com.hueljk.ibeacon.ui.home.banner.BannerNzFragment;
import com.hueljk.ibeacon.ui.home.banner.BannerXsFragment;
import com.hueljk.ibeacon.ui.home.banner.CycleViewPager;
import com.hueljk.ibeacon.ui.home.banner.ImageCycleViewListener;
import com.hueljk.ibeacon.ui.home.category.TwoFreshFragment;
import com.hueljk.ibeacon.ui.home.category.TwoCloFragment;
import com.hueljk.ibeacon.ui.home.category.TwoFoodFragment;
import com.hueljk.ibeacon.ui.home.category.TwoRyFragment;
import com.hueljk.ibeacon.ui.home.discount.TwoPaperFragment;
import com.hueljk.ibeacon.ui.home.discount.TwoWineFragment;
import com.hueljk.ibeacon.ui.home.discount.TwopuffFragment;
import com.hueljk.ibeacon.ui.setting.LoginFragment;
import com.hueljk.ibeacon.utils.DisplayUtils;
import com.hueljk.ibeacon.utils.JsonUtils;
import com.hueljk.ibeacon.wigdet.EditTextDrawableClick;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private GridView mGridView;
    private static OkHttpClient client;
    private HomeAdapter mAdapter;
    private ImageView mCategoryImage;
    private EditText mEditText;
    private TextView shipin_tv;
    private TextView riyong_tv;
    private TextView clothes_tv;
    private TextView shengxian_tv;
    private ImageView homezp_img;
    private ImageView homemj_img;
    private ImageView homeph_img;
    private Context mContext;
    private CycleViewPager mCycleViewPager;
    private FrameLayout mViewPagerFragmentLayout;
    private List<Goods> goods;
    private PreferenceManager mPreferenceManager;
    private String mSearchKeys;
    private EditTextDrawableClick mSearchET;

    static {
        client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPreferenceManager = PreferenceManager.getInstance();
        //hideSoftKeyboard();
        mCategoryImage=(ImageView)view.findViewById(R.id.category_img);
        mGridView = (GridView) view.findViewById(R.id.product_gridView);
        shipin_tv = (TextView) view.findViewById(R.id.shipin_tv);
        riyong_tv = (TextView) view.findViewById(R.id.riyong_tv);
        clothes_tv = (TextView) view.findViewById(R.id.clothes_tv);
        shengxian_tv = (TextView) view.findViewById(R.id.shengxian_tv);
        homezp_img = (ImageView) view.findViewById(R.id.homezp_img);
        homemj_img = (ImageView) view.findViewById(R.id.homemj_img);
        homeph_img = (ImageView) view.findViewById(R.id.homeph_img);
        mSearchET=(EditTextDrawableClick)view.findViewById(R.id.SearchEt);
        mContext = getContext();
        // 找到轮播控件
        mCycleViewPager = (CycleViewPager) getChildFragmentManager()
                .findFragmentById(R.id.main_fragment_banner);
        mAdapter = new HomeAdapter(getContext(), null);
        mGridView.setAdapter(mAdapter);
        initBanner();


    }


    @Override
    protected void setListener() {
        super.setListener();
        mCategoryImage.setOnClickListener(this);
        clothes_tv.setOnClickListener(this);
        riyong_tv.setOnClickListener(this);
        shipin_tv.setOnClickListener(this);
        shengxian_tv.setOnClickListener(this);
        homemj_img.setOnClickListener(this);
        homezp_img.setOnClickListener(this);
        homeph_img.setOnClickListener(this);

        mSearchET.setDrawableLeftListener(new EditTextDrawableClick.DrawableLeftListener() {
            @Override
            public void onDrawableLeftClick(View view) {
               // Toast.makeText(getContext(),"点击搜索",Toast.LENGTH_SHORT).show();
                mSearchKeys=mSearchET.getText().toString();
                mSearchET.setText("");
                Log.d("---------","SearchKeys:"+mSearchKeys);
                Bundle bundle = new Bundle();
                bundle.putString("searchKeys",mSearchKeys);
                mMainActivity.showFragment(SearchProductFragment.class,"Home_2_serach",bundle);

            }
        });

        mSearchET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                 //   Toast.makeText(getContext(),"回车搜索",Toast.LENGTH_SHORT).show();
                    mSearchKeys=mSearchET.getText().toString();
                    Log.d("---------","SearchKeys:"+mSearchKeys);
                    Bundle bundle = new Bundle();
                    bundle.putString("searchKeys",mSearchKeys);
                    mMainActivity.showFragment(SearchProductFragment.class,"Home_2_Search",bundle);
                    return true;
                }
                return false;
            }
        });
        /**
         * 点击事件
         *
         * 1.view控件的点击事件（botton，imageview等）：View.OnClickListener()
         * 2.列表的点击事件（item子控件的点击事件）：AdapterView.OnItemClickListener()
         */
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //goods.get(position).getId()当前位置的商品id
                //Toast.makeText(mContext,"goods id is -- "+goods.get(position).getId(),Toast.LENGTH_SHORT).show();

                //跳转到详情页
                //fragment之间的传值，需要在showFragment方法中增加一个参数
                Bundle bundle = new Bundle();
                bundle.putParcelable("goodsdetail",goods.get(position));
                bundle.putInt("goodsId",goods.get(position).getId());

                mMainActivity.showFragment(ProductDescFragment.class,"goodslist_2_detail",bundle);
            }
        });
        mAdapter.setOnCartClickListener(new HomeAdapter.CallBack() {
            @Override
            public void onCartClick(View v, int position) {
               // Toast.makeText(getContext(),"----"+position,Toast.LENGTH_SHORT).show();


                if(mPreferenceManager.getLoginStatus()){
                    addProductToCart(position);

                }else{
                    mMainActivity.showFragment(LoginFragment.class,"Home_2_Login");
                }

            }
        });
    }

    private void addProductToCart(int position) {
        String addCartUrl=UrlConstants.addCartUrl+"?userid="+mPreferenceManager.getUserId()+"&goodsid="+goods.get(position).getId()+"&number="+1;
        Log.d("-----------", "购物车: " + addCartUrl);
        Request request = new Request.Builder()
                .url(addCartUrl)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("-----", "error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonStr = response.body().string();
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("---------", "addCart: " + jsonStr);
                        Type type = new TypeToken<Result<String>>() {
                        }.getType();
                        //解析Json数据得到结果集
                        Result<String> result = JsonUtils.parse(jsonStr, type);
                        if(result.mCode==200){
                            Toast.makeText(getContext(),"成功加入购物车",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"加入购物车失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.category_img:
                showPopupMenu(mCategoryImage);
                //Toast.makeText(getContext(),"类别选择",Toast.LENGTH_LONG).show();
                break;
            case R.id.clothes_tv:
                mMainActivity.showFragment(TwoCloFragment.class, "Home_2_Clo");
                break;
            case R.id.riyong_tv:
                mMainActivity.showFragment(TwoRyFragment.class, "Home_2_Ry");
                break;
            case R.id.shipin_tv:
                mMainActivity.showFragment(TwoFoodFragment.class, "Home_2_Ry");
                break;
            case R.id.shengxian_tv:
                mMainActivity.showFragment(TwoFreshFragment.class, "Home_2_sx");
                break;
            case R.id.homemj_img:
                mMainActivity.showFragment(TwoWineFragment.class,"Home_2_wine");
                break;
            case R.id.homezp_img:
                mMainActivity.showFragment(TwoPaperFragment.class,"Home_2_paper");
                break;
            case R.id.homeph_img:
                mMainActivity.showFragment(TwopuffFragment.class,"Home_2_puff");
                break;

        }

    }

    private void showPopupMenu(View view) {
        //View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(),view);
        //menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_home,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();

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

    }

    public void execute() throws Exception {
        /**
         * http的get形式上传参数
         *
         * 参数都是拼接在url后面的
         *
         * UrlConstants.HomeUrl
         *
         * name=admin,pass=admin
         *
         * 拼接url,?id=1&pass=admin
         *
         * String url = UrlConstants.HomeUrl+"?name="+name+"&pass="+pass
         *
         */


        Request request = new Request.Builder()
                .url(UrlConstants.HomeUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("-----", "error");
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String ret = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("---", ret);
                        Type listType = new TypeToken<Result<Home>>() {
                        }.getType();
                        //解析Json数据得到结果集
                        Result<Home> listResult = JsonUtils.parse(ret, listType);
                        if (listResult.mCode == 200) {
                            Home homelist = listResult.mData;
                            //更新banner
                            List<BaseEntity> banners = homelist.getBanners();
                            setBanner(banners);
                            //更新dicounts
                            List<BaseEntity> discounts = homelist.getDiscounts();
                            Log.d("----", discounts.toString());
                            Glide
                                    .with(mContext)
                                    .load(UrlConstants.BannerDisUrl + discounts.get(0).getUrl())
                                    .placeholder(R.drawable.paper)
                                    .error(R.drawable.paper)
                                    .into(homemj_img);
                            Glide
                                    .with(mContext)
                                    .load(UrlConstants.BannerDisUrl + discounts.get(1).getUrl())
                                    .placeholder(R.drawable.shangpin1)
                                    .error(R.drawable.shangpin1)
                                    .into(homezp_img);
                            Glide
                                    .with(mContext)
                                    .load(UrlConstants.BannerDisUrl + discounts.get(2).getUrl())
                                    .placeholder(R.drawable.shangpin1)
                                    .error(R.drawable.shangpin1)
                                    .into(homeph_img);

                            //更新goods
                            goods = homelist.getGoods();
                            int height = goods.size() / 2 * 230 + 150;
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
                            DisplayUtils.init(getContext());
                            params.height = DisplayUtils.dip2px(height);
                            mGridView.setLayoutParams(params);
                            Log.d("-------", goods.size() + "");
                            Log.d("-------", goods.toString());
                            mAdapter.update(goods);
                        }
                    }
                });

            }
        });
    }
    // 由banner工厂生产出来的imageview
    private ImageView bannerImageView;
    private List<ImageView> mImageViews = new ArrayList<>();
    private void initBanner() {
        bannerImageView = BannerImageViewUtils.getImageView(mContext);
        bannerImageView.setImageResource(R.drawable.banner);
        mImageViews.add(bannerImageView);
        // 设置循环，在调用setData方法前调用
        mCycleViewPager.setCycle(false);
        // 在加载数据前设置是否循环
        mCycleViewPager.setData(mImageViews, null, null);
        // 设置轮播
        mCycleViewPager.setWheel(false);
        // 设置圆点指示图标组居中显示，默认靠右
        mCycleViewPager.setIndicatorCenter();
        mImageViews.clear();
    }
    private void setBanner(List<BaseEntity> banners) {
        DisplayUtils.init(mContext);
        mImageViews.clear();
        for (BaseEntity banner : banners) {
            bannerImageView = BannerImageViewUtils.getImageView(mContext);
            Glide.with(mContext).load(UrlConstants.BannerDisUrl + banner.getUrl())
                    .placeholder(R.drawable.banner)
                    .centerCrop()
                    .into(bannerImageView);
            mImageViews.add(bannerImageView);
        }
        //重新添加第一个view
        bannerImageView = BannerImageViewUtils.getImageView(mContext);
        Glide.with(mContext).load(UrlConstants.BannerDisUrl + banners.get(banners.size() - 1).getUrl()).centerCrop()
                .into(bannerImageView);
        mImageViews.add(0, bannerImageView);
        //重新添加最后一个view
        bannerImageView = BannerImageViewUtils.getImageView(mContext);
        Glide.with(mContext).load(UrlConstants.BannerDisUrl + banners.get(0).getUrl()).centerCrop()
                .into(bannerImageView);
        mImageViews.add(bannerImageView);
        // 设置循环，在调用setData方法前调用
        mCycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        mCycleViewPager.setData(mImageViews, banners, new ImageCycleViewListener() {
            @Override
            public void onImageClick(Object info, int position, View imageView) {
                //Toast.makeText(getContext(), "--"+position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 1:
                        //Toast.makeText(getContext(), "--" + position, Toast.LENGTH_SHORT).show();
                        mMainActivity.showFragment(BannerNzFragment.class,"Home_2_bannerNz");
                        break;
                    case 2:
                        //Toast.makeText(getContext(), "--" + position, Toast.LENGTH_SHORT).show();
                        mMainActivity.showFragment(BannerXsFragment.class,"Home_2_bannerXs");
                        break;
                    case 3:
                        //Toast.makeText(getContext(), "--" + position, Toast.LENGTH_SHORT).show();
                        mMainActivity.showFragment(BannerHfpFragment.class,"Home_2_hfp");
                        break;

                }
            }
        });
        //设置轮播
        mCycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        mCycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        mCycleViewPager.setIndicatorCenter();
    }



}
