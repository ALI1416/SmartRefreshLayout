package com.scwang.refreshlayout.activity.style;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.adapter.BaseRecyclerAdapter;
import com.scwang.refreshlayout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.layout.simple_list_item_2;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class BezierRadarStyleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static boolean isFirstEnter = true;
    private Toolbar mToolbar;
    private RefreshLayout mRefreshLayout;
    private BezierRadarHeader mRefreshHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_bezier);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshHeader = findViewById(R.id.header);
        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }

        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            List<Item> items = new ArrayList<>();
            items.addAll(Arrays.asList(Item.values()));
            items.addAll(Arrays.asList(Item.values()));
            recyclerView.setAdapter(new BaseRecyclerAdapter<Item>(items, simple_list_item_2, this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                    holder.text(android.R.id.text1, model.name());
                    holder.text(android.R.id.text2, model.nameId);
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (Item.values()[position % Item.values().length]) {
            case 内容不偏移:
                mRefreshLayout.setEnableHeaderTranslationContent(false);
                break;
            case 内容跟随偏移:
                mRefreshLayout.setEnableHeaderTranslationContent(true);
                break;
            case 蓝色主题:
                setThemeColor(R.color.colorPrimary, R.color.colorPrimaryDark);
                break;
            case 绿色主题:
                setThemeColor(android.R.color.holo_green_light, android.R.color.holo_green_dark);
                break;
            case 红色主题:
                setThemeColor(android.R.color.holo_red_light, android.R.color.holo_red_dark);
                break;
            case 橙色主题:
                setThemeColor(android.R.color.holo_orange_light, android.R.color.holo_orange_dark);
                break;
            case 打开左右拖动:
                mRefreshHeader.setEnableHorizontalDrag(true);
                break;
            case 关闭左右拖动:
                mRefreshHeader.setEnableHorizontalDrag(false);
                break;
        }
        mRefreshLayout.autoRefresh();
    }

    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {
        mToolbar.setBackgroundResource(colorPrimary);
        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimaryDark));
        }
    }

    private enum Item {
        打开左右拖动(R.string.item_style_horizontal_drag_on),
        关闭左右拖动(R.string.item_style_horizontal_drag_off),
        内容不偏移(R.string.item_style_content_translation_off),
        内容跟随偏移(R.string.item_style_content_translation_on),
        橙色主题(R.string.item_style_theme_orange_abstract),
        红色主题(R.string.item_style_theme_red_abstract),
        绿色主题(R.string.item_style_theme_green_abstract),
        蓝色主题(R.string.item_style_theme_blue_abstract),
        ;
        public int nameId;

        Item(@StringRes int nameId) {
            this.nameId = nameId;
        }
    }


}
