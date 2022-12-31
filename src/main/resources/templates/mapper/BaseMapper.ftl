<#include "/common/common.ftl" />
package ${javaMapperPackage};

import ${po};
import org.apache.ibatis.annotations.Param;

import ${primaryKeyTypeJava};
import java.util.List;
import java.util.Set;

/**
 * ${tableInfo.comment}Mapper
 *
 * @author ${author}
 * @time ${time}
 **/
public interface ${tableInfo.humpName}Mapper {

    ${tableInfo.humpName}PO selectByPrimaryKey(${primaryKeyTypeJavaSimple} ${primaryKeyJavaFieldName});

    List<${tableInfo.humpName}PO> selectByPrimaryKeys(@Param("list") Set<${primaryKeyTypeJavaSimple}> ${primaryKeyJavaFieldName}List);

    List<${tableInfo.humpName}PO> selectList(${tableInfo.humpName}PO row);

    ${tableInfo.humpName}PO selectOne(${tableInfo.humpName}PO row);

    int selectCount(${tableInfo.humpName}PO row);

    int insert(${tableInfo.humpName}PO row);

    int insertBatch(@Param("list") List<${tableInfo.humpName}PO> rows);

    int insertSelective(${tableInfo.humpName}PO row);

    int updateByPrimaryKeySelective(${tableInfo.humpName}PO row);

    int updateByPrimaryKey(${tableInfo.humpName}PO row);

    int deleteByPrimaryKey(${primaryKeyTypeJavaSimple} ${primaryKeyJavaFieldName});

    int deleteByPrimaryKeys(@Param("list") Set<${primaryKeyTypeJavaSimple}> ${primaryKeyJavaFieldName}List);
}