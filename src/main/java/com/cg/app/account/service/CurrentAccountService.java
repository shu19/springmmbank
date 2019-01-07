package com.cg.app.account.service;

import java.sql.SQLException;
import java.util.List;

import com.cg.app.account.CurrentAccount;
import com.cg.app.exception.AccountNotFoundException;

public interface CurrentAccountService {

	CurrentAccount createNewAccount(String accountHolderName, double accountBalance, double odLimit) throws ClassNotFoundException, SQLException;

	CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
	List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException;

	void fundTransfer(CurrentAccount sender, CurrentAccount receiver, double amount) throws ClassNotFoundException, SQLException;
	void deposit(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException;
	void withdraw(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException;

	
	List<CurrentAccount> getSortedAccounts(int sortBy) throws ClassNotFoundException, SQLException;

	int updateAccount(int accountnumber, String newAccountHolderName) throws ClassNotFoundException, SQLException;

	double checkAccountBalance(int accountnumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	CurrentAccount getAccountByHolderName(String accountHolderName) throws ClassNotFoundException, AccountNotFoundException, SQLException;

	List<CurrentAccount> getAllCurrentAccountInBalanceRange(
			double minimumAccountBalance, double maximumAccountBalance) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
}











