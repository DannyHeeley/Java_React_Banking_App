package accounts;

import com.main.app.core.Bank;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountFactory;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AdultAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.main.app.core.FactoryBase.AccountType.ADULT;
import static com.main.app.core.FactoryBase.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountManagerTest {

    private static final String CORRECT_PASSWORD = "Password123!";
    ArrayList<AccountBase> bankAccounts;
    String userNameAdult = "Adult";
    String userNameStudent = "Student";
    String firstName = "Fizz";
    String lastName = "Buzz";
    LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
    String email = "fizzbuzz@gmail.com";
    Float initialDeposit = 50f;

    @BeforeEach
    void setUp() {
        bankAccounts = AccountManager.getInstance().getBankAccounts();
    }

    @AfterEach
    void tearDown() {
        AccountManager.getInstance().clearBankAccountList();
        Bank.getInstance().resetBank();
    }

    @Test
    void returnsEmptyListOfBankAccounts() {
        assertThat(bankAccounts).isNotNull();
        assertThat(bankAccounts).isEmpty();
        assertThat(bankAccounts).isEqualTo(new ArrayList<>());
    }

    @Test
    void getsAllBankAccounts() {
        ArrayList<AccountBase> theseBankAccounts = AccountManager.getInstance().getBankAccounts();

        AccountBase account1 = AccountFactory.newAdultAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1);

        AccountBase account2 = AccountFactory.newStudentAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1, account2);

        AccountBase account3 = AccountFactory.newStudentAccount(
                STUDENT, "Another Student", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1, account2, account3);
    }

    @Test
    void accountsHasOneAdultAccount() {
        AdultAccount account = (AdultAccount) AccountFactory.newAdultAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account);
    }

    @Test
    void duplicateAdultAccountIsNotAddedToAccounts() {
        AdultAccount account1 = (AdultAccount) AccountFactory.newAdultAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newAdultAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnly(account1);
    }

    @Test
    void accountsHasOneStudentAccount() {
        AccountBase account = AccountFactory.newStudentAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account);
    }

    @Test
    void duplicateStudentAccountIsNotAddedToAccounts() {
        AccountBase account1 = AccountFactory.newStudentAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newStudentAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnly(account1);
    }

    @Test
    void returnsCorrectAccount() {
        AccountBase account1 = AccountFactory.newAdultAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountBase account2 = AccountFactory.newStudentAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 150f, firstName, lastName, dateOfBirth, email
        );
        assertThat(AccountManager.getInstance().getAccount(account1.getAccountId())).isEqualTo(account1);
        assertThat(AccountManager.getInstance().getAccount(account1.getAccountId())).isNotEqualTo(account2);
    }

    @Test
    void trueIfAccountExists() {
        AccountBase adultAccount = AccountFactory.newAdultAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountBase studentAccount = AccountFactory.newStudentAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertTrue(AccountManager.getInstance().accountExists(adultAccount.getUserName()));
        assertTrue(AccountManager.getInstance().accountExists(studentAccount.getUserName()));
    }
}
