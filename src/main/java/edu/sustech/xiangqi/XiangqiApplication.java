package edu.sustech.xiangqi;

import edu.sustech.xiangqi.model.*;
import edu.sustech.xiangqi.ui.*;

import javax.swing.*;

public class XiangqiApplication {
    public static void main(String[] args) {
        WelcomePage newWelcomePage = new WelcomePage();
        SwingUtilities.invokeLater(() -> {
            newWelcomePage.setVisible(true);
        });
    }
}
