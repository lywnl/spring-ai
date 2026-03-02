package com.lyw.mcp.service;

import com.lyw.mcp.enity.UserInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    //存储用户信息
    static Map<String, UserInfo> userInfoMap = new HashMap<>();

    static {
        userInfoMap.put("zhangsan", new UserInfo("zhangsan", 15, "男", "北京"));
        userInfoMap.put("lisi", new UserInfo("lisi", 16, "男", "上海"));
        userInfoMap.put("wangwu", new UserInfo("wangwu", 17, "男", "广州"));
        userInfoMap.put("zhaoliu", new UserInfo("zhaoliu", 18, "女", "深圳"));
        userInfoMap.put("sunqi", new UserInfo("sunqi", 19, "女", "香港"));
        userInfoMap.put("zhaoba", new UserInfo("zhaoba", 20, "女", "澳门"));
    }

    @Tool(description = "获取用户信息")
    public String getUserInfo(@ToolParam(description = "用户名称") String name){
        if (userInfoMap.containsKey(name)) {
            return userInfoMap.get(name).toString();
        }

        return "用户信息不存在";
    }

}
