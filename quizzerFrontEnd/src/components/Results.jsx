import { grid, width } from "@mui/system"
import { AgGridReact } from "ag-grid-react"
import { useEffect, useRef, useState } from "react"
import { useParams } from "react-router-dom"

function Results() {
    const { id } = useParams()
    const [answers, setAnswers] = useState([])
    const gridRef = useRef(null)
    
    const fetchAnswers = async (quizId) => {
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quiz/${id}/answers`)
            if (!response.ok) {
                throw new Error('Failed to fetch answers')
            }
            const data = await response.json();
            setAnswers(data)
        } catch (error) {
            console.error('Error fetching answers: ', error)
        }
    }

    useEffect(() => {
        fetchAnswers(id)
    }, [id])

    const columns = [
        { headerName: 'Question', field: 'questionText', width: 200 },
        { headerName: 'Difficulty level', field: 'difficultyLevel', width: 250 },
        { headerName: 'Total Answers', field: 'totalAnswers', width: 200 },
        { headerName: 'Correct Answers', field: 'correctAnswers', minWidth: 210 },
        { headerName: 'Wrong Answers', field: 'wrongAnswers', width: 210 },
        { headerName: 'Correct Answers Percentage', field: 'correctPercentage', valueFormatter: params => `${params.value.toFixed(2)}%`, width: 300 },
    ]

    return (
        <div style={{ height: '100%', width: '100%', paddingTop: '50px'}}>
            <div className="ag-theme-material custom-ag-theme" style={{ height: '100%', width: '100%' }}>
                <AgGridReact domLayout="autoHeight" ref={gridRef} rowData={answers}
                            columnDefs={columns} rowSelection="single"
                            onGridReady={params => {gridRef.current = params.api}} />
            </div>            
        </div>

    )

}

export default Results