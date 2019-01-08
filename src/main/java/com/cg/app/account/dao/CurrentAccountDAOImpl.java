package com.cg.app.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cg.app.account.CurrentAccount;
import com.cg.app.account.util.DBUtil;
import com.cg.app.exception.AccountNotFoundException;

public class CurrentAccountDAOImpl implements CurrentAccountDAO {

	public CurrentAccount createNewAccount(CurrentAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT (account_hn,account_bal,odLimit,type) VALUES(?,?,?,?)");
		preparedStatement.setString(1, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, account.getBankAccount().getAccountBalance());
		preparedStatement.setDouble(3, account.getOdLimit());
		preparedStatement.setString(4, "CA");
		preparedStatement.executeUpdate();
		preparedStatement.close();
		DBUtil.commit();
		return account;
		
	}

	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		List<CurrentAccount> currentAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT WHERE TYPE='CA'");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			currentAccounts.add(currentAccount);
		}
		DBUtil.commit();
		return currentAccounts;
	}
	
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT SET account_bal=? where account_id=? AND TYPE='CA'");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_id=? AND TYPE='CA'");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount currentAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			return currentAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	
	
	@Override
	public boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection=DBUtil.getConnection();
		String query="DELETE FROM ACCOUNT WHERE account_id=?";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setInt(1, accountNumber);
		
		boolean result=preparedStatement.execute();
		DBUtil.commit();
		return result;
		/*System.out.println("resultSet : "+resultSet);
		CurrentAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");*/
	}	

	@Override
	public List<CurrentAccount> getSortedAccounts(int choice) throws ClassNotFoundException, SQLException {
		
		List<CurrentAccount> currentAccounts = new ArrayList<CurrentAccount>();
		String query="SELECT * FROM ACCOUNT WHERE TYPE='CA'";

		switch(choice){
		case 1:
			query="SELECT * FROM ACCOUNT WHERE TYPE='CA' ORDER BY account_id";
			break;
		case 2:
			query="SELECT * FROM ACCOUNT WHERE TYPE='CA' ORDER BY account_id DESC";
			break;
		case 3:
			query="SELECT * FROM ACCOUNT WHERE TYPE='CA' ORDER BY account_hn";
			break;			
		case 4:		
			query="SELECT * FROM ACCOUNT WHERE TYPE='CA' ORDER BY account_hn DESC";
			break;
		case 5:
			query="SELECT * FROM ACCOUNT WHERE TYPE='CA' ORDER BY account_bal";
			break;
		case 6:
			query="SELECT * FROM ACCOUNT WHERE TYPE='CA' ORDER BY account_bal DESC";
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
			double odLimit = resultSet.getDouble("odLimit");
			CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			currentAccounts.add(currentAccount);
		}
//		DBUtil.commit();
		return currentAccounts;
		
	}

	@Override
	public int updateAccount(int accountnumber, String newAccountHolderName) throws ClassNotFoundException, SQLException {
		Connection connection=DBUtil.getConnection();
		String query="UPDATE ACCOUNT SET account_hn=? where account_id=?";		
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
	public CurrentAccount getAccountByHolderName(String accountHolderName) throws AccountNotFoundException, ClassNotFoundException, SQLException {
		Connection connection=DBUtil.getConnection();
		String query="SELECT * FROM ACCOUNT WHERE account_hn LIKE ?";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setString(1, "%"+accountHolderName+"%");
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount currentAccount = null;
		if(resultSet.next()) {
			int accountNumber=resultSet.getInt("account_id");
			String accountHN=resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit= resultSet.getDouble("odLimit");
			currentAccount = new CurrentAccount(accountNumber, accountHN, accountBalance,
					odLimit);
			return currentAccount;
		}
		throw new AccountNotFoundException("Account with account holder name "+accountHolderName+" does not exist.");		
		
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccountInBalanceRange(
			double minimumAccountBalance, double maximumAccountBalance) throws ClassNotFoundException, SQLException,AccountNotFoundException {

		Connection connection=DBUtil.getConnection();
		String query="SELECT * FROM ACCOUNT WHERE account_bal >= ? AND account_bal <= ? AND TYPE='CA'";
		PreparedStatement preparedStatement=connection.prepareStatement(query);
		preparedStatement.setDouble(1, minimumAccountBalance);
		preparedStatement.setDouble(2, maximumAccountBalance);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount currentAccount = null;
		List<CurrentAccount> listCurrentAccounts=new ArrayList<CurrentAccount>();
		while(resultSet.next()) {
			int accountNumber=resultSet.getInt("account_id");
			String accountHolderName=resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			listCurrentAccounts.add(currentAccount);
		}
		return listCurrentAccounts;
	}
}
