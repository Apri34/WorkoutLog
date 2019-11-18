package com.workoutlog.workoutlog;

import android.view.View;
import android.view.ViewGroup;

public class LayoutWrapContentUpdater {

    public static void wrapContentAgain(ViewGroup subTreeRoot)
    {
        if( !"main".equals( Thread.currentThread().getName() ) ) return;

        if (subTreeRoot == null)
            return;
        ViewGroup.LayoutParams layoutParams = subTreeRoot.getLayoutParams();

        int widthMeasureSpec 	= View.MeasureSpec.UNSPECIFIED;
        if ( layoutParams.width  != ViewGroup.LayoutParams.WRAP_CONTENT && subTreeRoot.getWidth() > 0 )
            widthMeasureSpec 	=  View.MeasureSpec.makeMeasureSpec( subTreeRoot.getWidth(), View.MeasureSpec.EXACTLY );
        int heightMeasureSpec 	= View.MeasureSpec.UNSPECIFIED;
        if ( layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT && subTreeRoot.getHeight() > 0 )
            heightMeasureSpec 	=  View.MeasureSpec.makeMeasureSpec( subTreeRoot.getHeight(), View.MeasureSpec.EXACTLY );
        subTreeRoot.measure( widthMeasureSpec, heightMeasureSpec );

        recurseWrapContent( subTreeRoot, false);
        subTreeRoot.requestLayout();
    }


    private static void recurseWrapContent( View nodeView, boolean relayoutAllNodes )
    {
        if ( nodeView.getVisibility() == View.GONE ) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = nodeView.getLayoutParams();
        boolean isWrapWidth  = ( layoutParams.width  == ViewGroup.LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;
        boolean isWrapHeight = ( layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;

        if ( isWrapWidth || isWrapHeight ) {

            boolean changed = false;
            int right  = nodeView.getRight();
            int bottom = nodeView.getBottom();

            if ( isWrapWidth  && nodeView.getMeasuredWidth() > 0 ) {
                right = nodeView.getLeft() + nodeView.getMeasuredWidth();
                changed = true;
            }
            if ( isWrapHeight && nodeView.getMeasuredHeight() > 0 ) {
                bottom = nodeView.getTop() + nodeView.getMeasuredHeight();
                changed = true;
            }

            if (changed) {
                nodeView.layout( nodeView.getLeft(), nodeView.getTop(), right, bottom );
            }
        }

        if ( nodeView instanceof ViewGroup ) {
            ViewGroup nodeGroup = (ViewGroup)nodeView;
            for (int i = 0; i < nodeGroup.getChildCount(); i++) {
                recurseWrapContent( nodeGroup.getChildAt(i), relayoutAllNodes );
            }
        }
    }
}
