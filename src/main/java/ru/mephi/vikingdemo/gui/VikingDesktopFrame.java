package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.*;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton lambdaButton = new JButton("Lambda Tools");
        lambdaButton.addActionListener(event -> openLambdaFrame());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(lambdaButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void openLambdaFrame() {
        VikingLambdaFrame lambdaFrame = new VikingLambdaFrame(lambdaService, vikingService);
        lambdaFrame.setVisible(true);
    }

    public void addNewViking(Viking viking) {
        SwingUtilities.invokeLater(() -> tableModel.addViking(viking));
    }

    public void removeViking(String name) {
        SwingUtilities.invokeLater(() -> tableModel.removeViking(name));
    }

    public void updateViking(String name, Viking updated) {
        SwingUtilities.invokeLater(() -> tableModel.updateViking(name, updated));
    }
}