package Main;
import java.util.Locale;
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
		
		//once the user checked in with their role, they will be routed to the appropriate menu
		if(username.equals("employee") && password.equals("12345")){
			 employeeLogin(scanner);
		}
		else if(username.equals("payroll_staff") && password.equals("12345")) {
			payroll_StaffLogin(scanner);
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
		
		scanner.close();
		
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
	private static void payroll_StaffLogin(Scanner scanner) {
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
             processPayroll(scanner);
         }
         else if (choice == 2) {
             System.out.println("Program terminated.");
         }
         else {
             System.out.println("Invalid choice.");
         }
		} while (choice != 2);
		
		scanner.close();
		
	}
	// this method allows the user (payroll_staff) to view one employee or all employees as they select the option
	private static void processPayroll(Scanner scanner) {
		System.out.println();
		System.out.println("Select option: ");
		System.out.println("1. One employee");
		System.out.println("2. All employees");
		System.out.println("2. Exit the program");
		System.out.println("Enter the number of your choice: ");
		
		int choice = scanner.nextInt();
		scanner.nextLine();
		
		if (choice == 1) {
         processOneEmployee(scanner); //to process/view payroll for only one employee
     }
		else if(choice == 2) {
			System.out.print("Enter month (1-12): ");
		    int month = scanner.nextInt();
		    scanner.nextLine();

		    processAllEmployees(month); //to process/view payroll for all employees
		}
     else if (choice == 3) {
         System.out.println("Program terminated.");
		
	}
	}
	//method used to process for one employee, only short because their is another method called processemployeeData connected here
	//also, this area is the key for processing all employee details
	private static void processOneEmployee(Scanner scanner) {
		
		System.out.print("Enter employee number: ");
	    String employeeNumber = scanner.nextLine();

	    System.out.print("Enter month (1-12): ");
	    int month = scanner.nextInt();
	    scanner.nextLine();

	    processEmployeeData(employeeNumber, month);
	}
	//compute sss contribution based on salary range from the SSS CSV file
	private static double computeSSS(double basicSalary) {
		//maximum SSS salary cap
		if (basicSalary > 24750) {
	        basicSalary = 24750;
	    }
		
		try {
			//reads the SSS contribution details based on salary range
		        BufferedReader reader = new BufferedReader(new FileReader("resources/SSS Contribution.csv"));
		        
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
	private static double computephilHealth(double basicSalary) {
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
		else if (basicSalary > salRange1 && basicSalary < salRange2) {
			
			double contribution = basicSalary * premiumRate;
			
			if (contribution > maxContribution ) {
				contribution = maxContribution;
		}
			return contribution;
		}
		else if (basicSalary > salRange3 ) {
			return maxContribution;
		}
		
		double premium = basicSalary * premiumRate;
		double employeeShare = premium / monthPremiumRate;
		
		return employeeShare;
	}
	//compute PagIbig contribution based on employee's salary
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
		
		int salRange1 = 20832;
		int salRange2 = 33333;
		int salRange3 = 66667;
		int salRange4 = 166667;
		int salRange5 = 666667;
		int salRange6 = 666667;
		
		// the condition for the tax deductions based on salary
		if (taxableIncome <= salRange1) {
			return 0;
		}
		else if (taxableIncome < salRange2) {
			return (taxableIncome - salRange1) * 0.20;
		}
		else if (taxableIncome < salRange3) {
			return 2500 + (taxableIncome - salRange2) * 0.25;
		}
		else if (taxableIncome < salRange4) {
			return 10833 + (taxableIncome - salRange3) * 0.30;
		}
		else if (taxableIncome < salRange5) {
			return 40833.33 + (taxableIncome - salRange4) * 0.32;
		}
		else {
			return 200833.33 + (taxableIncome - salRange6) * 0.35;
		}
		
	}
	// Compute hours worked between login and logout times
	private static double computeHours(LocalTime login, LocalTime logout) {
		
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime graceTime = LocalTime.of(8, 10);
		LocalTime cutoffTime = LocalTime.of(17, 0);
		//Adjust login time if the employee started earlier than 8
		if(login.isAfter(startTime)) {
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
	private static void processAllEmployees(int month) {

		try (BufferedReader reader =
		         new BufferedReader(new FileReader("resources/MotorPH_Employee Data - Employee Details.csv"))) {

		        String line;

		        reader.readLine();

		        while ((line = reader.readLine()) != null) {
		        	//skip the empty lines
		            if (line.trim().isEmpty()) continue;
		         // split the CSV line into data
		            String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

		            String employeeNumber = data[0];
		          //process payroll for the employee
		            processEmployeeData(employeeNumber, month);

		        }

		    } catch (Exception e) {
		        System.out.println("Error reading employee file.");
		        e.printStackTrace();
		    }
	}
	//processes payroll for all employees by reading through the employee details CSV
	private static void processEmployeeData(String employeeNumber, int month) {
		//employee personal details
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
	   
	    
	    try {
	    	 // read employee details file
			BufferedReader reader = new BufferedReader(new FileReader("resources/MotorPH_Employee Data - Employee Details.csv"));
			
			String line;
			
			reader.readLine();
			
			while ((line = reader.readLine()) !=null) {
				//to slip empty lines in CSV
				if (line.trim().isEmpty()) continue;
				//split the CSV line into data
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				
				if(!data[0].equals(employeeNumber)) continue;
				
				found = true;
				//to get the employee's basic information
				employeeNumber = data[0];
				lastName = data[1];
				firstName = data[2];
				birthday = data[3];
				//to get the basic salary
				try {
				    basicSalary = Double.parseDouble(data[13].replace(",", "").replace("\"", "").trim());
				} catch (NumberFormatException e) {
				    basicSalary = 0;
				}
				// to get the hourly rate
				try {
				    hourlyRate = Double.parseDouble(data[18].replace(",", "").replace("\"", "").trim());
				} catch (NumberFormatException e) {
				    hourlyRate = 0;
				}
				
				break;
			 }
		    reader.close();
		  
		} catch (Exception e) {
		    System.out.println("Error reading employee file.");
		}
	   // convert month number to month name or else it will only show 6 instead of June, etc
	    Month monthObj = Month.of(month);
	    String monthName = monthObj.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    // format used for login/logout time
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
		// get number of days in the selected month, helpful for the second cutoff since not all months end at day 30
		int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();
		
		
		try {
			//to read attendance file
			BufferedReader reader = new BufferedReader(new FileReader("resources/MotorPH_Employee Data - Attendance Record.csv"));
			
			String line2;
			
			reader.readLine();
			
			while ((line2 = reader.readLine()) !=null) {
				
				if (line2.trim().isEmpty()) continue;
				String [] data = line2.split(",");
				// check if record belongs to the employee
				if(!data[0].equals(employeeNumber)) continue;
				// split date from attendance record
				String[] dateParts = data[3].split("/");
				
				int recordMonth = Integer.parseInt(dateParts[0]);
				int day = Integer.parseInt(dateParts[1]);
				int year = Integer.parseInt(dateParts[2]);		
				// only use records from the selected month
				if(year !=2024 || recordMonth != month) continue;
				// get login and logout time
				LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
				LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
				double hours = computeHours(login, logout);
				// add hours to correct cutoff
				if(day <=15) {
					firstCutOff += hours;
				}
				else {secondCutOff += hours;
				}
			}
	        reader.close();
		}
		// helpful to check on which part is having an issue
		catch(Exception e) {
		System.out.println("Error reading file.");
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
		double philHealth = computephilHealth(basicSalary);
		double pagibig  = computePagibig(basicSalary);
		
		double taxableIncome = monthlyGrossSalary - (sss + philHealth + pagibig);
		double tax = computeTax(taxableIncome);
		
		double totalDeductions = sss + philHealth + pagibig + tax;
		
		double firstNetSalary = firstGrossSalary;
		double secondNetSalary = secondGrossSalary - totalDeductions;
		
		//first cut off display
		System.out.println("\n================================================");
		System.out.println("Employee Number: " + employeeNumber);
		System.out.println("Employee Name: " + lastName+ ", " + firstName);
		System.out.println("Birthday: " + birthday);
		System.out.println("Cutoff Date: " + monthName + " 1 to " + monthName + " 15");
		System.out.println("Total Hours Worked: " + firstCutOff);
	    System.out.println("Gross Salary: " + firstGrossSalary);
	    System.out.println("Net Salary:: " + firstNetSalary);
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
	
}


