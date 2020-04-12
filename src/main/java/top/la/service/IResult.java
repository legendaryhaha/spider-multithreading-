package top.la.service;

import com.alibaba.fastjson.JSONObject;
import top.la.vo.ResponseVo;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/11 18:43
 * @Description
 */

public interface IResult<T> {
    ResponseVo<T> handler(JSONObject jsonObject);
}
