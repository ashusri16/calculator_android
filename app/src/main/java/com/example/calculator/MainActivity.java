package com.example.calculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText display;
    private final Pattern pattern = Pattern.compile("[0-9]+");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        display.setShowSoftInputOnFocus(false);

    }

    private void updateText(String appendStr) {
        String lastEle;
        String prevStr = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String rightStr = prevStr.substring(cursorPos);
        int strLen = prevStr.length();
        if (strLen != 0) {
            lastEle = prevStr.substring(strLen - 1);
        } else {
            lastEle = "0";
        }
        Matcher matchLastEle = pattern.matcher(lastEle);
        if (!matchLastEle.matches()) {
            Matcher matchAppEle = pattern.matcher(appendStr);
            if (!matchAppEle.matches()) {
                cursorPos = cursorPos - 1;
            }
        }
        String leftStr = prevStr.substring(0, cursorPos);
        display.setText(String.format("%s%s%s", leftStr, appendStr, rightStr));
        display.setSelection(cursorPos + 1);

    }

    public void zeroBTN(View view) {
        updateText("0");
    }

    public void oneTN(View view) {
        updateText("1");
    }

    public void twoBTN(View view) {
        updateText("2");
    }

    public void threeBTN(View view) {
        updateText("3");
    }

    public void fourBTN(View view) {
        updateText("4");
    }

    public void fiveBTN(View view) {
        updateText("5");
    }

    public void sixBTN(View view) {
        updateText("6");
    }

    public void sevenBTN(View view) {
        updateText("7");
    }

    public void eightBTN(View view) {
        updateText("8");
    }

    public void nineBTN(View view) {
        updateText("9");
    }

    public void addBTN(View view) {

        if (!display.getText().toString().equals(""))
            updateText("+");
    }

    public void subBTN(View view) {
        if (!display.getText().toString().equals(""))
            updateText("-");
    }

    public void mulBTN(View view) {
        if (!display.getText().toString().equals(""))
            updateText("×");
    }

    public void divBTN(View view) {
        if (!display.getText().toString().equals(""))
            updateText("÷");
    }

    public void dotBTN(View view) {
        if (!display.getText().toString().equals(""))
            updateText(".");
    }

    public void clearBTN(View view) {
        display.setText("");
    }

    public void eqlBTN(View view) {
        String exp = display.getText().toString();
        exp = exp.replaceAll("×", "*");
        exp = exp.replaceAll("÷", "/");
        Log.d("Result", exp);
        String result;
        result = String.valueOf(evaluate(exp));
        display.setText(result);
        display.setSelection(result.length());
    }

    public void bkspBTN(View view) {
        int cursorPos = display.getSelectionStart();
        int textLen = display.getText().length();

        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            display.setText(selection);
            display.setSelection(cursorPos - 1);
        }
    }

    public int evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Integer> values = new
                Stack<>();

        Stack<Character> ops = new
                Stack<>();

        for (int i = 0; i < tokens.length; i++) {

            if (tokens[i] == ' ')
                continue;


            if (tokens[i] >= '0' &&
                    tokens[i] <= '9') {
                StringBuilder sbuf = new
                        StringBuilder();

                while (i < tokens.length &&
                        tokens[i] >= '0' &&
                        tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.
                        toString()));
                i--;
            } else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/') {
                while (!ops.empty() &&
                        hasPrecedence(tokens[i],
                                ops.peek()))
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));

                ops.push(tokens[i]);
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(),
                    values.pop(),
                    values.pop()));


        return values.pop();
    }

    public static boolean hasPrecedence(
            char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') ||
                (op2 != '+' && op2 != '-');
    }

    public static int applyOp(char op,
                              int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException(
                            "Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

}