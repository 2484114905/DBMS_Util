package com.hundsun.dbutil.controller;

import com.hundsun.dbutil.util.DBUtil;
import com.hundsun.fund.ecmp.common.log.LoggersUtil;
import java.sql.SQLException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用于返回主页
 * @author wangyang31647
 * @date 2020/08/10
 */
@Controller
public class IndexController {

    /**
     *
     * @return 主页
     */
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
