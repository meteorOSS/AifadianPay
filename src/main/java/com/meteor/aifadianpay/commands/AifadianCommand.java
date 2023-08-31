package com.meteor.aifadianpay.commands;

import com.meteor.aifadianpay.AifadianPay;
import com.meteor.aifadianpay.commands.sub.*;
import org.bukkit.plugin.java.JavaPlugin;

public class AifadianCommand extends AbstractCommandManager{
    public AifadianCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        register(new CheckCmd(AifadianPay.getPlugin(AifadianPay.class)));
        register(new ReloadCmd(AifadianPay.getPlugin(AifadianPay.class)));
        register(new UpdateCmd(AifadianPay.getPlugin(AifadianPay.class)));
        register(new ShowUrl(AifadianPay.getPlugin(AifadianPay.class)));
        register(new HelpCmd(AifadianPay.getPlugin(AifadianPay.class)));
    }
}
