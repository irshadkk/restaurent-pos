
package com.floreantpos.waiter;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.extension.OrderServiceFactory; 
import com.floreantpos.model.Ticket;
import com.floreantpos.model.Waiter; 
import com.floreantpos.model.dao.WaiterDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.QuickCustomerForm;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class DefaultWaiterListView extends WaiterSelector {

	private PosButton btnCreateNewCustomer;
	private WaiterTable waiterTable;
//	private POSTextField tfMobile;
//	private POSTextField tfLoyaltyNo;
	private POSTextField tfName;
	private PosButton btnInfo;
	protected Waiter selectedWaiter;
	private PosButton btnRemoveCustomer;

	private Ticket ticket;
	private PosButton btnCancel;
	private QwertyKeyPad qwertyKeyPad;
	private PosButton btnNext;
	private PosButton btnPrevious;
	private WaiterListTableModel waiterListTableModel;
	private JLabel lblNumberOfItem;

	public DefaultWaiterListView() {
		initUI();
	}

	public DefaultWaiterListView(Ticket ticket) {
		this.ticket = ticket;
		initUI();
//		loadCustomerFromTicket();
                doSearchWaiterByIndex();
	}

	public void initUI() {
		setLayout(new MigLayout("fill", "[grow]", "[grow][grow][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JPanel searchPanel = new JPanel(new MigLayout());

//		JLabel lblByPhone = new JLabel(Messages.getString("CustomerSelectionDialog.1")); //$NON-NLS-1$
//		JLabel lblByLoyality = new JLabel(Messages.getString("CustomerSelectionDialog.16")); //$NON-NLS-1$
		JLabel lblByName = new JLabel(Messages.getString("CustomerSelectionDialog.19")); //$NON-NLS-1$

//		tfMobile = new POSTextField(16);
//		tfLoyaltyNo = new POSTextField(16);
		tfName = new POSTextField(16);

		tfName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchWaiterByIndex();
			}
		});
//		tfLoyaltyNo.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				doSearchCustomerByIndex();
//			}
//		});
//		tfMobile.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				doSearchCustomerByIndex();
//			}
//		});

		PosButton btnSearch = new PosButton(Messages.getString("CustomerSelectionDialog.15")); //$NON-NLS-1$
		btnSearch.setFocusable(false);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchWaiterByIndex();
			}
		});

		PosButton btnKeyboard = new PosButton(IconFactory.getIcon("/images/", "keyboard.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnKeyboard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				qwertyKeyPad.setCollapsed(!qwertyKeyPad.isCollapsed());
			}
		});

