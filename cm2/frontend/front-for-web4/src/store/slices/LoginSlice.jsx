import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const loginUser = createAsyncThunk(
    "users/login",
    async ({ name, password }, thunkAPI) => {
        try {
            let link = "http://localhost:8080/api/auth/authenticate";
            const params = {
                name: name,
                password: password
            };
            const response = await axios.post(link, params, {
                headers: { "Content-Type": "application/json" }
            });
            let data = await response.data;
            if (response.status === 200) {
                localStorage.setItem("token", data.token);
                return data;
            } else {
                return thunkAPI.rejectWithValue(data);
            }
        } catch (e) {
            return thunkAPI.rejectWithValue(e.response.data);
        }
    }
);

export const LoginSlice = createSlice({
    name: "login",
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
            .addCase(loginUser.fulfilled, (state, { payload }) => {
                state.token = payload.token;
                state.isFetching = false;
                state.isSuccess = true;
                state.errorMessage = "";
                return state;
            })
            .addCase(loginUser.rejected, (state, { payload }) => {
                state.isFetching = false;
                state.isError = true;
                state.errorMessage = payload
            })
            .addCase(loginUser.pending, (state) => {
                state.isFetching = true;
            })
    }
});

export const { clearState } = LoginSlice.actions;

export const loginSelector = (state) => state.login;