package com.liujun.learn.mybatis.process;

import cn.hutool.core.io.IoUtil;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.liujun.learn.mybatis.bean.MapperStatement;
import com.liujun.learn.mybatis.bean.MyBatisConfiguration;
import com.liujun.learn.mybatis.utils.ResourceUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 配制文件的处理操作
 *
 * @author liujun
 * @since 2022/6/27
 */
public class ConfigParseBuilder {

  private MyBatisConfiguration configuration;

  public ConfigParseBuilder() {
    this.configuration = new MyBatisConfiguration();
  }

  public MyBatisConfiguration readConfig(InputStream input) throws Exception {

    try (BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
      Document dom = new SAXReader().read(bufferedInput);
      Element root = dom.getRootElement();

      // 设置数据源信息
      Element dataSourceElement = root.element("dataSource");
      Properties properties = new Properties();
      List<Element> propertyList = dataSourceElement.elements("property");
      for (Element propertyItem : propertyList) {
        String name = propertyItem.attribute("name").getValue();
        String value = propertyItem.attribute("value").getValue();
        properties.put(name, value);
      }
      configuration.setDataSource(DruidDataSourceFactory.createDataSource(properties));

      Map<String, MapperStatement> mapperElement = new HashMap<>(8);
      List<Element> elementList = root.elements("import");
      for (Element resourceElement : elementList) {
        String resourcePath = resourceElement.attributeValue("resource");
        // 解析配制的SQL信息
        mapperElement.putAll(
            MapperStatementBuilder.readerMapper(ResourceUtils.getResource(resourcePath)));
      }

      configuration.setStatementMap(mapperElement);
    } finally {
      IoUtil.close(input);
    }

    return configuration;
  }
}