//		searchPanel.add(lblByPhone, "growy"); //$NON-NLS-1$
//		searchPanel.add(tfMobile, "growy"); //$NON-NLS-1$
//		searchPanel.add(lblByLoyality, "growy"); //$NON-NLS-1$
//		searchPanel.add(tfLoyaltyNo, "growy"); //$NON-NLS-1$
		searchPanel.add(lblByName, "growy"); //$NON-NLS-1$
		searchPanel.add(tfName, "growy"); //$NON-NLS-1$
		searchPanel.add(btnKeyboard, "growy,w " + PosUIManager.getSize(80) + "!,h " + PosUIManager.getSize(35) + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		searchPanel.add(btnSearch, ",growy,h " + PosUIManager.getSize(35) + "!"); //$NON-NLS-1$ //$NON-NLS-2$

		add(searchPanel, "cell 0 1"); //$NON-NLS-1$

		JPanel centerPanel = new JPanel(new BorderLayout());
		setBorder(new TitledBorder(null, POSConstants.SELECT_WAITER.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, null, null)); //$NON-NLS-1$

		JPanel customerListPanel = new JPanel(new BorderLayout(0, 0));
		customerListPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

		waiterTable = new WaiterTable();
		waiterListTableModel = new WaiterListTableModel();
		waiterListTableModel.setPageSize(20);
		waiterTable.setModel(waiterListTableModel);
		waiterTable.setFocusable(false);
		waiterTable.setRowHeight(30);
		waiterTable.getTableHeader().setPreferredSize(new Dimension(100, 35));
		waiterTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		waiterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedWaiter = waiterTable.getSelectedCustomer();
				if (selectedWaiter != null) {
					//btnInfo.setEnabled(true);
				}
				else {
					btnInfo.setEnabled(false);
				}
			}
		});
		PosScrollPane scrollPane = new PosScrollPane();
		scrollPane.setFocusable(false);
		scrollPane.setViewportView(waiterTable);

		customerListPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel(new MigLayout("fill", "[][center, grow][]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnInfo = new PosButton(Messages.getString("CustomerSelectionDialog.23")); //$NON-NLS-1$
		btnInfo.setFocusable(false);
		panel.add(btnInfo, " skip 1, split 6"); //$NON-NLS-1$
		btnInfo.setEnabled(false);

		PosButton btnHistory = new PosButton(Messages.getString("CustomerSelectionDialog.24")); //$NON-NLS-1$
		btnHistory.setEnabled(false);
		panel.add(btnHistory, ""); //$NON-NLS-1$

		btnCreateNewCustomer = new PosButton(Messages.getString("CustomerSelectionDialog.25")); //$NON-NLS-1$
		btnCreateNewCustomer.setFocusable(false);
		panel.add(btnCreateNewCustomer, ""); //$NON-NLS-1$
		btnCreateNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				doCreateNewCustomer();
			}
		});

		btnRemoveCustomer = new PosButton(Messages.getString("CustomerSelectionDialog.26")); //$NON-NLS-1$
		btnRemoveCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemoveCustomerFromTicket();
			}
		});
		panel.add(btnRemoveCustomer, ""); //$NON-NLS-1$

		PosButton btnSelect = new PosButton(Messages.getString("CustomerSelectionDialog.28")); //$NON-NLS-1$
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isCreateNewTicket()) {
//					doCreateNewTicket();
				}
				else {
					closeDialog(false);
				}
			}
		});
		panel.add(btnSelect, ""); //$NON-NLS-1$

		btnCancel = new PosButton(Messages.getString("CustomerSelectionDialog.29")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog(true);
			}
		});
		panel.add(btnCancel, ""); //$NON-NLS-1$

		btnNext = new PosButton("NEXT");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (waiterListTableModel.hasNext()) {
					waiterListTableModel.setCurrentRowIndex(waiterListTableModel.getNextRowIndex());
					doSearchWaiter();
				}

			}
		});
		btnPrevious = new PosButton("PREVIOUS");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (waiterListTableModel.hasPrevious()) {
					waiterListTableModel.setCurrentRowIndex(waiterListTableModel.getPreviousRowIndex());
					doSearchWaiter();
				}
				updateButtonStatus();
			}
		});
		lblNumberOfItem = new JLabel();
		panel.add(lblNumberOfItem);
		panel.add(btnPrevious);
		panel.add(btnNext);
		customerListPanel.add(panel, BorderLayout.SOUTH);
		centerPanel.add(customerListPanel, BorderLayout.CENTER); //$NON-NLS-1$

		add(centerPanel, "cell 0 2,grow"); //$NON-NLS-1$

		qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		qwertyKeyPad.setCollapsed(false);
		add(qwertyKeyPad, "cell 0 3,grow"); //$NON-NLS-1$

		updateButtonStatus();
	}

	public void updateButtonStatus() {
		btnNext.setEnabled(waiterListTableModel.hasNext());
		btnPrevious.setEnabled(waiterListTableModel.hasPrevious());
	}

	private void loadCustomer() {
		Waiter customer = getCustomer();
		if (customer != null) {
			doSearchWaiterByIndex();
		}
		else {
			doSearchWaiterByIndex();
		}
	}

	private void loadCustomerFromTicket() {
		String customerIdString = ticket.getProperty(Ticket.CUSTOMER_ID);
		if (StringUtils.isNotEmpty(customerIdString)) {
			int customerId = Integer.parseInt(customerIdString);
			Waiter customer = WaiterDAO.getInstance().get(customerId);

			List<Waiter> list = new ArrayList<Waiter>();
			list.add(customer);
//			customerTable.setModel(new WaiterListTableModel(list));
		}

	}

	private void closeDialog(boolean canceled) {
		Window windowAncestor = SwingUtilities.getWindowAncestor(DefaultWaiterListView.this);
		if (windowAncestor instanceof POSDialog) {
			((POSDialog) windowAncestor).setCanceled(false);
			windowAncestor.dispose();
		}
	}

	protected void doSetWaiter(Waiter customer) {
		if (ticket != null) {
			ticket.setWaiter(customer);
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
	}

//	private void doCreateNewTicket() {
//		try {
//			Waiter selectedCustomer = getSelectedCustomer();
//
//			if (selectedCustomer == null) {
//				return;
//			}
//			closeDialog(false);
//			OrderServiceFactory.getOrderService().createNewTicket(getOrderType(), null, selectedCustomer);
//
//		} catch (TicketAlreadyExistsException e) {
//
//		}
//	}

	protected void doRemoveCustomerFromTicket() {
		int option = POSMessageDialog.showYesNoQuestionDialog(this,
				Messages.getString("CustomerSelectionDialog.2"), Messages.getString("CustomerSelectionDialog.32")); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.removeCustomer();
		TicketDAO.getInstance().saveOrUpdate(ticket);
		//setCanceled(false);
		//dispose();
	}

	private void doSearchWaiterByIndex() {
		waiterListTableModel.setCurrentRowIndex(0);
		doSearchWaiter();
	}

	protected void doSearchWaiter() {
            System.out.println("Inside Do search waiter");
            List<Waiter> waiterList=new ArrayList<Waiter>();
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			qwertyKeyPad.setCollapsed(true);
//			String mobile = tfMobile.getText();
			String name = tfName.getText();
//			String loyalty = tfLoyaltyNo.getText();

			if (/*StringUtils.isEmpty(mobile) && StringUtils.isEmpty(loyalty) && */StringUtils.isEmpty(name)) {
//				waiterListTableModel.setNumRows(WaiterDAO.getInstance().getNumberOfCustomers());
				waiterList=WaiterDAO.getInstance().findAll();
			}
			else {
//				waiterListTableModel.setNumRows(CustomerDAO.getInstance().getNumberOfCustomers(/*mobile, loyalty,*/ name));
//				CustomerDAO.getInstance().findBy(/*mobile, loyalty, */name, waiterListTableModel);
                           waiterList= WaiterDAO.getInstance().findAll();
			}
                        waiterListTableModel.setRows(waiterList);
                        System.out.println("Inside Do search waiter and waiterList length=="+waiterList.size());

//			int startNumber = waiterListTableModel.getCurrentRowIndex() + 1;
//			int endNumber = waiterListTableModel.getNextRowIndex();
//			int totalNumber = waiterListTableModel.getNumRows();
//			if (endNumber > totalNumber) {
//				endNumber = totalNumber;
//			}
//			lblNumberOfItem.setText(String.format("Showing %s to %s of %s", startNumber, endNumber, totalNumber));
//
//			waiterListTableModel.fireTableDataChanged();
//			customerTable.repaint();
//			updateButtonStatus();
		} catch (Exception e) {
			PosLog.error(DefaultWaiterListView.class, e);
			e.printStackTrace();
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

//	protected void doCreateNewCustomer() {
//		boolean setKeyPad = true;
//
//		QuickCustomerForm form = new QuickCustomerForm(setKeyPad);
//
//		//TODO: handle exception
//
//		form.enableCustomerFields(true);
//		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), form);
//		dialog.setResizable(false);
//		dialog.open();
//
//		if (!dialog.isCanceled()) {
//			selectedWaiter = (Waiter) form.getBean();
//
//			WaiterListTableModel model = (WaiterListTableModel) waiterTable.getModel();
//			model.addItem(selectedWaiter);
//		}
//	}

	@Override
	public String getName() {
		return "C"; //$NON-NLS-1$
	}

	public Waiter getSelectedCustomer() {
		return selectedWaiter;
	}

	public void setRemoveButtonEnable(boolean enable) {
		btnRemoveCustomer.setEnabled(enable);
	}

	@Override
	public void updateView(boolean update) {
		if (update) {
			btnCancel.setVisible(true);
		}
		else {
			btnCancel.setVisible(false);
		}
		loadCustomer();
	}

	@Override
	public void redererCustomers() {

	}
}
