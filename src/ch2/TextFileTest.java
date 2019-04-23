package ch2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Scanner;

/**
 * @Author: buku.ch
 * @Date: 2019-04-23 11:18
 */


public class TextFileTest {

    public static void main(String[] args) {

        Employee[] staff = new Employee[3];
        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        try (PrintWriter out = new PrintWriter("employee.dat","utf-8")){
            writeData(staff,out);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try (Scanner in = new Scanner(new FileInputStream("employee.dat"),"utf-8")){
            Employee[] newStaff = readData(in);
            for (Employee employee : newStaff) {
                System.out.println(employee);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static Employee[] readData(Scanner in) {
        int n = in.nextInt();
        in.nextLine();
        Employee[] staff = new Employee[n];
        for (int i = 0; i < n; i++) {
            staff[i] = readEmplyee(in);
        }
        return staff;
    }

    private static Employee readEmplyee(Scanner in) {
        String line = in.nextLine();
        String[] tokens = line.split("\\|");
        String name = tokens[0];
        int salary = Integer.parseInt(tokens[1]);
        LocalDate date = LocalDate.parse(tokens[2]);
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return new Employee(name, salary, year, month, day);
    }

    private static void writeData(Employee[] staff, PrintWriter out) {
        out.println(staff.length);
        for (Employee employee : staff) {
            writeEmployee(out,employee);
        }
    }

    private static void writeEmployee(PrintWriter out, Employee employee) {
        out.println(employee.getName()+"|"+employee.getSalary()+"|"+employee.getHireDay());
    }

    private static class Employee {

        private String name;
        private int salary;
        private LocalDate hireDay;

        public Employee(String name, int salary, int year, int month, int day) {
            this.name = name;
            this.salary = salary;
            this.hireDay = LocalDate.of(year,month,day);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public LocalDate getHireDay() {
            return hireDay;
        }

        public void setHireDay(LocalDate hireDay) {
            this.hireDay = hireDay;
        }


        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", salary=" + salary +
                    ", hireDay=" + hireDay +
                    '}';
        }
    }
}
