import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

class DatabaseConnector {
    static final String URL = "jdbc:mysql://localhost:3306/portfolio_tracker";
    static final String USER = "root";
    static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static  String ADMIN_TABLE = "Admins";

    public static void createAdminTable() {
        try (Connection connection = getConnection()) {
            String createAdminTableQuery = "CREATE TABLE IF NOT EXISTS Admins(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "email VARCHAR(255) UNIQUE, " +
                    "password VARCHAR(255))";

            try (PreparedStatement statement = connection.prepareStatement(createAdminTableQuery)) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
class UsersCrypto
{
    
    String firstname;
    String lastname;
    String email;
    String password;
    String phone_no;
    public UsersCrypto( String firstname, String lastname, String email, String phone_no,String password) {
    
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone_no=phone_no;
        this.password = password;
    }
   
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
  
    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
}


class Admin {
    private String email;
    private String password;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class Cryptocurrency {
   static int userId;
    static String symbol;
     static double quantity;
    static double purchasePrice;

    public Cryptocurrency(int userId, String symbol, double quantity, double purchasePrice) {
        this.userId = userId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

}

class AllMethods{
    boolean isAdmin = false;
    boolean isUser=false;
    Scanner sc=new Scanner(System.in);
    

   static boolean userLogin(String email, String password) throws SQLException {
    if (!isValidGmail(email)) {
        System.out.println("The email provided by you does not exist or The format is wrong \n please provide your email id in ||| @gmail.com ||| format");
        return false; 
    }


    try (Connection connection = DatabaseConnector.getConnection()) {
        String query = "select * from New_users where email=? AND password=? ";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet resultSet = pst.executeQuery()) {
                return resultSet.next();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    static boolean userRegister(String firstname, String lastname, String email, String phone_no, String password) {
    try (Connection connection = DatabaseConnector.getConnection()) {
       
        if (!isValidGmail(email)) {
            System.out.println("Please enter your gmail in @gmail.com form");
            return false; 
        }

        if (!isValidPhoneNumber(phone_no)) {
            System.out.println("Please enter a 10-digit phone number");
            return false;
        }

          if (!isValidPassword(password)) {
            System.out.println("Password must meet the following conditions:\n" +
                               "1. At least 8 characters long\n" +
                               "2. Contains both uppercase and lowercase letters\n" +
                               "3. Contains at least one digit");
            return false;
        }

        String query = "insert into New_users(firstname,lastname,email,phone_no,password) values(?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, firstname);
            pst.setString(2, lastname);
            pst.setString(3, email);
            pst.setString(4, phone_no);
            pst.setString(5, password);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false; 
    }
}

// Check if the email is in the correct @gmail.com format
static boolean isValidGmail(String email) {
    return email != null && email.endsWith("@gmail.com");
}


static boolean isValidPhoneNumber(String phone_no) {
    if (phone_no != null && phone_no.length() == 10) {
        try {
            Long.parseLong(phone_no); // Try to parse the phone number as a long integer
            return true; // If parsing succeeds, it's a valid 10-digit number
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid phone number \n Phone number should be in 10 digit");
            return false;
        }
    }
    return false; // Not 10 digits or null
}


static boolean isValidPassword(String password) {
    if (password != null && password.length() >= 8) {
        boolean containsUppercase = false;
        boolean containsLowercase = false;
        boolean containsDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUppercase = true;
            } else if (Character.isLowerCase(c)) {
                containsLowercase = true;
            } else if (Character.isDigit(c)) {
                containsDigit = true;
            }

            // Break early if all conditions are met
            if (containsUppercase && containsLowercase && containsDigit) {
                return true;
            }
        }
    }
    return false;
}



    int getUserIdByEmail(String email) {
        try (Connection connection = DatabaseConnector.getConnection()) {

            if (!isValidGmail(email)) {
                 System.out.println("The email provided by you does not exist or The format is wrong \n please provide your email id in ||| @gmail.com ||| format");
        return -1; 
    }
            String query = "SELECT id FROM New_users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    String getEmailByEmail() {
        System.out.println("Enter your Email-id:");
        return sc.nextLine();
    }


    boolean adminLogin(String email, String password) {

        try (Connection connection = DatabaseConnector.getConnection()) {
            if (!isValidGmail(email)) {
                 System.out.println("The email provided by you does not exist or The format is wrong \n please provide your email id in ||| @gmail.com ||| format");
        return false; 
    }

     
            String query = "SELECT * FROM Admins WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    isAdmin = resultSet.next();
                    return isAdmin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    boolean adminRegister(String email, String password) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            if (!isValidGmail(email)) {
                System.out.println("Please enter your gmail in @gmail.com form");
        return false;
    }
     if (!isValidPassword(password)) {
            System.out.println("Password must meet the following conditions:\n" +
                               "1. At least 8 characters long\n" +
                               "2. Contains both uppercase and lowercase letters\n" +
                               "3. Contains at least one digit");
            return false;
        }
            String query = "INSERT INTO admins (email, password) VALUES (?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    boolean isAdmin() {
        return isAdmin;
    }

    boolean addCryptocurrency(String Symbol,String crypto_name,double Quantity,double purchase_price) {
    if (isAdmin) {
         
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO cryptoportfolio (symbol,crypto_name,quantity,purchase_price) values (?,?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
              preparedStatement.setString(1, Symbol);
              preparedStatement.setString(2, crypto_name);
              preparedStatement.setDouble(3, Quantity);
              preparedStatement.setDouble(4,purchase_price);
              int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return false;
    }


    
     boolean removeCryptocurrency(int id,String symbol) throws SQLException
    {
        try(Connection connection=DatabaseConnector.getConnection()){
        String query="delete from cryptoportfolio  Where id=? AND symbol=?";
        try(PreparedStatement pst=connection.prepareStatement(query))
        {
            pst.setInt(1, id);
            pst.setString(2, symbol);
            int rowsAffected=pst.executeUpdate();
            return rowsAffected>0;
        }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    boolean updateCryptocurrency(int id,String symbol, double quantity,double purchase_price) throws SQLException
    {
        try(Connection connection=DatabaseConnector.getConnection())
        {
            String query="update cryptoportfolio SET quantity = ? , purchase_price = ? WHERE id = ? AND symbol = ? ";
            try(PreparedStatement pst=connection.prepareStatement(query))
            {
                pst.setDouble(1, quantity);
                pst.setDouble(2, purchase_price);
                pst.setInt(3, id);
                pst.setString(4, symbol);
                
                int rowsAffected=pst.executeUpdate();
                return rowsAffected>0;
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;

    }

    boolean displayCryptocurrencyInfo() throws Exception
    {
        try(Connection connection = DatabaseConnector.getConnection())
        {
            String query="SELECT * FROM cryptoportfolio";

             try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String symbol = resultSet.getString("symbol");
                    String cryptoName = resultSet.getString("crypto_name");
                    double quantity = resultSet.getDouble("quantity");
                    double purchasePrice = resultSet.getDouble("purchase_price");

                    System.out.println("ID: " + id);
                    System.out.println("Symbol: " + symbol);
                    System.out.println("Crypto Name: " + cryptoName);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Purchase Price: " + purchasePrice);
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    boolean cryptoExists(int cryptoId) {
    try (Connection connection = DatabaseConnector.getConnection()) {
        String query = "SELECT id FROM cryptoportfolio WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, cryptoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); 
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; 
 }

   boolean investInCrypto(String email,int id,String symbol, double quantity, double purchase_price, Timestamp purchase_Time,int cryptoId){
    try (Connection connection = DatabaseConnector.getConnection()) {
         if (!isValidGmail(email)) {
                System.out.println("Please enter your gmail in @gmail.com form");
        return false;
    }
        int userId = getUserIdByEmail(email);
        if (userId == -1) {
            System.out.println("User with this email does not exist.");
            return false;
        }

        if (!cryptoExists(cryptoId)) {
            System.out.println("Cryptocurrency with this ID does not exist.");
            return false;
        }
        
         boolean hasPaidTax = checkUserTaxStatus(userId);
           if (!hasPaidTax) {
            System.out.println("You must pay the tax before making an investment.");
            return false;
        }

        String insertQuery = "INSERT INTO investment_of_users (id,symbol, quantity, purchase_price, purchase_Time, crypto_id) VALUES (?,?, ?, ?, ?, ? )";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, symbol);
            preparedStatement.setDouble(3, quantity);
            preparedStatement.setDouble(4, purchase_price);
            preparedStatement.setTimestamp(5, purchase_Time);
            preparedStatement.setInt(6, cryptoId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Investment successful!");
                return true;
            } else {
                System.out.println("Failed to make the investment.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    boolean checkUserTaxStatus(int userId) {
    try (Connection connection = DatabaseConnector.getConnection()) {
        String query = "SELECT has_paid_tax FROM new_users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    boolean hasPaidTax = resultSet.getBoolean("has_paid_tax");
                    return hasPaidTax;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
    }

        public double calculateTax(double purchaseAmount) {
        
        double taxRate = 0.18; 
        double taxAmount = purchaseAmount * taxRate;
        return taxAmount;
    }

     public boolean markTaxAsPaid(int userId, double purchaseAmount) {
        try (Connection connection = DatabaseConnector.getConnection()) {
           
            double taxRate = 0.18; 
            double taxAmount = purchaseAmount * taxRate;

           
            String updateQuery = "UPDATE new_users SET has_paid_tax = ?, tax_amount = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setBoolean(1, true); 
                preparedStatement.setDouble(2, taxAmount); 
                preparedStatement.setInt(3, userId);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; 
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    boolean showInvestment(int id) throws Exception
    {
        try(Connection connection=DatabaseConnector.getConnection())
        {
            
        }
        return false;

    }


}

 class CryptocurrencyTrackerApp01 {
    public static void main(String[] args) throws Exception {
      
        AllMethods allMethods = new AllMethods();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Cryptocurrency Tracker!");

        while (true) {
            System.out.println("1. Login as Admin");
            System.out.println("2. Register as Admin");
            System.out.println("3. Login as User");
            System.out.println("4. Register as User");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter your email: ");
                String email = scanner.nextLine();
                System.out.print("Enter your password: ");
                String password = scanner.nextLine();

                if (allMethods.adminLogin(email, password)) {
                    System.out.println("Admin login successful!");
                    adminOptions(allMethods);
                } else {
                    System.out.println("Admin login failed (OR) Admin does not Exists. Please try again.");
                }
            } else if (choice == 2) {
                System.out.print("Enter your email: ");
                String email = scanner.nextLine();
                System.out.print("Enter your password: ");
                String password = scanner.nextLine();

                if (allMethods.adminRegister(email, password)) {
                    System.out.println("Admin registration successful!");
                } else {
                    System.out.println("Admin registration failed. Please try again.");
                }
            } else if(choice == 3)
            {
                System.out.println("Enter your email: ");
                String user_email=scanner.nextLine();
                System.out.print("Enter your password: ");
                String user_password = scanner.nextLine();
                if(allMethods.userLogin(user_email, user_password))
                {
                     System.out.println("User login successful!");
                     userOptions(allMethods);
                }
                else
                {
                    System.out.println("User login failed (OR) User does not Exists. Please try again.");
                }
            }
            else if(choice == 4)
            {
                 System.out.println("Enter Firstname of user:");
                 String firstname = scanner.nextLine();

                 System.out.println("Enter Lastname of user:");
                 String lastname = scanner.nextLine();

                 System.out.println("Enter Email_id of user:");
                 String email_id = scanner.nextLine();

                 System.out.println("Enter Phone-Number of user:");
                 String phone_no = scanner.nextLine();
        
                 System.out.println("Enter new password of user:");
                 String password = scanner.nextLine();

                 if(allMethods.userRegister(firstname, lastname, email_id, phone_no, password))
                 {
                    System.out.println("User registration successful!");
                 }
                 else
                 {
                    System.out.println("User registration failed. Please try again.");
                 }

            }
            
            else if (choice == 5) {
                System.out.println("Exiting the Cryptocurrency Tracker. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please choose a valid option.");
            }
        }

        scanner.close();
    }

    static void adminOptions(AllMethods allMethods) throws Exception {
    Scanner scanner = new Scanner(System.in);

    while (true) 
    {
        System.out.println("Admin Options:");
        System.out.println("1. Add Cryptocurrency");
        System.out.println("2. Remove Cryptocurrency");
        System.out.println("3. Update CryptoCurrency");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.print("Enter the cryptocurrency symbol: ");
            String symbol = scanner.nextLine();
            System.out.println("Crypto currency name: ");
            String crypto_name = scanner.nextLine();
            System.out.print("Enter the quantity: ");
            double quantity = scanner.nextDouble();
            System.out.print("Enter the purchase price: ");
            double purchasePrice = scanner.nextDouble();
            

            if (allMethods.addCryptocurrency(symbol, crypto_name, quantity, purchasePrice)) {
                System.out.println("Cryptocurrency added successfully.");
                
            } else {
                System.out.println("Failed to add cryptocurrency.");
            }
        } else if (choice == 2) {
            System.out.print("Enter the ID of the crypto: ");
            int Id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter the cryptocurrency symbol: ");
            String symbol_crypto = scanner.nextLine();

            if (allMethods.removeCryptocurrency(Id, symbol_crypto)) {
                System.out.println("Cryptocurrency removed successfully.");
            } else {
                System.out.println("Failed to remove cryptocurrency.");
            }
        } else if (choice == 3) {
            System.out.print("Enter the cypto ID: ");
            int update_Id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter the cryptocurrency symbol: ");
            String update_symbol = scanner.nextLine();
            System.out.print("Enter the quantity: ");
            double update_quantity = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter the purchase price: ");
            double update_purchasePrice = scanner.nextDouble();
            scanner.nextLine();

            if (allMethods.updateCryptocurrency(update_Id, update_symbol,update_quantity, update_purchasePrice)) {
                System.out.println("Cryptocurrency updated successfully.");
            } else {
                System.out.println("Failed to update cryptocurrency.");
            }
        } else if (choice == 4) {
            System.out.println("Logging out.");
            break;
        } else {
            System.out.println("Invalid choice. Please choose a valid option.");
        }
    }

    scanner.close();
}

    static void userOptions(AllMethods allMethods) throws Exception {
    Scanner scanner = new Scanner(System.in);

        while(true)
        {
            System.out.println(" Users Options ");
            System.out.println("1. ALL CryptoCurrency available in market");
            System.out.println("2. Invest in Crypto");
            System.out.println("3. Calculate Tax ");
            System.out.println("4. Pay Tax");
            System.out.println("5. View Investment");
            System.out.println("6. Exit");

            int choice=scanner.nextInt();
            scanner.nextLine();

            if(choice == 1)
            {
                allMethods.displayCryptocurrencyInfo();
            }
            else if(choice == 2)
            {
                System.out.println("Enter user email-id: ");
                String email=scanner.nextLine();
                scanner.nextLine();
                System.out.println("Enter id");
                int Id=scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter symbol of crypto: ");
                String symbol=scanner.nextLine();
                System.out.println("Enter quantity you want to invest: ");
                double quantity=scanner.nextDouble();
                System.out.println("Enter purchase amount: ");
                double purchase_amount=scanner.nextDouble();

                Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                

                System.out.println("Enter crypto_id");
                int crypto_id=scanner.nextInt();

                if(allMethods.investInCrypto(email,Id,symbol, quantity, purchase_amount, currentTimestamp, crypto_id))
                {
                    System.out.println("Investment has been succesfully completed");
                }
                else
                {
                    System.out.println("Investment has been Failed");
                }

            }
            else if(choice == 3)
            {
                System.out.println("Enter purchase amount");
                double purchase_amount=scanner.nextDouble();
                double taxAmount = allMethods.calculateTax(purchase_amount);
                double totalPurchaseAmount = purchase_amount + taxAmount;

                System.out.println("Purchase Amount: " + purchase_amount);
                System.out.println("Tax Amount (18%): " + taxAmount);
                System.out.println("Total Purchase Amount (including tax): " + totalPurchaseAmount);
            }

            else if(choice == 4)
            {
                System.out.println("Enter user_id for paying a tax");
                int user_id=scanner.nextInt();
                System.out.println("Enter purchase amount");
                double purchase_amount=scanner.nextDouble();

                if(allMethods.markTaxAsPaid(user_id, purchase_amount))
                {
                    System.out.println("Tax has been paid successfully");
                }
                else
                {
                    System.out.println("Tax has been failed");
                }
            }
            else if(choice == 5)
            {
                System.out.println("Logging out.");
             break;
            }
            else {
            System.out.println("Invalid choice. Please choose a valid option.");
         }
        }
        scanner.close();
    }
}