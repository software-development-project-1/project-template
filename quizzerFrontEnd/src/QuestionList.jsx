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
import Snackbar from '@mui/material/Snackbar';
import {Container, Box}from '@mui/material';

function QuestionList() {
    const [questionList, setQuestionList] = useState([]);
    const [answers, setAnswers] = useState([]);
    const [inputAnswers, setInputAnswers] = useState({});
    const [correctAnswer, setCorrectAnswer] = useState('');
    const [open, setOpen] = useState(false);
    const [correct, setCorrect] = useState(false);
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
            console.error("Failed to load questions: ",error);
        }
    };
    useEffect(() => {
        fetchAnswers();
    }, [id]);
    const fetchAnswers = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/QuizApp/quiz/${id}/answers`);
            if (!response.ok) {
                throw new Error("Error in retrieving all quizzes answers" + response.statusText);
            }
            const data = await response.json();
            console.log("Answers fetched:", data)
            setAnswers(data);
        } catch (error) {
            console.error("Failed to load answers: ", error);
        }
    }

    const handleSubmitAnswer = (questionId) =>{
        console.log("Question ID for submit:", questionId);
        const correctAnswerObj = answers.find(answer => answer.questionId === questionId);

        if(!correctAnswerObj){
            console.error("No answer found for question ID:", questionId);
            return;
        }
        setCorrectAnswer(correctAnswerObj.answerText);
        const userAnswer = inputAnswers[questionId].trim().toLowerCase();
        if(userAnswer === correctAnswer.trim().toLowerCase()){
            setCorrect(true);
        }else{
            setCorrect(false);
        }

        setOpen(true);
    }
    const handleCheckAnswer = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    };

    return (
        <Container maxWidth="md" style={{ marginTop: '20px' }}>
            <Typography variant="h2" component="h1" gutterBottom align="center">
            {questionList.length > 0 ? questionList[0].quiz.quizName : ''}
            </Typography>
            <Typography variant="h4" component="h2" gutterBottom align="center" color="textSecondary">
                {questionList.length > 0 ? questionList[0].quiz.quizDescription : ''}
            </Typography>
            <div>
            {questionList.map((question, index) =>(
                <Accordion key={index}>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls={`panel${index}a-content`}
                        id={`panel${index}a-header`}
                    >
                        <Box>
                            <Typography variant="h5">{question.questionText}</Typography>
                            <Typography variant="subtitle1" gutterBottom>Difficulty Level:
                                <Chip label={` ${question.difficultyLevel}`} size="medium" color="success"/>
                            </Typography>
                        </Box>
                    </AccordionSummary>
                    <AccordionDetails>
                        <TextField
                            fullWidth
                            label="Your Answer"
                            variant="outlined"
                            placeholder="Type your answer here"
                            value={inputAnswers[question.questionId] || ''}
                            onChange={e => setInputAnswers({...inputAnswers, [question.questionId]: e.target.value})}
                        />
                        <Typography
                            variant="body2"
                            onClick={() => handleSubmitAnswer(question.questionId)}
                            style={{ cursor: 'pointer', color: 'blue', marginTop: '8px'  }}
                        >
                        SUBMIT YOUR ANSWER
                        </Typography>
                    </AccordionDetails>
            </Accordion>
            ))}
            </div>

            <Snackbar
                open={open}
                autoHideDuration={6000}
                onClose={handleCheckAnswer}
                message={correct ? "This is correct, good job!" : `That is not correct the correct answer is '${correctAnswer}' !` }
            />
         </Container>
    );
}

export default QuestionList;