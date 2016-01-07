package com.avoscloud.chat.view;

import android.content.Context;
import android.graphics.Color;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import com.avoscloud.chat.App;
import com.avoscloud.chat.R;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class CheckBoxPreferenceFontSetting extends CheckBoxPreference {
    public CheckBoxPreferenceFontSetting(Context context) {
        super(context);
    }

    public CheckBoxPreferenceFontSetting(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public CheckBoxPreferenceFontSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CheckBoxPreferenceFontSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        CheckBox checkboxView = (CheckBox)view.findViewById(android.R.id.checkbox);
        if (checkboxView != null && checkboxView instanceof Checkable) {
            checkboxView.setBackgroundColor(App.ctx.getResources().getColor(R.color.accent_material_light));
        }
        TextView title = (TextView) view.findViewById(android.R.id.title);
        title.setTextColor(Color.BLACK);
    }

}
