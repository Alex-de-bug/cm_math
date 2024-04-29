package com.example.web4.math;

import java.util.ArrayList;

public class EndDifference extends Method {


    public EndDifference(Integer size, Double arg, ArrayList<Double> xVal, ArrayList<Double> yVal) {
        super(size, arg, xVal, yVal);
    }

    @Override
    public void calculate() {
        ArrayList<ArrayList<Double>> tempDefy = new ArrayList<>();
        tempDefy.add(new ArrayList<>(yVal));
        for (int i = 1; i < size; i++) {
            ArrayList<Double> column = new ArrayList<>();
            ArrayList<Double> previousColumn = tempDefy.get(i - 1);
            for (int j = 0; j < size - i; j++) {
                column.add(previousColumn.get(j + 1) - previousColumn.get(j));
            }
            tempDefy.add(column);
        }
        this.defy = tempDefy;
    }

    @Override
    public String getNameMethod() {
        return "Конечные разности";
    }

    @Override
    public String getAnswer() {
        if (defy == null || defy.isEmpty()) {
            return "No data calculated.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Table\\ of\\ end\\ difference\\\\\\\\ \\begin{array}{");
        sb.append("|c".repeat(Math.max(0, size + 1)));
        sb.append("c|");
        sb.append("}\n");

        sb.append("\\hline\\ x_i& ");
        for (int j = 0; j < size; j++) {
            sb.append(xVal.get(j));
            if (j < size - 1) {
                sb.append(" & ");
            }
        }
        sb.append(" \\\\\n \\hline\\ ");
        sb.append("y_i& ");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append("\\Delta^{").append(i).append("}y_i& ");
            }

            for (int j = 0; j < size - i; j++) {
                sb.append(formatScientificNotation(defy.get(i).get(j).toString()));
                if (j < size - i - 1) {
                    sb.append(" & ");
                }
            }
            sb.append(" \\\\\n \\hline");
        }

        sb.append("\\end{array}");

        return sb.toString();
    }
}
