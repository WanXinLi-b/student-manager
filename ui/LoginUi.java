package ui;

import bean.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginUi extends JFrame implements ActionListener {
    private JTextField loginNameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JButton loginButton; // 登录按钮
    private JButton registerButton; // 注册按钮
    static ArrayList<User> allUsers = new ArrayList<>();

    // 初始化测试用户
    static {
        allUsers.add(new User("管理员", "123456", "admin"));
        allUsers.add(new User("张鸣", "dazui", "dawaizui"));
        allUsers.add(new User("李四", "123456", "lisi"));
    }

    public LoginUi() {
        super("登录界面");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        // 字体和颜色设置
        Font customFont = new Font("微软雅黑", Font.PLAIN, 16);
        Color primaryColor = new Color(66, 135, 245);

        // 标题
        JLabel titleLabel = new JLabel("卓景京学员管理系统");
        titleLabel.setBounds(50, 30, 300, 30);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        panel.add(titleLabel);

        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setBounds(50, 100, 150, 30);
        usernameLabel.setFont(customFont);
        panel.add(usernameLabel);

        loginNameField = new JTextField();
        loginNameField.setBounds(160, 100, 190, 30);
        loginNameField.setFont(customFont);
        panel.add(loginNameField);

        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(50, 150, 150, 30);
        passwordLabel.setFont(customFont);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 150, 190, 30);
        passwordField.setFont(customFont);
        passwordField.setEchoChar('●');
        panel.add(passwordField);

        // 登录按钮
        loginButton = new JButton("登    录");
        loginButton.setBounds(35, 200, 150, 30);
        loginButton.setFont(customFont);
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        // 注册按钮
        registerButton = new JButton("注    册"); // 1. 先初始化按钮
        registerButton.setBounds(200, 200, 150, 30); // 2. 设置坐标
        registerButton.setFont(customFont);
        registerButton.setBackground(primaryColor);
        registerButton.setForeground(Color.WHITE);
        panel.add(registerButton); // 3. 添加到面板
        registerButton.addActionListener(e -> { // 4. 最后绑定监听器
            new RegisterUi(); // 直接打开注册界面
        });

        this.add(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 仅处理登录按钮点击
        if (e.getSource() == loginButton) {
            System.out.println("登录开始...");
            login();
        }
    }

    private void login() {
        String loginName = loginNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // 非空校验
        if (loginName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = getUserByLoginName(loginName);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                System.out.println("登录成功");
                new EmployeeManagerUi(user.getUsername());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "密码错误，请重新输入");
            }
        } else {
            JOptionPane.showMessageDialog(this, "用户名不存在，请重新输入");
        }
    }

    private User getUserByLoginName(String loginName) {
        for (User user : allUsers) {
            if (user.getLoginName().equals(loginName)) {
                return user;
            }
        }
        return null;
    }
}