package edu.sustech.xiangqi.ui;

import java.awt.*;
import java.io.File;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Style {
    public static Color defaultColor = new Color(245, 222, 179);
    public static Color transprentColor = new Color(0, 0, 0, 0);

    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static Font defaultFont = new Font("隶书", Font.BOLD, 28);

    public static void initGlobalFont() {
        try {
            File fontFile = new File("src/main/java/edu/sustech/xiangqi/assets/fonts/vivoSansSCVF.ttf");
            if(fontFile.exists()){
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
                
                Enumeration<Object> keys = UIManager.getDefaults().keys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Object value = UIManager.get(key);
                    if (value instanceof FontUIResource) {
                        UIManager.put(key, new FontUIResource(customFont));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("字体加载失败: " + e.getMessage());
        }
    }
}
