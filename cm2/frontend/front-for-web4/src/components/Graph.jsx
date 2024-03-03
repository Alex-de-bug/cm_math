import React, { useState, useRef, useEffect } from 'react';

const Graph = ({fu}) => {
    const width = 900, height =900
    const canvasRef = useRef(null);
    var [bounds, setBounds] = useState({
        xmin: -10,
        xmax: 10,
        ymin: -10,
        ymax: 10,
    });
    const f = (x) => {
        switch (fu) {
            case "0":
                return x*x*x - 4.5*x*x - 9.21*x - 0.383;
            case "1":
                return x*x*x - x + 4;
            case "2":
                return Math.sin(x) + 0.1;
            case "3":
                return x;
            case "4":
                return 5 * (x * x) + 4 * x - 7;
            default:
                return Math.random() * 100;
        }
    };
    const g = (x) => {
        switch (fu) {
            case "3":
                return (x * x);
            case "4":
                return 3 * (x * x) + 12 * x +3;
            default:
                return Math.random() * 100;
        }
    };

    useEffect(() => {
        const canvas = canvasRef.current;
        const handleMouseMove = (event) => {
            const rect = canvas.getBoundingClientRect();
            const scaleX = width / (bounds.xmax - bounds.xmin);
            const scaleY = height / (bounds.ymax - bounds.ymin);
            const canvasX = event.clientX - rect.left;
            const canvasY = event.clientY - rect.top;
            const graphX = (canvasX / scaleX) + bounds.xmin;
            const graphY = bounds.ymax - (canvasY / scaleY);
            setCursorPosition({ x: graphX, y: graphY });
        };

        // Проверка, что границы равны по модулю
        const boundsAreValid = Math.abs(bounds.xmin) === Math.abs(bounds.xmax) && Math.abs(bounds.ymin) === Math.abs(bounds.ymax);

        if (boundsAreValid) {
            canvas.addEventListener('mousemove', handleMouseMove);
        }else{
            setCursorPosition({ x: 0, y: 0 });
        }

        return () => {
            if (boundsAreValid) {
                canvas.removeEventListener('mousemove', handleMouseMove);
            }
        };
    }, [bounds]);

    useEffect(() => {
        console.log(fu);
        const canvas = canvasRef.current;
        const context = canvas.getContext('2d');
        drawGraph(context, bounds);
    }, [fu]);

    const drawGraph = (ctx, bounds) => {
        const scaleX = width / (bounds.xmax - bounds.xmin);
        const scaleY = height / (bounds.ymax - bounds.ymin);

        const offsetX = width / 2;
        const offsetY = height / 2;

        ctx.clearRect(0, 0, width, height); // Очищаем canvas

        // Рисуем оси
        ctx.beginPath();
        ctx.moveTo(0, offsetY);
        ctx.lineTo(width, offsetY);
        ctx.moveTo(offsetX, 0);
        ctx.lineTo(offsetX, height);
        ctx.strokeStyle = 'black';
        ctx.stroke();

        // Функция для добавления делений и значений на оси
        const drawAxisLabels = () => {
            const tickSize = 5;
            let stepX, stepY;

            // Определяем шаг на основе диапазона значений
            if (bounds.xmin > -6 && bounds.xmax < 6) {
                stepX = 0.2;
                stepY = 2; // Пример, можно настроить отдельно для оси Y
            } else {
                stepX = (bounds.xmax - bounds.xmin) / 20; // Более крупный шаг для больших диапазонов
                stepY = (bounds.ymax - bounds.ymin) / 20; // Аналогично для оси Y
            }

            ctx.font = '10px Arial';
            ctx.fillStyle = 'black';
            ctx.textAlign = 'center';

            // Деления и значения по оси X
            for (let xValue = -30; xValue <= 30; xValue += stepX) {
                const x = offsetX + (xValue * scaleX);
                ctx.moveTo(x, offsetY - tickSize);
                ctx.lineTo(x, offsetY + tickSize);
                ctx.fillText(xValue.toFixed(1), x, offsetY + tickSize + 10);
            }

            // Деления и значения по оси Y
            for (let yValue = -50; yValue <= 50; yValue += stepY) {
                if (Math.abs(yValue)>=1) { // Подписываем только целые числа
                    const y = offsetY - (yValue * scaleY);
                    ctx.moveTo(offsetX - tickSize, y);
                    ctx.lineTo(offsetX + tickSize, y);
                    ctx.fillText(yValue.toFixed(1), offsetX - tickSize - 10, y + 3);
                }
            }

            ctx.stroke();
        };

        drawAxisLabels();

        // Рисуем график
        ctx.beginPath();
        ctx.strokeStyle = 'red';
        for (let i = -30; i <= 30; i += (bounds.xmax - bounds.xmin) / width) {
            const x = offsetX + (i * scaleX);
            const y = offsetY - (f(i) * scaleY);
            if (i === bounds.xmin) {
                ctx.moveTo(x, y);
            } else {
                ctx.lineTo(x, y);
            }
        }
        ctx.stroke();
        if((fu === "3")||(fu === "4")){
            ctx.beginPath();
            ctx.strokeStyle = 'red';
            for (let i = -30; i <= 30; i += (bounds.xmax - bounds.xmin) / width) {
                const x = offsetX + (i * scaleX);
                const y = offsetY - (g(i) * scaleY);
                if (i === bounds.xmin) {
                    ctx.moveTo(x, y);
                } else {
                    ctx.lineTo(x, y);
                }
            }
            ctx.stroke();
        }
    };


    useEffect(() => {
        const canvas = canvasRef.current;
        const context = canvas.getContext('2d');
        drawGraph(context, bounds);
    }, [bounds]);

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        let newValue = parseFloat(value);

        // Применяем ограничения для xmin и xmax
        if (name === 'xmin' && (newValue < -30 || newValue > -3)) {
            return; // Игнорируем ввод, если он вне допустимого диапазона
        } else if (name === 'xmax' && (newValue < 3 || newValue > 30)) {
            return; // Аналогично для xmax
        }
        // Применяем ограничения для xmin и xmax
        if (name === 'ymin' && (newValue < -50 || newValue > -3)) {
            return; // Игнорируем ввод, если он вне допустимого диапазона
        } else if (name === 'ymax' && (newValue < 3 || newValue > 50)) {
            return; // Аналогично для xmax
        }

        setBounds({
            ...bounds,
            [name]: newValue,
        });
    };

    const [cursorPosition, setCursorPosition] = useState({ x: 0, y: 0 }); // Состояние для хранения координат курсора

    return (
        <>
            <canvas ref={canvasRef} width={width} height={height}/>
            <div>
                <label>
                    xmin:
                    <input
                        type="number"
                        name="xmin"
                        value={bounds.xmin}
                        onChange={handleInputChange}
                        max={-3}
                        min={-30}
                    />
                </label>
                <label>
                    xmax:
                    <input
                        type="number"
                        name="xmax"
                        value={bounds.xmax}
                        onChange={handleInputChange}
                        max={30}
                        min={3}
                    />
                </label>
                <br/>
                <label>
                    ymin:
                    <input
                        type="number"
                        name="ymin"
                        value={bounds.ymin}
                        onChange={handleInputChange}
                        max={-3}
                        min={-50}
                    />
                </label>
                <label>
                    ymax:
                    <input
                        type="number"
                        name="ymax"
                        value={bounds.ymax}
                        onChange={handleInputChange}
                        max={50}
                        min={3}
                    />
                </label>
            </div>
            <p>X: {cursorPosition.x.toFixed(2)}, Y: {cursorPosition.y.toFixed(2)}</p>
        </>
    );
};

export default Graph;
