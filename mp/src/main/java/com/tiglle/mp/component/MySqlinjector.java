package com.tiglle.mp.component;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.tiglle.mp.mysqlscriptinjector.RemoveById;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MySqlinjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        //调用super的方法，否则mybatisplus的默认方法都不能用
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        //加入自定义的方法
        methodList.add(new RemoveById());
        return methodList;
    }
}
