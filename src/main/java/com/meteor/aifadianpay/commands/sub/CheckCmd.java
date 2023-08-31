package com.meteor.aifadianpay.commands.sub;

import com.meteor.aifadianpay.AifadianPay;
import com.meteor.aifadianpay.QueryTask;
import com.meteor.aifadianpay.commands.Icmd;
import com.meteor.aifadianpay.data.Order;
import com.meteor.aifadianpay.data.ShopItem;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CheckCmd extends Icmd {
    public CheckCmd(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "check";
    }

    @Override
    public String getPermission() {
        return "apl.admin.check";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "查看订单详情";
    }

    @Override
    public void perform(CommandSender p0, String[] p1) {
        if(p1.length<2){
            p0.sendMessage(AifadianPay.messageBox.getMessage(null,"message.pleaseInputTradeNo"));
            return;
        }
        Map<String,String> params = new HashMap<>();
        String s = p1[1];
        Order order = QueryTask.aifadianApi.queryOrder(s);
        params.put("@tradeNo@",s);
        if(order==null){
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.notExistOrder"));
            return;
        }
        params.put("@shopitem@",order.getPlanTitle());
        params.put("@state@", QueryTask.isHandlerOrder(s)?"§a已处理":"§c未处理");
        params.put("@player@",order.getRemark());

        AifadianPay.messageBox.getMessageList(params,"message.info")
                .forEach(m->{
                    p0.sendMessage(m);
                });
    }
}
