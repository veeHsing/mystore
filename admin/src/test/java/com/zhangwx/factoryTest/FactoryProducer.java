package com.zhangwx.factoryTest;

import com.zhangwx.factoryTest.factory.ColorFactory;
import com.zhangwx.factoryTest.factory.ShapeFactory;

public class FactoryProducer {

    public static AbstractFactory getFactory(String choice){
        if(choice.equalsIgnoreCase("SHAPE")){
            return new ShapeFactory();
        } else if(choice.equalsIgnoreCase("COLOR")){
            return new ColorFactory();
        }
        return null;
    }
}
