import { configureStore } from "@reduxjs/toolkit";
import {HomeSlice} from "./slices/HomeSlice.jsx";

const store = configureStore({
    reducer: {
        home: HomeSlice.reducer
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            immutableCheck: false,
            serializableCheck: false
        })
});

export default store;
