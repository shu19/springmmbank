package com.cg.app.account.service;

import java.sql.SQLException;
import java.util.List;

import com.cg.app.account.SavingsAccount;
import com.cg.app.exception.AccountNotFoundException;

public interface SavingsAccountService {

	SavingsAccount createNewAccount(String accountHolderName, double accountBalance, boolean salary) throws ClassNotFoundException, SQLException;

//	SavingsAccount updateAccount(SavingsAccount account);

	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;

	void fundTransfer(SavingsAccount sender, SavingsAccount receiver, double amount) throws ClassNotFoundException, SQLException;
	void deposit(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException;
	void withdraw(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException;

	
	List<SavingsAccount> getSortedAccounts(int sortBy) throws ClassNotFoundException, SQLException;

	int updateAccount(int accountnumber, String newAccountHolderName) throws ClassNotFoundException, SQLException;
	SavingsAccount updateAccount(SavingsAccount savingsAccount) throws ClassNotFoundException, SQLException;

	double checkAccountBalance(int accountnumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount getAccountByHolderName(String accountHolderName) throws ClassNotFoundException, AccountNotFoundException, SQLException;

	List<SavingsAccount> getAllSavingsAccountInBalanceRange(
			double minimumAccountBalance, double maximumAccountBalance) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
}











