package com.zhangwx.factoryTest.model;

import com.zhangwx.factoryTest.interfac.Shape;

public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("画了一个圆");
    }
}
