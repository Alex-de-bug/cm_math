import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home.jsx";
import SignIn from "./pages/Login.jsx";
import SignUp from "./pages/SignUp.jsx";
import PrivateRoute from "./PrivateRoute.jsx";

function Router() {
    return (
        <Routes>
            <Route path="/" element={<SignIn />} />
            <Route path="/signUp" element={<SignUp />} />
            <Route element={<PrivateRoute/>}>
                <Route path="/home" element={<Home />} />
            </Route>
        </Routes>
    );
}

export default Router;
