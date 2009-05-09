package com.faktel;

import java.util.ArrayList;

/**
 * Контейнер за данните от един ред от фактурата. Абстрахира всички видове
 * фактури. Не съдържа полета от евентуален user data файл.
 * 
 * @version 1.1
 */
public class InvoiceRow {
	private Object sim;
	private Object telNumber;
	private Object description;
	private Object amount;
	private Object price;

	// всякви бози тука - само за дебъгване.
	// private Object shits;

	public InvoiceRow() {

	}

	public InvoiceRow(String[] row) {
		// specific parse of row data
		// THIS CODE WILL BE DELETED, THE FIELDS SHOULD BE MAPPED AUTOMATICALLY
		// m_metaData = row;
	}

	@Override
	public String toString() {
		// StringBuilder bld = new StringBuilder();
		// for (String d : m_metaData) {
		// bld.append(d + " ");
		// }
		// return bld.toString();
		return String.format("SIM=%s, TEL=%s, DESC=%s, AMOUNT=%s, PRICE=%s",
				sim, telNumber, description, amount, price);
	}

	public ArrayList<Object> toArrayList() {
		ArrayList<Object> res = new ArrayList<Object>(5);
		res.add(sim);
		res.add(telNumber);
		res.add(description);
		res.add(amount);
		res.add(price);
		return res;
	}
}
