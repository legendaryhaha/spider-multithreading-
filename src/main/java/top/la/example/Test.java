package top.la.example;

import top.la.service.IParamsBuilder;
import top.la.service.IRequest;
import top.la.service.IResult;
import top.la.service.impl.GetRequestService;
import top.la.thread.SpiderThreadPool;
import top.la.vo.GetRequestVO;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/12 9:07
 * @Description
 */


public class Test {
    public static void main(String[] args) {
        String content = "";
        IParamsBuilder paramsBuilder = () -> {
            Map<String, String> headerMap = setRequestHeader();
            Map<String, Object> result = new HashMap<>();
            // target为实际请求所需要的key, content为实际请求需要填写的内容
            result.put("target", content);
            result.put("headers", headerMap);
            return result;
        };
        String url = "";
        GetRequestVO vo = new GetRequestVO(url, (IResult) json-> {
            // 返回结果是一个json字段, 可以对其进行遍历, 自定义获取的内容

            return null;
        }, paramsBuilder);

        IRequest request = new GetRequestService(vo);
        SpiderThreadPool threadPool = SpiderThreadPool.getSingle();
        threadPool.add(null, request);
    }

    private static Map<String, String> setRequestHeader() {
        return null;
    }
}
