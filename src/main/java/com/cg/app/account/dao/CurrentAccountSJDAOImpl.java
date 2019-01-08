package com.cg.app.account.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cg.app.account.CurrentAccount;
import com.cg.app.exception.AccountNotFoundException;

@Primary
@Repository
public class CurrentAccountSJDAOImpl implements CurrentAccountDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public CurrentAccount createNewAccount(CurrentAccount account) throws ClassNotFoundException, SQLException {
		String query="INSERT INTO ACCOUNT (account_hn,account_bal,odLimit,type) VALUES(?,?,?,?)";
		jdbcTemplate.update(query,new Object[] {
				account.getBankAccount().getAccountHolderName(),
				account.getBankAccount().getAccountBalance(),
				account.getOdLimit(),
				"CA"
		});
		return null;
	}

	@Override
	public CurrentAccount getAccountById(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAccount(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CurrentAccount> getSortedAccounts(int choice) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateAccount(int accountnumber, String newAccountHolderName)
			throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAccountBalance(int accountnumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CurrentAccount getAccountByHolderName(String accountHolderName)
			throws AccountNotFoundException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccountInBalanceRange(double minimumAccountBalance,
			double maximumAccountBalance) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
