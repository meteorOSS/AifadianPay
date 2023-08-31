package com.meteor.aifadianpay.commands.sub;

import com.meteor.aifadianpay.AifadianPay;
import com.meteor.aifadianpay.commands.Icmd;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCmd extends Icmd {
    public HelpCmd(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "apl.use.help";
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
        AifadianPay.messageBox.getMessageList(null,"message.help")
                .forEach(mes->p0.sendMessage(mes));
    }
}
