package org.total.example.spring_core.advanced.screen_saver;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@Setter
public abstract class ColorFrame extends JFrame {

    public ColorFrame() {
        setSize(300, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showOnRandomPlace() {
        Random random = new Random();
        setLocation(random.nextInt(1200), random.nextInt(700));
        getContentPane().setBackground(getColor());
        repaint();
    }

    protected abstract Color getColor();
}
