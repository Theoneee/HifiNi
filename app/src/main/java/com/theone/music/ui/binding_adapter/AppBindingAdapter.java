package com.theone.music.ui.binding_adapter;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.theone.music.R;
import com.theone.music.app.util.ColorUtil;
import com.theone.music.data.constant.DownloadStatus;
import com.theone.music.data.model.DownloadResult;
import com.theone.music.data.model.Singer;
import com.theone.music.ui.fragment.signer.SignerSearchFragment;
import com.theone.music.ui.view.PlayPauseView;
import com.theone.music.ui.view.TheSelectImageView;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class AppBindingAdapter {

    @BindingAdapter(value = {"download"}, requireAll = false)
    public static void setDownloadStatus(QMUIRoundButton btn, DownloadResult download) {
        String status = "下载失败";
        int color = R.color.qmui_config_color_red;
        switch (download.status) {
            case DownloadStatus
                    .DOWNLOADING:
                status = "下载中";
                color = R.color.qmui_config_color_50_blue;
                break;
            case DownloadStatus.SUCCESS:
                status = "已下载";
                color = R.color.qmui_config_color_blue;
                break;
            case DownloadStatus.FILE_DELETE:
                status = "文件已删除";
                color = R.color.qmui_config_color_gray_9;
                break;
            default:
                break;
        }
        btn.setText(status);
        btn.setBackgroundColor(ContextCompat.getColor(btn
                .getContext(), color));
    }


    @BindingAdapter(value = {"singers","host"}, requireAll = false)
    public static void layoutSingers(QMUIFloatLayout floatLayout, List<Singer> singers, QMUIFragment host) {
        Context context = floatLayout.getContext();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatLayout.removeAllViews();

        for (Singer singer : singers) {
            QMUIFrameLayout container = new QMUIFrameLayout(context);
            int space = QMUIDisplayHelper.dp2px(context,10);
            container.setPadding(0,0,space,space);
            TextView tag = new TextView(context);
            int padding = QMUIDisplayHelper.dp2px(context,4);
            int padding2 = padding*2;
            tag.setPadding(padding2,padding,padding2,padding);
            tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            tag.setMaxLines(1);
            tag.setTextColor(ColorUtil.INSTANCE.randomColor());
            tag.setText(singer.getName());
            tag.setBackground(ContextCompat.getDrawable(context,R.drawable.tree_tag_bg));
            container.addView(tag,layoutParams);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    host.startFragment(SignerSearchFragment.newInstance(singer));
                }
            });
            floatLayout.addView(container,layoutParams);
        }

    }

    @BindingAdapter(value = {"res"}, requireAll = false)
    public static void setResource(ImageView imageView, int res) {
        imageView.setImageResource(res);
    }

    @BindingAdapter(value = {"isPlaying"}, requireAll = false)
    public static void isPlaying(PlayPauseView pauseView, boolean isPlaying) {
        if (isPlaying) {
            pauseView.play();
        } else {
            pauseView.pause();
        }
    }

    @BindingAdapter(value = {"circleAlpha"}, requireAll = false)
    public static void circleAlpha(PlayPauseView pauseView, int circleAlpha) {
        pauseView.setCircleAlpha(circleAlpha);
    }

    @BindingAdapter(value = {"drawableColor"}, requireAll = false)
    public static void drawableColor(PlayPauseView pauseView, int drawableColor) {
        pauseView.setDrawableColor(drawableColor);
    }


    @BindingAdapter(value = {"selectListener", "select"}, requireAll = false)
    public static void setSelectImageListener(TheSelectImageView selectImageView, TheSelectImageView.OnSelectChangedListener listener, Boolean select) {
        selectImageView.setOnSelectChangedListener(listener);
        selectImageView.setSelect(select);
    }

    @BindingAdapter(value = {"enable"}, requireAll = false)
    public static void setViewEnalbe(View view, Boolean enable) {
        view.setEnabled(enable);
    }

    @BindingAdapter(value = {"progress", "max", "changeListener"}, requireAll = false)
    public static void seekBar(AppCompatSeekBar seekBar, int progress, int max, AppCompatSeekBar.OnSeekBarChangeListener changeListener) {
        seekBar.setMax(max);
        seekBar.setProgress(progress, true);
        seekBar.setOnSeekBarChangeListener(changeListener);
    }

}
