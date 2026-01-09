package org.shaoshuai.middleware.boot.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author yan
 * @Date 2026/1/9
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student {
    private String rowId;

    private String name;

    private Integer age;

    private String createdDate;

    public Student(String name, Integer age, String createdDate) {
        this.name = name;
        this.age = age;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rowId='" + rowId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
