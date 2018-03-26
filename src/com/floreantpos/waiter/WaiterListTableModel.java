package com.floreantpos.waiter;

import java.util.List;

import com.floreantpos.Messages; 
import com.floreantpos.model.Waiter;
import com.floreantpos.swing.PaginatedTableModel;

public class WaiterListTableModel extends PaginatedTableModel {

	private final static String[] columns = {
			Messages.getString("CustomerListTableModel.1"), //first name 
            Messages.getString("CustomerListTableModel.7") // last name
        }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

	public WaiterListTableModel() {
		super(columns);
	}

	public WaiterListTableModel(List<Waiter> customers) {
		super(columns, customers);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Waiter customer = (Waiter) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return customer.getFirstName();
			case 1:
				return customer.getLastName();
 
		}
		return null;
	}
}
