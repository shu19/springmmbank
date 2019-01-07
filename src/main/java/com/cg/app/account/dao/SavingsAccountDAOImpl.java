package com.cg.app.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cg.app.account.SavingsAccount;
import com.cg.app.account.util.DBUtil;
import com.cg.app.exception.AccountNotFoundException;

@Repository
public class SavingsAccountDAOImpl implements SavingsAccountDAO {

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT (account_hn,account_bal,salary,type) VALUES(?,?,?,?)");
		preparedStatement.setString(1, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, account.getBankAccount().getAccountBalance());
		preparedStatement.setBoolean(3, account.isSalary());
		preparedStatement.setString(4, "SA");
		preparedStatement.executeUpdate();
		preparedStatement.close();
		DBUtil.commit();
		return account;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT WHERE TYPE='SA'");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
		DBUtil.commit();
		return savingsAccounts;
	}
	
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT SET account_bal=? where account_id=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account WHERE account_id=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	
	
	@Override
	public boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection=DBUtil.getConnection();
		String query="DELETE FROM ACCOUNT WHERE TYPE='SA' AND account_id=?";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setInt(1, accountNumber);
		
		boolean result=preparedStatement.execute();
		DBUtil.commit();
		return result;
		/*System.out.println("resultSet : "+resultSet);
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");*/
	}	

	@Override
	public List<SavingsAccount> getSortedAccounts(int choice) throws ClassNotFoundException, SQLException {
		
		List<SavingsAccount> savingsAccounts = new ArrayList<SavingsAccount>();
		String query="SELECT * FROM ACCOUNT WHERE TYPE='SA'";

		switch(choice){
		case 1:
			query="SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_id";
			break;
		case 2:
			query="SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_id DESC";
			break;
		case 3:
			query="SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_hn";
			break;			
		case 4:		
			query="SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_hn DESC";
			break;
		case 5:
			query="SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_bal";
			break;
		case 6:
			query="SELECT * FROM ACCOUNT WHERE TYPE='SA' ORDER BY account_bal DESC";
			break;
			
		default:
			
			break;
		}
		
		
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();		
		
		ResultSet resultSet = statement.executeQuery(query);
		while (resultSet.next()) {// Check if row(s) is present in table
			
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
//		DBUtil.commit();
		return savingsAccounts;
		
	}

	@Override
	public int updateAccount(int accountnumber, String newAccountHolderName) throws ClassNotFoundException, SQLException {
		Connection connection=DBUtil.getConnection();
		String query="UPDATE ACCOUNT SET account_hn=? where TYPE='SA' AND account_id=?";		
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setString(1, newAccountHolderName);
		preparedStatement.setInt(2, accountnumber);
		int result = preparedStatement.executeUpdate();
		DBUtil.commit();
		return result;
	}

	@Override
	public double getAccountBalance(int accountnumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection=DBUtil.getConnection();
		String query="SELECT account_bal FROM ACCOUNT WHERE account_id=?";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setInt(1, accountnumber);
		ResultSet resultSet=preparedStatement.executeQuery();
		if(resultSet.next()){
			return resultSet.getDouble("account_bal");
		}
		throw new AccountNotFoundException("Account with account number "+accountnumber+ " not found.");
	}

	@Override
	public SavingsAccount getAccountByHolderName(String accountHolderName) throws AccountNotFoundException, ClassNotFoundException, SQLException {
		Connection connection=DBUtil.getConnection();
		String query="SELECT * FROM ACCOUNT WHERE TYPE='SA' AND account_hn LIKE ?";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setString(1, "%"+accountHolderName+"%");
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			int accountNumber=resultSet.getInt("account_id");
			String accountHN=resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHN, accountBalance,
					salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account holder name "+accountHolderName+" does not exist.");		
		
	}

	@Override
	public List<SavingsAccount> getAllSavingsAccountInBalanceRange(
			double minimumAccountBalance, double maximumAccountBalance) throws ClassNotFoundException, SQLException,AccountNotFoundException {

		Connection connection=DBUtil.getConnection();
		String query="SELECT * FROM ACCOUNT WHERE TYPE='SA' AND account_bal >= ? AND account_bal <= ?";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setDouble(1, minimumAccountBalance);
		preparedStatement.setDouble(2, maximumAccountBalance);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		List<SavingsAccount> listSavingsAccounts=new ArrayList<SavingsAccount>();
		while(resultSet.next()) {
			int accountNumber=resultSet.getInt("account_id");
			String accountHolderName=resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			listSavingsAccounts.add(savingsAccount);
		}
		return listSavingsAccounts;
	}

	@Override
	public SavingsAccount updateAccount(SavingsAccount savingsAccount) throws ClassNotFoundException, SQLException {
		Connection connection=DBUtil.getConnection();
		String query="UPDATE ACCOUNT SET account_hn=?,salary=?  where TYPE='SA' AND account_id=?";		
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setString(1, savingsAccount.getBankAccount().getAccountHolderName());
		preparedStatement.setBoolean(2, savingsAccount.isSalary());
		preparedStatement.setInt(3, savingsAccount.getBankAccount().getAccountNumber());
		int result = preparedStatement.executeUpdate();
		DBUtil.commit();		
		return savingsAccount;
	}
}
