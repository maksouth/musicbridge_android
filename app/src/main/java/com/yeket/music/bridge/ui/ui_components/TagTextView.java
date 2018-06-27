package com.yeket.music.bridge.ui.ui_components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import com.yeket.music.R;

import static com.yeket.music.bridge.constants.Dimensions.SMALL_TEXT;

public class TagTextView extends android.support.v7.widget.AppCompatTextView {

    public TagTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        customize(context);
    }

    public TagTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        customize(context);
    }

    private void customize(Context context ) {

        int sidePadding = SizeConverter.toPx(10, context);
        int topBottomPadding = SizeConverter.toPx(7, context);

        setTextSize(SMALL_TEXT);
        setBackground(getResources().getDrawable(R.drawable.tag_round_shape));
        setPadding(sidePadding, topBottomPadding, sidePadding, topBottomPadding);
        setGravity(Gravity.CENTER);

        setSingleLine(true);
        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.END);
        setMaxEms(10);

    }

    public TagTextView(Context context) {
        super(context);

        customize(context);
    }
}
