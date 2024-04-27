import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { homeSelector, sendTry } from "../store/slices/HomeSlice";
import 'katex/dist/katex.min.css';
import {
    Snackbar,
    Alert,
    Grid,
    Typography,
    Slider,
    TextField,
    Box,
    Button,
    Container,
    Paper,
    FormControl,
    InputLabel,
    Select,
    MenuItem
} from '@mui/material';
import { BlockMath } from 'react-katex';
import "../styles/Home.css";
import Graph from "../components/Graph.jsx";

function Home() {
    const dispatch = useDispatch();
    const { isFetching, isError, errorMessage, message1, message2,message3,message4,message5,message6,} = useSelector(homeSelector);
    const [formData, setFormData] = useState({
        sliderValue: 8,
        points: Array.from({ length: 8 }, () => ({ x: '', y: '' })),
        saveToFile: 0,
    });
    const [openError, setOpenError] = useState(false);

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

    const handleSliderChange = (event, newValue) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            sliderValue: newValue,
            points: Array.from({ length: newValue }, (_, index) => prevFormData.points[index] || { x: '', y: '' }),
        }));
    };

    const handleFileChange = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();

        reader.onload = (e) => {
            const content = e.target.result;
            const pointsFromFile = content.trim().split('\n').map(line => {
                const [x, y] = line.split(',').map(Number);
                return { x, y };
            });
            setFormData({
                points: pointsFromFile,
                sliderValue: pointsFromFile.length,
                saveToFile: 0,
            });
        };

        reader.readAsText(file);
    };

    const handleSaveToFileChange = (event, newValue) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            saveToFile: newValue,
        }));
    };

    const updatePoint = (index, coord, value) => {
        setFormData(prevFormData => ({
            ...prevFormData,
            points: prevFormData.points.map((point, i) => i === index ? { ...point, [coord]: value } : point),
        }));
    };

    // 5 лаба
    const [entry, setEntry] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        const data = entry.split(',').map(pair => {
            const [x, y] = pair.trim().split(' ').map(Number);
            return { x, y };
        });
        onSubmit(data);
        setEntry('');
    };

    const handleFileRead = async (e) => {
        const file = e.target.files[0];
        const text = await file.text();
        const data = text.split('\n').map(line => {
            const [x, y] = line.split(',').map(Number);
            return { x, y };
        });
        onLoad(data);
    };

    const [functionType, setFunctionType] = useState('sin');
    const [interval, setInterval] = useState({ start: 0, end: 2 * Math.PI });
    const [numPoints, setNumPoints] = useState(100);

    const generateData = () => {
        const step = (interval.end - interval.start) / numPoints;
        const data = Array.from({ length: numPoints }, (_, i) => {
            const x = interval.start + i * step;
            const y = functionType === 'sin' ? Math.sin(x) : Math.cos(x);
            return { x, y };
        });
        onSubmit(data);
    };

    return (
        <div>
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Paper sx={{ p: 4, bgcolor: 'grey', }} >
                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <TextField
                            label="Enter data (format: x y, x y, ...)"
                            variant="outlined"
                            value={entry}
                            onChange={(e) => setEntry(e.target.value)}
                            fullWidth
                        />
                    </Box>
                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <Button variant="contained" component="label">
                            Upload Data File
                            <input type="file" hidden onChange={handleFileRead} />
                        </Button>
                    </Box>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <FormControl fullWidth>
                            <InputLabel>Function</InputLabel>
                            <Select
                                value={functionType}
                                label="Function"
                                onChange={(e) => setFunctionType(e.target.value)}
                            >
                                <MenuItem value="sin">sin(x)</MenuItem>
                                <MenuItem value="cos">cos(x)</MenuItem>
                            </Select>
                        </FormControl>
                        <TextField
                            label="Interval Start"
                            type="number"
                            value={interval.start}
                            onChange={(e) => setInterval({ ...interval, start: Number(e.target.value) })}
                            fullWidth
                        />
                        <TextField
                            label="Interval End"
                            type="number"
                            value={interval.end}
                            onChange={(e) => setInterval({ ...interval, end: Number(e.target.value) })}
                            fullWidth
                        />
                        <TextField
                            label="Number of Points"
                            type="number"
                            value={numPoints}
                            onChange={(e) => setNumPoints(Number(e.target.value))}
                            fullWidth
                        />
                        <Button onClick={generateData} variant="contained">Generate</Button>
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
            {message1 && message1.length > 0 && (
                <Container sx={{ mt: 3 }}>
                    <Paper sx={{ p: 3, bgcolor: 'grey' }}>
                        <BlockMath math={message1}/>
                        <Graph fu={"0"} points={formData.points}/>
                    </Paper>
                </Container>
            )}
            {message2 && message2.length > 0 && (
                <Container sx={{ mt: 3 }}>
                    <Paper sx={{ p: 3, bgcolor: 'grey' }}>
                        <BlockMath math={message2}/>
                        <Graph fu={"1"} points={formData.points}/>
                    </Paper>
                </Container>
            )}
            {message3 && message3.length > 0 && (
                <Container sx={{ mt: 3 }}>
                    <Paper sx={{ p: 3, bgcolor: 'grey' }}>
                        <BlockMath math={message3}/>
                        <Graph fu={"2"} points={formData.points}/>
                    </Paper>
                </Container>
            )}
            {message4 && message4.length > 0 && (
                <Container sx={{ mt: 3 }}>
                    <Paper sx={{ p: 3, bgcolor: 'grey' }}>
                        <BlockMath math={message4}/>
                        <Graph fu={"3"} points={formData.points}/>
                    </Paper>
                </Container>
            )}
            {message5 && message5.length > 0 && (
                <Container sx={{ mt: 3 }}>
                    <Paper sx={{ p: 3, bgcolor: 'grey' }}>
                        <BlockMath math={message5}/>
                        <Graph fu={"4"} points={formData.points}/>
                    </Paper>
                </Container>
            )}
            {message6 && message6.length > 0 && (
                <Container sx={{ mt: 3 }}>
                    <Paper sx={{ p: 3, bgcolor: 'grey' }}>
                        <BlockMath math={message6}/>
                        <Graph fu={"5"} points={formData.points}/>
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
