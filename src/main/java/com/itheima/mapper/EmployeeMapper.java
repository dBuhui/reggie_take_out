package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author akk
 * @version 1.0
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
