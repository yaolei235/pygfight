package cn.itcast.core.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UserToMapUtil {
    /**
     * 将任意vo转化成map
     *
     * @param t vo对象
     * @return
     */
    //
    public static  <T>  Map<String, Object> convert2Map(T t){
        Map<String, Object> result = new HashMap<String, Object>();
        Method[] methods = t.getClass().getMethods();
        try {
            for (Method method : methods) {
                Class<?>[] paramClass = method.getParameterTypes();
                if (paramClass.length > 0) { // 如果方法带参数，则跳过
                    continue;
                }
                String methodName = method.getName() ;
                if (methodName.startsWith("get")) {
                    Object value = method.invoke(t);
                    methodName=methodName.substring(3);
                    methodName=methodName.toLowerCase();
                    result.put(methodName, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
