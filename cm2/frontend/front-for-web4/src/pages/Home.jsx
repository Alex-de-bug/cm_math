import Table from "../components/Table.jsx";
import { useDispatch, useSelector } from "react-redux";
import { homeSelector, sendTry } from "../store/slices/HomeSlice.jsx";
import { useState } from "react";
import Graph from "../components/Graph.jsx";
import 'katex/dist/katex.min.css';
import {Snackbar, Alert, Grid} from '@mui/material';
import {
    Button,
    Checkbox,
    Container,
    FormControl,
    FormControlLabel,
    FormGroup,
    FormLabel,
    Paper
} from "@mui/material";
import { BlockMath } from 'react-katex';
import "../styles/Home.css";

function Home() {
    const dispatch = useDispatch();
    const { isFetching, errorMessage } = useSelector(homeSelector);
    const [value1, setValue1] = useState(0); // Первое значение
    const [value2, setValue2] = useState(0); // Второе значение
    const [value3, setValue3] = useState(0.01); // Второе значение
    const [formData, setFormData] = useState({
        func: '0',
        metod: '0',
        a: 0,
        b: 0,
        eps: 0,
        file: false
    });
    const [openError, setOpenError] = useState(false);
    const [method, setMethod] = useState('0');



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

    const [func, setFunc] = useState('0');

    // Обработчик изменения значения выпадающего списка
    const handleSelectChangeFunc = (event) => {
        formData.func = event.target.value;
        setFunc(event.target.value);
        if((event.target.value === '3')||(event.target.value === '4')){
            formData.metod = "3";
            setMethod('3');
        }else{
            formData.metod = "0";
        }
        console.log(formData);
    };
    const myArray = ["x^3 - 4.5x^2 - 9.21x - 0.383", "x^3 - x + 4", "sin(x) + 0.1", "\\frac{1}{x^2+y^2+z^2}", "\\frac{1}{x^4+y^2+z^2}"];

    const handleChangeType = (event) => {
        formData.metod = event.target.value;
        setMethod(event.target.value);
        console.log(formData);
    };

    const handleInputChange = (event, propertyName) => {
        const value = parseFloat(event.target.value);
        setFormData({
            ...formData,
            [propertyName]: value
        });
    };

    const handleFileInputChange = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();

        reader.onload = (e) => {
            const fileContents = e.target.result;
            const data = JSON.parse(fileContents); // Предполагая, что файл содержит JSON данные
            setFormData(data);
        };

        reader.readAsText(file);
    };

    const handleFileChange = () => {
        const tmp1 = !formData.file
        formData.file = tmp1;
    };



    return (
        <div>
            <Graph fu={func}/>
            <Container maxWidth="sm" sx={{mt: 4}}>
                <Paper sx={{p: 4}}>
                    <Grid item xs={12}>
                        <FormControl component="fieldset">
                            <FormLabel component="legend">Функция</FormLabel>
                            <BlockMath math={myArray[parseInt(func)]}/>
                            <FormGroup row>
                                {['0', '1', '2', '3', '4'].map((value) => (
                                    <FormControlLabel
                                        key={`r_${value}`}
                                        control={
                                            <Checkbox
                                                name="func"
                                                value={value}
                                                onChange={handleSelectChangeFunc}
                                                checked={func === value}
                                            />
                                        }
                                        label={value}
                                    />
                                ))}
                            </FormGroup>
                        </FormControl>
                    </Grid>
                    <div>
                        {((func === '0') || (func === '1') || (func === '2')) && (
                            <select name={"typeFunction"} value={formData.metod} onChange={handleChangeType}>
                                <option value={"0"}>Метод половинного деления</option>
                                <option value={"1"}>Метод Ньютона</option>
                                <option value={"2"}>Метод простой итерации</option>
                            </select>
                        )}
                        {((func === '3') || (func === '4')) && (
                            <select name={"typeFunction"} value={method} onChange={handleChangeType}>
                                <option value={"3"}>Метод простой итерации</option>
                            </select>
                        )}
                    </div>
                    <p>Выберите начальное приближение</p>
                    <div>
                        <label>a: </label>
                        <input type="number" value={formData.a} onChange={(e) => handleInputChange(e, 'a')}
                               step="0.01" max={50}/>
                    </div>
                    <div>
                        <label>b: </label>
                        <input type="number" value={formData.b} onChange={(e) => handleInputChange(e, 'b')}
                               step="0.01" max={50}/>
                    </div>
                    <p>Выберите погрешность</p>
                    <div>
                        <label>eps: </label>
                        <input type="number" value={formData.eps} onChange={(e) => handleInputChange(e, 'eps')}
                               step="0.01" max={50}/>
                    </div>
                    <p>Нужно ли сорхранить результаты работы в файл</p>
                    <div>
                        <input type="checkbox" onChange={handleFileChange}/>
                    </div>

                    <input type="file" onChange={handleFileInputChange}/>

                    <form onSubmit={handleFormSubmit}>
                        <Grid item xs={6}>
                            <Button variant="contained" type="submit" disabled={isFetching}>
                                {isFetching ? 'Sending...' : 'Send'}
                            </Button>
                        </Grid>
                    </form>
                </Paper>
                <Table/>
            </Container>
            <Snackbar open={openError} autoHideDuration={3000} onClose={() => setOpenError(false)}>
                <Alert severity="error" onClose={() => setOpenError(false)}>
                    {errorMessage}
                </Alert>
            </Snackbar>
            <div>
                {errorMessage && (
                    <div style={{color: 'red', marginTop: '10px'}}>
                        Ошибка: {errorMessage}
                    </div>
                )}
            </div>
        </div>
    );

}

