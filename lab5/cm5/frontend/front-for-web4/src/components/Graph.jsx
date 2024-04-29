import React from 'react';
import {useSelector} from 'react-redux';
import {homeSelector} from "../store/slices/HomeSlice.jsx";
import DesmosGraph from "./DesmosGraph.jsx";

const Graph = () => {
    const {message1, message3, message4} = useSelector(homeSelector);
    const originalXValues = message1[1];
    const yValues = message1[2];

    const functionStringN = message3[1].replace(/,/g, '.');
    const functionStringG = message4[1].replace(/,/g, '.');

    // Обновим expressions для включения точек
    const expressions = [
        {
            id: 'Ньютон',
            latex: `y=${functionStringN}`, // Убедитесь, что выражение начинается с y=
            color: 'red',
            lineStyle: Desmos.Styles.SOLID
        },
        {
            id: 'Гаусс',
            latex: `y=${functionStringG}`, // Убедитесь, что выражение начинается с y=
            color: 'blue',
            lineStyle: Desmos.Styles.SOLID
        },
        // Для точек создаем отдельные выражения
        ...originalXValues.map((x, index) => ({
            id: `point${index}`,
            latex: `(${x}, ${yValues[index]})`, // Каждая точка как отдельное выражение
            color: 'green',
            style: Desmos.Styles.POINT,
            showLabel: false // Опционально, можно отключить отображение меток
        }))
    ];

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
            <DesmosGraph expressions={expressions}/>
        </div>
    );
};

export default Graph;
