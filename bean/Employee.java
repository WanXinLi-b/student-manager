package bean;

import lombok.*;

import java.util.Date;

// 员工信息:"ID", "姓名","性别", "年龄","电话","专业", "入学日期","班级"
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private int id;
    private String name;
    private String sex;
    private int age;
    private String phone;
    private String major;
    @Setter
    @Getter
    private Date date;
    private String classname;

    public Employee(String name, String sex, int age, String phone, String major, Date date, String className) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.phone = phone;
        this.major = major;
        this.date = date;
        this.classname = className;
    }

}
