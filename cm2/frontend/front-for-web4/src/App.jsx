import { BrowserRouter } from "react-router-dom";
import Router from "./Router.jsx";
import "./styles/NavigationBar.css"
export default function App() {
  return (
    <BrowserRouter>
      <Router />
    </BrowserRouter>
  );
}
