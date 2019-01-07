package com.cg.app.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.app.account.SavingsAccount;
import com.cg.app.account.service.SavingsAccountService;
import com.cg.app.account.service.SavingsAccountServiceImpl;
import com.cg.app.account.util.DBUtil;
import com.cg.app.exception.AccountNotFoundException;

@Component
public class AccountCUI {
	private Scanner scanner = new Scanner(System.in);
	@Autowired
	private SavingsAccountService savingsAccountService;

	public void start() {

		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Savings Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Savings Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println();
			System.out.println("Make your choice: ");

			int choice = scanner.nextInt();

			performOperation(choice);

		} while (true);
	}

	private void performOperation(int choice) {
		switch (choice) {
		case 1:
			acceptInput("SA");
			break;
		case 2:
			updateAccount();
			break;
		case 3:
			closeAcount();
			break;
		case 4:
			searchMenu();
			break;
		case 9:
			showAllAccounts();
			break;
		case 5:
			withdraw();
			break;
		case 6:
			deposit();
			break;
		case 7:
			fundTransfer();
			break;
		case 8:
			checkAccountBalance();
			break;
		case 10:
			sortMenu();
			break;
		case 11:
			try {
				DBUtil.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		default:
			System.err.println("Invalid Choice!");
			break;
		}

	}

	private void searchMenu() {
		do {
			System.out.println("* * * * * Ways of searching * * * * * ");
			System.out.println("1. Account Number");
			System.out.println("2. Account Holder Name");
			System.out.println("3. Account Balance Range");
			System.out.println("4. Exit from searching");

			int choice = scanner.nextInt();
			if (choice != 4) {
				searchAccount(choice);
			} else {
				break;
			}
		} while (true);
	}

	private void searchAccount(int choice) {

		switch (choice) {
		case 1:
			System.out.println("Enter Account Number");
			int accountNumber = scanner.nextInt();
			try {
				SavingsAccount savingsAccount = savingsAccountService
						.getAccountById(accountNumber);
				System.out.println(savingsAccount);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;

		case 2:
			System.out.println("Enter Account Holder Name");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			try {
				SavingsAccount savingsAccount = savingsAccountService
						.getAccountByHolderName(accountHolderName);
				System.out.println(savingsAccount);
			} catch (ClassNotFoundException | AccountNotFoundException
					| SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;

		case 3:
			System.out.println("Enter Account Balance Range");
			System.out.println("Enter Minimum Account Balance");
			double minimumAccountBalance = scanner.nextDouble();
			System.out.println("Enter Maximum Account Balance");
			double maximumAccountBalance = scanner.nextDouble();

			List<SavingsAccount> savingsAccountsList = null;
			try {
				savingsAccountsList = savingsAccountService
						.getAllSavingsAccountInBalanceRange(
								minimumAccountBalance, maximumAccountBalance);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			for (SavingsAccount savingsAccount : savingsAccountsList) {
				System.out.println(savingsAccount);
			}

			break;
		default:
			break;
		}
	}

	private void checkAccountBalance() {

		System.out.println("Enter Account Number");
		int accountnumber = scanner.nextInt();

		try {
			double balance = savingsAccountService
					.checkAccountBalance(accountnumber);
			System.out.println("Your Account balance is " + balance);

		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateAccount() {

		System.out.println("Enter Account Number");
		int accountnumber = scanner.nextInt();

		System.out.println("Enter new Account Holder Name");
		String newAccountHolderName = scanner.nextLine();
		newAccountHolderName = scanner.nextLine();
		try {
			int result = savingsAccountService.updateAccount(accountnumber,
					newAccountHolderName);
			if (result != 0) {
				System.out.println("Your Account is updated successfully!");
			} else {
				System.out.println("Account number not found");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeAcount() {

		System.out.println("Enter Account Number");
		int accountnumber = scanner.nextInt();
		try {
			boolean savingsAccount = savingsAccountService
					.deleteAccount(accountnumber);
			System.out.println(savingsAccount);
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void fundTransfer() {
		System.out.println("Enter Account Sender's Number: ");
		int senderAccountNumber = scanner.nextInt();
		System.out.println("Enter Account Receiver's Number: ");
		int receiverAccountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		try {
			SavingsAccount senderSavingsAccount = savingsAccountService
					.getAccountById(senderAccountNumber);
			SavingsAccount receiverSavingsAccount = savingsAccountService
					.getAccountById(receiverAccountNumber);
			savingsAccountService.fundTransfer(senderSavingsAccount,
					receiverSavingsAccount, amount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deposit() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.deposit(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void withdraw() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.withdraw(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void sortMenu() {
		do {
			System.out.println("+++++Ways of Sorting+++++++");
			System.out.println("1. Sort by account number in ascending order");
			System.out.println("2. Sort by account number in descending order");
			System.out.println("3. Sort by account holder name in ascending order");
			System.out.println("4. Sort by account holder name in descending order");
			System.out.println("5. Sort by account balance in ascending order");
			System.out.println("6. Sort by account balance in descending order");
			System.out.println("7. Exit from Sorting");

			
			int choice = scanner.nextInt();
			if (choice != 7) {
				showSortedAccount(choice);
			} else {
				break;
			}

		} while (true);

	}

	private void showSortedAccount(int choice) {
		List<SavingsAccount> sortedAccounts;
		try {
			sortedAccounts = savingsAccountService.getSortedAccounts(choice);
			for (SavingsAccount savingsAccount : sortedAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showAllAccounts() {
		List<SavingsAccount> savingsAccounts;
		try {
			savingsAccounts = savingsAccountService.getAllSavingsAccount();
			for (SavingsAccount savingsAccount : savingsAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	private void acceptInput(String type) {
		if (type.equalsIgnoreCase("SA")) {
			System.out.println("Enter your Full Name: ");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			System.out
					.println("Enter Initial Balance(type na for Zero Balance): ");
			String accountBalanceStr = scanner.next();
			double accountBalance = 0.0;
			if (!accountBalanceStr.equalsIgnoreCase("na")) {
				accountBalance = Double.parseDouble(accountBalanceStr);
			}
			System.out.println("Salaried?(y/n): ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			createSavingsAccount(accountHolderName, accountBalance, salary);
		}
	}

	private void createSavingsAccount(String accountHolderName,
			double accountBalance, boolean salary) {
		try {
			savingsAccountService.createNewAccount(accountHolderName,
					accountBalance, salary);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}