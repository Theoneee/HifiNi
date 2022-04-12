package com.theone.music.ui.binding_adapter;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.theone.common.widget.TheSelectImageView;
import com.theone.music.R;
import com.theone.music.data.constant.DownloadStatus;
import com.theone.music.data.model.DownloadResult;
import com.theone.music.ui.view.PlayPauseView;

import android.view.View;
import android.widget.ImageView;

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
        selectImageView.toggleSelect(select);
    }

    @BindingAdapter(value = {"enable"}, requireAll = false)
    public static void setViewEnalbe(View view, Boolean enable) {
        view.setEnabled(enable);
    }

    @BindingAdapter(value = {"progress", "max", "changeListener"}, requireAll = false)
    public static void seekBar(AppCompatSeekBar seekBar, int progress, int max, AppCompatSeekBar.OnSeekBarChangeListener changeListener) {
        seekBar.setMax(max);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(changeListener);
    }

}
