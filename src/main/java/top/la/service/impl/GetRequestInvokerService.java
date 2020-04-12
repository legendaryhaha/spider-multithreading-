package top.la.service.impl;

import com.alibaba.fastjson.JSONObject;
import top.la.service.IGetRequestInvoker;
import top.la.service.IResult;
import top.la.util.HttpClientUtil;
import top.la.vo.ResponseVo;

import java.util.Map;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/12 1:41
 * @Description
 */


public class GetRequestInvokerService<T> implements IGetRequestInvoker {

    @Override
    public ResponseVo get(String url, Map params, Map headers, IResult result) {
        String response = "";
        try {
            response = HttpClientUtil.get(url, params, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        return result.handler(jsonObject);
    }
}
