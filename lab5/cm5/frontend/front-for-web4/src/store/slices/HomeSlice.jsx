import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";

export const sendTry = createAsyncThunk(
    "home/sendTry",
    async ({val, points, func, range, step, inputType}, thunkAPI) => {
        await new Promise(resolve => setTimeout(resolve, 100));
        try {
            let link = "http://localhost:8080/api/integration/calculate";
            let interval = [range.from, range.to];
            const params = {
                val: val,
                points: points,
                function: func,
                a: interval[0],
                b: interval[1],
                step: step,
                type: inputType === "points" ? 0 : 1
            };

            console.log("prevent: ", params, inputType);
            switch (inputType) {
                case 'points': {
                    // Check if all points have valid numbers
                    let allFieldsFilled = points.every(point => {
                        const x = parseFloat(point.x);
                        const y = parseFloat(point.y);
                        return !isNaN(x) && !isNaN(y);
                    });
                    let minX = points.reduce((min, p) => p.x < min ? p.x : min, points[0].x);
                    let maxX = points.reduce((max, p) => p.x > max ? p.x : max, points[0].x);
                    if (!(val >= minX && val <= maxX) || !allFieldsFilled) {
                        return thunkAPI.rejectWithValue("All fields must be filled with valid numbers for points input.");
                    }
                    break;
                }
                case 'function': {
                    if (!(val >= range.from && val <= range.to) || range.from > range.to || step < 0.01 || (step >= (range.to - range.from))) {
                        return thunkAPI.rejectWithValue("Function input requires valid range and step values.");
                    }
                    break;
                }
                default:
                    return thunkAPI.rejectWithValue("Unknown input type or other error.");
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
