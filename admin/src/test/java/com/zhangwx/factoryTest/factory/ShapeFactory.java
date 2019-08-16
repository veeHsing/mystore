package com.zhangwx.factoryTest.factory;

import com.zhangwx.factoryTest.AbstractFactory;
import com.zhangwx.factoryTest.interfac.Color;
import com.zhangwx.factoryTest.interfac.Shape;
import com.zhangwx.factoryTest.model.Circle;
import com.zhangwx.factoryTest.model.Square;

public class ShapeFactory extends AbstractFactory {
    @Override
    public Shape getShape(String shape) {
        if (shape == null){
            return null;
        }
       if (shape.equalsIgnoreCase("circle")){
            return new Circle();
       }
       if (shape.equalsIgnoreCase("square")){
            return new Square();
       }
       return null;
    }

    @Override
    public Color getColor(String color) {
        return null;
    }
}
