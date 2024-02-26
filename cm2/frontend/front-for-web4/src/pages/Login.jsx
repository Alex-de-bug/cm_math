import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { loginUser, loginSelector, clearState } from '../store/slices/LoginSlice';
import { Typography, Container, Box, TextField, Button, Snackbar, Alert, useMediaQuery } from '@mui/material';
import Navbar from '../components/Navbar';
import { useForm } from 'react-hook-form';

function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { register, handleSubmit } = useForm();
  const { isFetching, isSuccess, isError, errorMessage } = useSelector(loginSelector);
  const [openError, setOpenError] = React.useState(false);

  const onSubmit = (data) => {
    dispatch(loginUser(data));
  };

  const handleFormSubmit = (data) => {
    onSubmit(data);
  };

  const navigateToHome = () => {
    navigate('/home');
  };

  useEffect(() => {
    if (isError) {
      setOpenError(true);
      setTimeout(() => {
        setOpenError(false);
        dispatch(clearState());
      }, 3000);
    }

    if (isSuccess) {
      dispatch(clearState());
      navigateToHome();
    }
  }, [isError, isSuccess, dispatch, navigate]);

  const isDesktop = useMediaQuery('(min-width: 1260px)');
  const isTablet = useMediaQuery('(min-width: 781px) and (max-width: 1259px)');
  const isMobile = useMediaQuery('(max-width: 780px)');

  return (
      <div>
        <Navbar />
        <Container maxWidth="sm">
          <Box sx={{ mt: isDesktop ? 8 : isTablet ? 6 : isMobile ? 4 : 2 }}>
            <Typography variant="h4" gutterBottom>
              Sign in to your account
            </Typography>
            <form onSubmit={handleSubmit(handleFormSubmit)}>
              <TextField
                  label="Name"
                  name="name"
                  {...register('name', { required: true })}
                  required
                  fullWidth
                  margin="normal"
              />
              <TextField
                  label="Password"
                  type="password"
                  name="password"
                  {...register('password', { required: true })}
                  required
                  fullWidth
                  margin="normal"
              />
              {isFetching ? (
                  <Button variant="contained" type="submit" disabled>
                    Signing in...
                  </Button>
              ) : (
                  <Button variant="contained" type="submit">
                    Sign in
                  </Button>
              )}
            </form>
          </Box>
          <Snackbar open={openError} autoHideDuration={3000} onClose={() => setOpenError(false)}>
            <Alert severity="error" onClose={() => setOpenError(false)}>
              {errorMessage}
            </Alert>
          </Snackbar>
        </Container>
      </div>
  );
}

export default Login;

