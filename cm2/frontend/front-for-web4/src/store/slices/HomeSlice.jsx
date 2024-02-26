import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const sendTry = createAsyncThunk(
    "home/sendTry",
    async ({ x, y, r }, thunkAPI) => {
        await new Promise(resolve => setTimeout(resolve, 100));
        try {
            let link = "http://localhost:8080/api/auth/attempt";
            const params = {
                x: x,
                y: y,
                r: r,
                token: localStorage.getItem('token')
            };
            const response = await axios.post(link, params, {
                headers: { "Content-Type": "application/json" }
            });
            let data = await response.data;
            if (response.status === 200||response.status === 201) {
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

export const getTry = createAsyncThunk(
    "home/getTry",
    async (thunkAPI) => {
        await new Promise(resolve => setTimeout(resolve, 100));
        try {
            let link = "http://localhost:8080/api/auth/attempt";
            const config = {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            };
            const response = await axios.get(link, config);
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

export const deleteTry = createAsyncThunk(
    "home/deleteTry",
    async (thunkAPI) => {
        await new Promise(resolve => setTimeout(resolve, 100));
        try {
            let link = "http://localhost:8080/api/auth/attempt";
            const config = {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            };
            const response = await axios.delete(link, config);
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



export const HomeSlice = createSlice({
    name: "home",
    initialState: {
        token: "",
        isFetching: false,
        isSuccess: false,
        isError: false,
        errorMessage: "",
        array: "",

    },
    reducers: {
        clearState: (state) => {
            state.isError = false;
            state.isSuccess = false;
            state.isFetching = false;
            return state;
        },
        clearAllStats: (state) => {
            state.token = "";
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
                console.log(payload);
                state.token = payload.token;
                state.isFetching = false;
                return state;
            })
            .addCase(sendTry.rejected, (state, { payload }) => {
                console.log(payload);
                state.isFetching = false;
                state.isError = true;
                // state.errorMessage = payload
            })
            .addCase(sendTry.pending, (state) => {
                state.isFetching = true;
            })
            .addCase(getTry.fulfilled, (state, { payload }) => {
                // console.log(payload);
                state.array = payload;
                state.isFetching = false;
                return state;
            })
            .addCase(getTry.rejected, (state, { payload }) => {
                console.log(payload);
                state.isFetching = false;
                state.errorMessage = "Ошибка при загрузке данных, доступ запрещён"
                state.array="";
            })
            .addCase(getTry.pending, (state) => {
                state.isFetching = true;
            })
    }
});

export const { clearState } = HomeSlice.actions;

export const homeSelector = (state) => state.home;
