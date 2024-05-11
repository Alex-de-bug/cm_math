import React from 'react';
import { useSelector } from 'react-redux';
import { homeSelector } from "../store/slices/HomeSlice.jsx";
import DesmosGraph from "./DesmosGraph.jsx";

const Graph = ({ message }) => {
    // If message is not provided or empty, we avoid processing
    if (!message || message.length === 0) {
        return <div>No data available</div>;
    }

    const data = useSelector(homeSelector);

    const expressions = [];

    // Assuming that the message is an array of point strings
    message.forEach((pointsString, seriesIndex) => {
        pointsString.split('),').forEach((point, pointIndex) => {
            let cleanedPoint = point.replace(/[()]/g, '').trim();
            cleanedPoint = cleanedPoint.replace(/(\d),(\d)/g, '$1.$2'); // Replace comma decimal separators
            const [x, y] = cleanedPoint.split(', ');
            expressions.push({
                id: `point${seriesIndex}${pointIndex}`,
                latex: `(${x}, ${y})`,
                color: 'green',
                style: Desmos.Styles.POINT,
                showLabel: false
            });
        });
    });

    // Adding custom equation if available in Redux state
    if (data.message4) {
        expressions.push({
            id: 'customEquation',
            latex: data.message4,
            color: 'blue',
            lineStyle: Desmos.Styles.SOLID
        });
    }

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
