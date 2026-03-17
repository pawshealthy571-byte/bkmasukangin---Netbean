package bkpembawastress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public final class DbUtil {
    private DbUtil() {
    }

    public static void fillCombo(JComboBox<String> combo, String firstItem, String sql) {
        combo.removeAllItems();
        if (firstItem != null) {
            combo.addItem(firstItem);
        }
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String value = rs.getString(1);
                if (value != null) {
                    value = value.trim();
                    if (!value.isEmpty()) {
                        combo.addItem(value);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(combo, "Gagal memuat data: " + e.getMessage(), "Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}
