# BankingSystem

A Java-based banking system developed as a project to simulate real-world banking operations such as account creation, user authentication, deposits, withdrawals, and fund transfers. The application uses JavaFX for the GUI and JDBC for database connectivity.

## 🧾 Features

- Create and manage user accounts
- Secure login/logout functionality
- Deposit and withdraw funds
- Transfer money between accounts
- Clean, styled JavaFX interface
- Modular architecture with MVC pattern

## 🏗️ Project Structure

```
BankingSystem/
├── Application/        # Main application launcher
├── Interfaces/         # Interface definitions for service abstraction
├── Service/            # Business logic (account, user, transaction services)
├── Styles/             # CSS for UI styling
├── controller/         # JavaFX controllers
├── database/           # Database utilities and setup
├── model/              # POJOs representing data (User, Account, etc.)
├── resources/          # Static resources (images, etc.)
├── utils/              # Helper classes and utilities
└── view/               # FXML UI layouts
```

## 🚀 Getting Started

### Prerequisites

- Java 8 or above
- JavaFX SDK
- MySQL or compatible relational database
- IDE like IntelliJ IDEA or Eclipse

### 🛠️ Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/Shreyansh-iittirupati/BankingSystem.git
   cd BankingSystem
   ```

2. **Set up your database**
   - Create a database named `banking_system`.
   - Import the schema and sample data using the SQL scripts in `database/` folder.

3. **Configure DB credentials**
   - Open `database/DBConnection.java` and update the `url`, `username`, and `password` fields with your MySQL config.

4. **Run the application**
   - Open `Application/Main.java` in your IDE and run it.
   - Alternatively, compile and run it via terminal:
     ```bash
     javac -cp . Application/Main.java
     java Application.Main
     ```

## 🧱 Tech Stack

- Java
- JavaFX
- JDBC
- MySQL
- CSS (for JavaFX UI styling)


## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Made with 💻 by [Shreyansh](https://github.com/Shreyansh-iittirupati) and [Suhail](https://github.com/superss2104)
