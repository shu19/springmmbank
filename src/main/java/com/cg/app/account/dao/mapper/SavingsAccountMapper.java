package com.cg.app.account.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cg.app.account.SavingsAccount;

public class SavingsAccountMapper implements RowMapper<SavingsAccount>{

	@Override
	public SavingsAccount mapRow(ResultSet rs, int rowNum) throws SQLException {

		int accountNumber=rs.getInt(1);
		String accountHolderName=rs.getString(2);
		double balance=rs.getDouble(3);
		boolean isSalary=rs.getBoolean(4);
		
		return new SavingsAccount(accountNumber,accountHolderName,balance, isSalary);
		
	}

}
