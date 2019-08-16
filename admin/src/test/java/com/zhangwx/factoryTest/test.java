package com.zhangwx.factoryTest;

import com.zhangwx.factoryTest.interfac.Shape;
import org.apache.naming.factory.SendMailFactory;

public class test {

    public static void main(String[] args) {

        FactoryProducer producer;

//        AbstractFactory factory=producer.getFactory();

        //获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");
        //获取形状为 Circle 的对象
        Shape shape1 = shapeFactory.getShape("CIRCLE");

        //调用 Circle 的 draw 方法
        shape1.draw();

    }
}
