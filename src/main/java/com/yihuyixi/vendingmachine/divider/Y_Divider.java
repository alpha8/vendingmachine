package com.yihuyixi.vendingmachine.divider;

import android.support.annotation.ColorInt;

public class Y_Divider {

    public Y_SideLine leftSideLine;
    public Y_SideLine topSideLine;
    public Y_SideLine rightSideLine;
    public Y_SideLine bottomSideLine;
    public class Y_DividerBuilder {

        private Y_SideLine leftSideLine;
        private Y_SideLine topSideLine;
        private Y_SideLine rightSideLine;
        private Y_SideLine bottomSideLine;


        public Y_DividerBuilder setLeftSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.leftSideLine = new Y_SideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public Y_DividerBuilder setTopSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.topSideLine = new Y_SideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public Y_DividerBuilder setRightSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.rightSideLine = new Y_SideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public Y_DividerBuilder setBottomSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.bottomSideLine = new Y_SideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public Y_Divider create() {
            //提供一个默认不显示的sideline，防止空指针
            Y_SideLine defaultSideLine = new Y_SideLine(false, 0xff666666, 0, 0, 0);

            leftSideLine = (leftSideLine != null ? leftSideLine : defaultSideLine);
            topSideLine = (topSideLine != null ? topSideLine : defaultSideLine);
            rightSideLine = (rightSideLine != null ? rightSideLine : defaultSideLine);
            bottomSideLine = (bottomSideLine != null ? bottomSideLine : defaultSideLine);

            return new Y_Divider(leftSideLine, topSideLine, rightSideLine, bottomSideLine);
        }


    }

    public Y_Divider(Y_SideLine leftSideLine, Y_SideLine topSideLine, Y_SideLine rightSideLine, Y_SideLine bottomSideLine) {
        this.leftSideLine = leftSideLine;
        this.topSideLine = topSideLine;
        this.rightSideLine = rightSideLine;
        this.bottomSideLine = bottomSideLine;
    }

    public Y_SideLine getLeftSideLine() {
        return leftSideLine;
    }

    public void setLeftSideLine(Y_SideLine leftSideLine) {
        this.leftSideLine = leftSideLine;
    }

    public Y_SideLine getTopSideLine() {
        return topSideLine;
    }

    public void setTopSideLine(Y_SideLine topSideLine) {
        this.topSideLine = topSideLine;
    }

    public Y_SideLine getRightSideLine() {
        return rightSideLine;
    }

    public void setRightSideLine(Y_SideLine rightSideLine) {
        this.rightSideLine = rightSideLine;
    }

    public Y_SideLine getBottomSideLine() {
        return bottomSideLine;
    }

    public void setBottomSideLine(Y_SideLine bottomSideLine) {
        this.bottomSideLine = bottomSideLine;
    }
}