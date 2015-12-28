package yalan.bevelop;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Alan Ding on 2016/2/23.
 */
public class ViewHidingOnScrollListener extends RecyclerView.OnScrollListener {

    private final View toolbarContainer;
    private final View toolbar;
    private final View parallaxScrollingView;
    private final View lastToolbarView;

    private float parallaxScrollingFactor = 0.7f;
    private Boolean autoShowOnHidden = true;

    public ViewHidingOnScrollListener(View toolbarContainer, View toolbar, View lastToolbarView) {
        this(toolbarContainer, toolbar, lastToolbarView, null);
    }

    public ViewHidingOnScrollListener(View toolbarContainer, View toolbar, View lastToolbarView, View parallaxScrollingView) {
        this.toolbarContainer = toolbarContainer;
        this.toolbar = toolbar;
        this.parallaxScrollingView = parallaxScrollingView;
        this.lastToolbarView = lastToolbarView;
    }

    public void setParallaxScrollingFactor(float parallaxScrollingFactor) {
        this.parallaxScrollingFactor = parallaxScrollingFactor;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (Math.abs(toolbarContainer.getTranslationY()) > toolbar.getHeight()) {
                hideToolbar();
            } else {
                if (autoShowOnHidden) {
                    showToolbar();
                }
            }
        } else {
            toolbarContainer.clearAnimation();
        }
    }

    public void showToolbar() {
        toolbarContainer.clearAnimation();
        toolbarContainer
                .animate()
                .translationY(0)
                .start();

    }

    public void hideToolbar() {
        toolbarContainer.clearAnimation();
        toolbarContainer
                .animate()
                .translationY(-lastToolbarView.getBottom())
                .start();

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        scrollColoredViewParallax(dy);

        if (dy > 0) {
            hideToolbarBy(dy);
        } else {
            showToolbarBy(dy);
        }
    }

    private void scrollColoredViewParallax(int dy) {
        if (parallaxScrollingView != null) {
            int absoluteTranslationY = (int) (parallaxScrollingView.getTranslationY() - dy * parallaxScrollingFactor);
            parallaxScrollingView.setTranslationY(Math.min(absoluteTranslationY, 0));
        }
    }


    private void hideToolbarBy(int dy) {
        if (cannotHideMore(dy)) {
            toolbarContainer.setTranslationY(-lastToolbarView.getBottom());
        } else {
            toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
        }
    }

    private boolean cannotHideMore(int dy) {
        return Math.abs(toolbarContainer.getTranslationY() - dy) > lastToolbarView.getBottom();
    }


    protected void showToolbarBy(int dy) {
        if (cannotShowMore(dy)) {
            toolbarContainer.setTranslationY(0);
        } else {
            toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
        }
    }

    private boolean cannotShowMore(int dy) {
        return toolbarContainer.getTranslationY() - dy > 0;
    }

    public Boolean isAutoShowOnHidding() {
        return autoShowOnHidden;
    }

    public void setAutoShowOnHidden(Boolean autoShowOnHidden) {
        this.autoShowOnHidden = autoShowOnHidden;
    }
}