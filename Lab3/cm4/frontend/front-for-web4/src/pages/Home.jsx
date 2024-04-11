import { useDispatch, useSelector } from "react-redux";
import { homeSelector, sendTry } from "../store/slices/HomeSlice.jsx";
import {useEffect, useState} from "react";
import 'katex/dist/katex.min.css';
import {
    Snackbar,
    Alert,
    Grid,
    InputLabel,
    Select,
    MenuItem,
    OutlinedInput,
} from '@mui/material';
import {
    Button,
    Container,
    FormControl,
    FormLabel,
    Paper
} from "@mui/material";
import { BlockMath } from 'react-katex';
import "../styles/Home.css";

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

function Home() {
    const dispatch = useDispatch();
    const { isFetching, isError, errorMessage, array } = useSelector(homeSelector);
    const [formData, setFormData] = useState({
        typeFunc: 0,
        a: 0,
        b: 0,
        eps: 0.01,
        method: 1
    });
    const [openError, setOpenError] = useState(false);
    const [err, setErr] = useState('');
    const myArray = ["\\int_a^b 3x^3-2x^2+7x+26\\ dx",
        "\\int_a^b 3x^{5}+x^{2}+0.1\\ dx",
        "\\int_a^b \\sin(x)+\\cos(x)\\ dx",
        "\\int_a^b (1-x^2)^{-\\frac{1}{2}}\\ dx",
        "\\int_a^b \\frac{1}{x}\\ dx"];
    const myArrayWithKeys = myArray.map((funct, index) => ({ funct, index }));

    useEffect(() => {
        if(errorMessage!== ""&&isError){
            setOpenError(true);
            setErr(errorMessage);
        }
        homeSelector.isError =false;
    }, [errorMessage, isError]);
    const handleFormSubmit = async (e) => {
        e.preventDefault();
        try {
            await onSubmit(formData);
        } catch (error) {
            console.log('Error submitting form: ' + error.message);
        }
    };
    const onSubmit = (data) => {
        console.log(data);
        dispatch(sendTry(data));
    };


    const handleSelectChangeFunc = (event) => {
        const { value } = event.target;
        setFormData(prevFormData => ({
            ...prevFormData,
            typeFunc: Number(value),
        }));
    };
    const handleChangeType = (event) => {
        const { value } = event.target;
        setFormData(prevFormData => ({
            ...prevFormData,
            method: Number(value),
        }));
    };
    const handleInputChange = (event, propertyName) => {
        const value = parseFloat(event.target.value);
        setFormData({
            ...formData,
            [propertyName]: value
        });
    };

    return (
        <div>
            <Container maxWidth="sm" sx={{mt: 4}}>
                <Paper sx={{p: 4}}>
                    <FormControl>
                        <InputLabel id="demo-multiple-name-label">Function</InputLabel>
                        <Select
                            labelId="demo-multiple-name-label"
                            id="demo-multiple-name"
                            value={formData.typeFunc}
                            onChange={handleSelectChangeFunc}
                            input={<OutlinedInput label="Name"/>}
                            MenuProps={MenuProps}
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
                    <FormControl>
                        <InputLabel id="demo-simple-select-label">Method</InputLabel>
                        <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={formData.method}
                            label="Method"
                            onChange={handleChangeType}
                        >
                            <MenuItem value={1}>Метод прямоугольников левые</MenuItem>
                            <MenuItem value={2}>Метод прямоугольников правые</MenuItem>
                            <MenuItem value={3}>Метод прямоугольников средние</MenuItem>
                            <MenuItem value={4}>Метод трапеций</MenuItem>
                            <MenuItem value={5}>Метод Симпсона</MenuItem>
                        </Select>
                    </FormControl>

                    <FormLabel component="legend">Выберите начальное приближение</FormLabel>
                    <div>
                        <label>a: </label>
                        <input type="number" value={formData.a} onChange={(e) => handleInputChange(e, 'a')}
                               step="0.01"/>
                    </div>
                    <div>
                        <label>b: </label>
                        <input type="number" value={formData.b} onChange={(e) => handleInputChange(e, 'b')}
                               step="0.01"/>
                    </div>
                    <FormLabel component="legend">Выберите погрешность</FormLabel>
                    <div>
                        <label>eps: </label>
                        <input type="number" value={formData.eps} onChange={(e) => handleInputChange(e, 'eps')}
                               step="0.01"/>
                    </div>
                    <br/>

                    <form onSubmit={handleFormSubmit}>
                        <Grid item xs={6}>
                            <Button variant="contained" type="submit" disabled={isFetching}>
                                {isFetching ? 'Sending...' : 'Send'}
                            </Button>
                        </Grid>
                    </form>

                </Paper>
            </Container>
            <br/>
            {array && array.length > 0 && (
                <Container sx={{mt: 3}}>
                    <Paper sx={{p: 3}}>

                            <div>
                                Ответ:
                            </div>
                        <BlockMath math={array}/>

                    </Paper>
                </Container>
            )}
            <Snackbar open={openError} autoHideDuration={3000} onClose={() => setOpenError(false)}>
                <Alert severity="error" onClose={() => setOpenError(false)}>
                    {err}
                </Alert>
            </Snackbar>
        </div>
    );

}

export default Home;


