package com.vitasoy.catint.vitasoy.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lapism.searchview.SearchFilter;
import com.lapism.searchview.SearchView;
import com.vitasoy.catint.vitasoy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yodazone on 2016/9/25.
 */

public class CustomSearchView extends SearchView {
    private Context mContext;

    public CustomSearchView(Context context) {
        super(context);
        mContext = context;
    }

    public CustomSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public CustomSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    /**
     * @deprecated Use {@link #setRadiusFilters(List)} instead.
     */
    @Deprecated
    @Override
    public void setFilters(@Nullable List<SearchFilter> filters) {
        super.setFilters(filters);
    }

    /**
     * @deprecated Use {@link #getCheckedFilterTag()} instead.
     */
    @Deprecated
    @Override
    public List<Boolean> getFiltersStates() {
        return super.getFiltersStates();
    }

    //implement radiusGroup with checkbox (replace )
    public void setRadiusFilters(@Nullable List<RadiusFilter> filters) {
        mFiltersContainer.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = 0;
            params.bottomMargin = 0;
            mFiltersContainer.setLayoutParams(params);
        } else {
            mSearchFiltersStates = new ArrayList<>();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = mContext.getResources().getDimensionPixelSize(com.lapism.searchview.R.dimen.filter_margin_top);
            params.bottomMargin = params.topMargin / 2;
            mFiltersContainer.setLayoutParams(params);
            for (RadiusFilter filter : filters) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(mContext);
                checkBox.setText(filter.getTitle());
                checkBox.setTextSize(12);
                checkBox.setChecked(false);
                checkBox.setTag(filter.getTag());
                checkBox.setOnClickListener(mRadiusCheckListener);
                checkBox.setTextColor(Color.BLACK);
                checkBox.setButtonTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.accent)));
                mCheckBoxGroup.add(checkBox);
                mFiltersContainer.addView(checkBox);
                mSearchFiltersStates.add(false);
            }
            if (mCheckBoxGroup.size() > 0) {
                mCheckBoxGroup.get(0).setChecked(true);
            }
        }
    }

    public String getCheckedFilterTag() {
        for (AppCompatCheckBox v : mCheckBoxGroup){
             if (v.isChecked())
                 return (String) v.getTag();
        }
        return "";
    }

    private List<AppCompatCheckBox> mCheckBoxGroup = new ArrayList<>();
    private OnClickListener mRadiusCheckListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = 0;
            mSearchFiltersStates.clear();
            for (AppCompatCheckBox checkBox : mCheckBoxGroup) {
                if (checkBox == v) {
                    checkBox.setChecked(true);
                    mSearchFiltersStates.add(true);
                } else {
                    checkBox.setChecked(false);
                    mSearchFiltersStates.add(false);
                }
                position++;
            }
        }
    };

    public static class RadiusFilter {
        private final String mTitle;
        private final String mTag;

        public RadiusFilter(String title, String extra) {
            mTitle = title;
            mTag = extra;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getTag() {
            return mTag;
        }

    }

}
