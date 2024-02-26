import Navbar from "../components/Navbar.jsx";
import Table from "../components/Table.jsx";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { clearState, deleteTry, getTry, homeSelector, sendTry } from "../store/slices/HomeSlice.jsx";
import { useEffect, useState } from "react";
import Decart from "../components/Decart.jsx";
import {Snackbar, Alert, Grid} from '@mui/material';
import {
    Button,
    Checkbox,
    Container,
    FormControl,
    FormControlLabel,
    FormGroup,
    FormLabel,
    Input,
    InputLabel,
    Paper
} from "@mui/material";
import "../styles/Home.css";

function Home() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { isFetching, isSuccess, isError, errorMessage } = useSelector(homeSelector);
    const [selectedItemX, setSelectedItemX] = useState();
    const [selectedItemR, setSelectedItemR] = useState();
    const [formData, setFormData] = useState({
        x: '',
        y: '',
        r: '0'
    });
    const [openError, setOpenError] = useState(false); // Состояние для управления видимостью ошибки


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
        if (name === "x") {
            setSelectedItemX(e.target.value);
        }
        if (name === "r") {
            setSelectedItemR(e.target.value);
        }
    };

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
        dispatch(sendTry(data)).then(() => {
            dispatch(getTry());
        });
    };

    const onDelete = () => {
        dispatch(deleteTry()).then(() => {
            dispatch(getTry());
        });
    };

    useEffect(() => {
        return () => {
            dispatch(clearState());
            dispatch(getTry());
        };
    }, [isSuccess]);

    useEffect(() => {
        if (isError) {
            dispatch(clearState());
            setOpenError(true);
            setTimeout(() => {
                setOpenError(false);
                dispatch(clearState());
            }, 3000);
        }

        if (isError) {
            dispatch(clearState());
            navigate('/');
            localStorage.removeItem('token');
            window.location.reload();
        }
    }, [isError, isSuccess]);


    return (
        <div>
            <Navbar />
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Paper sx={{ p: 4 }}>

                    <div className="parent">
                        <div className="centered">
                            <Decart r={formData.r}/>
                        </div>
                    </div>

                    {/*<Decart r={formData.r} />*/}
                    <form onSubmit={handleFormSubmit}>
                        <Grid container spacing={2}> {/* Используем Grid для управления расположением элементов */}
                            <Grid item xs={12}>
                                <FormControl component="fieldset">
                                    <FormLabel component="legend">Координата по оси X:</FormLabel>
                                    <FormGroup row>
                                        {['-3', '-2', '-1', '0', '1', '2', '3', '4', '5'].map((value) => (
                                            <FormControlLabel
                                                key={`x_${value}`}
                                                control={
                                                    <Checkbox
                                                        name="x"
                                                        value={value}
                                                        onChange={handleChange}
                                                        checked={selectedItemX === value}
                                                    />
                                                }
                                                label={value}
                                            />
                                        ))}
                                    </FormGroup>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth>
                                    <InputLabel shrink>Координата по оси Y:</InputLabel>
                                    <Input
                                        type="number"
                                        name="y"
                                        value={formData.y}
                                        onChange={handleChange}
                                        inputProps={{ min: -5, max: 5 }}
                                        required
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl component="fieldset">
                                    <FormLabel component="legend">Размер графика R:</FormLabel>
                                    <FormGroup row>
                                        {['0', '1', '2', '3', '4', '5'].map((value) => (
                                            <FormControlLabel
                                                key={`r_${value}`}
                                                control={
                                                    <Checkbox
                                                        name="r"
                                                        value={value}
                                                        onChange={handleChange}
                                                        checked={selectedItemR === value}
                                                    />
                                                }
                                                label={value}
                                            />
                                        ))}
                                    </FormGroup>
                                </FormControl>
                            </Grid>
                            <Grid item xs={6}>
                                <Button variant="contained" type="submit" disabled={isFetching}>
                                    {isFetching ? 'Sending...' : 'Send'}
                                </Button>
                            </Grid>
                            <Grid item xs={6}>
                                <Button variant="contained" onClick={onDelete} disabled={isFetching}>
                                    {isFetching ? 'Deleting...' : 'Delete'}
                                </Button>
                            </Grid>
                        </Grid>
                    </form>
                </Paper>
                <Table />
            </Container>
            <Snackbar open={openError} autoHideDuration={3000} onClose={() => setOpenError(false)}>
                <Alert severity="error" onClose={() => setOpenError(false)}>
                    {errorMessage}
                </Alert>
            </Snackbar>
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
