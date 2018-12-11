package com.aiyi.core.util.cache;

import com.aiyi.core.exception.RequestParamException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Project : Xunhengda
 * @Prackage Name : com.aiyi.core.util.cache
 * @Description : 缓存工具类
 * @Author : 郭胜凯
 * @Creation Date : 2018/5/7 下午10:23
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018/5/7
 */
public class CacheUtils {
  private static final Map<String, CacheData> cacheMap = new HashMap<>();

  private static boolean keeper = false;
  private static class CacheData{
    private long time = 0;
    private Object data;

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
    }

    public Object getData() {
      return data;
    }

    public void setData(Object data) {
      this.data = data;
    }
  }
  /**
   * @param key   缓存数据的key
   * @param value 缓存数据的值
   * @param time  缓存数据有效时间
   * @Description : 设置缓存数据
   * @Creation Date : 2018/5/7 下午10:25
   * @Author : 郭胜凯
   */
  public static void set(String key, Object value, long time){
    if (key == null || "".equals(key.trim())){
      throw new RequestParamException("缓存数据的Key值不能为空");
    }
    CacheData data = new CacheData();
    data.setTime(System.currentTimeMillis() + time);
    data.setData(value);
    cacheMap.put(key, data);
    timekeeper();
  }

  /**
   * @param key 要取出缓存数据的Key
   * @Description : 从缓存中取出数据
   * @Creation Date : 2018/5/7 下午10:54
   * @Author : 郭胜凯
   */
  public static<T> T get(String key){
    CacheData data = cacheMap.get(key);
    if (null == data){
      return null;
    }
    return (T) data.getData();
  }

  /**
   * @param key   要删除缓存数据的key
   * @Description : 从缓存中删除指定数据, 这个过程是异步的, 有0.几秒的延迟
   * @Creation Date : 2018/5/7 下午10:57
   * @Author : 郭胜凯
   */
  public static void remove(String key){
    // 这里创建一个生命周期为0的空对象, 让计时器自动去销毁它
    // 不调用HashMap的Remove方法, 以免和计时器产生线程冲突.
    cacheMap.put(key, new CacheData());
  }

  /**
   * @Description : 计时, 清理有效周期到期的Key
   * @Creation Date : 2018/5/7 下午10:44
   * @Author : 郭胜凯
   */
  private static void timekeeper(){
    if (keeper){
      return;
    }
    keeper = true;
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true){
          Set<String> strings = cacheMap.keySet();
          for (String key :
                  strings) {
            CacheData data = cacheMap.get(key);
            if (data.getTime() <= System.currentTimeMillis()){
              cacheMap.remove(key);
            }
          }
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    t.start();
  }

  /**
   * @Description : 通用缓存常量
   * @Creation Date : 2018/5/10 上午11:15
   * @Author : 郭胜凯
   */
  public static final class KEYS{
    /**
     * @Description : 短信相关缓存常量
     * @Creation Date : 2018/5/10 上午11:16
     * @Author : 郭胜凯
     */
    public static final class SMS{
      /**
       * @Description : 短信发送令牌缓存前缀
       * @Creation Date : 2018/5/10 上午11:16
       * @Author : 郭胜凯
       */
      public static final String TOKEN = "SMS.TOKEN:";
      /**
       * @Description : 当天短信发送记录缓存前缀
       * @Creation Date : 2018/5/10 上午11:16
       * @Author : 郭胜凯
       */
      public static final String SEND_LOG = "SMS.SENDLOG.PHONE:";

      /**
       * @Description : 手机号码对应发送的验证码缓存前缀
       * @Creation Date : 2018/5/11 下午5:40
       * @Author : 郭胜凯
       */
      public static final String SEND_CODE = "SMS.SENDCODE.PHONE:";
    }

    /**
     * @Description : Session类
     * @Creation Date : 2018/5/17 下午6:34
     * @Author : 郭胜凯
     */
    public static final class SESSION{
      /**
       * @Description : 用户登录访问凭证
       * @Creation Date : 2018/5/17 下午6:36
       * @Author : 郭胜凯
       */
      public static final String USER_LOGIN_SESSION = "USER.SESSION.TOKEN:";
    }
  }


}
