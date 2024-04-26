import { useState, useEffect, useRef } from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { useParams } from 'react-router-dom';

function QuestionList() {
    const [questionList, setquestionList] = useState([]);
    const gridRef = useRef(null);
    const { id } = useParams();

    const columns = [
        {
            headerName: 'Question',
            field: 'questionText',
            width: 150
        },
        {
            headerName: 'Correct Answer',
            field: 'correctAnswer',
            width: 150
        },
        {
            headerName: 'Difficulty Level',
            field: 'difficultyLevel',
            width: 150
        }
    ];

    useEffect(() => {
        fetchQuestionList();
    }, []);

    const fetchQuestionList = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/QuizApp/quiz/' + id + '/questions');
            if (!response.ok) {
                throw new Error("Error in retrieving quizes " + response.statusText);
            }
            const data = await response.json();
            console.log(data)
            setquestionList(data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <>
            <h1>{questionList.length > 0 ? questionList[0].quiz.quizName : ''}</h1>
            <h2>{questionList.length > 0 ? questionList[0].quiz.quizDescription : ''}</h2>
            <div className="ag-theme-material" style={{ height: '400px', width: '900px' }}>
                <AgGridReact
                    domLayout="autoHeight"
                    ref={gridRef}
                    rowData={questionList}
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

export default QuestionList;