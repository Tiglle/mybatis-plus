package com.tiglle.mp.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
填充处理器：自定义填充内容
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //是否有此字段的set方法
        boolean hasSetter = metaObject.hasSetter("createTime");
        //判断此字段是否设置了值
        Object fieldVal = getFieldValByName("createTime", metaObject);
        //如果手动设置了值，就不用自动填充了
        if(null==fieldVal){
        /*
        insert 时填充,只会填充 fill 被标识为 INSERT 与 INSERT_UPDATE 的字段
        指定填充字段和填充内容
        fieldName:实体类字段
         */
            setInsertFieldValByName("createTime", LocalDateTime.now(),metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //判断此字段是否设置了值
        Object fieldVal = getFieldValByName("createTime", metaObject);
        //如果手动设置了值，就不用自动填充了
        if(null==fieldVal) {
            /*
            update 时填充,只会填充 fill 被标识为 UPDATE 与 INSERT_UPDATE 的字段
            指定填充字段和填充内容
            fieldName:实体类字段
             */
                setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}
