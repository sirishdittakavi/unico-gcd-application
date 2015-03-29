package com.unico.gcd.jms.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InputBean implements Serializable {
	
	private static final long serialVersionUID = -3534643016991988966L;
	
	private int firstNumber;

	private int secondNumber;

	/**
	 * @return the firstNumber
	 */
	public int getFirstNumber() {
		return firstNumber;
	}

	/**
	 * @param firstNumber
	 *            the firstNumber to set
	 */
	public void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}

	/**
	 * @return the secondNumber
	 */
	public int getSecondNumber() {
		return secondNumber;
	}

	/**
	 * @param secondNumber
	 *            the secondNumber to set
	 */
	public void setSecondNumber(int secondNumber) {
		this.secondNumber = secondNumber;
	}
	
	@Override
	public boolean equals(Object o) {
		InputBean input = (InputBean)o;
		
		if ((input.getFirstNumber() == this.getFirstNumber()) && (input.getSecondNumber() == this.getSecondNumber())) {
			return true;
		} else {
			return false;
		}
	}

}
