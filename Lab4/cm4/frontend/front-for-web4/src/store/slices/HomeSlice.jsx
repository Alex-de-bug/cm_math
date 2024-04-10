import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const sendTry = createAsyncThunk(
    "home/sendTry",
    async ({ typeFunc, method, a, b, eps }, thunkAPI) => {
        await new Promise(resolve => setTimeout(resolve, 100));
        try {
            let link = "http://localhost:8080/api/integration/calculate";
            const params = {
                typeFunc: typeFunc,
                a: a,
                b: b,
                eps: eps,
                method: method
            };
            const response = await axios.post(link, params, {
                headers: { "Content-Type": "application/json" }
            });
            let data = await response.data;
            console.log("status: "+response.status)
            // if (response.status === 200||response.status === 201) {
            if (response.status === 200) {
                return data;
            } else {
                return thunkAPI.rejectWithValue(data);
            }
        } catch (e) {
            console.log("status: "+e.response.status)
            return thunkAPI.rejectWithValue(e.response.data);
        }
    }
);

export const HomeSlice = createSlice({
    name: "home",
    initialState: {
        isFetching: false,
        isSuccess: false,
        isError: false,
        errorMessage: "",
        array: ""
    },
    reducers: {
        clearState: (state) => {
            state.isError = false;
            state.isSuccess = false;
            state.isFetching = false;
            return state;
        },
        clearAllStats: (state) => {
            state.errorMessage = "";
            state.array = "";
            state.isError = false;
            state.isSuccess = false;
            state.isFetching = false;
            return state;
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(sendTry.fulfilled, (state, { payload }) => {
                console.log("fulfilled with data: "+payload);
                state.isFetching = false;
                state.isSuccess = true;
                state.errorMessage = "";
                state.array = payload;
                return state;
            })
            .addCase(sendTry.rejected, (state, { payload }) => {
                console.log("rejected with error: "+payload);
                state.isFetching = false;
                state.isError = true;
                state.isSuccess = false;
                const now = new Date();
                const pad = (num) => num.toString().padStart(2, '0');
                const hours = pad(now.getHours());
                const minutes = pad(now.getMinutes());
                const seconds = pad(now.getSeconds());
                const formattedTime = `${hours}:${minutes}:${seconds}`;
                state.errorMessage = payload + "; Время запроса: " + formattedTime;
                state.array = "";
            })
            .addCase(sendTry.pending, (state) => {
                console.log("pending");
                state.isFetching = true;
            })
    }
});

export const { clearState } = HomeSlice.actions;

export const homeSelector = (state) => state.home;
