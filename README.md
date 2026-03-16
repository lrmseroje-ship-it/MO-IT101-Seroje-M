# MO-IT101-Seroje-M
Initial Payroll System Code
README
Team Details

Team Member: Marjury Seroje

Contribution:
For this project, I was responsible for developing the overall structure of the program. I designed the system flow and implemented the login feature for both employees and payroll staff. I developed the payroll processing functions and handled the computation of salaries, including the deductions for SSS, PhilHealth, Pag-IBIG, and withholding tax. I also implemented the part of the program that reads employee information and attendance records from the CSV files and uses this data for payroll computation. In addition, I created the payroll output display for both cutoff periods. To improve usability, I included loops in the menu options so users can return to the menu and check another employee’s data or process payroll again without restarting the program.

Program Details

This program is a simple payroll system written in Java. It reads employee information and attendance records from CSV files and uses them to compute the payroll of employees.

The system has two types of users: employee and payroll staff. Employees can log in and enter their employee number to see their basic information. Payroll staff can process payroll for one employee or process payroll for all employees for a selected month.

The program reads the employee details, attendance records, and SSS contributions from the CSV files. It calculates the total hours worked based on the login and logout times. The working hours are divided into two cutoff periods, which are days 1 to 15 and days 16 to the end of the month.

After computing the gross salary, the program also calculates the deductions such as SSS, PhilHealth, Pag-IBIG, and withholding tax. These deductions are used to compute the employee’s net salary.

The program also uses menu loops so the user can return to the menu after completing an action. This makes it easier to check another employee’s data or process payroll again without running the program from the beginning.

Project Plan Link
Project Plan:



