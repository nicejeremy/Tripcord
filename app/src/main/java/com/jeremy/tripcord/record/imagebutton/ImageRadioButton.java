package com.jeremy.tripcord.record.imagebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class ImageRadioButton extends LinearLayout {

    private List<ImageButton> buttonList;
    private boolean canSelectMultipleItems = false;

    public ImageRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView() {

        inflate(getContext(), R.layout.view_image_radio_button, this);

        buttonList = new ArrayList<ImageButton>();
        setGravity(Gravity.RIGHT);
    }

    public void addButton(int resource, int backgroundResource, int width, int height, String value) {

        int widthToDp = ImageUtil.pxToDp(getContext(), width);
        int heightToDp = ImageUtil.pxToDp(getContext(), height);

        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setLayoutParams(new ViewGroup.LayoutParams(widthToDp, heightToDp));
        imageButton.setImageResource(resource);
        imageButton.setBackgroundResource(backgroundResource);
        imageButton.setOnClickListener(onClickListener);
        imageButton.setTag(value);

        if (buttonList.size() != 0) {
            View view = new View(getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            addView(view);
        }

        addView(imageButton);
        buttonList.add(imageButton);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!canSelectMultipleItems) {
                for (ImageButton imageButton : buttonList) {
                    if (imageButton.isSelected()) {
                        imageButton.setSelected(false);
                    }
                }
            }

            ImageButton imageButton = (ImageButton) view;
            imageButton.setSelected(true);
        }
    };

    public List<Integer> getSelectedItemIndexs() {

        List<Integer> selectedItemIndexs = new ArrayList<Integer>();
        for (int i = 0; i < buttonList.size(); i++) {

            ImageButton imageButton = buttonList.get(i);

            if (imageButton.isSelected()) {
                selectedItemIndexs.add(i);
            }
        }

        if (selectedItemIndexs.size() != 0) {
            return selectedItemIndexs;
        } else {
            return null;
        }
    }

    public List<String> getSelectedItemValues() {

        List<String> selectedItemValues = new ArrayList<String>();
        for (int i = 0; i < buttonList.size(); i++) {

            ImageButton imageButton = buttonList.get(i);

            if (imageButton.isSelected()) {
                selectedItemValues.add((String) imageButton.getTag());
            }
        }

        if (selectedItemValues.size() != 0) {
            return selectedItemValues;
        } else {
            return null;
        }
    }

    public void setCanSelectMultipleItems(boolean canSelectMultipleItems) {
        this.canSelectMultipleItems = canSelectMultipleItems;
    }
}
