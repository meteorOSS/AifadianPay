package com.meteor.aifadianpay.data;

import com.meteor.aifadianpay.AifadianPay;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ShopItem {
    private String id;
    private String displayName;
    private int point;
    private String showUrl;
    private List<String> commands;
    private String price;
    public static final PlayerPointsAPI playerPointsApi = PlayerPoints.getInstance().getAPI();

    public static Map<String,ShopItem> shopItems = new HashMap<>();

    private ShopItem(ConfigurationSection configurationSection){
        this.id = configurationSection.getName();
        this.displayName = configurationSection.getString("displayName");
        this.price = configurationSection.getString("price","100");
        this.point = configurationSection.getInt("point");
        this.showUrl = configurationSection.getString("showUrl");
        this.commands = configurationSection.getStringList("commands");
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPoint() {
        return point;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getPrice() {
        return price;
    }

    public static boolean handlerOrder(Order order){
        String shopitem = order.getPlanTitle();
        if(shopItems.containsKey(shopitem)){
            ShopItem shopItem = shopItems.get(shopitem);
            String p = order.getRemark();
            UUID uuid = null;
            if(Bukkit.getPlayerExact(p)!=null){
                uuid = Bukkit.getPlayerExact(p).getUniqueId();
            }else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p);
                if(offlinePlayer!=null)
                    uuid = offlinePlayer.getUniqueId();
                else return false;
            }
            playerPointsApi.give(uuid,shopItem.getPoint());
            Map<String,String> params = new HashMap<>();
            params.put("@player@",p);
            params.put("@shopitem@",shopItem.getDisplayName());
            params.put("@point@",shopItem.getPoint()+"");
            params.put("@price@",shopItem.getPrice());
            params.put("@tradeNo@",order.getOutTradeNo());
            params.put("@current_point@",playerPointsApi.look(uuid)+"");
            Player playerExact = Bukkit.getPlayerExact(p);
            for (String command : shopItem.getCommands()) {
                if(command.startsWith("[title]")){
                    command = command.replace("[title]","");
                    command = ChatColor.translateAlternateColorCodes('&',command);
                    String[] split = command.split("/");
                    if(playerExact!=null){
                        playerExact.sendTitle(replace(params,split[0]),
                                replace(params,split[1]));
                    }
                    continue;
                }else if(command.startsWith("[message]")){
                    command = command.replace("[message]","");
                    command = ChatColor.translateAlternateColorCodes('&',command);
                    if(playerExact!=null){
                        playerExact.sendMessage(replace(params,command));
                    }
                    continue;
                }
                String finalCommand = command;
                Bukkit.getScheduler().runTask(AifadianPay.getPlugin(AifadianPay.class),()->{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand.replace("@p@",order.getRemark()));
                });
            }
            return true;
        }
        return false;
    }
    private static String replace(Map<String,String> params,String mes){
        for (String k : params.keySet()) {
            mes = mes.replace(k, ChatColor.translateAlternateColorCodes('&',params.get(k)));
        }
        return mes;
    }

    public static void reload(AifadianPay plugin){
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection shopItem = config.getConfigurationSection("shopItem");
        ShopItem.shopItems.clear();
        for (String key : shopItem.getKeys(false)) {
            ShopItem.shopItems.put(key,new ShopItem(shopItem.getConfigurationSection(key)));
        }
    }
}
