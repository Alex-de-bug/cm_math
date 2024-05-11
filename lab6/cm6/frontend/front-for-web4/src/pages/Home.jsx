import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {homeSelector, sendTry} from '../store/slices/HomeSlice';
import {
    Alert,
    Box,
    Button,
    Container,
    FormControl,
    FormControlLabel,
    Grid,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Paper,
    Select,
    Snackbar,
    Switch,
    TextField
} from '@mui/material';
import "../styles/Home.css";
import Graph from "../components/Graph.jsx";
import {BlockMath} from 'react-katex';
import 'katex/dist/katex.min.css';
function parseData(data) {
    return data.map(item => {
        const cleanedItem = item.replace(/[()]/g, ''); // Remove parentheses
        const [x, y] = cleanedItem.split(',').map(Number); // Split by comma and convert to Number
        return [x, y];
    });
}

function DataTable({ data }) {
    return (
        <table style={{ margin: 'auto', borderCollapse: 'collapse', width: '80%' }}>
            <thead>
            <tr>
                <th style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>x</th>
                <th style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>y</th>
            </tr>
            </thead>
            <tbody>
            {data.map(([x, y], index) => (
                <tr key={index}>
                    <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>{x}</td>
                    <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>{y}</td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}


function DataComponent({ messages, message5 }) {
    return (
        <>
            {messages.map((message, idx) => (
                message.length > 0 && (
                    <Container key={idx} sx={{ mt: 3 }}>
                        <Paper sx={{p: 3, bgcolor: 'grey'}}>
                            <h2>{message5[idx]}</h2>
                            <br/>
                            <Graph message={message}/>
                            <br/>
                            <DataTable data={parseData(message)}/>
                        </Paper>
                    </Container>
                )
            ))}
        </>
    );
}

function Home() {
    const dispatch = useDispatch();
    const {
        isFetching,
        isError,
        errorMessage,
        message1,
        message2,
        message3,
        message4,
        message5,
        message6
    } = useSelector(homeSelector);
    const [formData, setFormData] = useState({
        x0: 0,
        y0: 0,
        func: 0,
        xn: 3,
        step: 1,
        eps: 0.1
    });
    const [openError, setOpenError] = useState(false);

    // for submit&system
    useEffect(() => {
        if (isError && errorMessage !== "") {
            setOpenError(true);
        }
    }, [errorMessage, isError]);
    const handleCloseSnackbar = () => {
        setOpenError(false);
    };
    const handleFormSubmit = async (e) => {
        e.preventDefault();
        dispatch(sendTry(formData));
    };

    const handleSelectChangeFunc = (event) => {
        const {value} = event.target;
        setFormData(prevFormData => ({
            ...prevFormData,
            func: Number(value),
        }));
    };
    const myArray = ["4x+\\frac{y}{3}", "x^2+y", "y\\cdot cos(x)"];
    const myArrayWithKeys = myArray.map((funct, index) => ({funct, index}));

    const handleStepChange = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            step: value
        }));
    };
    const handleX0Change = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            x0: value
        }));
    };
    const handleY0Change = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            y0: value
        }));
    };
    const handleEpsChange = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            eps: value
        }));
    };
    const handleXNChange = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            xn: value
        }));
    };

    const renderFunctionInput = () => (

        <Box sx={{mt: 2}}>
            <FormControl>
                <InputLabel id="demo-multiple-name-label">f(x, y)</InputLabel>
                <Select
                    labelId="demo-multiple-name-label"
                    id="demo-multiple-name"
                    value={formData.func}
                    onChange={handleSelectChangeFunc}
                    input={<OutlinedInput label="Name"/>}
                >
                    {myArrayWithKeys.map((item) => (
                        <MenuItem
                            key={item.funct}
                            value={item.index}
                        >
                            <BlockMath math={item.funct}/>
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <br/><br/>
            <TextField
                label="x0"
                type="number"
                value={formData.x0}
                onChange={(e) => handleX0Change(parseFloat(e.target.value))}
                variant="outlined"
                sx={{mt: 2, mr: 1}}
            />
            <TextField
                label="y0"
                type="number"
                value={formData.y0}
                onChange={(e) => handleY0Change(parseFloat(e.target.value))}
                variant="outlined"
                sx={{mt: 2, ml: 1}}
            />
            <TextField
                label="xn"
                type="number"
                value={formData.xn}
                onChange={(e) => handleXNChange(parseFloat(e.target.value))}
                variant="outlined"
                sx={{mt: 2, mr: 1}}
            />
            <TextField
                label="Eps"
                type="number"
                value={formData.eps}
                onChange={(e) => handleEpsChange(parseFloat(e.target.value))}
                variant="outlined"
                sx={{mt: 2, ml: 1}}
            />
            <TextField
                label="Step"
                type="number"
                value={formData.step}
                onChange={(e) => handleStepChange(parseFloat(e.target.value))}
                variant="outlined"
                sx={{mt: 2}}
            />
        </Box>
    );

    return (
        <div>
            <Container maxWidth="sm" sx={{mt: 4}}>
                <Paper sx={{p: 4, bgcolor: 'grey',}}>
                    <Box sx={{width: '100%', margin: 'auto', textAlign: 'center'}}>
                        {renderFunctionInput()}
                    </Box>
                    <br/>
                    <form onSubmit={handleFormSubmit}>
                        <Button color="secondary" variant="contained" type="submit" disabled={isFetching}>
                            {isFetching ? 'Sending...' : 'Send'}
                        </Button>
                    </form>
                </Paper>
            </Container>
            <br/>
            <DataComponent messages={[message1, message2, message3]} message5={message5}/>

            <Snackbar open={openError} autoHideDuration={3000} onClose={handleCloseSnackbar}>
                <Alert severity="error" onClose={handleCloseSnackbar}>
                    {errorMessage}
                </Alert>
            </Snackbar>
        </div>
    );
}

export default Home;