export default Home;


// import Navbar from "../components/Navbar.jsx";
// import Table from "../components/Table.jsx";
// import {useDispatch, useSelector} from "react-redux";
// import {useNavigate} from "react-router-dom";
// import {clearState, deleteTry, getTry, homeSelector, sendTry} from "../store/slices/HomeSlice.jsx";
// import {useEffect, useState} from "react";
// import Decart from "../components/Decart.jsx";
//
// function Home() {
//     const dispatch = useDispatch();
//     const navigate = useNavigate();
//     const { isFetching, isSuccess, isError, errorMessage} = useSelector(
//         homeSelector
//     );
//     const [selectedItemX, setSelectedItemX] = useState();
//     const [selectedItemR, setSelectedItemR] = useState();
//     const [formData, setFormData] = useState({
//         x: '',
//         y: '',
//         r: '0'
//     });
//
//
//     const handleChange = (e) => {
//         const { name, value } = e.target;
//         setFormData(prevState => ({
//             ...prevState,
//             [name]: value
//         }));
//         if(name==="x"){
//             setSelectedItemX(e.target.value);
//         }
//         if(name==="r"){
//             setSelectedItemR(e.target.value);
//         }
//     };
//     const handleFormSubmit = async (e) => {
//         e.preventDefault();
//         try {
//             await onSubmit(formData);
//         } catch (error) {
//             console.log('Error submitting form: ' + error.message);
//         }
//     };
//     const onSubmit = (data) => {
//         console.log(data);
//         dispatch(sendTry(data)).then(() => {
//             dispatch(getTry());
//         });
//     };
//
//     const onDelete = () => {
//         dispatch(deleteTry()).then(() => {
//             dispatch(getTry());
//         });
//     };
//
//
//     useEffect(() => {
//         return () => {
//             dispatch(clearState());
//             dispatch(getTry());
//         };
//     }, [isSuccess]);
//
//     useEffect(() => {
//         if (isError) {
//             dispatch(clearState());
//         }
//
//         if (isSuccess) {
//             dispatch(clearState());
//             navigate('/');
//         }
//     }, [isError, isSuccess]);
//     return (
//         <div>
//             <Navbar />
//             <Decart r={formData.r}/>
//             <div>
//                 <form onSubmit={handleFormSubmit}>
//                     <label>
//                         Координата по оси X:
//                         <div>
//                             {['-3','-2','-1','0','1','2','3','4','5'].map((value) => (
//                                 <label key={`x_${value}`}>
//                                     <input
//                                         type="checkbox"
//                                         name="x"
//                                         value={value}
//                                         onChange={handleChange}
//                                         checked={selectedItemX === value}
//                                     />
//                                     {value}
//                                 </label>
//                             ))}
//                         </div>
//                     </label>
//                     <label>
//                         Координата по оси Y:
//                         <input
//                             type="number"
//                             name="y"
//                             value={formData.y}
//                             onChange={handleChange}
//                             min={-5}
//                             max={5}
//                             required
//                         />
//                     </label>
//                     <br/>
//                     <label>
//                         Размер графика R:
//                         <div>
//                             {['0','1','2','3','4','5'].map((value) => (
//                                 <label key={`r_${value}`}>
//                                     <input
//                                         type="checkbox"
//                                         name="r"
//                                         value={value}
//                                         onChange={handleChange}
//                                         checked={selectedItemR === value}
//                                     />
//                                     {value}
//                                 </label>
//                             ))}
//                         </div>
//                     </label>
//                     {isFetching ? (
//                         <button type="submit" disabled={isFetching}>
//                             Sending...
//                         </button>
//                     ) : (
//                         <button type="submit">
//                             Send
//                         </button>
//                     )}
//                 </form>
//                 {isFetching ? (
//                     <button disabled={isFetching}>
//                         Delete...
//                     </button>
//                 ) : (
//                     <button onClick={onDelete}>
//                         Delete
//                     </button>
//                 )}
//                 {errorMessage}
//             </div>
//             <Table />
//         </div>
//     );
// }
//
// export default Home;
