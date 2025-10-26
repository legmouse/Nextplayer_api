package kr.co.nextplayer.lib.util.mybatis;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.google.common.collect.Lists;
import com.google.protobuf.GeneratedMessageV3;

/**
 * MyBatis ReturnType이 Protobuf Builder 인 경우 변환해주는 Interceptor
 */
// Mybatis supports interception of Executor, StatementHandler, PameterHandler and ResultSetHandler interfaces
 @Intercepts({
  @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
})
public class ProtobufMybatisInterceptor implements Interceptor {
  public Object intercept(Invocation invocation) throws Throwable {
    DefaultResultSetHandler value = (DefaultResultSetHandler) invocation.getTarget();

    Object result = invocation.getMethod().invoke(value, invocation.getArgs());
    if (result != null) {
      if (List.class.isAssignableFrom(result.getClass())) {
        List<?> list = (List<?>) result;
        return this.process(list);
      }
    }

    return result;
  }

  private List<?> process(List<?> list) {
    if (list.size() > 0) {
      if (GeneratedMessageV3.Builder.class.isAssignableFrom(list.get(0).getClass())) {
        List<Object> resultList = Lists.newArrayList();
        for (Object val: list) {
          @SuppressWarnings("rawtypes")
          Object rtnVal = ((GeneratedMessageV3.Builder) val).build();
          resultList.add(rtnVal);
        }
        return resultList;
      }
    }
    return list;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {}
}