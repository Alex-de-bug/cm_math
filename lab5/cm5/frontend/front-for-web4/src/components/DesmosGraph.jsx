import React, {useEffect, useRef} from 'react';

const DesmosGraph = ({expressions}) => {
    const calculatorRef = useRef(null);

    useEffect(() => {
        const calculator = window.Desmos.GraphingCalculator(calculatorRef.current);
        expressions.forEach(expression => {
            calculator.setExpression(expression);
        });

        return () => calculator.destroy(); // Чистка при размонтировании компонента
    }, [expressions]);

    return <div ref={calculatorRef} style={{width: '600px', height: '400px'}}/>;
};

export default DesmosGraph;
