package com.zhangwx.factoryTest.factory;

import com.zhangwx.factoryTest.AbstractFactory;
import com.zhangwx.factoryTest.interfac.Color;
import com.zhangwx.factoryTest.interfac.Shape;

public class ColorFactory extends AbstractFactory {
    @Override
    public Shape getShape(String shape) {
        return null;
    }

    @Override
    public Color getColor(String color) {
        return null;
    }
}
