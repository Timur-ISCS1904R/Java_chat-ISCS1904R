package client;
import jdk.nashorn.internal.scripts.JO;
import network.Listener;
import java.lang.Object;
import network.TCPCon;
import sun.security.util.ArrayUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.util.*;
import javax.swing.plaf.OptionPaneUI;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
public class GUI extends JFrame implements ActionListener, Listener {
    private TCPCon con;
    private JTextField input = new JTextField();
    private JTextArea area = new JTextArea();
    private JScrollPane jsp = new JScrollPane(area);
    private JPanel dp = new JPanel(new BorderLayout());
    private JPanel up = new JPanel(new BorderLayout());
    private JButton cn = new JButton("Сменить имя");
    private JButton jb = new JButton("Отправить");
    private JButton cb = new JButton("Выход");
    private JPanel myPanel = new JPanel();
    private JTextField field1 = new JTextField(10);
    private JTextField field2 = new JTextField(7);
    private JTextField field3 = new JTextField(10);
    private JLabel l1 = new JLabel("Введите ip");
    private JLabel l2 = new JLabel("Введите порт");
    private JLabel l3 = new JLabel("Введите имя");
    private JButton ac=new JButton("Показать имена людей в чате");
    private JTextArea aca = new JTextArea();
    Random rand = new Random();
    int pn=rand.nextInt(10000+256);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });

    }
    private GUI() {
        super("Project Java Chat");
        DataBase db = new DataBase();
        db.connect();
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText", "Готово");
        myPanel.add(l1);
        myPanel.add(field1);
        myPanel.add(l2);
        myPanel.add(field2);
        myPanel.add(l3);
        myPanel.add(field3);
        JOptionPane.showMessageDialog(null, "1.Ваше имя не должно содержать пробелов.\n" + "2.Ваше сообщение не должно содержать пробелов,находящихся в начале или в конце сообщения.", "Правила", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, myPanel, "Ввод данных", JOptionPane.INFORMATION_MESSAGE);
        if (field1.getText().equals("") && field2.getText().equals("") && field3.getText().equals("")) {
            System.exit(0);
        }
        char[] k = field3.getText().toCharArray();
        while (field3.getText().equals("") || k[0] == ' ' || k[k.length - 1] == ' ') {
            field3.setText(JOptionPane.showInputDialog(null, "Вы не ввели имя,или ввели его некорректно.", "Ошибка", JOptionPane.ERROR_MESSAGE));
            k = field3.getText().toCharArray();
        }
        db.addName(pn,field3.getText());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        area.setEditable(false);
        area.setLineWrap(true);
        aca.setEditable(false);
        aca.setLineWrap(true);
        add(dp, BorderLayout.SOUTH);
        dp.add(input, BorderLayout.CENTER);
        add(jsp, BorderLayout.CENTER);
        dp.add(jb, BorderLayout.EAST);
        add(up, BorderLayout.NORTH);
        up.add(ac,BorderLayout.CENTER);
        ac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList nicks=db.AllConnections();
                for(int i=0;i<nicks.size();i++){
                    aca.append(nicks.get(i)+"\n");
                }
                JOptionPane.showMessageDialog(null,aca,"Все подключенные",JOptionPane.INFORMATION_MESSAGE);
                aca.setText(null);
            }
        });
        up.add(cn, BorderLayout.WEST);
        cn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = field3.getText();
                field3.setText(JOptionPane.showInputDialog(null, "Введите новое имя", "Смена имени", JOptionPane.NO_OPTION));
                if (field3.getText().equals("")) {
                    field3.setText(s);
                }
                char[] k = field3.getText().toCharArray();
                while (k[0] == ' ' || k[k.length - 1] == ' ') {
                    field3.setText(JOptionPane.showInputDialog(null, "Вы не ввели имя,или ввели его некорректно.", "Ошибка", JOptionPane.ERROR_MESSAGE));
                    k = field3.getText().toCharArray();
                }
                if (!field3.getText().equals(s)) {
                    con.sendMes(s + " сменил имя на " + field3.getText());
                     db.changeName(pn,field3.getText());
                }
            }
        });
        up.add(cb, BorderLayout.EAST);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.Delete(pn);
                System.exit(0);
            }
        });
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m = input.getText();
                char[] k = m.toCharArray();
                if (m.equals("") || k[0] == ' ' || k[k.length - 1] == ' ') {
                    JOptionPane.showMessageDialog(null, "Уберите лишние пробелы");
                    return;
                }
                input.setText(null);
                con.sendMes(field3.getText() + ": " + m);
            }
        });
        input.addActionListener(this);
        try {
            con = new TCPCon(this, field1.getText(), Integer.parseInt(field2.getText()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Сервер не надйен", "Ошибка", JOptionPane.ERROR_MESSAGE);
            db.Delete(pn);
            System.exit(0);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                db.Delete(pn);
                System.exit(1);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String m = input.getText();
        char[] k = m.toCharArray();
        if (m.equals("") || k[0] == ' ' || k[k.length - 1] == ' ') {
            JOptionPane.showMessageDialog(null, "Уберите лишние пробелы");
            return;
        }
        input.setText(null);
        con.sendMes(field3.getText() + ": " + m);
    }

    @Override
    public void ConSucces(TCPCon tcpcon) {
        print("Подключение прошло успешно");
    }

    @Override
    public void Receiv(TCPCon tcpcon, String s) {
        print(s);
    }

    @Override
    public void DisCon(TCPCon tcpcon) {
        print("Вы отключились от сервера!!!");
    }

    @Override
    public void Exep(TCPCon tcpcon, Exception e) {
    }
    private synchronized void print(String m) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                area.append(m + "\n");
                area.setCaretPosition(area.getDocument().getLength());
            }
        });
    }
}

