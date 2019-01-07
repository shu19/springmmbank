package com.cg.app.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.cg.app.account.SavingsAccount;
import com.cg.app.exception.AccountNotFoundException;

public interface SavingsAccountDAO {
	
	SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException;
//	SavingsAccount updateAccount(SavingsAccount account);
	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	List<SavingsAccount> getSortedAccounts(int choice) throws ClassNotFoundException, SQLException;
	int updateAccount(int accountnumber, String newAccountHolderName) throws ClassNotFoundException, SQLException;
	double getAccountBalance(int accountnumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	SavingsAccount getAccountByHolderName(String accountHolderName) throws AccountNotFoundException, ClassNotFoundException, SQLException;
	List<SavingsAccount> getAllSavingsAccountInBalanceRange(
			double minimumAccountBalance, double maximumAccountBalance) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	SavingsAccount updateAccount(SavingsAccount savingsAccount) throws ClassNotFoundException, SQLException;
	
	
}
