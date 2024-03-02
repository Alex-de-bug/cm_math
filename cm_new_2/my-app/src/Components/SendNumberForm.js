

import React, { useState } from 'react';
import axios from 'axios';

function SendNumberForm() {
    const [number, setNumber] = useState(0);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/send', number);
            console.log(response.data);
        } catch (error) {
            console.error('Ошибка отправки числа:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="number"
                value={number}
                onChange={e => setNumber(parseInt(e.target.value, 10))}
            />
            <button type="submit">Отправить</button>
        </form>
    );
}

export default SendNumberForm;
