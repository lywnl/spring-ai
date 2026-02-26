package com.lyw.alibaba.tool;


public class WeatherTools {

    String getCurrentWeatherByCityName(String cityName) {
        switch (cityName) {
            case "北京":
                return "北京今天天气：晴空万里";
            case "上海":
                return "上海今天天气：电闪雷鸣";
            case "广州":
                return "广州今天天气：细雨蒙蒙";
            default:
                return "没有该城市的天气信息";
        }
    }
}
