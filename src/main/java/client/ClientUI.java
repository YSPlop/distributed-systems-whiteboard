package client;

import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO save when new
//TODO disable kick self
public class ClientUI extends JFrame{
    //private DefaultListModel listModel;
    private String userID;
    private ClientUserList clientUserList;

    private ClientMessenger clientMessenger;

    private ClientDrawer clientDrawer;
    private JPanel mainPanel;

    private JPanel canvas;
    private BufferedImage bImg;

    /**
     * lock for not only drawing, but reset...
     */
    private Lock drawLock;

    private CanvasListener canvasListener;
    private JButton sendButton;

    private JButton kickButton;

    private JTextField chatInputField;
    private JList userList;
    private JScrollPane userPane;
    private JScrollPane chatPane;
    private JTextArea chatArea;
    private JButton circButton;
    private JButton rectButton;
    private JButton ovalButton;
    private JButton triButton;
    private JButton lineButton;
    private JButton freeButton;
    private JButton textButton;
    private JButton colorButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton newButton;

    public ClientMessenger getClientMessenger() {
        return clientMessenger;
    }

    public String getUserID(){
        return userID;
    }

    public void setClientDrawer(ClientDrawer clientDrawer){
        this.clientDrawer = clientDrawer;
        canvasListener.setClientDrawer(clientDrawer);
    }

    public ClientUI(String userID, ClientUserList clientUserList){
        this.userID = userID;
        if(userID.charAt(userID.length() - 2) == '0'){
            setTitle("WhiteBoard-Admin");
        }else{
            setTitle("WhiteBoard-Client");
        }

        setSize(800, 515);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(mainPanel);
        setVisible(true);
        setResizable(false);

        canvas.setBorder(BorderFactory.createLineBorder(Color.black));
        bImg = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D imgGraphics = bImg.createGraphics();
        imgGraphics.setColor(Color.white);
        imgGraphics.fillRect(1, 1, getWidth(), getHeight());

        drawLock = new ReentrantLock();
        //TODO
        canvasListener = new CanvasListener(this);


        chatArea.setEditable(false);

        if(clientUserList != null){
            this.clientUserList = clientUserList;
        }

        userList = new JList(clientUserList.getListModel());

        userPane.getViewport().add(userList);

        clientMessenger = new ClientMessenger(chatArea);



        //add listeners
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String msg = userID + "$" + chatInputField.getText();
                try {
                    clientMessenger.commit(msg);
                } catch (RemoteException ex) {
                    System.err.println("RemoteException when send message: " + ex);
                }
                chatInputField.setText("");
            }
        });
        kickButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if('0' != userID.charAt(userID.length() - 2)){
                    JOptionPane.showMessageDialog(mainPanel,
                            "You're not admin.",
                            "Don't even try",
                            JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                if(userList.getSelectedValue() != null ) {
                    try {
                        //kick admin, kick everyone
                        if(userList.getSelectedValue().equals(userID)){
                            clientUserList.removeAll();
                        }
                        clientUserList.removeElement((String) userList.getSelectedValue());

                    }
                    catch (RemoteException er){
                        System.err.println("RemoteException when kick user: " + er);
                    }
                }
            }
        });
        ovalButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                canvasListener.setShape("Oval");

            }
        });
        triButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                canvasListener.setShape("Triangle");
            }
        });

        canvas.addMouseListener(canvasListener);
        canvas.addMouseMotionListener(canvasListener);
        textButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                canvasListener.setShape("Text");
            }
        });

        freeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                canvasListener.setShape("Free");
            }
        });
        rectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                canvasListener.setShape("Rect");
            }
        });
        colorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Color c = JColorChooser.showDialog(canvas, "Choose color", null);

                canvas.getGraphics().setColor(c);
                bImg.getGraphics().setColor(c);
                canvasListener.setColor(c);

            }
        });
        circButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                canvasListener.setShape("Circle");
            }
        });
        lineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                canvasListener.setShape("Line");
            }
        });
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                //TODO show canvas after resize
                //canvas.repaint();
            }
        });
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                saveAs();
            }
        });
        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String fName = JOptionPane.showInputDialog("Filename:");
                File f = new File(fName);
                try {
                    bImg = ImageIO.read(f);
                } catch (IOException ex) {
                    System.err.println("Load file failed: " + ex);
                }

                canvas.getGraphics().drawImage(bImg, 0, 0, null);

                System.out.println("Loaded");

            }
        });
        newButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String op = (String) JOptionPane.showInputDialog(canvas, "Do you want to save current graphics?", "save", JOptionPane.PLAIN_MESSAGE, null, new String[]{"yes", "no"}, "yes");
                if(op.equals("yes")){
                    saveAs();
                }

                Command command = new Command("CanvasListener", "Clear", Color.white);
                try {
                    draw(command);
                    bImg.createGraphics().setBackground(Color.WHITE);
                    clientDrawer.commit(command);

                } catch (RemoteException ex) {
                    System.err.println("New file failed: " + ex);
                }
            }
        });
    }

    public void draw(Command command){
        //TODO username
        if(Objects.equals(command.name, userID)){
            return;
        }
        drawLock.lock();
        draw(command, canvas.getGraphics());
        draw(command, bImg.getGraphics());
        drawLock.unlock();
    }

    /**
     * should only invoke when drawLock is held.
     * @param command
     * @param graphics
     */
    private void draw(Command command, Graphics graphics){
        graphics.setColor(command.color);
        //System.out.println(command.toString());
        switch (command.shape){
            case "Line":
                graphics.drawLine(command.num1, command.num2, command.num3, command.num4);
                break;
            case "Oval":
                graphics.drawOval(command.num1, command.num2, command.num3, command.num4);
                break;
            case "Circle":
                graphics.drawOval(command.num1, command.num2, command.num3, command.num3);
                break;
            case "Rect":
                graphics.drawRect(command.num1, command.num2, command.num3, command.num4);
                break;
            case "Free":
                graphics.drawOval(command.num1, command.num2, 2, 2);
                break;
            case "Triangle":
                graphics.drawLine(command.num1, command.num2, command.num3, command.num4);
                graphics.drawLine(command.num1, command.num2, command.num3, command.num2);
                graphics.drawLine(command.num3, command.num2, command.num3, command.num4);
                break;
            case "Text":
                //name type size string x y
                Font f = new Font(null, Font.PLAIN, Math.max(command.num1, 10));
                graphics.setFont(f);
                graphics.drawString(command.str1, command.num2, command.num3);
                break;
            case "Clear":
                graphics.clearRect(1, 1, getWidth(), getHeight());
                graphics.setColor(Color.black);
                break;

        }
    }

    private void saveAs(){
        String fName = JOptionPane.showInputDialog("Filename:");
        File f = new File(fName);
        try {

            ImageIO.write(bImg, "png", f);

            System.out.println("saved");
        } catch (IOException ex) {
            System.err.println("Save file failed: " + ex);
        }
    }

}
