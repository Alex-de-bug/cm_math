import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { signupUser, signupSelector, clearState } from '../store/slices/SignUpSlice';
import Navbar from '../components/Navbar';
import {Button, Typography, Container, Box, Snackbar, Alert, TextField} from '@mui/material';

function Signup() {
    const [showPassword, setShowPassword] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { isFetching, isSuccess, isError, errorMessage } = useSelector(signupSelector);
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: ''
    });
    const [openError, setOpenError] = useState(false); // Состояние для управления видимостью ошибки

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: value
        }));
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
        dispatch(signupUser(data));
    };

    useEffect(() => {
        return () => {
            dispatch(clearState());
        };
    }, [dispatch]);

    useEffect(() => {
        if (isError) {
            setOpenError(true); // Показываем Snackbar с сообщением об ошибке
            setTimeout(() => {
                setOpenError(false); // Скрываем Snackbar через определенное время
                dispatch(clearState());
            }, 3000); // Время показа Snackbar в миллисекундах (например, 3 секунды)
        }

        if (isSuccess) {
            dispatch(clearState());
            navigate('/');
        }
    }, [isError, isSuccess, dispatch, navigate]);

    return (
        <div>
            <Navbar />
            <Container maxWidth="sm">
                <Box sx={{ mt: 8 }}>
                    <Typography variant="h4" gutterBottom>
                        Sign up
                    </Typography>
                    <form onSubmit={handleFormSubmit}>
                        <TextField
                            label="Name"
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                            fullWidth
                            margin="normal"
                        />
                        <TextField
                            label="Email address"
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            fullWidth
                            margin="normal"
                        />
                        <TextField
                            label="Password"
                            type={showPassword ? 'text' : 'password'}
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            fullWidth
                            margin="normal"
                        />
                        <Button type="button" onClick={() => setShowPassword(!showPassword)}>
                            {showPassword ? 'Hide' : 'Show'}
                        </Button>
                        {isFetching ? (
                            <Button variant="contained" type="submit" disabled>
                                Signing up...
                            </Button>
                        ) : (
                            <Button variant="contained" type="submit">
                                Sign up
                            </Button>
                        )}
                    </form>
                    <Snackbar open={openError} autoHideDuration={3000} onClose={() => setOpenError(false)}>
                        <Alert severity="error" >
                            {errorMessage}
                        </Alert>
                    </Snackbar>
                </Box>
            </Container>
        </div>
    );
}

export default Signup;

