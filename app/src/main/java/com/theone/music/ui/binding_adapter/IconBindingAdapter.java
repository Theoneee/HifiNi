package com.theone.music.ui.binding_adapter;

import androidx.databinding.BindingAdapter;

import com.theone.music.ui.view.PlayPauseView;
import com.theone.music.ui.view.TheSelectImageView;

public class IconBindingAdapter {

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


    @BindingAdapter(value = {"selectListener"}, requireAll = false)
    public static void setSelectImageListener(TheSelectImageView selectImageView, TheSelectImageView.OnSelectChangedListener listener) {
        selectImageView.setOnSelectChangedListener(listener);
    }

    @BindingAdapter(value = {"select"}, requireAll = false)
    public static void setSelectImage(TheSelectImageView selectImageView, Boolean select) {
        selectImageView.setSelect(select);
    }

}
