/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2 * (the "License"),
 * being the Mozilla Public License * Version 1.1 with a permitted attribution
 * clause; you may not use this * file except in compliance with the License.
 * You may obtain a copy of * the License at
 * http://www.floreantpos.org/license.html * Software distributed under the
 * License is distributed on an "AS IS" * basis, WITHOUT WARRANTY OF ANY KIND,
 * either express or implied. See the * License for the specific language
 * governing rights and limitations * under the License. * The Original Code is
 * FLOREANT POS. * The Initial Developer of the Original Code is OROCUBE LLC *
 * All portions are Copyright (C) 2015 OROCUBE LLC * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.util.POSUtil;
import java.util.ArrayList;
import java.util.List;

public class ItemSearchDialog extends OkCancelOptionDialog {

    private JTextField tfNumber;
//    private JTextField tfNumber1;
    private String value;
    private QwertyKeyPad qwertyKeyPad;

    public ItemSearchDialog() {
        super(POSUtil.getFocusedWindow(), POSConstants.SEARCH_ITEM_BUTTON_TEXT);
        init();
    }

    public ItemSearchDialog(Frame parent) {
        super(parent, POSConstants.SEARCH_ITEM_BUTTON_TEXT);
        init();
    }

    private void init() {
        setResizable(false);

        JPanel contentPane = getContentPanel();

        MigLayout layout = new MigLayout("inset 0"); //$NON-NLS-1$ 
        contentPane.setLayout(layout);

        tfNumber = new JTextField();
//        tfNumber1= new JTextField();
        tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
//        tfNumber.setFocusable(true);
//        tfNumber.requestFocus();
        tfNumber.setBackground(Color.WHITE);

        tfNumber.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doOk();
            }
        });

        qwertyKeyPad = new QwertyKeyPad();

        contentPane.add(tfNumber, "spanx, grow"); //$NON-NLS-1$
//        contentPane.add(tfNumber1, "spanx, grow"); //$NON-NLS-1$
        
        contentPane.add(qwertyKeyPad, "spanx ,grow"); //$NON-NLS-1$

        AutoSuggestor autoSuggestor = new AutoSuggestor(tfNumber, this, null, Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f) {
            @Override
            boolean wordTyped(String typedWord) {
                MenuItemDAO dao = new MenuItemDAO();

                List<MenuItem> allMenuItem = dao.getAllItemNames();
                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> words = new ArrayList<>();
                 for (MenuItem oneMenuItem : allMenuItem) {
                    words.add(oneMenuItem.getName());
                }

                setDictionary(words);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
    }

    @Override
    public void doOk() {
//        String s = tfNumber.getText();
//        if (s.equals("0") || s.equals("")) {
//            POSMessageDialog.showError(Application.getPosWindow(), "Please enter barcode or item no.");
//            return;
//        }
        setValue(tfNumber.getText().replaceAll("\\s+$", ""));
        setCanceled(false);
        dispose();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
