import { useState, useEffect, useRef } from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { format } from "date-fns";
import { Link } from 'react-router-dom'; 

function QuizList() {
    const [quizList, setQuizList] = useState([]);
    const gridRef = useRef(null);

    const columns = [
        {
            headerName: 'Name',
            field: 'quizName',
            width: 150,
            cellRenderer: (params) => {
                const link = `/quiz/${params.data.id}`;
                console.log('Link:', link); // Debugging
                return <Link to={link}>{params.value}</Link>;
            }
        },
        {
            headerName: 'Description',
            field: 'quizDescription',
            width: 150
        },
        {
            headerName: 'Category',
            valueGetter: params => params.data.category ? params.data.category.name : '',  // Value getter for nested property
            width: 150
        },
        {
            headerName: 'Added on',
            field: 'createdAt',
            valueFormatter: (params => {
                return format(new Date(params.value), "dd.MM.yyyy");
            }),
            width: 150
        },
        {
            headerName: 'Published',
            field: 'publishedDisplay',
            width: 150
        }
    ];

    useEffect(() => {
        fetchQuizList();
    }, []);

    const fetchQuizList = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/QuizApp/quizes?published=true');
            if (!response.ok) {
                throw new Error("Error in retrieving quizes " + response.statusText);
            }
            const data = await response.json();
            console.log(data)
            setQuizList(data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <>
            <div className="ag-theme-material" style={{ height: '400px', width: '900px' }}>
                <AgGridReact
                    domLayout="autoHeight"
                    ref={gridRef}
                    rowData={quizList}
                    columnDefs={columns}
                    rowSelection="single"
                    onGridReady={params => {
                        gridRef.current = params.api;
                    }}
                />
            </div>
        </>
    );
}

export default QuizList;
