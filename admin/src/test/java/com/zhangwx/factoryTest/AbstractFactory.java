package com.zhangwx.factoryTest;

import com.zhangwx.factoryTest.interfac.Color;
import com.zhangwx.factoryTest.interfac.Shape;

public abstract class AbstractFactory {

    public abstract Shape getShape(String shape);

    public abstract Color getColor(String color);

}
