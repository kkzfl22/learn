package com.liujun.learn.mybatis.process;

import com.liujun.learn.mybatis.bean.MapperStatement;
import com.liujun.learn.mybatis.constant.OperatorType;
import com.liujun.learn.mybatis.constant.Symbol;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import cn.hutool.core.io.IoUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配制文件的处理操作
 *
 * @author liujun
 * @since 2022/6/27
 */
public class MapperStatementBuilder {

  /**
   * 执行配制的文件读取操作
   *
   * @param input
   * @return
   * @throws Exception
   */
  public static Map<String, MapperStatement> readerMapper(InputStream input) throws Exception {

    Map<String, MapperStatement> result;
    Document document = null;
    try (BufferedInputStream buffered = new BufferedInputStream(input)) {
      document = new SAXReader().read(buffered);
      result = new HashMap<>();

      Element rootElement = document.getRootElement();
      String nameSpace = rootElement.attributeValue("namespace");

      // 查询的SQL读取
      List<Element> selectElement = rootElement.elements("select");
      for (Element elementItem : selectElement) {
        String id = elementItem.attribute("id").getValue();
        String type = elementItem.attribute("type").getValue();
        String resultType = elementItem.attribute("resultType").getValue();
        String context = elementItem.getText().trim();

        MapperStatement statement =
            MapperStatement.builder()
                .type(OperatorType.QUERY)
                .id(id)
                .parameterType(type)
                .resultType(resultType)
                .sqlContext(context)
                .build();
        result.put(nameSpace + Symbol.DOC + id, statement);
      }

      // 添加的SQL读取
      List<Element> insertElement = rootElement.elements("insert");
      updateProcess(nameSpace, insertElement, result);

      // 修改SQL读取
      List<Element> updateElement = rootElement.elements("update");
      updateProcess(nameSpace, updateElement, result);

      // 删除的SQL读取
      List<Element> deleteElement = rootElement.elements("delete");
      updateProcess(nameSpace, deleteElement, result);

    } finally {
      IoUtil.close(input);
    }

    return result;
  }

  /**
   * 修改的数据处理
   *
   * @param nameSpace
   * @param listElement
   * @param result
   */
  private static void updateProcess(
      String nameSpace, List<Element> listElement, Map<String, MapperStatement> result) {
    for (Element elementItem : listElement) {
      String id = elementItem.attribute("id").getValue();
      String type = elementItem.attribute("type").getValue();
      String context = elementItem.getText().trim();

      MapperStatement statementUpdate =
          MapperStatement.builder()
              .type(OperatorType.UPDATE)
              .id(id)
              .parameterType(type)
              .sqlContext(context)
              .build();
      result.put(nameSpace + Symbol.DOC + id, statementUpdate);
    }
  }
}
