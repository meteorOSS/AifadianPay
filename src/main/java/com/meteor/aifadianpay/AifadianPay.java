package com.meteor.aifadianpay;

import com.meteor.aifadianpay.api.events.GainRewardsEvent;
import com.meteor.aifadianpay.commands.AifadianCommand;
import com.meteor.aifadianpay.data.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class AifadianPay extends JavaPlugin{

    private QueryTask queryTask;
    public static MessageBox messageBox;
    private Metrics metrics;

    private AifadianCommand aifadianCommand;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        reload();

        (aifadianCommand = new AifadianCommand(this)).init();
        getCommand("apl").setExecutor(aifadianCommand);
        metrics = new Metrics(this,19687);

        File logs = new File(getDataFolder()+"/logs.yml");
        this.queryTask = new QueryTask(logs.exists()? YamlConfiguration.loadConfiguration(logs)
                :new YamlConfiguration());
        queryTask.runTaskTimerAsynchronously(this,20L,getConfig().getLong("setting.delay",3)*60*20);
        getLogger().info("plugin version: "+getDescription().getVersion());
        getLogger().info("插件已启用，请参考mcbbs贴内教程使用!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("拜拜喵");
    }



    public void reload(){
        reloadConfig();
        ShopItem.reload(this);
        File file = new File(getDataFolder()+"/message.yml");
        if(!file.exists()) saveResource("message.yml",false);
        messageBox = MessageBox.createMessageBox(this,"message.yml");
        QueryTask.init(getConfig().getString("setting.userId"),getConfig().getString("setting.token"));
    }
}
