package com.meteor.aifadianpay.commands.sub;

import com.meteor.aifadianpay.AifadianPay;
import com.meteor.aifadianpay.commands.Icmd;
import com.meteor.aifadianpay.data.ShopItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowUrl extends Icmd {
    public ShowUrl(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "showurl";
    }

    @Override
    public String getPermission() {
        return "apl.use.showurl";
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
    public List<String> getTab(Player p, int i, String[] args) {
        if(i==1){
            List<String> ps = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> ps.add(player.getName()));
            return ps;
        }else if(i==2){
            return new ArrayList<>(ShopItem.shopItems.keySet());
        }
        return null;
    }

    @Override
    public void perform(CommandSender p0, String[] p1) {
        if(p1.length<3){
            p0.sendMessage(AifadianPay.messageBox.getMessage(null,"message.lackArgs"));
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("@player@",p1[1]);
        Player player = Bukkit.getPlayerExact(p1[1]);
        if(player==null){
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.notFoundPlayer"));
            return;
        }

        ShopItem shopItem = ShopItem.shopItems.get(p1[2]);
        params.put("@shopitem@",p1[2]);
        if(shopItem==null){
            p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.notExistShopitem"));
            return;
        }

        AifadianPay.messageBox.getMessageList(params,"message.showUrl").forEach(mes->{
            mes = mes.replace("@shopitem_display_name@", shopItem.getDisplayName())
                    .replace("@price@",shopItem.getPrice());
            if(mes.contains("@click@")){
                mes = mes.replace("@click@","");
                TextComponent textComponent = new TextComponent(mes);
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,shopItem.getShowUrl()));
                player.spigot().sendMessage(textComponent);
                return;
            }
            player.sendMessage(mes);
        });

        p0.sendMessage(AifadianPay.messageBox.getMessage(params,"message.showUrlToPlayer"));
    }
}
