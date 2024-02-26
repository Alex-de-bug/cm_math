// import { Link } from "react-router-dom";
import "../styles/NavigationBar.css";
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import {Link} from "react-router-dom";
import "../styles/NavigationBar.css"
import {ButtonGroup, Menu, MenuItem} from "@mui/material";
import {useState} from "react";

function removeTokenFromLocalStorage() {
    localStorage.removeItem('token');
    window.location.reload();
}


export default function WithSubnavigation() {
    const token = localStorage.getItem('token');


    return (
        <DesktopNav hasToken={!!token} />
    );
}


const DesktopNav = ({ hasToken }) => {
    const [anchorEl, setAnchorEl] = useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };
    const renderNavigation = () => {
        if (hasToken) {
            return (
                <Box sx={{ flexGrow: 1 }}>
                    <AppBar color="inherit" position="static">
                        <Toolbar>
                            <IconButton
                                size="large"
                                edge="start"
                                color="inherit"
                                aria-label="menu"
                                sx={{ mr: 2 }}
                            >
                            </IconButton>
                            <Typography className="left-text" variant="h6" component="div" sx={{ flexGrow: 1 }}>
                                Tries
                            </Typography>
                            <ButtonGroup
                                disableElevation
                                variant="contained"
                                aria-label="Disabled elevation buttons"
                            >
                                <Button
                                    color="inherit"
                                    aria-controls="menu"
                                    aria-haspopup="true"
                                    onClick={handleClick}
                                >
                                    Menu
                                </Button>
                                <Menu
                                    id="menu"
                                    anchorEl={anchorEl}
                                    open={Boolean(anchorEl)}
                                    onClose={handleClose}
                                >
                                    <MenuItem onClick={handleClose}>
                                        <Link to="/home" className="linkRR">
                                            Home
                                        </Link>
                                    </MenuItem>
                                    <MenuItem onClick={() => { handleClose(); removeTokenFromLocalStorage(); }}>
                                        <Link to="/" className="linkRR">
                                            Exit
                                        </Link>
                                    </MenuItem>
                                </Menu>
                                <Button color="inherit" href="https://github.com/Alex-de-bug">
                                    Alex D. ISU: 367193 VAR: 2298
                                </Button>
                            </ButtonGroup>

                        </Toolbar>
                    </AppBar>
                </Box>
            );
        } else {
            return (
                <Box sx={{ flexGrow: 1 }}>
                    <AppBar color="inherit" position="static">
                        <Toolbar>
                            <IconButton
                                size="large"
                                edge="start"
                                color="inherit"
                                aria-label="menu"
                                sx={{ mr: 2 }}
                            >
                            </IconButton>
                            <Typography className="left-text" variant="h6" component="div" sx={{ flexGrow: 1 }}>
                                WEB
                            </Typography>
                            <ButtonGroup
                                disableElevation
                                variant="contained"
                                aria-label="Disabled elevation buttons"
                            >
                                <Button
                                    color="inherit"
                                    aria-controls="menu"
                                    aria-haspopup="true"
                                    onClick={handleClick}
                                >
                                    Menu
                                </Button>
                                <Menu
                                    id="menu"
                                    anchorEl={anchorEl}
                                    open={Boolean(anchorEl)}
                                    onClose={handleClose}
                                >
                                    {NAV_ITEMS.map((navItem) => (
                                        <MenuItem key={navItem.label} onClick={handleClose}>
                                            <Link to={navItem.href} className="linkRR">
                                                {navItem.label}
                                            </Link>
                                        </MenuItem>
                                    ))}
                                </Menu>
                                <Button color="inherit" href="https://github.com/Alex-de-bug">
                                    Alex D. ISU: 367193 VAR: 2298
                                </Button>
                            </ButtonGroup>
                        </Toolbar>
                    </AppBar>
                </Box>
            );
        }
    };

    return renderNavigation();
};

const NAV_ITEMS = [
    {
        label: "Login",
        href: "/"
    },
    {
        label: "Sign",
        href: "/signUp"
    }
];
