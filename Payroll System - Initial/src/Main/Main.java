package Main;
/* Payroll System Update 03/22
 *
 * This updated program processes employee payroll based on attendance records and employee details stored in CSV files.
 *
 * Key Features for the updated program:
 * - Efficient data handling using Maps, no longer repeats the reading means no redundant file reading
 * - Accurate payroll computation including SSS, PhilHealth, Pag-IBIG, and tax
 *
 * Design Improvements:
 * - Attendance and employee data are loaded once into memory
 * - Avoids performance bottlenecks like the attendance part when processing multiple employees
 * - Uses try-with-resources to prevent resource leaks
 *
 * This structure ensures scalability, maintainability, and correctness.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
public class Main {
	public static void main(String[] args) {
		// the Scanner is used here for user input
		Scanner scanner = new Scanner(System.in);
		//this part is where the user is ask whether or not he is an employee or payroll_staff
		System.out.println("Enter username");
		String username = scanner.nextLine();
		
		System.out.println("Enter password");
		String password = scanner.nextLine();
		
		Map<String, List<String[]>> attendanceMap = loadAttendance();
		Map<String, String[]> employeeMap = loadEmployees();
		//once the user checked in with their role, they will be routed to the appropriate menu
		if(username.equals("employee") && password.equals("12345")){
			 employeeLogin(scanner);
		}
		else if(username.equals("payroll_staff") && password.equals("12345")) {
			payroll_StaffLogin(scanner, attendanceMap, employeeMap);
		}
		else {
			System.out.println("Incorrect username and/or password. Access denied.");
			System.exit(0);
		}
		scanner.close();
	}
	// this method is the menu for the employees
	private static void employeeLogin(Scanner scanner) {
		int choice;
		
		//Loop until the user chooses to exit the program
		do {
			System.out.println(); //adding this so there would be a space between the select option and employee data
			System.out.println("Select option: ");
			System.out.println("1. Enter your employee number");
			System.out.println("2. Exit the program");
			System.out.println("Enter the number of your choice: ");
		
	    	choice = scanner.nextInt();
	    	scanner.nextLine();
	             
	    	if (choice == 1) {
	    		employeeData(scanner); // display employee details
         }
         else if (choice == 2) {
             System.out.println("Program terminated.");
         }
         else {
             System.out.println("Invalid choice.");
         }
		} while (choice != 2);
		
	}
	// this method here reads employee details from the MotorPH_Employee Data - Employee Details.csv
	public static void employeeData (Scanner scanner) {
			
			System.out.println("Enter your employee number: ");
			String employeeData = scanner.nextLine();
			
			String employeeNumber = "";
			String firstName = "";
			String lastName = "";
			String birthday = "";
			
			boolean found = false;
			
			try {
				//read the employee details from the CSV file 
				BufferedReader reader = new BufferedReader(new FileReader("resources/MotorPH_Employee Data - Employee Details.csv"));
				
				String line;
				
				reader.readLine();
				
				while ((line = reader.readLine()) !=null) {
					if (line.trim().isEmpty()) continue;
					String [] data = line.split(",");
					// to check if the employee number matches with the data, crucial part of this program
					if(data[0].equals(employeeData)) {
						employeeNumber = data[0];
						lastName = data[1];
						firstName = data[2];
						birthday = data[3];
						found = true;
		                break;
					}
		        }
		        reader.close();
			}
			catch(Exception e) {
				System.out.println("Error reading employee file.");
				return;
			}
			if(!found){
		        System.out.println("Employee number does not exist.");
		        return;
			}
			//display employee details
			System.out.println("\n===================================");
			System.out.println("Employee Number: " + employeeNumber);
			System.out.println("Employee Name: " + lastName+ ", " + firstName);
			System.out.println("Birthday: " + birthday);
			System.out.println("===================================");
		
	}
		// this method is the menu of the payaroll_staff
	private static void payroll_StaffLogin(Scanner scanner,  Map<String, List<String[]>> attendanceMap,
			Map<String, String[]> employeeMap) {
		int choice;
		// the options will show again after showing an employee data, can choose 2 to exit the program
		do {
			System.out.println();
			System.out.println("Select option: ");
			System.out.println("1. Process Payroll");
			System.out.println("2. Exit the program");
			System.out.println("Enter the number of your choice: ");
		
	    	choice = scanner.nextInt();
	    	scanner.nextLine();
	             
	    	if (choice == 1) {
             processPayroll(scanner, attendanceMap, employeeMap);
         }
         else if (choice == 2) {
             System.out.println("Program terminated.");
         }
         else {
             System.out.println("Invalid choice.");
         }
		} while (choice != 2);
		
		
	}
	// this method allows the user (payroll_staff) to view one employee or all employees as they select the option
	private static void processPayroll(Scanner scanner, Map<String, List<String[]>> attendanceMap, Map<String, String[]> employeeMap ) {
		System.out.println();
		System.out.println("Select option: ");
		System.out.println("1. One employee");
		System.out.println("2. All employees");
		System.out.println("3. Exit the program");
		System.out.println("Enter the number of your choice: ");
		
		int choice = scanner.nextInt();
		scanner.nextLine();
		
		if (choice == 1) {
         processOneEmployee(scanner, attendanceMap, employeeMap); //to process/view payroll for only one employee
     }
		else if(choice == 2) {
			System.out.print("Enter month (1-12): ");
		    int month = scanner.nextInt();
		    scanner.nextLine();

		    processAllEmployees(month, attendanceMap, employeeMap); //to process/view payroll for all employees
		}
     else if (choice == 3) {
         System.out.println("Program terminated.");
		
	}
	}
	//method used to process for one employee, only short because their is another method called processemployeeData connected here
	//also, this area is the key for processing all employee details
	private static void processOneEmployee(Scanner scanner, Map<String, List<String[]>> attendanceMap,
			Map<String, String[]> employeeMap) {
		
		System.out.print("Enter employee number: ");
	    String employeeNumber = scanner.nextLine();

	    System.out.print("Enter month (1-12): ");
	    int month = scanner.nextInt();
	    scanner.nextLine();

	    processEmployeeData(employeeNumber, month, attendanceMap, employeeMap);
	}
	//compute SSS contribution based on salary range from the SSS CSV file
	private static double computeSSS(double basicSalary) {
		//maximum SSS salary cap
		if (basicSalary > 24750) {
	        basicSalary = 24750;
	    }
		
		try (
			//reads the SSS contribution details based on salary range
		        BufferedReader reader = new BufferedReader(new FileReader("resources/SSS Contribution.csv"))){
		        
		        String line;
		        reader.readLine();
		        
		        while ((line = reader.readLine()) != null) {
		        	
		            if (line.trim().isEmpty()) continue;
		           // used to ignore non-numeric rows, as in the CSV there is below the salary then over the salary range
		            //if this is not added, error would occur
		            if (line.toLowerCase().contains("below") || line.toLowerCase().contains("over")) {
		                continue;
		            }
		            line = line.replace(",", "");
		            //extract numeric values from line
		            String cleaned = line.replaceAll("[^0-9. ]", " ").trim();
		            String[] numbers = cleaned.split("\\s+");

		            if (numbers.length < 3) continue;

		            double minSalary = Double.parseDouble(numbers[0]);
		            double maxSalary = Double.parseDouble(numbers[1]);
		            double contribution = Double.parseDouble(numbers[2]);
		            //return contribution if employee salary falls within range
		            if (basicSalary >= minSalary && basicSalary <= maxSalary) {
		                return contribution;
		            }
		            
		        }
		       
		 } catch (Exception e) {
		     System.out.println("Error reading SSS Contribution file.");
		    //e.printStackTrace(); // used for debugging to find where the issue is at
		 	}
		 	
		    return 0;
		}
	//compute PhilHealth contribution based on employee's salary
	private static double computePhilHealth(double basicSalary) {
		// PhilHealth contribution thresholds for 2024
		double salRange1 = 10000;
		double salRange2 = 59999.99;
		double salRange3 = 60000;
		
		double premiumRate = 0.03; 
		double monthPremiumRate = 0.5;
		double minContribution = 300;
		double maxContribution = 1800;
		
		if (basicSalary < salRange1) {
			return minContribution;
		}
		else if (basicSalary > salRange1  && basicSalary < salRange2) {
			
			double contribution = basicSalary * premiumRate;
			
			if (contribution > maxContribution ) {
				contribution = maxContribution;
		}
			return contribution;
		}
		else if (basicSalary > salRange3) {
			return maxContribution;
		}
		
		double premium = basicSalary * premiumRate;
		double employeeShare = premium / monthPremiumRate;
		
		return employeeShare;
	}
	//compute PagIbig contribution based on employee's salary
	// returns fixed maximum contribution based on policy
	private static double computePagibig(double basicSalary) {
		
		if (basicSalary <=1000) {
			basicSalary = 1000;
		}
		if (basicSalary >1500) {
			basicSalary = 1500;
		}
		//double employeeContribution = basicSalary * 0.01;
		double employeeMaxContribution = 100;
		
		return employeeMaxContribution;
	}
	//compute withholding based on the taxable income
	private static double computeTax(double taxableIncome) {
		//Tax bracket threshold for 2024
		//computes total working hours based on company rules:
		final int TAX_BRACKET_1 = 20832;
		final int TAX_BRACKET_2 = 33333;
		final int TAX_BRACKET_3 = 66667;
		final int TAX_BRACKET_4 = 166667;
		final int TAX_BRACKET_5 = 666667;
		final int TAX_BRACKET_6 = 666667;
		
		// the condition for the tax deductions based on salary
		if (taxableIncome <= TAX_BRACKET_1) {
			return 0;
		}
		else if (taxableIncome < TAX_BRACKET_2) {
			return (taxableIncome - TAX_BRACKET_1) * 0.20;
		}
		else if (taxableIncome < TAX_BRACKET_3) {
			return 2500 + (taxableIncome - TAX_BRACKET_2) * 0.25;
		}
		else if (taxableIncome < TAX_BRACKET_4) {
			return 10833 + (taxableIncome - TAX_BRACKET_3) * 0.30;
		}
		else if (taxableIncome < TAX_BRACKET_5) {
			return 40833.33 + (taxableIncome - TAX_BRACKET_4) * 0.32;
		}
		else {
			return 200833.33 + (taxableIncome - TAX_BRACKET_6) * 0.35;
		}
		
	}
	// Compute hours worked between login and logout times
	// - 1 hour lunch break is deducted
	private static double computeHours(LocalTime login, LocalTime logout) {
		
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime graceTime = LocalTime.of(8, 10);
		LocalTime cutoffTime = LocalTime.of(17, 0);
		//Adjust login time if the employee started earlier than 8
		if(login.isBefore(startTime)) {
			login = startTime;
		}
		//this limits the log out time to official cutoff
		if (logout.isAfter(cutoffTime)) {
			logout = cutoffTime;
		}
		
		long minutesWorked = Duration.between(login, logout).toMinutes();
		// 1 hour Lunch break deduction
		if (minutesWorked > 60) {
			minutesWorked -= 60; 
		}
		
		double hours = minutesWorked / 60.0;
		//return full 8 hours if employee is on time
		if (!login.isAfter(graceTime) && logout.equals(cutoffTime)) {
			return 8.0; // Full 8 hours if on time
			}
		return Math.min(hours, 8.0);
		
	}
	// reads all employees from the employee file and processes their payroll
	private static void processAllEmployees(int month, Map<String, List<String[]>> attendanceMap,
			Map<String, String[]> employeeMap) {
		// loop through all employees using the employeeMap
		// this avoids reopening the employee file again 
		for (String employeeNumber : employeeMap.keySet()) {
		    processEmployeeData(employeeNumber, month, attendanceMap, employeeMap);
		}
	}
	// this is the main method for payroll computation
	// this uses employeeMap and attendanceMap to avoid repeated file reading (as a concern in the previous program because teh file is read many times0
	// calculates hours worked, salary, and deductions
	private static void processEmployeeData(String employeeNumber, int month, Map<String, List<String[]>> attendanceMap,
			Map<String, String[]> employeeMap) {
		//employee personal details
		// get employee details directly from map instead of reading file again
	    String firstName = "";
	    String lastName = "";
	    String birthday = "";
	    //salary information
	    double basicSalary = 0;
	    double hourlyRate = 0;
	    //to check if the employee exists in the file
	    boolean found = false;
	    //total hours for each cutoff
	    double firstCutOff = 0;
	    double secondCutOff = 0;
	   
	 
	    String[] data = employeeMap.get(employeeNumber);

	    if (data == null) {
	    	    System.out.println("Employee number does not exist.");
	    	    return;
	    }

	    found = true;

	    lastName = data[1];
	    firstName = data[2];
	    birthday = data[3];
	    
	    // for parsing salary: remove commas and quotes before parsing to avoid number format error
	    try {
	    	    basicSalary = Double.parseDouble(data[13].replace(",", "").replace("\"", "").trim());
	    } catch (NumberFormatException e) {
	    	    basicSalary = 0;
	    }

	    try {
	    	    hourlyRate = Double.parseDouble(data[18].replace(",", "").replace("\"", "").trim());
	    } catch (NumberFormatException e) {
	    	    hourlyRate = 0;
	    }
	    
	   // convert month number to month name or else it will only show 6 instead of June, etc
	    Month monthObj = Month.of(month);
	    String monthName = monthObj.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    // format used for login/logout time
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
		// get number of days in the selected month, helpful for the second cutoff since not all months end at day 30
		int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();
		
		List<String[]> records = attendanceMap.get(employeeNumber);

		// loop through attendance records of the employee
		if (records != null) {
			// used 'record' here because this contains attendance data which is used in this block
		    for (String[] record : records) {

		        String[] dateParts = record[3].split("/");

		        int recordMonth = Integer.parseInt(dateParts[0]);
		        int day = Integer.parseInt(dateParts[1]);
		        int year = Integer.parseInt(dateParts[2]);
		        // only include records that match selected month and year
		        if (year != 2024 || recordMonth != month) continue;

		        LocalTime login = LocalTime.parse(record[4].trim(), timeFormat);
		        LocalTime logout = LocalTime.parse(record[5].trim(), timeFormat);

		        double hours = computeHours(login, logout);
		        // add hours to correct cutoff period (1–15 or 16–end of month)
		        if (day <= 15) firstCutOff += hours;
		        else secondCutOff += hours;
		    }
		}
		// if employee was not found
		if(!found){
	        System.out.println("Employee number does not exist.");
	        return;
		}
		// compute salary values in cutoffs and monthly gross
		double firstGrossSalary = firstCutOff * hourlyRate;
		double secondGrossSalary = secondCutOff * hourlyRate;
		double monthlyGrossSalary = firstGrossSalary + secondGrossSalary;
		// the deductions
		double sss = computeSSS(basicSalary);
		double philHealth = computePhilHealth(basicSalary);
		double pagibig  = computePagibig(basicSalary);
		
		double taxableIncome = monthlyGrossSalary - (sss + philHealth + pagibig);
		double tax = computeTax(taxableIncome);
		
		double totalDeductions = sss + philHealth + pagibig + tax;
		
		double firstNetSalary = firstGrossSalary; // first cutoff has no deductions yet
		double secondNetSalary = secondGrossSalary - totalDeductions; // second cutoff includes all deductions
		
		//first cut off display
		System.out.println("\n================================================");
		System.out.println("Employee Number: " + employeeNumber);
		System.out.println("Employee Name: " + lastName+ ", " + firstName);
		System.out.println("Birthday: " + birthday);
		System.out.println("Cutoff Date: " + monthName + " 1 to " + monthName + " 15");
		System.out.println("Total Hours Worked: " + firstCutOff);
	    System.out.println("Gross Salary: " + firstGrossSalary);
	    System.out.println("Net Salary: " + firstNetSalary);
		//second cut off display
	    System.out.println("\nCutoff Date: " + monthName + " 16 to " + monthName + " "+ daysInMonth);
	    System.out.println("Total Hours Worked : " + secondCutOff);
	    System.out.println("Gross Salary: " + secondGrossSalary);
	    System.out.println("Deductions: ");
	    System.out.println("  SSS: " + sss);
	    System.out.println("  PhilHealth: " + philHealth);
	    System.out.println("  Pag-IBIG: " + pagibig);
	    System.out.println("  Tax: " + tax);
	    System.out.println("Net Salary: " + secondNetSalary);
	    System.out.println("================================================");
	}
	// newly added method, this loads attendance records into a Map grouped by employee number
	// the goal is to avoid reading the attendance file multiple times for each employee, as it is the Bottleneck in the previous program
	// with this, it improves performance especially when processing all employees
	private static Map<String, List<String[]>> loadAttendance() {

	    Map<String, List<String[]>> attendanceMap = new HashMap<>();

	    try (BufferedReader reader = new BufferedReader(
	            new FileReader("resources/MotorPH_Employee Data - Attendance Record.csv"))) {

	        String line;
	        reader.readLine();

	        while ((line = reader.readLine()) != null) {

	            if (line.trim().isEmpty()) continue;

	            String[] data = line.split(",");

	            String empId = data[0];
	            // this creates list if employee that does not exist yet, then add record
	            attendanceMap.putIfAbsent(empId, new ArrayList<>());
	            attendanceMap.get(empId).add(data);
	        }

	    } catch (Exception e) {
	        System.out.println("Error loading attendance.");
	    }

	    return attendanceMap;
	}
	//using the method below to load employee details into a Map using employee number as key
	// this allows direct access to employee data without scanning the file again
	private static Map<String, String[]> loadEmployees() {

	    Map<String, String[]> employeeMap = new HashMap<>();

	    try (BufferedReader reader = new BufferedReader(
	            new FileReader("resources/MotorPH_Employee Data - Employee Details.csv"))) {

	        String line;
	        reader.readLine(); // skip header

	        while ((line = reader.readLine()) != null) {

	            if (line.trim().isEmpty()) continue;

	            String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); //  used to correctly split CSV values even if there are commas inside quotes

	            String employeeNumber = data[0];

	            employeeMap.put(employeeNumber, data);
	        }

	    } catch (Exception e) {
	        System.out.println("Error loading employee file.");
	    }

	    return employeeMap;
	}
}


