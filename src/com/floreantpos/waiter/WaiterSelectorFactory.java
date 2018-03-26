package com.floreantpos.waiter;

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.OrderType;

public class WaiterSelectorFactory {
	private static WaiterSelector customerSelector;

	public static WaiterSelectorDialog createWaiterSelectorDialog(OrderType orderType) {
		OrderServiceExtension orderServicePlugin = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		if (customerSelector == null) {
			if (orderServicePlugin == null) {
				customerSelector = new DefaultWaiterListView();
			}
			else {
				customerSelector = orderServicePlugin.createNewWaiterSelector();
			}
		}
		customerSelector.setOrderType(orderType);
		customerSelector.redererCustomers();

		return new WaiterSelectorDialog(customerSelector);
	}
}
