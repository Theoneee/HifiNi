package com.theone.music.ui.binding_adapter;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.databinding.BindingAdapter;

import com.theone.music.ui.view.PlayPauseView;
import com.theone.music.ui.view.TheSelectImageView;
import android.view.View;

public class AppBindingAdapter {

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


    @BindingAdapter(value = {"selectListener","select"}, requireAll = false)
    public static void setSelectImageListener(TheSelectImageView selectImageView, TheSelectImageView.OnSelectChangedListener listener, Boolean select) {
        selectImageView.setOnSelectChangedListener(listener);
        selectImageView.setSelect(select);
    }

    @BindingAdapter(value = {"enable"}, requireAll = false)
    public static void setViewEnalbe(View view, Boolean enable) {
        view.setEnabled(enable);
    }

    @BindingAdapter(value = {"progress","max"}, requireAll = false)
    public static void seekBar(AppCompatSeekBar seekBar, int progress,int max) {
        seekBar.setMax(max);
        seekBar.setProgress(progress,true);
    }

}
