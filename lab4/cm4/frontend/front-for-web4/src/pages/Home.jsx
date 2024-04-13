import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { homeSelector, sendTry } from "../store/slices/HomeSlice";
import 'katex/dist/katex.min.css';
import {
    Snackbar, Alert, Grid, Typography, Slider, TextField, Box, Button, Container, Paper
} from '@mui/material';
import { BlockMath } from 'react-katex';
import "../styles/Home.css";
import Graph from "../components/Graph.jsx";
import {grey} from "@mui/material/colors";

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

    return (
        <div>

            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Paper sx={{ p: 4, bgcolor: 'grey', }} >
                    <Box sx={{ width: 300, margin: 'auto', textAlign: 'center' }}>
                        <Typography id="input-slider" gutterBottom>
                            Number of Points
                        </Typography>
                        <Slider
                            color="secondary"
                            aria-label="Points"
                            valueLabelDisplay="auto"
                            step={1}
                            marks
                            min={8}
                            max={12}
                            value={formData.sliderValue}
                            onChange={handleSliderChange}
                        />
                        <input
                            accept=".txt"
                            id="contained-button-file"
                            multiple
                            type="file"
                            style={{ display: 'none' }}
                            onChange={handleFileChange}
                        />
                        <label htmlFor="contained-button-file">
                            <Button color="secondary" variant="contained" component="span" sx={{ mt: 2 }}>
                                Upload File
                            </Button>
                        </label>
                        <Grid container spacing={2} sx={{ mt: 2 }}>
                            {formData.points.slice(0, formData.sliderValue).map((point, index) => (
                                <React.Fragment key={index}>
                                    <Grid item xs={6}>
                                        <TextField
                                            label="X"
                                            type="text" // Меняем тип обратно на text
                                            value={point.x}
                                            onChange={(e) => {
                                                const value = e.target.value;
                                                const number = parseFloat(value);

                                                // Проверяем, является ли преобразованное значение числом и соответствует ли исходное значение формату числа
                                                if (!isNaN(number) && value.match(/^-?\d*\.?\d*$/)) {
                                                    updatePoint(index, 'x', number);
                                                }
                                            }}
                                            variant="outlined"
                                            fullWidth
                                        />
                                    </Grid>
                                    <Grid item xs={6}>
                                        <TextField
                                            label="Y"
                                            type="text" // Аналогично для Y
                                            value={point.y}
                                            onChange={(e) => {
                                                const value = e.target.value;
                                                const number = parseFloat(value);

                                                if (!isNaN(number) && value.match(/^-?\d*\.?\d*$/)) {
                                                    updatePoint(index, 'y', number);
                                                }
                                            }}
                                            variant="outlined"
                                            fullWidth

                                        />
                                    </Grid>
                                </React.Fragment>
                            ))}
                        </Grid>
                        <Typography sx={{ mt: 4 }} gutterBottom>
                            Save to File?
                        </Typography>
                        <Slider
                            color="secondary"
                            aria-label="SaveToFile"
                            valueLabelDisplay="auto"
                            step={1}
                            marks={[{value: 0, label: 'No'}, {value: 1, label: 'Yes'}]}
                            min={0}
                            max={1}
                            value={formData.saveToFile}
                            onChange={handleSaveToFileChange}
                        />
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
