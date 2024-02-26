import {useEffect, useRef, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {getTry, homeSelector, sendTry} from "../store/slices/HomeSlice.jsx";
import "../styles/Home.css"

const CanvasGraph = ({ r }) => {
    const dispatch = useDispatch();
    const canvasRef = useRef(null);
    const {array} = useSelector(
        homeSelector
    );
    const [formData, setFormData] = useState({
        x: '',
        y: '',
        r: '',
        token: localStorage.getItem('token')
    });



    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        drawG(r,ctx, canvas);

        for (let i = 0; i < array.length; i++) {
            const currentData = array[i];
            const x = parseFloat(currentData.x);
            const y = parseFloat(currentData.y);
            const r1 = parseFloat(currentData.r);
            if(r==r1){
                if(window.innerWidth<550){
                    drawPointe(
                        x * 14 + 110,
                        (-y * 14 + 110),
                        x,
                        y, ctx, canvas
                    );
                }else{
                    drawPointe(
                        x * 40 + 250,
                        (-y * 40 + 250),
                        x,
                        y, ctx, canvas
                    );
                }
            }
            // if(window.innerWidth<550){
            //     drawPointe(
            //         x * 14 + 110,
            //         (-y * 14 + 110),
            //         x,
            //         y, ctx, canvas
            //     );
            // }else{
            //     drawPointe(
            //         x * 40 + 250,
            //         (-y * 40 + 250),
            //         x,
            //         y, ctx, canvas
            //     );
            // }

        }
    }, [r, array]);

    // Перерисовываем canvas при изменении размеров окна
    const handleResize = () => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        drawG(r,ctx, canvas);
        for (let i = 0; i < array.length; i++) {
            const currentData = array[i];
            const x = parseFloat(currentData.x); // Преобразование строки в число
            const y = parseFloat(currentData.y); // Преобразование строки в число

            const r1 = parseFloat(currentData.r);
            if(r==r1){
                if(window.innerWidth<550){
                    drawPointe(
                        x * 14 + 110,
                        (-y * 14 + 110),
                        x,
                        y, ctx, canvas
                    );
                }else{
                    drawPointe(
                        x * 40 + 250,
                        (-y * 40 + 250),
                        x,
                        y, ctx, canvas
                    );
                }
            }

            // if(window.innerWidth<550){
            //     drawPointe(
            //         x * 14 + 110,
            //         (-y * 14 + 110),
            //         x,
            //         y, ctx, canvas
            //     );
            // }else{
            //     drawPointe(
            //         x * 40 + 250,
            //         (-y * 40 + 250),
            //         x,
            //         y, ctx, canvas
            //     );
            // }

        }
    };

    window.addEventListener('resize', handleResize);



    const handleCanvasClick = (event) => {
        const canvas = canvasRef.current;

        let mouseX = parseFloat(((event.clientX - canvas.getBoundingClientRect().left - 250) / 40).toFixed(2));
        let mouseY = parseFloat(-((event.clientY - canvas.getBoundingClientRect().top - 250) / 40).toFixed(2));

        if(window.innerWidth<550){
            mouseX = parseFloat(((event.clientX - canvas.getBoundingClientRect().left - 110) / 14).toFixed(2));
            mouseY = parseFloat(-((event.clientY - canvas.getBoundingClientRect().top - 110) / 14).toFixed(2));
        }

        formData.x = mouseX;
        formData.y = mouseY;
        formData.r = r;
        dispatch(sendTry(formData)).then(() => {
            dispatch(getTry());
        });

    };


    function drawG(r, ctx, canvas){
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        canvas.width = 500;
        canvas.height = 500;
        let radiusSpec = 200*r/5;
        let radius = 200;

        if(window.innerWidth<550){
            canvas.width = 220;
            canvas.height = 220;
            radiusSpec = 80*r/5;
            radius = 70;
        }

        const centerX = (canvas.width) / 2;
        const centerY = canvas.height / 2;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        const pointRadius = 3;

        ctx.strokeStyle = "#2e2e2e";
        ctx.lineWidth = 2;
        ctx.fillStyle = "#ffffff";


        ctx.fillStyle = "#FF69B4";
        // sign x axis
        ctx.fillText("X", canvas.width - 90, centerY + 30);
        ctx.fillStyle = "#FF69B4";
        // sign y axis
        ctx.fillText("Y", centerX - 30, 90);


        ctx.fillStyle = 'blue';
        ctx.strokeStyle = '#2e2e2e';
        ctx.lineWidth = 1;

        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radiusSpec/2, 2/2*Math.PI, 3/2*Math.PI); // Изменение углов
        ctx.fillStyle = "rgba(160,105,255,0.66)";
        ctx.fill();
        ctx.stroke();

        // triangle
        ctx.beginPath();
        ctx.moveTo(centerX + radiusSpec, centerY);
        ctx.lineTo(centerX , centerY  +radiusSpec/2);
        ctx.lineTo(centerX, centerY);
        ctx.fillStyle = "rgba(255,173,105,0.66)";
        ctx.fill();
        ctx.stroke();

        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX, centerY - radiusSpec);
        ctx.lineTo(centerX + radiusSpec, centerY - radiusSpec);
        ctx.lineTo(centerX + radiusSpec, centerY);
        ctx.closePath();
        ctx.fillStyle = "rgba(255, 105, 180, 0.66)";
        ctx.fill();
        ctx.stroke();


        ctx.strokeStyle = "#2e2e2e";
        ctx.lineWidth = 2;
        ctx.fillStyle = "#FF69B4";


        // x
        ctx.beginPath();
        ctx.moveTo(30, centerY);
        ctx.lineTo(canvas.width -30, centerY);
        ctx.stroke();

        // x arrow
        ctx.beginPath();
        ctx.moveTo(canvas.width - 50, centerY - 14);
        ctx.lineTo(canvas.width - 30, centerY);
        ctx.lineTo(canvas.width - 50, centerY + 14);
        ctx.fillStyle = '#2e2e2e';
        ctx.fill();

        // x arrow
        ctx.beginPath();
        ctx.moveTo(canvas.width - 50, centerY - 10);
        ctx.lineTo(canvas.width - 30, centerY);
        ctx.lineTo(canvas.width - 50, centerY + 10);
        ctx.fillStyle = '#FF69B4';
        ctx.fill();

        // y axis
        ctx.beginPath();
        ctx.moveTo(centerX, 30);
        ctx.lineTo(centerX, canvas.height-30);
        ctx.stroke();

        // y arrow
        ctx.beginPath();
        ctx.moveTo(centerX - 14, 50);
        ctx.lineTo(centerX, 30);
        ctx.lineTo(centerX + 14, 50);
        ctx.fillStyle = '#2e2e2e';
        ctx.fill();

        // y arrow
        ctx.beginPath();
        ctx.moveTo(centerX - 10, 50);
        ctx.lineTo(centerX, 30);
        ctx.lineTo(centerX + 10, 50);
        ctx.fillStyle = '#FF69B4';
        ctx.fill();

        // ox
        ctx.beginPath();
        ctx.arc(centerX + radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX + 4/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX + 3/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX + 2/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX + 1/5*radius, centerY, pointRadius, 0, 2 * Math.PI);

        ctx.arc(centerX - radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX - 4/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX - 3/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX - 2/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX - 1/5*radius, centerY, pointRadius, 0, 2 * Math.PI);
        ctx.fillStyle = '#2e2e2e';
        ctx.fill();

        ctx.font = '12px Arial';
        ctx.fillStyle = '#2e2e2e';

        ctx.fillText('5', centerX + (5/5) * radius + pointRadius, centerY + 20);
        ctx.fillText('4', centerX + (4/5) * radius + pointRadius, centerY + 20);
        ctx.fillText('3', centerX + (3/5) * radius + pointRadius, centerY + 20);
        ctx.fillText('2', centerX + (2/5) * radius + pointRadius, centerY + 20);
        ctx.fillText('1', centerX + (1/5) * radius + pointRadius, centerY + 20);

        ctx.fillText('-5', centerX - (5/5) * radius - pointRadius, centerY + 20);
        ctx.fillText('-4', centerX - (4/5) * radius - pointRadius, centerY + 20);
        ctx.fillText('-3', centerX - (3/5) * radius - pointRadius, centerY + 20);
        ctx.fillText('-2', centerX - (2/5) * radius - pointRadius, centerY + 20);
        ctx.fillText('-1', centerX - (1/5) * radius - pointRadius, centerY + 20);

        // точки оу
        ctx.beginPath();
        ctx.arc(centerX, centerY + radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY + (4/5) * radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY + (3/5) * radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY + (2/5) * radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY + (1/5) * radius, pointRadius, 0, 2 * Math.PI);

        ctx.arc(centerX, centerY - radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY - (4/5) * radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY - (3/5) * radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY - (2/5) * radius, pointRadius, 0, 2 * Math.PI);
        ctx.arc(centerX, centerY - (1/5) * radius, pointRadius, 0, 2 * Math.PI);

        ctx.fillStyle = '#2e2e2e';
        ctx.fill();

        ctx.font = '12px Arial';
        ctx.fillStyle = '#2e2e2e';
        ctx.fillText('-5', centerX + 20, centerY + (5/5) * radius);
        ctx.fillText('-4', centerX + 20, centerY + (4/5) * radius);
        ctx.fillText('-3', centerX + 20, centerY + (3/5) * radius);
        ctx.fillText('-2', centerX + 20, centerY + (2/5) * radius);
        ctx.fillText('-1', centerX + 20, centerY + (1/5) * radius);

        ctx.fillText('5', centerX + 20, centerY - (5/5) * radius);
        ctx.fillText('4', centerX + 20, centerY - (4/5) * radius);
        ctx.fillText('3', centerX + 20, centerY - (3/5) * radius);
        ctx.fillText('2', centerX + 20, centerY - (2/5) * radius);
        ctx.fillText('1', centerX + 20, centerY - (1/5) * radius);
    }

    function drawPointe(x, y, xt, yt, ctx) {
        xt = Number.parseFloat(xt);
        yt = Number.parseFloat(yt);
        let kode = validate(xt, yt, r);
        let color = '#2e2e2e';
        let colore = '#FF69B4';
        if(kode){
            colore = '#ffffff';
            color = '#FF69B4';
        }

        ctx.beginPath();
        ctx.arc(x, y, 3+1, 0, 2 * Math.PI);
        ctx.fillStyle = colore;
        ctx.fill();

        ctx.beginPath();
        ctx.arc(x, y, 3, 0, 2 * Math.PI);
        ctx.fillStyle = color;
        ctx.fill();

        ctx.font = '12px Arial';
        ctx.fillStyle = '#000000';
        ctx.fillText('('+xt+'; '+yt+')', x + 10, y);
    }
    function validate(x, y, r){
        if(r===0){
            return false;
        }
        return ((x >= 0 && y <= 0) && (y >= x/2 - r/2)) || ((x >= 0 && y >= 0) && (x <= r && y <= r))||(x <= 0 && y >= 0) && (x*x + y*y <= r*r/4);

    }

    return (
            <canvas
                ref={canvasRef}
                width={150}
                height={150}
                onClick={handleCanvasClick}
            ></canvas>
    );
};

export default CanvasGraph;
