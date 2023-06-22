package com.algonquincollege.lelesandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the Main Activity for the application. It is responsible for taking user input, validating
 * it, and displaying messages according to the validation result.
 *
 * @author Lele Li
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * TextView for displaying the status of the password. If the password meets the complexity
     * requirements, it will display "Your password meets the requirements", otherwise it will display
     * "You shall not pass!".
     */
    private TextView statusTextView = null;

    /**
     * EditText for user to input their password. The password input will then be validated according
     * to the complexity requirements.
     */
    private EditText passwordEditText = null;

    /**
     * Button for users to submit their input password. On clicking this button, the app will validate
     * the entered password and display the appropriate message.
     */
    private Button loginButton = null;

    /**
     * On create lifecycle method of the activity. This method initializes the UI elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     * then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.textView);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginBtn);

        loginButton.setOnClickListener( clk ->{
            String password=passwordEditText.getText().toString();
            if(checkPasswordComplexity( password )){
                statusTextView.setText("Your password meets the requirements");
            }else{
                statusTextView.setText("You shall not pass!");
            }
        });
    }

    /**
     * Checks if a given character is a special character from "#$%^&*!@?".
     * @param c the character to check
     * @return true if c is a special character, false otherwise
     */
    private boolean isSpecialCharacter(char c) {
        String specialChars = "#$%^&*!@?";
        return specialChars.contains(String.valueOf(c));
    }

    /**
     * This function checks the complexity of a given password and shows a toast message if it is invalid.
     * The password must have at least one upper case letter, one lower case letter, one digit and one special character from "#$%^&*!@?".
     * The password must also be at least 8 characters long.
     * @param pw the password to check
     * @return Returns the result of the check, return true if the password is acceptable and vice versa.
     */
    public boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        // Check if password length is less than 8
        if (pw.length() < 8) {
            Toast.makeText(this, "Your password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }



        if (foundUpperCase && foundLowerCase && foundNumber && foundSpecial) {
            Toast.makeText(this, "Your password is valid", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            if (!foundUpperCase) {
                Toast.makeText(this, "Your password must contain at least one uppercase letter.", Toast.LENGTH_SHORT).show();
            }
            if (!foundLowerCase) {
                Toast.makeText(this, "Your password must contain at least one lowercase letter.", Toast.LENGTH_SHORT).show();
            }
            if (!foundNumber) {
                Toast.makeText(this, "Your password must contain a number", Toast.LENGTH_SHORT).show();
            }
            if (!foundSpecial) {
                Toast.makeText(this, "Your password must contain a special character", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

}
