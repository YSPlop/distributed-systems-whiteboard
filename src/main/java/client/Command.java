package client;

import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Command implements Serializable {
    public String name;
    public String shape;
    public Color color;
    public int  num1;
    public int num2;
    public int num3;
    public int num4;
    public String str1;

    public Command(String name, String shape, Color color) {


        this.name = name;
        this.shape = shape;
        this.color = color;
        this.str1 = "none";
    }

}
