package com.meteor.aifadianpay.commands.sub;

import com.meteor.aifadianpay.AifadianPay;
import com.meteor.aifadianpay.commands.Icmd;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCmd extends Icmd {
    public ReloadCmd(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "apl.admin.reload";
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
        AifadianPay.getPlugin(AifadianPay.class).reload();
        p0.sendMessage(AifadianPay.messageBox.getMessage(null,"message.reload"));
    }
}
