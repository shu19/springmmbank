package com.cg.app.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cg.app.account.SavingsAccount;
import com.cg.app.account.dao.mapper.SavingsAccountMapper;
import com.cg.app.account.util.DBUtil;
import com.cg.app.exception.AccountNotFoundException;

@Primary
@Repository
public class SavingsAccountSJDAOImpl implements SavingsAccountDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO ACCOUNT (account_hn,account_bal,salary,type) VALUES(?,?,?,?)",
				new Object[] { account.getBankAccount().getAccountHolderName(),
						account.getBankAccount().getAccountBalance(), account.isSalary(), "SA" });
		return account;
	}

	@Override
	public SavingsAccount getAccountById(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return jdbcTemplate.queryForObject("SELECT * FROM account WHERE account_id=?", new Object[] { accountNumber },
				new SavingsAccountMapper());
	}

	@Override
	public boolean deleteAccount(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return jdbcTemplate.update("DELETE FROM ACCOUNT WHERE TYPE='SA' AND account_id=?", accountNumber) > 0;
	}

	@Override
	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM ACCOUNT";
		return jdbcTemplate.query(query, new SavingsAccountMapper());
	}

	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("UPDATE ACCOUNT SET account_bal=? where account_id=?",
				new Object[] { currentBalance, accountNumber });
	}

	@Override
	public List<SavingsAccount> getSortedAccounts(int choice) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM ACCOUNT WHERE TYPE='SA'";

		switch (choice) {
		case 1:
			query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_id";
			break;
		case 2:
			query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_id DESC";
			break;
		case 3:
			query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_hn";
			break;
		case 4:
			query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_hn DESC";
			break;
		case 5:
			query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_bal";
			break;
		case 6:
			query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_bal DESC";
			break;

		default:

			break;
		}

		return jdbcTemplate.query(query, new SavingsAccountMapper());
	}

	@Override
	public double getAccountBalance(int accountnumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {
		String query = "SELECT account_bal FROM ACCOUNT WHERE account_id=?";

		return jdbcTemplate.queryForObject(query, new Object[] { accountnumber }, Double.class);
	}

	@Override
	public SavingsAccount getAccountByHolderName(String accountHolderName)
			throws AccountNotFoundException, ClassNotFoundException, SQLException {
		String query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' AND account_hn LIKE ?";

		return jdbcTemplate.queryForObject(query, new Object[] { "%" + accountHolderName + "%" },
				new SavingsAccountMapper());
	}

	@Override
	public List<SavingsAccount> getAllSavingsAccountInBalanceRange(double minimumAccountBalance,
			double maximumAccountBalance) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		String query = "SELECT * FROM ACCOUNT WHERE TYPE='SA' AND account_bal >= ? AND account_bal <= ?";
		return jdbcTemplate.query(query, new Object[] { minimumAccountBalance, maximumAccountBalance },
				new SavingsAccountMapper());

	}

	@Override
	public SavingsAccount updateAccount(SavingsAccount savingsAccount) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("UPDATE ACCOUNT SET account_hn=?,salary=?  where TYPE='SA' AND account_id=?",
				new Object[] { savingsAccount.getBankAccount().getAccountHolderName(), savingsAccount.isSalary(),
						savingsAccount.getBankAccount().getAccountNumber() });
		return savingsAccount;
	}

	@Override
	public int updateAccount(int accountnumber, String newAccountHolderName)
			throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
