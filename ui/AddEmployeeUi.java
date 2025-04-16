package ui;

import bean.Employee;
import util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEmployeeUi extends JFrame {
    private JTextField txtName, txtSex, txtAge, txtPhone, txtMajor, txtClass;
    private JFormattedTextField txtEnrollmentDate;
    private JButton btnSave, btnCancel;
    private EmployeeManagerUi employeeManagerUi;
    private boolean isEditing; // 标志变量，用于区分是添加还是编辑
    private Employee employeeToEdit; // 编辑时使用的员工对象


    public AddEmployeeUi(EmployeeManagerUi employeeManagerUi) {
        this(employeeManagerUi, null);
    }

    public AddEmployeeUi(EmployeeManagerUi employeeManagerUi, Employee employeeToEdit) {
        super("添加/编辑学员");
        this.employeeManagerUi = employeeManagerUi;
        this.employeeToEdit = employeeToEdit;
        if (employeeToEdit != null) {
            isEditing = true;
        } else {
            isEditing = false;
        }
        initialize();
        this.setVisible(true);
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // 设置组件之间的间距

        Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);

        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel jLabel = new JLabel("姓名：");
        jLabel.setFont(labelFont);
        add(jLabel, gbc);

        gbc.gridx = 1;
        txtName = new JTextField(20);
        add(txtName, gbc);

        // 性别
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel jLabel2 = new JLabel("性别：");
        jLabel2.setFont(labelFont);
        add(jLabel2, gbc);

        gbc.gridx = 1;
        txtSex = new JTextField(20);
        add(txtSex, gbc);

        // 年龄
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jLabel3 = new JLabel("年龄：");
        jLabel3.setFont(labelFont);
        add(jLabel3, gbc);

        gbc.gridx = 1;
        txtAge = new JTextField(20);
        add(txtAge, gbc);

        // 电话
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel jLabel4 = new JLabel("电话：");
        jLabel4.setFont(labelFont);
        add(jLabel4, gbc);

        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        add(txtPhone, gbc);

        // 专业
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel jLabel5 = new JLabel("专业：");
        jLabel5.setFont(labelFont);
        add(jLabel5, gbc);

        gbc.gridx = 1;
        txtMajor = new JTextField(20);
        add(txtMajor, gbc);

        // 入学日期
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel jLabel6 = new JLabel("入学日期：");
        jLabel6.setFont(labelFont);
        add(jLabel6, gbc);

        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtEnrollmentDate = new JFormattedTextField(dateFormat);
        txtEnrollmentDate.setColumns(20);
        add(txtEnrollmentDate, gbc);

        // 班级
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel jLabel7 = new JLabel("班级：");
        jLabel7.setFont(labelFont);
        add(jLabel7, gbc);

        gbc.gridx = 1;
        txtClass = new JTextField(20);
        add(txtClass, gbc);

        // 按钮
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSave = new JButton("保存");
        btnSave.setFont(labelFont);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });
        add(btnSave, gbc);

        gbc.gridy = 8;
        btnCancel = new JButton("取消");
        btnCancel.setFont(labelFont);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(btnCancel, gbc);

        // 如果是编辑模式，填充表单
        if (isEditing) {
            txtName.setText(employeeToEdit.getName());
            txtSex.setText(employeeToEdit.getSex());
            txtAge.setText(String.valueOf(employeeToEdit.getAge()));
            txtPhone.setText(employeeToEdit.getPhone());
            txtMajor.setText(employeeToEdit.getMajor());
            txtEnrollmentDate.setText(dateFormat.format(employeeToEdit.getDate()));
            txtClass.setText(employeeToEdit.getClassname());
        }

        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void saveEmployee() {
        // 字段非空校验

        String name = txtName.getText();
        String sex = txtSex.getText();
        int age = Integer.parseInt(txtAge.getText());
        try {
            age = Integer.parseInt(txtAge.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "年龄必须为数字", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String phone = txtPhone.getText();
        String major = txtMajor.getText();
        String className = txtClass.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (txtName.getText().isEmpty() || txtSex.getText().isEmpty() || txtAge.getText().isEmpty() || txtPhone.getText().isEmpty() || txtMajor.getText().isEmpty() || txtEnrollmentDate.getText().isEmpty() || txtClass.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整员工信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            date = dateFormat.parse(txtEnrollmentDate.getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "日期格式不正确，请使用 yyyy-MM-dd 格式", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isEditing) {
            // 更新员工信息
            employeeToEdit.setName(name);
            employeeToEdit.setSex(sex);
            employeeToEdit.setAge(age);
            employeeToEdit.setPhone(phone);
            employeeToEdit.setMajor(major);
            employeeToEdit.setDate(date);
            employeeToEdit.setClassname(className);
            updateEmployee(employeeToEdit);
        } else {
            // 添加新员工
            Employee newEmployee = new Employee(name, sex, age, phone, major, date, className);
            addEmployee(newEmployee);
        }
        employeeManagerUi.updateTableModel();
        dispose();
    }

    private void addEmployee(Employee employee) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO employees (name, sex, age, phone, major, date, classname) VALUES (?, ?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getSex());
            statement.setInt(3, employee.getAge());
            statement.setString(4, employee.getPhone());
            statement.setString(5, employee.getMajor());
            statement.setDate(6, new java.sql.Date(employee.getDate().getTime()));
            statement.setString(7, employee.getClassname());
            if (txtEnrollmentDate.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "入学日期不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            statement.executeUpdate();

            // 获取生成的ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                employee.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存员工信息失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee(Employee employee) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE employees SET name = ?, sex = ?, age = ?, phone = ?, major = ?, date = ?, classname = ? WHERE id = ?")) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getSex());
            statement.setInt(3, employee.getAge());
            statement.setString(4, employee.getPhone());
            statement.setString(5, employee.getMajor());
            statement.setDate(6, new java.sql.Date(employee.getDate().getTime()));
            statement.setString(7, employee.getClassname());
            statement.setInt(8, employee.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "更新员工信息失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
