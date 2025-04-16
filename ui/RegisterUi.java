package ui;

import bean.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUi extends JFrame implements ActionListener {
    private JTextField usernameField, loginNameField;
    private JPasswordField passwordField;
    private JButton btnRegister;

    public RegisterUi() {
        super("注册界面");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 240, 240));
        Font font = new Font("微软雅黑", Font.PLAIN, 16);

        // 用户名
        JLabel lblUsername = new JLabel("用户名:");
        lblUsername.setBounds(50, 50, 100, 30);
        lblUsername.setFont(font);
        panel.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(160, 50, 190, 30);
        panel.add(usernameField);

        // 登录名
        JLabel lblLoginName = new JLabel("登录名:");
        lblLoginName.setBounds(50, 100, 100, 30);
        lblLoginName.setFont(font);
        panel.add(lblLoginName);

        loginNameField = new JTextField();
        loginNameField.setBounds(160, 100, 190, 30);
        panel.add(loginNameField);

        // 密码
        JLabel lblPassword = new JLabel("密码:");
        lblPassword.setBounds(50, 150, 100, 30);
        lblPassword.setFont(font);
        panel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 150, 190, 30);
        panel.add(passwordField);

        // 注册按钮
        btnRegister = new JButton("注    册");
        btnRegister.setBounds(100, 200, 200, 30);
        btnRegister.setFont(font);
        btnRegister.setBackground(new Color(66, 135, 245));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(this);
        panel.add(btnRegister);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String loginName = loginNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || loginName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查登录名是否已存在
        if (LoginUi.allUsers.stream().anyMatch(u -> u.getLoginName().equals(loginName))) {
            JOptionPane.showMessageDialog(this, "登录名已存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 添加新用户
        LoginUi.allUsers.add(new User(username, password, loginName));
        JOptionPane.showMessageDialog(this, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}