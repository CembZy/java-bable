package com.concurrent.ch06.instance;


/**
 * 懒加载
 */
public class LazyLoad {

    private static class LazyHolder {
        private final static LazyLoad lazyLoad = new LazyLoad();
    }

    private LazyLoad() {
    }

    public static LazyLoad instance() {
        return LazyHolder.lazyLoad;
    }
}
