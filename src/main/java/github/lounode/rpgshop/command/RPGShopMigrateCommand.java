package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.shop.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 迁移数据从Yml到数据库
 *
 * @author Lounode
 * @date 2024/08/27
 */
public class RPGShopMigrateCommand extends RPGShopCommand{
    public RPGShopMigrateCommand(RPGShop plugin) {
        super(plugin);
        this.command = "migrate";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder()+ File.separator + "RPGShop.db");
            statement = connection.createStatement();

            List<Shop> shops = plugin.getShopManager().getShops();

            for (Shop shop : shops) {
                String insertSQL = String.format("INSERT INTO Shops (id, title, row, owner, create_time, last_editor, last_edit_time) " +
                        "VALUES ('%s', '%s', %d, '%s', '%s', '%s', '%s')", shop.getID(), shop.getTitle(), shop.getRows(), shop.getOwner(), shop.getCreateTime(), shop.getLastEditor(), shop.getLastEditTime());
                statement.executeUpdate(insertSQL);
            }
            /*
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER)";
            statement.executeUpdate(createTableSQL);

            // 插入数据
            String insertSQL = "INSERT INTO users (name, age) VALUES ('John', 25)";
            statement.executeUpdate(insertSQL);
            */
            plugin.getLogger().info("数据插入成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭Statement和Connection
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
