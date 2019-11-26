package com.tiglle.mp.config;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Configuration
public class MybatisPlusConfiguration {

    //测试动态表名
    public static String testDynamicTableName;

    /*mybatis plus分页插件*/
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);

//===================================================================================================
        /*
        多租户sql解析器：多用户时自动添加区分用户的条件
        属于sql解析部分，依赖mp的分页插件，需要在分页插件中设置解析器，但是不是只针对分页方法有效，只是因为拦截时机问题，所以在分页插件中设置
         */
        //sql解析集合接口
        ArrayList<ISqlParser> iSqlParsers = new ArrayList<>();
        //租户sql解析器
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        //设置租户处理器
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            //区分租户的字段
            @Override
            public String getTenantIdColumn() {
                return "customer_no";
            }

            //租户的值
            @Override
            public Expression getTenantId() {
                /*
                一般从session等中获取
                session.getUserId();
                session.getCustomerId();
                 */
                //这里写死
                return new LongValue(101);
            }

            /*
            根据表名判断是否需要过滤
            true:过滤
            false:不过滤
             */
            @Override
            public boolean doTableFilter(String tableName) {
                //t_role过滤掉，不增加租户信息
                if("t_role".equals(tableName)){
                    return true;
                }
                return false;
            }
        });
 //===================================================================================================
        //动态表名sql解析器
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        //key为需要替换的表名，value为处理逻辑，返回最终替换后的表名
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        //把plan替换为用户传入的表名
        tableNameHandlerMap.put("plan", new ITableNameHandler() {
            //处理逻辑，返回为最终替换后的表名,由用户传入(如果返回为null，还是查询原表plan)
            @Override
            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
                return testDynamicTableName;
            }
        });
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);



        //加入到sql解析接口中(多租户、动态表名)
        iSqlParsers.add(tenantSqlParser);
        iSqlParsers.add(dynamicTableNameParser);
        //设置到分页插件中（多个filters）
        paginationInterceptor.setSqlParserList(iSqlParsers);
        //单个filter：特定sql过滤：某个方法不增加sql信息 和 不增加动态表名的替换
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            /*
            特定sql过滤：某个方法不增加sql信息 和 不增加动态表名的替换
            true:过滤掉
            false:不过滤
             */
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
                if("com.tiglle.mp.mapper.PlanMapper.selectCustomPage".equals(mappedStatement.getId())){
                    return true;
                }
                return false;
            }
        });
        return paginationInterceptor;
    }

    /*
    逻辑删除
    3.1.1版本之后的都可以缺省配置
    3.1.1之前版本的需要自行配置
     */
//    @Bean
//    public ISqlInjector sqlInjector(){
//        return new LogicSqlInjector();
//    }

    /*
    乐观锁
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    /*
    性能分析插件
     */
    @Bean
    //配置开启的环境，一般生产环境不开启
    //@Profile({"dev","test"})
    public PerformanceInterceptor performanceInterceptor(){
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //性能分析日志格式化
        performanceInterceptor.setFormat(true);
        //设置慢sql的最大允许执行时间（毫秒），超过时抛异常，停止执行
        //performanceInterceptor.setMaxTime(10L);
        /*
        org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException:
        ### Error updating database.  Cause: com.baomidou.mybatisplus.core.exceptions.MybatisPlusException:  The SQL execution time is too large, please optimize !
         */
        return  performanceInterceptor;
    }
}
