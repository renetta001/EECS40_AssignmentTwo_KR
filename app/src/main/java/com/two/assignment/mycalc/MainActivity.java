package com.two.assignment.mycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//public class MainActivity extends AppCompatActivity {

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity {
    // IDs of all the numeric buttons
    private int[] numbers = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
    // IDs of all the operator buttons
    private int[] operators = {R.id.buttonadd, R.id.buttonsubtract, R.id.buttonMultiply, R.id.buttondivide,R.id.buttonpercent,R.id.buttonback};
    // TextView used to display the output
    private TextView Screen;
    // Represent whether the lastly pressed key is numeric or not
    private boolean lastpressed;
    // Represent that current state is in error or not
    private boolean error;
    // If true, do not allow to add another DOT
    private boolean lastdecimal;
    //Last entryin calculator
    //private String Lastentry;
   // StringBuffer sb = new StringBuffer(40);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the TextView
        this.Screen = (TextView) findViewById(R.id.Screen);
        // Find and set OnClickListener to numeric buttons
        setNumericOnClickListener();
        // Find and set OnClickListener to operator buttons, equal button and decimal point button
        setOperatorOnClickListener();
    }

    /**
     * Find and set OnClickListener to numeric buttons.
     */
    private void setNumericOnClickListener() {
        // Create a common OnClickListener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View s) {
                // Just append/set the text of clicked button
                Button button = (Button) s;
                if (error) {
                    // If current state is Error, replace the error message
                    Screen.setText(button.getText());
                    error = false;
                } else {
                    // If not, already there is a valid expression so append to it
                    Screen.append(button.getText());
                   // Lastentry = sb.append(button.getText()).toString();
                }
                // Set the flag
                lastpressed = true;
            }
        };
        // Assign the listener to all the numeric buttons
        for (int id : numbers) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * Find and set OnClickListener to operator buttons, equal button and decimal point button.
     */
    private void setOperatorOnClickListener() {
        // Create a common OnClickListener for operators
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                // If the current state is Error do not append the operator
                // If the last input is number only, append the operator
                if (lastpressed && !error) {
                    Button button = (Button) screen;
                    Screen.append(button.getText());
                   // Lastentry = sb.append(button.getText()).toString();
                    lastpressed = false;
                    lastdecimal = false;    // Reset the DOT flag
                }
            }
        };
        // Assign the listener to all the operator buttons
        for (int id : operators) {
            findViewById(id).setOnClickListener(listener);
        }
        // Decimal point
        findViewById(R.id.buttondecimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                if (lastpressed && !error && !lastdecimal) {
                    Screen.append(".");
                   // Lastentry = sb.append(".").toString();
                    lastpressed = false;
                    lastdecimal = true;
                }
            }
        });

        // AllClear button
        findViewById(R.id.buttonclear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                Screen.setText("");  // Clear the screen
                // Reset all the states and flags
                lastpressed = false;
                error = false;
                lastdecimal = false;
            }
        });

        // Clear button
        findViewById(R.id.buttonclear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                //Button button = (Button) v;

                Screen.setText("");  // Clear the screen
                // Reset all the states and flags
                lastpressed = false;
                error = false;
                lastdecimal = false;
            }
        });

        // Equal button
        findViewById(R.id.buttonequal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                Result();
            }
        });
    }

    /**
     * Logic to calculate the solution.
     */
    private void Result() {
        // If the current state is error, nothing to do.
        // If the last input is a number only, solution can be found.
        if (lastpressed && !error) {
            // Read the expression
            String txt = Screen.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                Screen.setText(Double.toString(result));
                lastdecimal = true; // Result contains a decimal
            } catch (ArithmeticException ex) {
                // Display an error message
                Screen.setText("Error");
                error = true;
                lastpressed = false;
            }
        }
    }
}