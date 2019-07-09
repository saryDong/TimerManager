package com.abu.timermanager.event;

/**
 * @date: 2019/7/9 10:55
 * @author: 董长峰
 * @blog: https://www.jianshu.com/u/04a705fae99b
 * @description: 滚动事件类，当首页列表上滑时通过EvenBus通知底部导航栏隐藏和显示
 */
public class ScrollEvent {

    private Direction mDirection;

    public ScrollEvent(Direction d) {
        mDirection = d;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public enum Direction {
        UP, DOWN
    }
}
