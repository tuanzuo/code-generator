package ${javaRepositoryImplPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * ${tableInfo.comment}Repository实现
 *
 * @author ${author}
 * @time ${time}
 **/
@Repository
public class ${tableInfo.humpName}RepositoryImpl implements I${tableInfo.humpName}Repository {

    @Autowired
    private ${tableInfo.humpName}Mapper ${tableInfo.uncapitalizeName}Mapper;

}