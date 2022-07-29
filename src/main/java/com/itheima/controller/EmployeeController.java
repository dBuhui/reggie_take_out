package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.R;
import com.itheima.entity.Employee;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akk
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     *Params:[request, employee]
     *Return:com.itheima.common.R<com.itheima.entity.Employee>
     *     后台登录功能
     * 登录成功后把员工对象的ID存到session中,这样假如我们想获得当前登录用户就可以通过request.get来获取
     * 因此这里形参包含 request
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        //1.将页面提交的paaaword进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到就返回登录失败
        if (emp == null) {
            return R.error("登录失败");
        }
        //4.密码比对,假如不一致返回登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //5.查看员工状态如果已禁用就返回禁用
        if (emp.getStatus() == 0){
            return R.error("员工已禁用");
        }
        //6.登录成功,将员工ID存入session中,并返回登录结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     *Params:[request]
     *Return:com.itheima.common.R<java.lang.String>
     *员工退出功能
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除存入session中的员工ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

}
