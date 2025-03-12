package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.RemoteException;


public class CanvasListener implements MouseListener, MouseMotionListener {

    private String shape;

    private Color color;

    private ClientUI clientUI;

    private ClientDrawer clientDrawer;

    private int xPressed;

    private int yPressed;

    private int xReleased;

    private int yReleased;

    public void setClientDrawer(ClientDrawer clientDrawer) {
        this.clientDrawer = clientDrawer;
    }

    public CanvasListener(ClientUI clientUI){
        this.clientUI = clientUI;
        shape = "None";
        color = Color.black;
    }


    /*
    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setImgGraphics(Graphics imgGraphics){
        this.imgGraphics = imgGraphics;
    }
    */

    public void setShape(String shape){
        this.shape = shape;
    }
    public void setColor(Color color){
        this.color = color;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int xClicked = e.getX();
        int yClikcked = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xPressed = e.getX();
        yPressed = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        xReleased = e.getX();
        yReleased = e.getY();
        Command command = new Command("canvasListener", shape, color);
        if(shape=="Oval" || shape=="Rect" || shape=="Circle"){
            command.num1 = Math.min(xPressed, xReleased);
            command.num2 = Math.min(yPressed, yReleased);
            command.num3 = Math.abs(xPressed - xReleased);
            command.num4 = Math.abs(yPressed - yReleased);
            clientUI.draw(command);
        } else if (shape=="Line" || shape=="Triangle") {
            command.num1 = xPressed;
            command.num2 = yPressed;
            command.num3 = xReleased;
            command.num4 = yReleased;
            clientUI.draw(command);
        } else if (shape=="Text") {
            String text = JOptionPane.showInputDialog("Annotate:");
            if(text!=null) {
                command.str1 = text;
                command.num1 = Math.abs(xPressed - xReleased);
                command.num2 = xPressed;
                command.num3 = yPressed;
                clientUI.draw(command);
            }
        }
        if(command.shape != "None") {
            try {
                command.name = clientUI.getUserID();
                clientDrawer.commit(command);
            } catch (RemoteException ex) {
                System.err.println("RemoteException when commit draw: " + ex);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int xDragged = e.getX();
        int yDragged = e.getY();
        if(shape == "Free"){
            Command command = new Command("canvasListener", shape, color);
            command.num1 = xDragged;
            command.num2 = yDragged;
            command.num3 = 2;
            command.num4 = 2;

            clientUI.draw(command);
            try {
                command.name = clientUI.getUserID();
                clientDrawer.commit(command);
            } catch (RemoteException ex) {
                System.err.println("RemoteException when commit draw: " + ex);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
