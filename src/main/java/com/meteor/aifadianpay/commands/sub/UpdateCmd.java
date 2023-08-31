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

public class UpdateCmd extends Icmd {
    public UpdateCmd(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "update";
    }

    @Override
    public String getPermission() {
        return "apl.admin.update";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public void perform(CommandSender p0, String[] p1) {
        if(p1.length<2){
            p0.sendMessage(AifadianPay.messageBox.getMessage(null,"message.lackArgs"));
            return;
        }
        String s = p1[1];
        Order order = QueryTask.aifadianApi.queryOrder(s);
        Map<String,String> params = new HashMap<>();
        params.put("@tradeNo@",s);
        if(order==null){
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.notExistOrder"));
            return;
        }

        if(QueryTask.isHandlerOrder(s)){
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.receiveHandler"));
            return;
        }

        if(!ShopItem.handlerOrder(order)){
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.errorOrder"));
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.errorHelp"));
        }else {
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.handlerOrder"));
        }
    }
}
