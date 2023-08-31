package com.meteor.aifadianpay;

import com.meteor.aifadianpay.api.events.GainRewardsEvent;
import com.meteor.aifadianpay.data.Order;
import com.meteor.aifadianpay.data.Orders;
import com.meteor.aifadianpay.data.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class QueryTask extends BukkitRunnable {

    public static AifadianApi aifadianApi;


    public static YamlConfiguration logs;

    public QueryTask(YamlConfiguration logs){
        this.logs = logs;
    }


    public static void init(String user_id,String token){
        aifadianApi = new AifadianApi(user_id,token);
        if(!aifadianApi.isPass()){
            AifadianPay.getPlugin(AifadianPay.class).getLogger().info("插件未初始化,请在config内填入相关信息;详情参考mcbbs帖内教程");
        }
    }

    public static boolean isHandlerOrder(String id){
        return logs.getBoolean(id,false);
    }



    @Override
    public void run() {
        if(!aifadianApi.isPass()){
            AifadianPay.getPlugin(AifadianPay.class).getLogger().info("插件未初始化,请在config内填入相关信息;详情参考mcbbs帖内教程");
            return;
        }
        int page = 1;
        int totalPage = 0;
        int cnt = 0;
        while(true){
            if(totalPage!=0&&page>totalPage){
                break;
            }
            Orders orders = aifadianApi.queryOrders(page++,null);
            List<Order> orderList = orders.getOrderList();
            if(orderList.isEmpty()) totalPage=-1;
            else {
                for (Order order : orderList) {
                    if(!logs.getBoolean(order.getOutTradeNo(),false)||order.getRemark()==null){
                        if(ShopItem.handlerOrder(order)){
                            String shopitem = order.getSkuDetail().get(0).getName();
                            logs.set(order.getOutTradeNo(),true);
                            Bukkit.getScheduler().runTask(AifadianPay.getPlugin(AifadianPay.class),()->{
                                GainRewardsEvent gainRewardsEvent = new GainRewardsEvent(shopitem,order.getRemark());
                                Bukkit.getPluginManager().callEvent(gainRewardsEvent);
                            });
                            cnt++;
                        }else {
                            AifadianPay.getPlugin(AifadianPay.class)
                                    .getLogger().info("订单 "+order.getOutTradeNo()+"处理异常,请检查商品id是否错误或填写玩家不存在...");
                        }
                    }
                    if(totalPage==0){
                        totalPage = orders.getTotalPage();
                    }
                }
            }
        }
        AifadianPay.getPlugin(AifadianPay.class)
                .getLogger().info("已发货 "+cnt+" 笔新的订单");
        try {
            logs.save(new File(AifadianPay.getPlugin(AifadianPay.class).getDataFolder()+"/logs.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
