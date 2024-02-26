import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const signupUser = createAsyncThunk(
    "users/signupUser",
    async ({ name, email, password }, thunkAPI) => {
        try {
            let link = "http://localhost:8080/api/auth/register";
            const params = {
                email: email,
                name: name,
                password: password
            };
            const response = await axios.post(link, params, {
                headers: { "Content-Type": "application/json" }
            });
            let data = await response.data;
            if (response.status === 200) {
                return data;
            } else {
                return thunkAPI.rejectWithValue(data);
            }
        } catch (e) {
            console.log("Error", e.response.data);
            return thunkAPI.rejectWithValue(e.response.data);
        }
    }
);

export const SignupSlice = createSlice({
    name: "signup",
    initialState: {
        token: "",
        isFetching: false,
        isSuccess: false,
        isError: false,
        errorMessage: ""
    },
    reducers: {
        clearState: (state) => {
            state.isError = false;
            state.isSuccess = false;
            state.isFetching = false;
            return state;
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(signupUser.fulfilled, (state, { payload }) => {
                console.log(payload);
                state.token = payload.token;
                state.isFetching = false;
                state.isSuccess = true;
                state.errorMessage = "Регистрация прошла успешно!"
                return state;
            })
            .addCase(signupUser.rejected, (state, { payload }) => {
                console.log(payload);
                state.isFetching = false;
                state.isError = true;
                state.errorMessage = payload
            })
            .addCase(signupUser.pending, (state) => {
                state.isFetching = true;
            })
    }
});

export const { clearState } = SignupSlice.actions;

export const signupSelector = (state) => state.signup;
