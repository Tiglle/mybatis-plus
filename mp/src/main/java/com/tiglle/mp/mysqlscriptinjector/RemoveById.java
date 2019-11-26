package com.tiglle.mp.mysqlscriptinjector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/*
自定义通用方法
 */
public class RemoveById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        //定义sql语句
        String mySqlScript = "delete from "+tableInfo.getTableName();
        tableInfo.getKeyProperty()
        //定义方法名,不能和已有的重复
        String methodName = "removeById";
        //创建sqlSource
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, mySqlScript, mapperClass);
        return addDeleteMappedStatement(mapperClass,methodName,sqlSource);
    }
}
