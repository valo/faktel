package com.faktel;

import java.util.ArrayList;

import com.faktel.mvc.GridRow;

/**
 * Контейнер за данните от един ред от фактурата. Абстрахира всички видове
 * фактури. Не съдържа полета от евентуален user data файл.
 * 
 * @version 1.1
 */
public class InvoiceRow {
	private String sim;
	private String telNumber;
	private String description;
	private String amount;
	private String price;
	//private String originalRow; от де да го 'зема да го либат калинките

	// всякви бози тука - само за дебъгване.
	// private Object shits;

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

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
	
	public GridRow toGridRow(){
		GridRow res = new GridRow(toArrayList().toArray());
		return res;
	}
}
