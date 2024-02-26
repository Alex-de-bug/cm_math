import { configureStore } from "@reduxjs/toolkit";
import { LoginSlice } from "./slices/LoginSlice.jsx";
import { SignupSlice } from "./slices/SignUpSlice.jsx";
import {HomeSlice} from "./slices/HomeSlice.jsx";

const store = configureStore({
    reducer: {
        login: LoginSlice.reducer,
        signup: SignupSlice.reducer,
        home: HomeSlice.reducer
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            immutableCheck: false,
            serializableCheck: false
        })
});

export default store;
