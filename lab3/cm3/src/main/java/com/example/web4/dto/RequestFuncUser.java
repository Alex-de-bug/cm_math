package com.example.web4.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestFuncUser implements Cloneable{
    private double typeFunc; //тип функции
    private double a; //пределы интегрирования
    private double b;
    private double eps; //точность
    private double method; //метод, которым будем считать
    // 1 - Метод прямоугольников левые
    // 2 - Метод прямоугольников правые
    // 3 - Метод прямоугольников средние
    // 4 - Метод трапеций
    // 5 - Метод Симпсона
    @Override
    public RequestFuncUser clone() {
        try {
            return (RequestFuncUser) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Не должно произойти
        }
    }
}
