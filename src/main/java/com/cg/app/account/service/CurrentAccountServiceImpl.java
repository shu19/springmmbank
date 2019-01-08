package com.cg.app.account.service;

import java.sql.SQLException;
import java.util.List;

import com.cg.app.account.CurrentAccount;
import com.cg.app.account.dao.CurrentAccountDAO;
import com.cg.app.account.dao.CurrentAccountDAOImpl;
import com.cg.app.account.factory.AccountFactory;
import com.cg.app.account.util.DBUtil;
import com.cg.app.exception.AccountNotFoundException;
import com.cg.app.exception.InsufficientFundsException;
import com.cg.app.exception.InvalidInputException;

public class CurrentAccountServiceImpl implements CurrentAccountService {

	private AccountFactory factory;
	private CurrentAccountDAO currentAccountDAO;

	public CurrentAccountServiceImpl() {
		factory = AccountFactory.getInstance();
		currentAccountDAO = new CurrentAccountDAOImpl();
	}

	@Override
	public CurrentAccount createNewAccount(String accountHolderName, double accountBalance, double odLimit)
			throws ClassNotFoundException, SQLException {
		CurrentAccount account = factory.createNewCurrentAccount(accountHolderName, accountBalance, odLimit);
		currentAccountDAO.createNewAccount(account);
		return null;
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		return currentAccountDAO.getAllCurrentAccount();
	}

	@Override
	public void deposit(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
			currentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			// savingsAccountDAO.commit();
		} else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}

	@Override
	public void withdraw(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance() + account.getOdLimit();
		if (amount > 0 && currentBalance >= amount) {
			currentBalance -= amount;
			currentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			// savingsAccountDAO.commit();
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}

	@Override
	public void fundTransfer(CurrentAccount sender, CurrentAccount receiver, double amount)
			throws ClassNotFoundException, SQLException {
		try {
			withdraw(sender, amount);
			deposit(receiver, amount);
			DBUtil.commit();
		} catch (InvalidInputException | InsufficientFundsException e) {
			e.printStackTrace();
			DBUtil.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			DBUtil.rollback();
		}
	}

	@Override
	public CurrentAccount getAccountById(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return currentAccountDAO.getAccountById(accountNumber);
	}

	@Override
	public boolean deleteAccount(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return currentAccountDAO.deleteAccount(accountNumber);
	}

	@Override
	public List<CurrentAccount> getSortedAccounts(int choice) throws ClassNotFoundException, SQLException {
		return currentAccountDAO.getSortedAccounts(choice);
	}

	@Override
	public int updateAccount(int accountnumber, String newAccountHolderName)
			throws ClassNotFoundException, SQLException {

		return currentAccountDAO.updateAccount(accountnumber, newAccountHolderName);
	}

	@Override
	public double checkAccountBalance(int accountnumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return currentAccountDAO.getAccountBalance(accountnumber);
	}

	@Override
	public CurrentAccount getAccountByHolderName(String accountHolderName)
			throws ClassNotFoundException, AccountNotFoundException, SQLException {

		return currentAccountDAO.getAccountByHolderName(accountHolderName);
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccountInBalanceRange(double minimumAccountBalance,
			double maximumAccountBalance) throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return currentAccountDAO.getAllCurrentAccountInBalanceRange(minimumAccountBalance, maximumAccountBalance);
	}

}
