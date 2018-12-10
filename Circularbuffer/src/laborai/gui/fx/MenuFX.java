/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laborai.gui.fx;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author Martynas
 */
public abstract class MenuFX extends MenuBar implements EventHandler<ActionEvent> {

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    public MenuFX() {
        initComponents();
    }

    private void initComponents() {
        // Sukuriamas meniu      
        Menu menu1 = new Menu(MESSAGES.getString("menu1"));
        MenuItem menuItem11 = new MenuItem(MESSAGES.getString("menuItem11"));
        menuItem11.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        menuItem11.setOnAction(this);
        MenuItem menuItem13 = new MenuItem(MESSAGES.getString("menuItem13"));
        menuItem13.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        menuItem13.setOnAction(this);
        menu1.getItems().addAll(menuItem11, new SeparatorMenuItem(), menuItem13);

        Menu menu2 = new Menu(MESSAGES.getString("menu2"));
        MenuItem menuItem21 = new MenuItem(MESSAGES.getString("menuItem21"));
        menuItem21.setAccelerator(new KeyCodeCombination(KeyCode.F1, KeyCombination.SHIFT_DOWN));
        menuItem21.setOnAction(this);
        menu2.getItems().add(menuItem21);

        getMenus().addAll(menu1, menu2);
    }

    @Override
    public abstract void handle(ActionEvent ae);
}

