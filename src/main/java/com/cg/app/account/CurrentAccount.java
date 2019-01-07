package com.cg.app.account;

public class CurrentAccount {

	private BankAccount bankAccount;
	private double odLimit;

	/**
	 * This constructor is used for creating new current account
	 * 
	 * @param accountHolderName
	 * @param accountBalance
	 * @param odLimit
	 */
	public CurrentAccount(String accountHolderName, double accountBalance, double odLimit) {
		bankAccount = new BankAccount(accountHolderName, accountBalance);
		this.setOdLimit(odLimit);
	}

	/**
	 * This constructor is used for creating object of current account. 
	 * 
	 * 
	 * @param accountNumber
	 * @param accountHolderName
	 * @param accountBalance
	 * @param odLimit
	 */
	public CurrentAccount(int accountNumber, String accountHolderName, double accountBalance, double odLimit) {
		bankAccount = new BankAccount(accountNumber, accountHolderName, accountBalance);
		this.setOdLimit(odLimit);
	}
	
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public String toString() {
		return "CurrentAccount [bankAccount=" + bankAccount + ", odLimit="
				+ odLimit + "]";
	}

	public double getOdLimit() {
		return odLimit;
	}

	public void setOdLimit(double odLimit) {
		this.odLimit = odLimit;
	}
}