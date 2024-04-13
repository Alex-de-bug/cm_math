package com.example.web4.math.approx;


import com.example.web4.dto.RequestFuncUser;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public abstract class Method {
    private static final Logger logger = LoggerFactory.getLogger(LogApprox.class);

    protected RequestFuncUser requestFuncUser;
    protected Double S = 0.0; // мера отклонения

    protected Double sko;

    protected Double korrelPirs;
    @Getter
    protected Double determ;

    ArrayList<ArrayList<Double>> table = new ArrayList<>(); // x, y, f(x), eps

    public Method(RequestFuncUser requestFuncUser) {
        this.requestFuncUser = requestFuncUser;
    }
    public abstract void calculate();

    public String getAnswer(){
        return getNameMethod()+
                convertTableToKaTeX(table)+
                "\\\\ \\\\ Мера\\ отклонения\\ S\\ =\\ "+formatScientificNotation(S.toString())+
                ";\\\\ СКО\\ =\\ " + formatScientificNotation(sko.toString())+
                ";\\\\ Корреляция\\ =\\ " + formatScientificNotation(korrelPirs.toString())+
                ";\\\\ Коэффициент\\ детерминации\\ =\\ " + formatScientificNotation(determ.toString())+". \\\\"+
                "Функция\\ вида:\\\\ "
                +getStringFun().replace("+-", "-");
    }

    private String convertTableToKaTeX(ArrayList<ArrayList<Double>> table) {
        StringBuilder kaTeXTable = new StringBuilder("\\begin{array}{|c|c|c|c|}\n");

        kaTeXTable.append("\\hline x & y & \\phi(x) & \\varepsilon \\\\ \\hline\n");

        for (ArrayList<Double> row : table) {
            for (int i = 0; i < row.size(); i++) {
                kaTeXTable.append(formatScientificNotation(row.get(i).toString()));
                if (i < row.size() - 1) {
                    kaTeXTable.append(" & ");
                }
            }
            kaTeXTable.append(" \\\\\n \\hline");
        }

        kaTeXTable.append("\\end{array}");

        return kaTeXTable.toString();
    }
    protected String formatScientificNotation(String value) {
        if (value.contains("E")) {
            return value.replace("E", "\\cdot10 ^{") + "}";
        } else {
            return value;
        }
    }
    protected abstract String getNameMethod();
    protected abstract String getStringFun();

}
