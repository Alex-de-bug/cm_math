import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";

export const sendTry = createAsyncThunk(
    "home/sendTry",
    async ({x0, y0, func, xn, step, eps}, thunkAPI) => {
        await new Promise(resolve => setTimeout(resolve, 100));
        try {
            let link = "http://localhost:8080/api/integration/calculate";
            const params = {
                x0: x0,
                y0: y0,
                func: func,
                xn: xn,
                step: step,
                eps: eps
            };

            console.log("prevent: ", params);

            if(x0>=xn){
                return thunkAPI.rejectWithValue("Неверно введены границы");
            }
            if(x0+xn<step || step<0){
                return thunkAPI.rejectWithValue("Неверно введён шаг");
            }
            if(eps<0){
                return thunkAPI.rejectWithValue("Неверно введёна погрешность");
            }

            console.log("send");
            const response = await axios.post(link, params, {
                headers: {"Content-Type": "application/json"}
            });
            let data = await response.data;
            console.log("status: " + response.status)
            // if (response.status === 200||response.status === 201) {
            if (response.status === 200) {
                return data;
            } else {
                return thunkAPI.rejectWithValue(data);
            }
        } catch (e) {
            console.log("status: " + e.response.status)
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
        message1: "",
        message2: "",
        message3: "",
        message4: "",
        message5: "",
        message6: "",
        array: [],
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
            .addCase(sendTry.fulfilled, (state, {payload}) => {
                console.log("fulfilled with data: ", payload);
                const [message1, message2, message3, message4, message5, message6, dataArray] = payload;
                state.isFetching = false;
                state.isSuccess = true;
                state.errorMessage = "";
                state.array = dataArray;
                state.message1 = message1;
                state.message2 = message2;
                state.message3 = message3;
                state.message4 = message4;
                state.message5 = message5;
                state.message6 = message6;
                return state;
            })
            .addCase(sendTry.rejected, (state, {payload}) => {
                console.log("rejected with error: " + payload);
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
                state.array = [];
                state.message1 = "";
                state.message2 = "";
                state.message3 = "";
                state.message4 = "";
                state.message5 = "";
                state.message6 = "";
            })
            .addCase(sendTry.pending, (state) => {
                console.log("pending");
                state.isFetching = true;
            })
    }
});

export const {clearState} = HomeSlice.actions;

export const homeSelector = (state) => state.home;
