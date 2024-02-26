import { useSelector } from 'react-redux';
import { homeSelector } from '../store/slices/HomeSlice.jsx';
import { useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography } from '@mui/material';

export default function DataTable() {
    const token = localStorage.getItem('token');
    const { array } = useSelector(homeSelector);

    useEffect(() => {
        console.log(array);
    }, [array]);

    if (!!token && array.length !== 0) {
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>X</TableCell>
                            <TableCell>Y</TableCell>
                            <TableCell>R</TableCell>
                            <TableCell>Result</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {array.map((item, index) => (
                            <TableRow key={index}>
                                <TableCell>{item.x}</TableCell>
                                <TableCell>{item.y}</TableCell>
                                <TableCell>{item.r}</TableCell>
                                <TableCell>{item.result.toString()=="true" ? "Попадание":"Промах"}</TableCell>
                            </TableRow>
                        )).reverse()}
                    </TableBody>
                </Table>
            </TableContainer>
        );
    } else {
        return (
            <Typography variant="h6" align="center" sx={{ marginTop: '20px' }}>
                Нет доступных попыток
            </Typography>
        );
    }
}




// import {useSelector} from "react-redux";
// import {homeSelector} from "../store/slices/HomeSlice.jsx";
// import {useEffect} from "react";
//
// export default function Table() {
//     const token = localStorage.getItem('token');
//     const {array} = useSelector(
//         homeSelector
//     );
//     useEffect(() => {
//         console.log(array);
//     }, [array]);
//
//     if (!!token && array!=="") {
//         return (
//             <div>
//                 <table>
//                     <thead>
//                     <tr>
//                         <th>X</th>
//                         <th>Y</th>
//                         <th>R</th>
//                         <th>Result</th>
//                     </tr>
//                     </thead>
//                     <tbody>
//                     {array.map((item, index) => (
//                         <tr key={index}>
//                             <td>{item.x}</td>
//                             <td>{item.y}</td>
//                             <td>{item.r}</td>
//                             <td>{item.result.toString()}</td>
//                         </tr>
//                     ))}
//                     </tbody>
//                 </table>
//             </div>
//         );
//     } else {
//         return (
//             <div>
//                 Нет доступных попыток
//             </div>
//         );
//     }
// }


