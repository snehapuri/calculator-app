package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private TextView inputDisplay;
    private CalculatorViewModel calculatorViewModel;
    private StringBuilder currentInput = new StringBuilder();
    private StringBuilder expressionBuilder = new StringBuilder();
    private boolean isNewNumber = true;
    private boolean lastWasOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputDisplay = findViewById(R.id.input_display);
        calculatorViewModel = new CalculatorViewModel();


        setupNumberButton(R.id.button0, "0");
        setupNumberButton(R.id.button1, "1");
        setupNumberButton(R.id.button2, "2");
        setupNumberButton(R.id.button3, "3");
        setupNumberButton(R.id.button4, "4");
        setupNumberButton(R.id.button5, "5");
        setupNumberButton(R.id.button6, "6");
        setupNumberButton(R.id.button7, "7");
        setupNumberButton(R.id.button8, "8");
        setupNumberButton(R.id.button9, "9");
        setupNumberButton(R.id.buttonDot, ".");

        setupOperatorButton(R.id.buttonAdd, "+");
        setupOperatorButton(R.id.buttonSubtract, "-");
        setupOperatorButton(R.id.buttonMultiply, "×");
        setupOperatorButton(R.id.buttonDivide, "÷");

        findViewById(R.id.buttonEquals).setOnClickListener(v -> calculateResult());

        findViewById(R.id.buttonClear).setOnClickListener(v -> {
            currentInput.setLength(0);
            expressionBuilder.setLength(0);
            isNewNumber = true;
            lastWasOperator = false;
            inputDisplay.setText("0");
        });
    }

    private void setupNumberButton(int buttonId, String number) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (isNewNumber) {
                currentInput.setLength(0);
                isNewNumber = false;
            }
            if (number.equals(".") && currentInput.toString().contains(".")) {
                return;
            }
            currentInput.append(number);
            if (expressionBuilder.length() == 0) {
                expressionBuilder.append(currentInput);
            } else if (lastWasOperator) {
                expressionBuilder.append(number);
            } else {
                expressionBuilder.setLength(expressionBuilder.length() - currentInput.length() + 1);
                expressionBuilder.append(currentInput);
            }
            lastWasOperator = false;
            inputDisplay.setText(expressionBuilder.toString());
        });
    }

    private void setupOperatorButton(int buttonId, String operator) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (currentInput.length() > 0 && !lastWasOperator) {
                expressionBuilder.append(operator);
                currentInput.setLength(0);
                inputDisplay.setText(expressionBuilder.toString());
                isNewNumber = true;
                lastWasOperator = true;
            } else if (expressionBuilder.length() > 0 && lastWasOperator) {
                expressionBuilder.setLength(expressionBuilder.length() - 1);
                expressionBuilder.append(operator);
                inputDisplay.setText(expressionBuilder.toString());
            }
        });
    }

    private void calculateResult() {
        if (expressionBuilder.length() > 0) {
            String expression = expressionBuilder.toString();
            double result = evaluateExpression(expression);

            String resultStr = formatResult(result);

            inputDisplay.setText(resultStr);
            currentInput.setLength(0);
            currentInput.append(resultStr);
            expressionBuilder.setLength(0);
            expressionBuilder.append(resultStr);
            isNewNumber = true;
            lastWasOperator = false;
        }
    }

    private double evaluateExpression(String expression) {
        String[] numbers = expression.split("[+×÷-]");
        String[] operators = expression.split("[0-9.]+");

        double result = Double.parseDouble(numbers[0]);
        int operatorIndex = 1;

        for (int i = 1; i < numbers.length; i++) {
            String operator = operators[operatorIndex++];
            double number = Double.parseDouble(numbers[i]);

            switch (operator) {
                case "+":
                    result += number;
                    break;
                case "-":
                    result -= number;
                    break;
                case "×":
                    result *= number;
                    break;
                case "÷":
                    if (number != 0) {
                        result /= number;
                    } else {
                        return Double.NaN;
                    }
                    break;
            }
        }
        return result;
    }

    private String formatResult(double result) {
        if (Double.isNaN(result)) {
            return "Error";
        }

        String resultStr = String.valueOf(result);
        if (resultStr.endsWith(".0")) {
            resultStr = resultStr.substring(0, resultStr.length() - 2);
        }
        return resultStr;
    }
}