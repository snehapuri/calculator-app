package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalculatorViewModel extends ViewModel {
    private final CalculatorModel calculatorModel = new CalculatorModel();
    private final MutableLiveData<String> result = new MutableLiveData<>("");

    public LiveData<String> getResult() {
        return result;
    }

    public void performOperation(double operand1, double operand2, String operation) {
        double calculationResult = 0;
        try {
            switch (operation) {
                case "+":
                    calculationResult = calculatorModel.add(operand1, operand2);
                    break;
                case "-":
                    calculationResult = calculatorModel.subtract(operand1, operand2);
                    break;
                case "*":
                    calculationResult = calculatorModel.multiply(operand1, operand2);
                    break;
                case "/":
                    calculationResult = calculatorModel.divide(operand1, operand2);
                    break;
            }
            result.setValue(String.valueOf(calculationResult));
        } catch (Exception e) {
            result.setValue("Error: " + e.getMessage());
        }
    }
}

