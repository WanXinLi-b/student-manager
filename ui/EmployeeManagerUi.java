package ui;

import bean.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class EmployeeManagerUi extends JFrame {
    private JFrame frame;
    private JTable table; // 表格
    private DefaultTableModel model; // 表格模型
    private JTextField searchTextField; // 搜索框

    public EmployeeManagerUi() {
    }

    public EmployeeManagerUi(String username) {
        super("欢迎，" + username + "!卓景京学员管理系统");
        frame = this;
        initialize();
        this.setVisible(true);
    }

    private void initialize() {
        this.setBounds(100, 100, 800, 600); // 设置窗口大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        // 输入框和搜索按钮
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchTextField = new JTextField("请输入任意字段进行搜索", 20); // 初始化提示文字
        searchTextField.setForeground(Color.GRAY); // 设置提示文字颜色为灰色
        JButton btnSearch = new JButton("搜索");
        JButton btnAdd = new JButton("添加");
        topPanel.add(searchTextField);
        // 添加焦点监听器实现提示效果
        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().equals("请输入任意字段进行搜索")) {
                    searchTextField.setText("");
                    searchTextField.setForeground(Color.BLACK); // 输入时文字颜色为黑色
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setForeground(Color.GRAY);
                    searchTextField.setText("请输入任意字段进行搜索");
                }
            }
        });
        topPanel.add(btnSearch);
        topPanel.add(btnAdd);

        // 创建表格模型
        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "姓名", "性别", "年龄", "电话", "专业", "入学日期", "班级"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格不允许编辑
            }
        };
        // 创建表格
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(30);

        // 右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("编辑");
        JMenuItem deleteItem = new JMenuItem("删除");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.addMouseListener(new MouseAdapter() { // 使用鼠标监听器Adapter 而不是Listener
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // 判断是否是鼠标右键
                    int row = table.rowAtPoint(e.getPoint()); // 获取鼠标点击的行
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row); // 选中行
                    }
                    popupMenu.show(table, e.getX(), e.getY()); // 显示右键菜单
                }
            }
        });
        deleteItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // 获取选中的行索引
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "确定删除该记录吗？",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (Integer) model.getValueAt(selectedRow, 0); // 获取选中行的ID
                    deleteEmployee(id);
                    model.removeRow(selectedRow); // 从模型中移除该行
                }
            }
        });
        // 添加按钮监听器
        btnAdd.addActionListener(e -> {
            new AddEmployeeUi(this);
        });
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        editItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (Integer) model.getValueAt(selectedRow, 0); // 获取选中行的ID
                // 查找对应的员工对象
                Employee employee = findEmployeeById(id);
                if (employee != null) {
                    new AddEmployeeUi(this, employee);
                }
            }
        });
        // 搜索按钮监听器
        btnSearch.addActionListener(e -> {
            String searchText = searchTextField.getText().toLowerCase();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter(searchText));
        });

        // 添加组件到主框架
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 初始化数据
        loadEmployees();
    }

    private void loadEmployees() {
        try (Connection connection = util.DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String sex = resultSet.getString("sex");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String major = resultSet.getString("major");
                Date date = resultSet.getDate("date");
                if (date == null) {
                    date = new Date(); // 赋予默认日期
                }
                String classname = resultSet.getString("classname");

                model.addRow(new Object[]{id, name, sex, age, phone, major, date, classname});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee(int id) {
        try (Connection connection = util.DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM employees WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee employee) {
        try (Connection connection = util.DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO employees (name, sex, age, phone, major, date, classname) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getSex());
            statement.setInt(3, employee.getAge());
            statement.setString(4, employee.getPhone());
            statement.setString(5, employee.getMajor());
            statement.setDate(6, new java.sql.Date(employee.getDate().getTime()));
            statement.setString(7, employee.getClassname());
            statement.executeUpdate();

            // 添加一行到表格
            model.addRow(new Object[]{employee.getId(), employee.getName(), employee.getSex(), employee.getAge(),
                    employee.getPhone(), employee.getMajor(), employee.getDate(), employee.getClassname()});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Employee findEmployeeById(int id) {
        try (Connection connection = util.DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String sex = resultSet.getString("sex");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String major = resultSet.getString("major");
                Date date = resultSet.getDate("date");
                String classname = resultSet.getString("classname");

                return new Employee(id, name, sex, age, phone, major, date, classname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateEmployee(Employee updatedEmployee) {
        try (Connection connection = util.DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE employees SET name = ?, sex = ?, age = ?, phone = ?, major = ?, date = ?, classname = ? WHERE id = ?")) {
            statement.setString(1, updatedEmployee.getName());
            statement.setString(2, updatedEmployee.getSex());
            statement.setInt(3, updatedEmployee.getAge());
            statement.setString(4, updatedEmployee.getPhone());
            statement.setString(5, updatedEmployee.getMajor());
            statement.setDate(6, new java.sql.Date(updatedEmployee.getDate().getTime()));
            statement.setString(7, updatedEmployee.getClassname());
            statement.setInt(8, updatedEmployee.getId());
            statement.executeUpdate();

            updateTableModel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateTableModel() {
        model.setRowCount(0); // 清空表格
        loadEmployees(); // 重新加载数据
    }
}
