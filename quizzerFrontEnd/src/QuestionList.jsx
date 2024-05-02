import { useState, useEffect, useRef } from "react";
import { useParams } from 'react-router-dom';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import TextField from '@mui/material/TextField';
import Chip from '@mui/material/Chip';
import Snackbar from '@mui/material/Snackbar';
import { Container, Box, FormControl, InputLabel, Select, MenuItem } from '@mui/material';

function QuestionList() {
    const [questionList, setQuestionList] = useState([]);
    const [answers, setAnswers] = useState([]);
    const [inputAnswers, setInputAnswers] = useState({});
    const [correctAnswer, setCorrectAnswer] = useState('');
    const [open, setOpen] = useState(false);
    const [correct, setCorrect] = useState(false);
    const [selectedDifficulty, setSelectedDifficulty] = useState('');
    const { id } = useParams();
    const [currentQuestionId, setCurrentQuestionId] = useState(null); // New state to store the ID of the current question being answered

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
            setQuestionList(data);
        } catch (error) {
            console.error("Failed to load questions: ", error);
        }
    };

    useEffect(() => {
        fetchAnswers();
    }, [id]);

    const fetchAnswers = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/QuizApp/${id}/answers`);
            if (!response.ok) {
                throw new Error("Error in retrieving all quizzes answers" + response.statusText);
            }
            const data = await response.json();
            setAnswers(data);
        } catch (error) {
            console.error("Failed to load answers: ", error);
        }
    }

    const handleSubmitAnswer = (questionId) => {
        setCurrentQuestionId(questionId); // Set the current question ID before comparing answers

        const correctAnswerObj = answers.find(answer => answer.questionId === questionId);

        if (!correctAnswerObj) {
            console.error("No answer found for question ID:", questionId);
            return;
        }
        setCorrectAnswer(correctAnswerObj.answerText);
        setOpen(true);
    }

    useEffect(() => {
        if (open && currentQuestionId !== null) { 
            const userAnswer = inputAnswers[currentQuestionId]?.trim().toLowerCase();
            const correctAnswerText = correctAnswer.trim().toLowerCase();

            if (userAnswer === correctAnswerText) {
                setCorrect(true);
            } else {
                setCorrect(false);
            }
        }
    }, [open, correctAnswer, inputAnswers, currentQuestionId]);

    const handleCheckAnswer = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    };

    const handleDifficultyChange = (event) => {
        setSelectedDifficulty(event.target.value);
    };

    const filteredQuestions = questionList.filter(question => {
        if (selectedDifficulty === '') {
            return true; // Show all questions if no difficulty selected
        }
        return question.difficultyLevel.toLowerCase() === selectedDifficulty.toLowerCase();
    });

    return (
        <Container maxWidth="md" style={{ marginTop: '20px' }}>
            <Typography variant="h2" component="h1" gutterBottom align="center">
                {questionList.length > 0 ? questionList[0].quiz.quizName : ''}
            </Typography>
            <Typography variant="h4" component="h2" gutterBottom align="center" color="textSecondary">
                {questionList.length > 0 ? questionList[0].quiz.quizDescription : ''}
            </Typography>
            <Box sx={{ marginBottom: '20px' }}>
                <FormControl sx={{ minWidth: 120 }}>
                    <InputLabel id="difficulty-level-label">Difficulty Level</InputLabel>
                    <Select
                        labelId="difficulty-level-label"
                        id="difficulty-level-select"
                        value={selectedDifficulty}
                        label="Difficulty Level"
                        onChange={handleDifficultyChange}
                    >
                        <MenuItem value="">All</MenuItem>
                        <MenuItem value="easy">Easy</MenuItem>
                        <MenuItem value="normal">Normal</MenuItem>
                        <MenuItem value="hard">Hard</MenuItem>
                    </Select>
                </FormControl>
            </Box>
            <div>
                {filteredQuestions.map((question, index) => (
                    <Accordion key={index}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls={`panel${index}a-content`}
                            id={`panel${index}a-header`}
                        >
                            <Box>
                                <Typography variant="h5">{question.questionText}</Typography>
                                <Typography variant="subtitle1" gutterBottom>Difficulty Level:
                                    <Chip label={` ${question.difficultyLevel}`} size="medium" color="success" />
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
                                onChange={e => setInputAnswers({ ...inputAnswers, [question.questionId]: e.target.value })}
                            />
                            <Typography
                                variant="body2"
                                onClick={() => handleSubmitAnswer(question.questionId)}
                                style={{ cursor: 'pointer', color: 'blue', marginTop: '8px' }}
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
                message={correct ? "This is correct, good job!" : `That is not correct the correct answer is '${correctAnswer}' !`}
            />
        </Container>
    );
}

export default QuestionList;
