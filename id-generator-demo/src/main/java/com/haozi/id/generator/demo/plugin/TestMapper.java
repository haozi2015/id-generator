package com.haozi.id.generator.demo.plugin;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haozi
 * @date 2020/5/206:59 下午
 */
@Mapper
public interface TestMapper {
    @Insert("insert t_test(test1,test2) values(#{test1},#{test2})")
    int insert(Test test);

    @Insert("<script> insert into t_test (test1,test2) values  " +
            "  <foreach collection='result' item='item' separator=',' > " +
            "  (#{item.test1},#{item.test2})\n" +
            "  </foreach> </script>")
    int insert2(@Param("result") List<Test> testList);
}
