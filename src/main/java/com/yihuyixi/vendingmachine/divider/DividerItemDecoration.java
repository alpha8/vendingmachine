package com.yihuyixi.vendingmachine.divider;

import android.content.Context;
import android.support.annotation.Nullable;

public class DividerItemDecoration extends Y_DividerItemDecoration {
    public DividerItemDecoration(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider = null;
        switch (itemPosition % 3) {
            case 0:
                divider = new Y_DividerBuilder()
                        .setRightSideLine(true, 0xffffffff, 12, 0, 0)
                        .setBottomSideLine(true, 0xffffffff, 8, 0, 0)
                        .create();
                break;
            case 1:
                divider = new Y_DividerBuilder()
                        .setLeftSideLine(true, 0xffffffff, 6, 0, 0)
                        .setRightSideLine(true, 0xffffffff, 6, 0, 0)
                        .setBottomSideLine(true, 0xffffffff, 8, 0, 0)
                        .create();
                break;
            case 2:
                divider = new Y_DividerBuilder()
                        .setLeftSideLine(true, 0xffffffff, 12, 0, 0)
                        .setBottomSideLine(true, 0xffffffff, 8, 0, 0)
                        .create();
                break;
            default:
                break;
        }
        return divider;
    }
}
