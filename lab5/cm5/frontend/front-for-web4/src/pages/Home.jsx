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
        val: 0,
        points: Array.from({length: 3}, () => ({x: '', y: ''})),
        func: 0,
        range: {from: 0, to: 10},
        step: 1,
        inputType: 'points'
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

    // for points
    const handleAddPoint = () => {
        setFormData(prevFormData => ({
            ...prevFormData,
            points: [...prevFormData.points, {x: '', y: ''}]
        }));
    };
    const handleRemovePoint = (index) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            points: prevFormData.points.filter((_, i) => i !== index)
        }));
    };
    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();

            reader.onload = (e) => {
                const content = e.target.result;
                const pointsFromFile = content.trim().split('\n').map(line => {
                    const parts = line.split(',');  // Safely access parts of the line
                    const x = parseFloat(parts[0]);
                    const y = parseFloat(parts[1]);
                    if (!isNaN(x) && !isNaN(y)) {
                        return {x, y};
                    } else {
                        return null;
                    }
                }).filter(point => point !== null);

                if (pointsFromFile.length > 0) {
                    setFormData(prevFormData => ({
                        ...prevFormData,
                        points: pointsFromFile,
                    }));
                } else {
                    console.error("Failed to read file!");
                }
            };

            reader.onerror = () => {
                console.error("Failed to read file!");
            };

            reader.readAsText(file);
        } else {
            console.error("No file selected!");
        }
    };

    const updatePoint = (index, coord, value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            points: prevFormData.points.map((point, i) => i === index ? {...point, [coord]: value} : point),
        }));
    };

    const handleInputTypeChange = (event) => {
        const newInputType = event.target.checked ? 'function' : 'points';
        if (newInputType === 'function') {
            setFormData(prevFormData => ({
                ...prevFormData,
                points: Array.from({length: 3}, () => ({x: '', y: ''})),
                function: '',
                range: {from: 0, to: 10},
                step: 1,
                inputType: 'function'
            }));
        } else {
            // Reset function-related data when switching away from 'function' input
            setFormData(prevFormData => ({
                ...prevFormData,
                points: Array.from({length: 3}, () => ({x: '', y: ''})),
                function: '',
                range: {from: 0, to: 0},
                step: 0,
                inputType: 'points'
            }));
        }
    };
    const handleSelectChangeFunc = (event) => {
        const {value} = event.target;
        setFormData(prevFormData => ({
            ...prevFormData,
            func: Number(value),
        }));
    };
    const myArray = ["3sin(x) + cos8x + x",
        "cos(8x)"];
    const myArrayWithKeys = myArray.map((funct, index) => ({funct, index}));

    const handleRangeChange = (value, boundary) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            range: {...prevFormData.range, [boundary]: value}
        }));
    };

    const handleStepChange = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            step: value
        }));
    };
    const handleValChange = (value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            val: value
        }));
    };

    const renderPointsInput = () => (
        <Box sx={{width: '100%', margin: 'auto', textAlign: 'center'}}>
            <Grid container spacing={2} sx={{mt: 2}}>
                {formData.points.map((point, index) => (
                    <React.Fragment key={index}>
                        <Grid item xs={4}>
                            <TextField
                                label="X"
                                type="text"
                                value={point.x}
                                onChange={(e) => updatePoint(index, 'x', e.target.value)}
                                variant="outlined"
                                fullWidth
                            />
                        </Grid>
                        <Grid item xs={4}>
                            <TextField
                                label="Y"
                                type="text"
                                value={point.y}
                                onChange={(e) => updatePoint(index, 'y', e.target.value)}
                                variant="outlined"
                                fullWidth
                            />
                        </Grid>
                        {formData.points.length > 3 && (
                            <Grid item>
                                <Button onClick={() => handleRemovePoint(index)} color="error" variant="contained">
                                    Remove
                                </Button>
                            </Grid>
                        )}
                        {formData.points.length <= 3 && (
                            <Grid item>
                                <Button disabled color="error" variant="contained">
                                    Remove
                                </Button>
                            </Grid>
                        )}
                    </React.Fragment>
                ))}
            </Grid>
            <Button onClick={handleAddPoint} color="secondary" variant="contained" sx={{mt: 2}}>
                Add Point
            </Button>
            <br/>
            <input
                accept=".txt"
                id="contained-button-file"
                multiple
                type="file"
                style={{display: 'none'}}
                onChange={handleFileChange}
            />
            <label htmlFor="contained-button-file">
                <Button color="secondary" variant="contained" component="span" sx={{mt: 2}}>
                    Upload File
                </Button>
            </label>

        </Box>
    );

    const renderFunctionInput = () => (

        <Box sx={{mt: 2}}>
            <FormControl>
                <InputLabel id="demo-multiple-name-label">Func</InputLabel>
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
                label="From"
                type="number"
                value={formData.range.from}
                onChange={(e) => handleRangeChange(parseFloat(e.target.value), 'from')}
                variant="outlined"
                sx={{mt: 2, mr: 1}}
            />
            <TextField
                label="To"
                type="number"
                value={formData.range.to}
                onChange={(e) => handleRangeChange(parseFloat(e.target.value), 'to')}
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
                    <TextField
                        label="Argument"
                        type="number"
                        value={formData.val}
                        onChange={(e) => handleValChange(parseFloat(e.target.value))}
                        variant="outlined"
                        sx={{mt: 2}}
                    />
                    <FormControlLabel
                        control={<Switch checked={formData.inputType === 'function'} onChange={handleInputTypeChange}/>}
                        label="Input by Function"
                        labelPlacement="start"
                    />
                    <Box sx={{width: '100%', margin: 'auto', textAlign: 'center'}}>
                        {formData.inputType === 'points' ? renderPointsInput() : renderFunctionInput()}
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
            {message1[0] && message1[0].length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3, bgcolor: 'grey'}}>
                        <Graph/>
                    </Paper>
                    <Paper sx={{p: 3, bgcolor: 'grey', overflow: 'auto'}}>
                        <div style={{maxWidth: '100%'}}>
                            <BlockMath math={message1[0]}/>
                        </div>
                    </Paper>
                </Container>
            )}
            {message2 && message2.length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3, bgcolor: 'grey'}}>
                        <BlockMath math={message2}/>
                    </Paper>
                </Container>
            )}
            {message3[0] && message3[0].length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3, bgcolor: 'grey'}}>
                        <BlockMath math={message3[0]}/>
                    </Paper>
                </Container>
            )}
            {message4[0] && message4[0].length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3, bgcolor: 'grey'}}>
                        <BlockMath math={message4[0]}/>
                    </Paper>
                </Container>
            )}
            {message5 && message5.length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3, bgcolor: 'grey'}}>
                        <BlockMath math={message5}/>
                    </Paper>
                </Container>
            )}
            {message6 && message6.length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3, bgcolor: 'grey'}}>
                        <BlockMath math={message6}/>
                    </Paper>
                </Container>
            )}
            <Snackbar open={openError} autoHideDuration={3000} onClose={handleCloseSnackbar}>
                <Alert severity="error" onClose={handleCloseSnackbar}>
                    {errorMessage}
                </Alert>
            </Snackbar>
        </div>
    );
}

export default Home;
