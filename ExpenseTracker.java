import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class LoginPage extends JFrame implements ActionListener {

    JLabel titleLabel, userLabel, passLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton;

    LoginPage() {

        setTitle("Login Page");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(30, 30, 60));

        titleLabel = new JLabel("Expense Tracker Login");
        titleLabel.setBounds(70, 30, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 90, 100, 30);
        userLabel.setForeground(Color.WHITE);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 140, 100, 30);
        passLabel.setForeground(Color.WHITE);

        userField = new JTextField();
        userField.setBounds(150, 90, 150, 30);

        passField = new JPasswordField();
        passField.setBounds(150, 140, 150, 30);

        loginButton = new JButton("Login");
        loginButton.setBounds(140, 200, 100, 35);
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(this);

        add(titleLabel);
        add(userLabel);
        add(passLabel);
        add(userField);
        add(passField);
        add(loginButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String username = userField.getText();
        String password = String.valueOf(passField.getPassword());

        if(username.equals("admin") && password.equals("1234")) {

            JOptionPane.showMessageDialog(this,
                    "Login Successful");

            new ExpenseTracker();

            dispose();

        } else {

            JOptionPane.showMessageDialog(this,
                    "Invalid Username or Password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

public class ExpenseTracker extends JFrame implements ActionListener {

    JLabel titleLabel, categoryLabel, amountLabel,
            dateLabel, totalLabel;

    JTextField categoryField, amountField, dateField;

    JButton saveButton, viewButton,
            updateButton, deleteButton,
            totalButton, clearButton;

    JTable table;
    DefaultTableModel model;

    Connection con;

    ExpenseTracker() {

        setTitle("Personal Expense Tracker");
        setSize(950, 550);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240, 248, 255));

        // Title
        titleLabel = new JLabel(
                "PERSONAL EXPENSE TRACKER",
                JLabel.CENTER);

        titleLabel.setFont(new Font("Verdana",
                Font.BOLD, 28));

        titleLabel.setForeground(new Color(0, 70, 140));

        add(titleLabel, BorderLayout.NORTH);

        // Left Panel
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(7, 2, 15, 15));

        panel.setBackground(new Color(220, 235, 250));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Expense Details"));

        categoryLabel = new JLabel("Category:");
        amountLabel = new JLabel("Amount:");
        dateLabel = new JLabel("Date:");

        Font labelFont = new Font("Arial",
                Font.BOLD, 16);

        categoryLabel.setFont(labelFont);
        amountLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);

        categoryField = new JTextField();

        amountField = new JTextField();

        dateField = new JTextField(
                LocalDate.now().toString());

        dateField.setEditable(false);

        saveButton = createButton("Save");
        viewButton = createButton("View");
        updateButton = createButton("Update");
        deleteButton = createButton("Delete");
        totalButton = createButton("Total");
        clearButton = createButton("Clear All");

        saveButton.addActionListener(this);
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        totalButton.addActionListener(this);
        clearButton.addActionListener(this);

        panel.add(categoryLabel);
        panel.add(categoryField);

        panel.add(amountLabel);
        panel.add(amountField);

        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(saveButton);
        panel.add(viewButton);

        panel.add(updateButton);
        panel.add(deleteButton);

        panel.add(totalButton);
        panel.add(clearButton);

        add(panel, BorderLayout.WEST);

        // Table
        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Category");
        model.addColumn("Amount");
        model.addColumn("Date");

        table = new JTable(model);

        table.setFont(new Font("Arial",
                Font.PLAIN, 14));

        table.setRowHeight(25);

        table.getTableHeader().setFont(
                new Font("Arial",
                        Font.BOLD, 15));

        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                categoryField.setText(
                        model.getValueAt(row, 1).toString());

                amountField.setText(
                        model.getValueAt(row, 2).toString());

                dateField.setText(
                        model.getValueAt(row, 3).toString());
            }
        });

        JScrollPane pane = new JScrollPane(table);

        add(pane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();

        bottomPanel.setBackground(
                new Color(200, 220, 240));

        totalLabel = new JLabel(
                "Total Expense: Rs. 0");

        totalLabel.setFont(new Font(
                "Arial",
                Font.BOLD,
                20));

        totalLabel.setForeground(
                new Color(150, 0, 0));

        bottomPanel.add(totalLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        connectDatabase();

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    JButton createButton(String text) {

        JButton btn = new JButton(text);

        btn.setBackground(
                new Color(0, 120, 215));

        btn.setForeground(Color.WHITE);

        btn.setFont(new Font(
                "Arial",
                Font.BOLD,
                14));

        return btn;
    }

    void connectDatabase() {

        try {

            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection(
                    "jdbc:sqlite:expense.db");

            Statement stmt =
                    con.createStatement();

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS expenses (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "category TEXT," +
                            "amount REAL," +
                            "date TEXT)"
            );

        } catch(Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e);
        }
    }

    public void actionPerformed(ActionEvent e) {

        // SAVE
        if(e.getSource() == saveButton) {

            try {

                String category =
                        categoryField.getText();

                double amount =
                        Double.parseDouble(
                                amountField.getText());

                String date =
                        dateField.getText();

                PreparedStatement pst =
                        con.prepareStatement(
                                "INSERT INTO expenses(category, amount, date) VALUES(?,?,?)");

                pst.setString(1, category);
                pst.setDouble(2, amount);
                pst.setString(3, date);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Expense Saved Successfully");

                categoryField.setText("");
                amountField.setText("");

                loadTable();

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Input");
            }
        }

        // VIEW
        if(e.getSource() == viewButton) {

            loadTable();
        }

        // UPDATE
        if(e.getSource() == updateButton) {

            try {

                int row =
                        table.getSelectedRow();

                if(row == -1) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Select Row First");

                    return;
                }

                int id = Integer.parseInt(
                        model.getValueAt(row, 0)
                                .toString());

                String category =
                        categoryField.getText();

                double amount =
                        Double.parseDouble(
                                amountField.getText());

                String date =
                        dateField.getText();

                PreparedStatement pst =
                        con.prepareStatement(
                                "UPDATE expenses SET category=?, amount=?, date=? WHERE id=?");

                pst.setString(1, category);
                pst.setDouble(2, amount);
                pst.setString(3, date);
                pst.setInt(4, id);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Updated Successfully");

                loadTable();

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex);
            }
        }

        // DELETE
        if(e.getSource() == deleteButton) {

            try {

                int row =
                        table.getSelectedRow();

                if(row == -1) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Select Row First");

                    return;
                }

                int id = Integer.parseInt(
                        model.getValueAt(row, 0)
                                .toString());

                PreparedStatement pst =
                        con.prepareStatement(
                                "DELETE FROM expenses WHERE id=?");

                pst.setInt(1, id);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Deleted Successfully");

                loadTable();

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex);
            }
        }

        // TOTAL
        if(e.getSource() == totalButton) {

            try {

                Statement stmt =
                        con.createStatement();

                ResultSet rs =
                        stmt.executeQuery(
                                "SELECT SUM(amount) AS total FROM expenses");

                double total =
                        rs.getDouble("total");

                if(rs.wasNull()) {

                    total = 0;
                }

                totalLabel.setText(
                        "Total Expense: Rs. "
                                + total);

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex);
            }
        }

        // CLEAR ALL
        if(e.getSource() == clearButton) {

            try {

                int confirm =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Delete All Data?",
                                "Confirm",
                                JOptionPane.YES_NO_OPTION);

                if(confirm == JOptionPane.YES_OPTION) {

                    Statement stmt =
                            con.createStatement();

                    stmt.executeUpdate("DELETE FROM expenses");

                    stmt.executeUpdate(
                        "DELETE FROM sqlite_sequence WHERE name='expenses'"
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "All Data Cleared");

                    loadTable();

                    totalLabel.setText(
                            "Total Expense: Rs. 0");
                }

            } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex);
            }
        }
    }

    void loadTable() {

        try {

            model.setRowCount(0);

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT * FROM expenses");

            while(rs.next()) {

                model.addRow(new Object[] {

                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("date")
                });
            }

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex);
        }
    }

    public static void main(String[] args) {

        new LoginPage();
    }
}