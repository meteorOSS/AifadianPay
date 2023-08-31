package com.meteor.aifadianpay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meteor.aifadianpay.data.AfdOrderReq;
import com.meteor.aifadianpay.data.Order;
import com.meteor.aifadianpay.data.Orders;
import com.meteor.aifadianpay.request.AfadianRequest;
import com.meteor.aifadianpay.request.AfadianResponseData;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AifadianApi {
    private String user_id;
    private String token;

    private OkHttpClient okHttpClient;
    public AifadianApi(String user_id,String token){
        this.user_id = user_id;
        this.token = token;
        this.okHttpClient = new OkHttpClient();
    }

    public boolean isPass(){
        return !user_id.equalsIgnoreCase("user_id")&&!token.equalsIgnoreCase("token");
    }

    private String response(String json,String url){
        RequestBody requestBody=RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));;
        Request request = new Request.Builder().post(requestBody)
                .url(url).build();
        Call call = okHttpClient.newCall(request);
        Response execute = null;
        try {
            execute = call.execute();
            String data = execute.body().string();
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Order> queryOrders(int page){
        return queryOrders(page,null).getOrderList();
    }

    public Orders queryOrders(int page,String tradeNo){
        List<Order> orderList = new ArrayList<>();
        AfadianRequest afdRequest =
                new AfadianRequest();
        AfdOrderReq afdOrderReq = new AfdOrderReq(page);
        if(tradeNo!=null) afdOrderReq.setOut_trade_no(tradeNo);
        afdRequest.setUser_id(this.user_id);
        afdRequest.setToken(this.token);
        afdRequest.setParam(afdOrderReq);
        try {
            String json = AfadianRequest.mapper.writeValueAsString(afdRequest.init());
            String res = response(json, Order.ORDER_URL);
            AfadianResponseData afadianResponseData = AfadianRequest.mapper.readValue(res, AfadianResponseData.class);
            AifadianPay plugin = AifadianPay.getPlugin(AifadianPay.class);
            if(afadianResponseData.getEc()==400001){
                plugin.getLogger().info("参数传输错误,请联系开发者");
            }else if(afadianResponseData.getEc()==400002){
                plugin.getLogger().info("ts错误,请联系开发者");
            }else if(afadianResponseData.getEc()==400003){
                plugin.getLogger().info("参数传输错误,请联系开发者");
            }else if(afadianResponseData.getEc()==400004){
                plugin.getLogger().info("请在config中填入token和userid,详情参考mcbbs帖内教程");
            }else if(afadianResponseData.getEc()==400005){
                plugin.getLogger().info("签名出现错误,请检查填入token与userid是否正确;");
            }

            if(afadianResponseData.getEc()!=200){
                plugin.getLogger().info("错误状态码: "+afadianResponseData.getEc());
            }

            if(afadianResponseData.getEc()==200){
                Orders orders = AfadianRequest.mapper
                        .convertValue(afadianResponseData.getData(),
                                Orders.class);
                return orders;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    public Order queryOrder(String tradeNo){
        Orders orders = queryOrders(1, tradeNo);
        return orders==null?null:orders.getOrderList().get(0);
    }
}
