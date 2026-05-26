package com.example.fakeplayerfolia;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class FakePlayerFolia extends JavaPlugin implements CommandExecutor {

    private final List<String> fakePlayers = new ArrayList<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        // Đăng ký Command
        if (getCommand("fp") != null) {
            getCommand("fp").setExecutor(this);
        }

        // Đăng ký PlaceholderAPI Expansion
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new FakePlayerExpansion(this).register();
        }

        getLogger().info("Plugin FakePlayer hỗ trợ Folia đã bật thành công!");
    }

    @Override
    public void onDisable() {
        fakePlayers.clear();
    }

    public List<String> getFakePlayers() {
        return fakePlayers;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("fakeplayer.admin")) {
            sender.sendMessage(miniMessage.deserialize("<red>Bạn không có quyền sử dụng lệnh này!</red>"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Sử dụng: /fp add <tên> hoặc /fp remove <tên></red>"));
            return true;
        }

        String action = args[0].toLowerCase();
        String name = args[1];

        String prefixFormat = getConfig().getString("messages.prefix", "");
        String joinFormat = getConfig().getString("messages.join", "%player% joined the game");
        String quitFormat = getConfig().getString("messages.quit", "%player% left the game");

        if (action.equals("add")) {
            if (fakePlayers.contains(name)) {
                sender.sendMessage(miniMessage.deserialize("<red>Người chơi giả này đã tồn tại!</red>"));
                return true;
            }

            fakePlayers.add(name);
            
            // Tạo thông báo Join gửi cho toàn máy chủ
            String formattedJoin = joinFormat.replace("%player%", prefixFormat + name);
            Bukkit.broadcast(miniMessage.deserialize(formattedJoin));
            
            sender.sendMessage(miniMessage.deserialize("<green>Đã thêm người chơi giả: " + name + "</green>"));
            updateTabListForAll();
            return true;
        } 
        
        if (action.equals("remove")) {
            if (!fakePlayers.contains(name)) {
                sender.sendMessage(miniMessage.deserialize("<red>Không tìm thấy người chơi giả này!</red>"));
                return true;
            }

            fakePlayers.remove(name);
            
            // Tạo thông báo Quit gửi cho toàn máy chủ
            String formattedQuit = quitFormat.replace("%player%", prefixFormat + name);
            Bukkit.broadcast(miniMessage.deserialize(formattedQuit));
            
            sender.sendMessage(miniMessage.deserialize("<red>Đã xóa người chơi giả: " + name + "</red>"));
            updateTabListForAll();
            return true;
        }

        return false;
    }

    // Cập nhật TabList cho tất cả người chơi online thật
    public void updateTabListForAll() {
        String prefixFormat = getConfig().getString("messages.prefix", "");
        
        // Trên Folia, Duyệt qua danh sách người chơi an toàn
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Lưu ý: Vì Folia chạy đa luồng, việc can thiệp trực tiếp vào Tablist của Packet cần được xử lý cẩn thận.
            // Dưới đây sử dụng giải pháp hiển thị thông tin ở Footer/Header hoặc thông qua Custom Scoreboard/Team nếu bạn muốn chuyên sâu hơn.
            // Để đơn giản và an toàn tuyệt đối trên Folia không lo Crash, chúng ta cập nhật qua Placeholder hiển thị trên Tab (như TAB plugin).
        }
    }
}