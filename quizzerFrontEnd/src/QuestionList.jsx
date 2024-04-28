import { useState, useEffect, useRef } from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { useParams } from 'react-router-dom';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import TextField from '@mui/material/TextField';
import Chip from '@mui/material/Chip';

function QuestionList() {
    const [questionList, setQuestionList] = useState([]);
    const gridRef = useRef(null);
    const { id } = useParams();

    useEffect(() => {
        fetchQuestionList();
    }, [id]);

    const fetchQuestionList = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/QuizApp/quiz/${id}/questions`);
            if (!response.ok) {
                throw new Error("Error in retrieving questions" + response.statusText);
            }
            const data = await response.json();
            console.log(data)
            setQuestionList(data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <>
            <h1>{questionList.length > 0 ? questionList[0].quiz.quizName : ''}</h1>
            <h2>{questionList.length > 0 ? questionList[0].quiz.quizDescription : ''}</h2>
           <div>
            {questionList.map((question, index) =>(
                <Accordion key={index}>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls={`panel${index}a-content`}
                        id={`panel${index}a-header`}
                    >
                        <div>
                            <Typography variant="h4">{question.questionText}</Typography>
                            <br />
                            <Typography variant="h5">Difficulty Level:
                                <Chip label={` ${question.difficultyLevel}`} size="medium" color="success"/>
                            </Typography>
                        </div>
                    </AccordionSummary>
                    <AccordionDetails>
                        <TextField
                            fullWidth
                            label="Your Answer"
                            variant="outlined"
                            placeholder="Type your answer here"
                        />
                    </AccordionDetails>
            </Accordion>
            ))}
           </div>
        </>
    );
}

export default QuestionList;