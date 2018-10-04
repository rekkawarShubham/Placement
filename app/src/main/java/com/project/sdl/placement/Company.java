package com.project.sdl.placement;

public class Company {
    private String c_name;
    private String c_salary;
    private String c_skills;
    private String c_type;
    private int c_image;

    public Company(String c_name, String c_salary, String c_skills, String c_type, int c_image) {
        this.c_name = c_name;
        this.c_salary = c_salary;
        this.c_skills = c_skills;
        this.c_type = c_type;
        this.c_image = c_image;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_salary() {
        return c_salary;
    }

    public void setC_salary(String c_salary) {
        this.c_salary = c_salary;
    }

    public String getC_skills() {
        return c_skills;
    }

    public void setC_skills(String c_skills) {
        this.c_skills = c_skills;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public int getC_image() {
        return c_image;
    }

    public void setC_image(int c_image) {
        this.c_image = c_image;
    }
}
