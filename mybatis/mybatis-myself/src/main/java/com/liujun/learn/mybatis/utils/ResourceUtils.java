package com.liujun.learn.mybatis.utils;

import java.io.InputStream;

/**
 * @author liujun
 * @since 2022/6/27
 */
public class ResourceUtils {

  /**
   * 执行文件加载操作
   *
   * @param path
   * @return
   */
  public static InputStream getResource(String path) {
    return ResourceUtils.class.getClassLoader().getResourceAsStream(path);
  }
}
